package cc.niushuai.framework.spring.boot.requestlog.filter;

import cc.niushuai.framework.spring.boot.requestlog.util.StrUtil;
import cc.niushuai.framework.spring.boot.requestlog.wrapper.ReReadableHttpServletRequestWrapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest
                && HttpMethod.POST.name().equalsIgnoreCase(((HttpServletRequest) request).getMethod())
                && StrUtil.isEmpty(request.getContentType())
                && (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType()) || request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE))
        ) {
            // post + json 才走这个 其他的还是按照原来的走
            ReReadableHttpServletRequestWrapper requestWrapper = new ReReadableHttpServletRequestWrapper((HttpServletRequest) request);
            chain.doFilter(requestWrapper, response);
            return;
        }

        chain.doFilter(request, response);
    }
}
