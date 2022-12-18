package cc.niushuai.framework.spring.boot.requestlog.aspect;

import cc.niushuai.framework.spring.boot.requestlog.util.IoUtil;
import cc.niushuai.framework.spring.boot.requestlog.util.JsonUtil;
import cc.niushuai.framework.spring.boot.requestlog.util.StrUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

/**
 * 打印请求信息
 *
 * @author niushuai
 * @date 2022/11/8 16:47
 */
@Aspect
public class RequestLogAspect {

    private static final Logger log = LoggerFactory.getLogger(RequestLogAspect.class);

    @Pointcut("execution(public * *..*.*Controller.*(..))")
    public void request() {
    }

    @Before(value = "request()")
    public void beforeRequest(JoinPoint joinPoint) {
        try {
            run(joinPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run(JoinPoint joinPoint) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        StringBuffer buffer = new StringBuffer(System.lineSeparator());
        String head = "===================== " + request.getMethod() + " Request Log " + request.getSession().getId() + " =====================";
        buffer.append(head).append(System.lineSeparator());
        buffer.append("Url: {}").append(System.lineSeparator());
        buffer.append("Content-type: {}").append(System.lineSeparator());

        String params = null;
        switch (request.getMethod()) {
            case "GET":
                params = parseGetParams(request);
                break;
            case "POST":
                params = parsePostParams(request);
                break;
            default:
                log.warn("当前请求类型未适配: {}", request.getMethod());
                return;
        }

        buffer.append("Method: {}").append(System.lineSeparator());
        buffer.append("Params: {}").append(System.lineSeparator());
        buffer.append(StrUtil.repeat("=", head.length()));

        log.info(buffer.toString(), request.getRequestURL().toString(), request.getContentType(), getSignature(joinPoint), params);
    }

    private String getSignature(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return StrUtil.join(StrUtil.DOT, signature.getDeclaringTypeName(), signature.getName());
    }

    private String parseGetParams(HttpServletRequest request) {

        String queryString = request.getQueryString();

        if (StrUtil.isEmpty(queryString)) {
            return null;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("queryString", queryString);
        String[] paramItemArray = queryString.split(StrUtil.AMP);
        for (String item : paramItemArray) {
            String[] e = item.split(StrUtil.EQUAL);
            if (e.length == 2) {
                map.put(e[0], e[1]);
                continue;
            }
            map.put(e[0], "未构成参数");
        }

        return JsonUtil.toJsonStr(map);
    }

    private String parsePostParams(HttpServletRequest request) throws IOException {

        String contentType = request.getContentType();

        if (StrUtil.isEmpty(contentType)) {
            return null;
        } else if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) || contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            // json请求
            return IoUtil.read(request.getInputStream());
        } else {
            // 非json请求 取requestParameter
            return JsonUtil.toJsonStr(request.getParameterMap());
        }
    }
}
