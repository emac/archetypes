#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.scheduling;

public abstract class AbstractTask implements Task {
	
	/**
	 * 任务锁失效时长，单位为分钟
	 * 
	 * @return
	 */
	@Override
	public long getExpireTime() {
		return 4L;
	}

	/**
	 * 完成任务后sleep时长，单位为分钟
	 * 
	 * @return
	 */
	@Override
	public long getSleepTime() {
		return 2L;
	}
}
