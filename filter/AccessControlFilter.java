package com.ds.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessControlFilter implements Filter {
	public static Logger logger = LoggerFactory.getLogger(AccessControlFilter.class);
	public static final String CLIENT_CONFIG_FILE   = "accessAllow.properties";
    
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods",
				"POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		chain.doFilter(req, res);
	}
	@Override
	public void destroy() {
	}

}
