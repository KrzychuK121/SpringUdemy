package com.example.demo.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE) // Ten filtr wykona się przed wszystkimi innymi filtrami
@Component
public class LoggerFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    public void doFilter(
        final ServletRequest request,
        final ServletResponse response,
        final FilterChain chain
    ) throws IOException, ServletException {
        if(!(request instanceof HttpServletRequest)){
            chain.doFilter(request, response);
            return;
        }

        var httpRequest = (HttpServletRequest) request;
        logger.info(
            "[doFilter] " +
            httpRequest.getMethod() +" " +
            httpRequest.getRequestURI()
        );

        chain.doFilter(request, response);
        //logger.info("[doFilter 2]");
    }
}
