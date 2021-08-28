package com.cbsinc.cms.publisher.models;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestFilter implements Filter {

    private static ThreadLocal<HttpServletRequest> localRequest = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> localResponse= new ThreadLocal<HttpServletResponse>();


    public static HttpServletRequest getRequest() {
        return localRequest.get();
    }
    
    public static HttpServletResponse getResponse() {
        return localResponse.get();
    }
    

    public static HttpSession getSession() {
        HttpServletRequest request = localRequest.get();
        return (request != null) ? request.getSession() : null;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        
    	if (servletRequest instanceof HttpServletRequest) {
            localRequest.set((HttpServletRequest) servletRequest);
        }
        
        if (servletResponse instanceof HttpServletResponse) {
        	localResponse.set((HttpServletResponse) servletResponse);
        }
        

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            localRequest.remove();
            localResponse.remove();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
    
  
    public static ServletContext getServletContext() 
    {
    	return getRequest().getServletContext() ;
    }
    
}
