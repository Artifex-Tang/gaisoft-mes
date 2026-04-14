package com.gaisoft.common.annotation;

import com.gaisoft.common.annotation.Excel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Excels {
    public Excel[] value();
}
