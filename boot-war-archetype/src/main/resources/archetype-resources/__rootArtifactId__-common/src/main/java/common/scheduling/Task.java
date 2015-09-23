#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.scheduling;

public interface Task {

	/**
	 * 任务锁失效时长，单位为分钟
	 * 
	 * @return
	 */
	long getExpireTime();

	/**
	 * 完成任务后sleep时长，单位为分钟
	 * 
	 * @return
	 */
	long getSleepTime();

	/**
	 * 任务锁前缀
	 * 
	 * @return
	 */
	String getKeyPrefix();

	/**
	 * 任务名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 执行具体任务
	 */
	void run();
}
