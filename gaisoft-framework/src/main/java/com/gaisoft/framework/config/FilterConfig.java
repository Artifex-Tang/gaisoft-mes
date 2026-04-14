package com.gaisoft.framework.config;

import com.gaisoft.common.filter.RepeatableFilter;
import com.gaisoft.common.filter.XssFilter;
import com.gaisoft.common.utils.StringUtils;
import java.util.HashMap;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Value(value="${xss.excludes}")
    private String excludes;
    @Value(value="${xss.urlPatterns}")
    private String urlPatterns;

    @Bean
    @ConditionalOnProperty(value={"xss.enabled"}, havingValue="true")
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST, new DispatcherType[0]);
        registration.setFilter((Filter)new XssFilter());
        registration.addUrlPatterns(StringUtils.split((String)this.urlPatterns, (String)","));
        registration.setName("xssFilter");
        registration.setOrder(Integer.MIN_VALUE);
        HashMap<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("excludes", this.excludes);
        registration.setInitParameters(initParameters);
        return registration;
    }

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter((Filter)new RepeatableFilter());
        registration.addUrlPatterns(new String[]{"/*"});
        registration.setName("repeatableFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }
}
