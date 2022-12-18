package cc.niushuai.framework.spring.boot.requestlog;

import cc.niushuai.framework.spring.boot.requestlog.aspect.RequestLogAspect;
import cc.niushuai.framework.spring.boot.requestlog.filter.RequestLogFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * request log 自动配置类
 *
 * @author niushuai233
 * @date: 2022/12/16 21:36
 */
@Configuration
@EnableConfigurationProperties(RequestLogProperties.class)
public class RequestLogAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RequestLogAutoConfiguration.class);

    @Resource
    private RequestLogProperties requestLogProperties;


    /**
     * 打印请求详情aop bean
     *
     * @author niushuai
     * @date: 2022/11/9 9:19
     * @return: {@link RequestLogAspect}
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.request-log", name = "enable", havingValue = "true")
    public RequestLogAspect requestAspect() {
        log.info("Init RequestLogAspect Bean");
        return new RequestLogAspect();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.request-log", name = "enable", havingValue = "true")
    public RegistrationBean requestLogFilter() {
        FilterRegistrationBean<RequestLogFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setName(RequestLogFilter.class.getSimpleName());
        filterBean.setFilter(new RequestLogFilter());
        filterBean.setOrder(1);

        List<String> urlPatterns = requestLogProperties.getUrlPatterns();
        if (null != urlPatterns) {
            String[] patterns = urlPatterns.toArray(new String[urlPatterns.size()]);
            filterBean.addUrlPatterns(patterns);
            log.info("Register Request Log Filter Pattern: " + patterns);
        }

        return filterBean;
    }

    @PostConstruct
    public void setProperties() {

        log.info("Request Log Auto Configuration Succeed");
    }
}
