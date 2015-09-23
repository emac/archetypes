#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
#set( $package2 = $package.replace("\\.", "/") )
package ${package}.common;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import ${package}.common.interceptor.MySQLPageInterceptor;

@Configuration
@MapperScan("${package}.common.mapper")
public class DataSourceConfig {

	@Value("${druid.stat.loginUsername}")
	private String loginUsername;

	@Value("${druid.stat.loginPassword}")
	private String loginPassword;

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		// TODO 这里有BUG，通过java -jar运行不生效
		// sqlSessionFactoryBean.setTypeAliasesPackage("${package}.common.model");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:${package2}/common/mapper/impl/*.xml"));
		// 增加分页拦截器
		sqlSessionFactoryBean.setPlugins(new Interceptor[] { new MySQLPageInterceptor() });

		return sqlSessionFactoryBean.getObject();
	}

	@Bean(initMethod = "init", destroyMethod = "close")
	@ConfigurationProperties(prefix = "datasource.druid")
	public DataSource dataSource() {
		return new DruidDataSource();
	}

	@Bean
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		reg.addInitParameter("loginUsername", loginUsername);
		reg.addInitParameter("loginPassword", loginPassword);
		
		return reg;
	}
}
