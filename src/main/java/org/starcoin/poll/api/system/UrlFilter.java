package org.starcoin.poll.api.system;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.starcoin.poll.api.vo.ResultUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class UrlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        System.out.println(uri);
        if ((uri.length() >= 9 && "/v1/polls".equals(uri.substring(0, 9))) || uri.contains("swagger") || uri.contains("v3/api-docs")) {
            chain.doFilter(request, response);
        } else {
            doResponse(response);
        }
    }

    @Override
    public void destroy() {
    }

    private void doResponse(ServletResponse response) {
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSONObject.toJSONString(ResultUtils.failure()));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
