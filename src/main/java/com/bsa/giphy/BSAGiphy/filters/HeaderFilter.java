package com.bsa.giphy.BSAGiphy.filters;

import com.bsa.giphy.BSAGiphy.exception.Handler;
import com.bsa.giphy.BSAGiphy.exception.NoBsaGiphyHeaderException;
import org.apache.catalina.core.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HeaderFilter implements Filter {

    private Handler handler;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, NoBsaGiphyHeaderException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String header = request.getHeader("X-BSA-GIPHY");

        if(header == null) {
//            handler.handleNoBsaGiphyException(new NoBsaGiphyHeaderException("header was not found!"));
            throw new NoBsaGiphyHeaderException(); // idk why this dont work :(
        }

        filterChain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());

        this.handler = ctx.getBean(Handler.class);
    }

    @Override
    public void destroy() {

    }


}
