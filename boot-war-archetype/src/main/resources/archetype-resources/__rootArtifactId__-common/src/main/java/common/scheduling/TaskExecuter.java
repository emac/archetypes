#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.scheduling;

public interface TaskExecuter {

	void execute(Task task);
}
