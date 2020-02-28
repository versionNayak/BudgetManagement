package com.finlabs.finexa;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JWTAuthenticationFilter /*extends OncePerRequestFilter*/ implements Filter{
	private final static Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	
    @Override
   /* protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException */
    public void doFilter(ServletRequest request1, ServletResponse response1, FilterChain chain)
			throws IOException, ServletException {
    
   	Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
   	HttpServletRequest request = (HttpServletRequest) request1;
   	HttpServletResponse response = (HttpServletResponse) response1;
   	response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-Type,Authorization");
   	System.out.println("request.getHeader(\"Access-Control-Request-Method\")  "+request.getHeader("Access-Control-Request-Method"));
    try {
		//if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
    	if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			System.out.println("Filter");
			response.setStatus(HttpServletResponse.SC_OK);
		    if (log.isDebugEnabled()) {
		        log.debug("do pre flight...");
		    }
		    //response.setHeader("Access-Control-Allow-Origin", "http://localhost");
		   /* response.setHeader("Access-Control-Allow-Origin", "*");
		    response.setHeader("Access-Control-Allow-Methods", "POST,GET,HEAD");
		    response.setHeader("Access-Control-Max-Age", "3600");
		    response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-Type,Accept,x-auth-token,x-xsrf-token,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Access-Control-Allow-Origin,Content-Type, Authorization");*/
		    //response.setHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,x-auth-token");
		   }else {  
		    chain.doFilter(request, response); 
		    }
	} 
    catch (Exception e) {
        SecurityContextHolder.clearContext();
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad request");

    }
  }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
    }
