#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.wap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import ${package}.wap.web.filter.AppFilter;
import ${package}.common.AbstractWebConfig;

@Configuration
@ComponentScan("${package}")
@EnableScheduling
public class WebConfig extends AbstractWebConfig {

	@Value("${app.static.cachePeriod}")
	private int cachePeriod;

	@Bean
	public FilterRegistrationBean appFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean();
		reg.setFilter(new AppFilter());
		reg.addUrlPatterns("/*");

		return reg;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/WEB-INF/resources/").setCachePeriod(cachePeriod);
	}
}
