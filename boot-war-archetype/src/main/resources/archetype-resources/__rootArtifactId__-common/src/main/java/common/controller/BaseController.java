#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

	protected HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		return request;
	}

	protected HttpSession getSession() {
		HttpServletRequest request = getRequest();

		return request.getSession();
	}

    protected String getReqHost() {
        HttpServletRequest request = getRequest();

        String fh = request.getHeader("x-forwarded-for");
		return fh == null ? request.getRemoteHost() : fh;
	}

    protected String getReqAgent() {
        HttpServletRequest request = getRequest();

        return request.getHeader("User-Agent");
    }

    protected String getReqUrl() {
        HttpServletRequest request = getRequest();

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        return path;
    }

	protected Cookie[] getCookies() {
		HttpServletRequest request = getRequest();

		return request.getCookies();
	}

	protected Object getSessionAttribute(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}

		HttpSession session = getSession();
		if (null != session) {
			return session.getAttribute(key);
		}

		return null;
	}

	protected void setSessionAttribute(String key, Object value) {
		if (StringUtils.isBlank(key)) {
			return;
		}

		HttpSession session = getSession();
		if (null != session) {
			session.setAttribute(key, value);
		}
	}

	protected void setSessionMaxInactiveInterval(int max) {
		HttpSession session = getSession();

		session.setMaxInactiveInterval(max);
	}

	protected String getCookie(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}

		Cookie[] cookies = getCookies();
		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}

	protected void addCookie(HttpServletResponse response, String key, String value, int expiry) {
		if (StringUtils.isBlank(key) || null == response) {
			return;
		}

		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		cookie.setMaxAge(expiry);
		response.addCookie(cookie);
	}
	
	/**
	 * 仅当前会话中有效，关闭浏览器删除Cookie
	 * @param response
	 * @param key
	 * @param value
	 */
	protected void addCookie(HttpServletResponse response, String key, String value) {
		addCookie(response, key, value,-1);
	}
	
	protected void deleteCookie(HttpServletResponse response, String key) {
		addCookie(response, key, null, 0);
	}
	

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport(Boolean.valueOf(true)) {

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				super.getClass();
				if (StringUtils.isBlank(text)) {
					setValue(null);
					return;
				}
				if (StringUtils.isBlank(text)) {
					throw new IllegalArgumentException("Could not parse date: it is null");
				}

				if (NumberUtils.isDigits(text)) {
					setValue(new Date(NumberUtils.toLong(text)));
					return ;
				}

				Date parseDate = null;
				try {
					parseDate = DateUtils.parseDate(text, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
				} catch (ParseException e) {
					// e.printStackTrace();
				}
				setValue(parseDate);
			}

			@Override
			public String getAsText() {
				Date value = (Date) getValue();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				return df.format(value);
			}
		});
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
		binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, null, true));
		binder.registerCustomEditor(Integer.TYPE, null, new CustomNumberEditor(Integer.class, null, true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
		binder.registerCustomEditor(Long.TYPE, null, new CustomNumberEditor(Long.class, null, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
		binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
	}
}
