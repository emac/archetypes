#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.scheduling;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class TaskExecuterRedisImpl implements TaskExecuter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected RedisTemplate<String, Long> longRedisTemplate;
	
	private final RedisScript<Boolean> lockScript = new RedisScript<Boolean>() {

		private final String script = "local setResult = redis.call('SETNX',KEYS[1],ARGV[1]);if setResult == 1 then redis.call('EXPIRE',KEYS[1],ARGV[2]) end;return setResult;";
		
		@Override
        public String getSha1() {
			return DigestUtils.sha1DigestAsHex(script);
        }

		@Override
		public Class<Boolean> getResultType() {
			return Boolean.class;
        }

		@Override
        public String getScriptAsString() {
			return script;
        }
	};

	@Override
	public void execute(Task task) {
		String keyPrefix = task.getKeyPrefix();
		long expireTime = task.getExpireTime();
		long sleepTime = task.getSleepTime();
		String taskName = task.getName();
		logger.info("execute task,taskName:" + taskName + ",keyPrefix:" + keyPrefix + ",expireTime:" + expireTime + ",sleepTime:" + sleepTime);

		// 获取锁
		List<String> keys = Lists.newArrayList(keyPrefix);
		boolean flag = longRedisTemplate.execute(lockScript, keys, System.currentTimeMillis(), TimeUnit.SECONDS.convert(expireTime, TimeUnit.MINUTES));
		if (flag) {
			try {
				task.run();
			} catch (Exception e) {
				logger.warn("执行任务" + taskName + "失败：" + e.getMessage());
				longRedisTemplate.delete(keyPrefix);
			} finally {
				// 释放锁
				longRedisTemplate.expire(keyPrefix, sleepTime, TimeUnit.MINUTES);
			}
		} else {
			logger.info("未获取锁：" + keyPrefix);
		}
	}

}
