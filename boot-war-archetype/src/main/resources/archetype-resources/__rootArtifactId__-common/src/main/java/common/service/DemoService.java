#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${package}.common.mapper.DemoMapper;
import ${package}.common.model.Demo;

@Service
@Transactional
public class DemoService extends BaseService {

	@Autowired
	private DemoMapper demoMapper;

	@Cacheable(value = "demoCache", key = "'demo:'+#id", unless = "#result == null")
	public Demo findById(String id) {
		return demoMapper.findById(id);
	}

	@CacheEvict(value = "demoCache", key = "'demo:'+#demo.id")
	public Demo update(Demo demo) {
		demo.setUpdatedAt(new Date());
		demoMapper.update(demo);
		return demo;
	}

	@CacheEvict(value = "demoCache", key = "'demo:'+#demo.id")
	public Demo save(Demo demo) {
		Date curDate = new Date();
		demo.setCreatedAt(curDate);
		demo.setUpdatedAt(curDate);
		demoMapper.save(demo);
		return demo;
	}

	@CacheEvict(value = "demoCache", key = "'demo:'+#id")
	public void delete(String id) {
		demoMapper.delete(id);
	}

	@CacheEvict(value = "demoCache", allEntries = true)
	public void clearCache() {
	}
}
