#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.admin.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ${package}.common.service.BaseService;
import ${package}.common.service.DemoService;
import ${package}.common.scheduling.AbstractTask;
import ${package}.common.scheduling.TaskExecuter;

@Service
public class ScheduleService extends BaseService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TaskExecuter taskExecuter;

	@Autowired
	DemoService demoService;

	/**
	 * 每天凌晨7点定时清空缓存
	 */
	@Scheduled(cron = "0 0 7 * * ?")
	public void clearCache() {
		taskExecuter.execute(new AbstractTask() {
			@Override
			public void run() {
				demoService.clearCache();
			}

			@Override
			public String getName() {
				return "clearCache";
			}

			@Override
			public String getKeyPrefix() {
				return "lock:clearCache";
			}
		});
	}
}
