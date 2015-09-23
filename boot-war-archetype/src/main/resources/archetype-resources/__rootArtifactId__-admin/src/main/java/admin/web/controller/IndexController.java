#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.admin.web.controller;

import ${package}.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
public class IndexController extends BaseController {

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/accessDenied")
	@ResponseBody
	public String accessDenied() {
		return "你没有权限访问该页面";
	}

	@RequestMapping("/expired")
	@ResponseBody
	public String expired() {
		return "会话过期，请重新登录";
	}
}
