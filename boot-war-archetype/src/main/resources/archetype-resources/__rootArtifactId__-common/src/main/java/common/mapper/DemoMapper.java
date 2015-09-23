#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import ${package}.common.model.Demo;

public interface DemoMapper {

	@Select("select id,name,created_at as createdAt,updated_at as updatedAt,status from demo where id=#{id}")
	Demo findById(String id);

	@Update("update demo set name=#{name},updated_at=#{updatedAt} where id=#{id}")
	void update(Demo demo);

	@Insert("insert into demo(name,created_at,updated_at,status) values(#{name},#{createdAt},#{updatedAt},1)")
	void save(Demo demo);

	@Delete("delete from demo where id=#{id}")
	void delete(String id);
}
