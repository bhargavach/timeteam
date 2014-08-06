package au.com.sap.mcc.timeteam.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpresponse = (HttpServletResponse) response;
		httpresponse.setHeader("Access-Control-Allow-Origin", "*");
		httpresponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		httpresponse.setHeader("Access-Control-Max-Age", "3600");
		httpresponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
