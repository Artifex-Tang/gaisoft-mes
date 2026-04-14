package com.gaisoft.framework.aspectj;

import com.gaisoft.common.annotation.DataSource;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.framework.datasource.DynamicDataSourceContextHolder;
import java.lang.reflect.Method;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(value=1)
@Component
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut(value="@annotation(com.gaisoft.common.annotation.DataSource)|| @within(com.gaisoft.common.annotation.DataSource)")
    public void dsPointCut() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Around(value="dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = this.getDataSource(point);
        if (StringUtils.isNotNull((Object)dataSource)) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }
        try {
            Object object = point.proceed();
            return object;
        }
        finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature)point.getSignature();
        DataSource dataSource = (DataSource)AnnotationUtils.findAnnotation((Method)signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }
        return (DataSource)AnnotationUtils.findAnnotation((Class)signature.getDeclaringType(), DataSource.class);
    }
}
