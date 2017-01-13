package org.quizzical.frontend.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectFilter implements Filter{

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		String pathInfo = request.getPathInfo();
		if (pathInfo.endsWith("favicon.ico")
				|| pathInfo.endsWith("favicon.png")
				|| pathInfo.endsWith("bundle.js")
				|| pathInfo.endsWith("vendor.bundle.js")) {
			chain.doFilter(req, resp);
		}
		else if (pathInfo.startsWith("/api")) {
			chain.doFilter(req, resp);
		}
		else if(pathInfo.indexOf("index.html") < 0) {
			try {
				request.getRequestDispatcher("/index.html").forward(req, resp);
			} catch (Exception e) {
			}
		} else {
			chain.doFilter(req, resp);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}

}