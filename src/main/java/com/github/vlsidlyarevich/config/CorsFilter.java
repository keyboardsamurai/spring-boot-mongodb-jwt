package com.github.vlsidlyarevich.config;

import com.github.vlsidlyarevich.security.constants.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CorsFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods",
                "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", SecurityConstants.AUTH_HEADER_NAME+", Content-Type");
        response.setHeader("Access-Control-Expose-Headers", SecurityConstants.AUTH_HEADER_NAME+", Content-Type");

        final HttpServletRequest request = (HttpServletRequest) req;

        if (request.getMethod().equals("OPTIONS")) {
            try {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e) {
                logger.warn("Could not write to response",e);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
