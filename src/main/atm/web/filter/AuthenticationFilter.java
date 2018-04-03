// Copyright 2001-2015: Thomson Reuters Global Resources: All Rights Reserved. Proprietary and confidential information
// of TRGR. Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
package main.atm.web.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.activation.UnsupportedDataTypeException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {
	
	private static Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
	
	public AuthenticationFilter() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		HttpServletRequest newRequest = null;

		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;

			filterChain.doFilter(httpRequest, httpResponse);
		} else {
			throw new UnsupportedDataTypeException("request or response needs to be HttpServletRequest or HttpServletResponse");
		}
	}
}
