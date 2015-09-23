#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ${package}.common.model.Page;

/**
 * mybatis 分页拦截器抽象类
 */
public abstract class MybatisPageInterceptor implements Interceptor {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected static String DEFAULT_PAGE_SQL_ID = ".*(Page|PageBy.+)$";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);

		while (metaStatementHandler.hasGetter("h")) {
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = SystemMetaObject.forObject(object);
		}

		while (metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = SystemMetaObject.forObject(object);
		}

		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		if (mappedStatement.getId().matches(DEFAULT_PAGE_SQL_ID)) {
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			Object paramterObject = boundSql.getParameterObject();
			Page<?> page = getPageFromParamters(paramterObject);
			if (page != null) {
				String sql = boundSql.getSql();
				String pageSql = getPageSql(page, sql);
				if (page.isCountRecords()) {
					int totalRecords = getCount(mappedStatement, paramterObject, boundSql, sql);
					page.setTotalRecords(totalRecords);
				}
				metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
			}
		}
		return invocation.proceed();
	}

	@SuppressWarnings("unchecked")
	protected Page<?> getPageFromParamters(Object paramterObject) {
		if (paramterObject == null) {
			return null;
		} else if (paramterObject instanceof Page<?>) {
			return (Page<?>) paramterObject;
		} else if (paramterObject instanceof Map<?, ?>) {
			Map<String, ?> map = (Map<String, ?>) paramterObject;
			Object obj = map.get("page");
			if (obj != null && obj instanceof Page<?>) {
				return (Page<?>) obj;
			}
		}
		return null;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}

	public int getCount(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, String sql) throws SQLException {
		String count_sql = getCountSql(sql);
		Connection connection = null;
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
			countStmt = connection.prepareStatement(count_sql);
			DefaultParameterHandler handler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
			handler.setParameters(countStmt);
			rs = countStmt.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return count;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} finally {
				try {
					if (countStmt != null) {
						countStmt.close();
					}
				} finally {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				}
			}
		}
	}

	protected abstract String getPageSql(Page<?> page, String sql);

	protected abstract String getCountSql(String sql);
}
