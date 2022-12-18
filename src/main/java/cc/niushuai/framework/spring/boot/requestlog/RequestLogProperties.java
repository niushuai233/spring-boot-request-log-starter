package cc.niushuai.framework.spring.boot.requestlog;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * request log properties
 *
 * @author niushuai233
 * @date: 2022/12/16 21:36
 */
@ConfigurationProperties(prefix = "spring.request-log")
public class RequestLogProperties {

    private Boolean enable = Boolean.TRUE;

    private List<String> urlPatterns = Arrays.asList("/**");

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    @Override
    public String toString() {
        return "RequestLogProperties [enable = " + this.enable + ", urlPattern = " + this.urlPatterns.stream().collect(Collectors.joining(",")) + "]";
    }
}
