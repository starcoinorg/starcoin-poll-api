package org.starcoin.poll.api.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HttpRequestUriFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestUriFilter.class);

    private final static String[] ALLOWED_PATHS = new String[]{"/v1/polls", "/poll-api-doc", "/swagger", "/v3/api-docs", "/favicon.ico"};

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        if (isIllegalUri(uri)) {
            chain.doFilter(request, response);
        } else {
            LOG.info("Intercepted URI：{}", uri);
            doResponseFailure(response, uri);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isIllegalUri(String uri) {
        for (String str : ALLOWED_PATHS) {
            if (uri.indexOf(str) == 0) {
                return true;
            }
        }
        return false;
    }


    private void doResponseFailure(ServletResponse servletResponse, String uri) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (IOException exception) {
            LOG.error("Response sendError error. Request URI: " + uri, exception);
        }
    }
}
