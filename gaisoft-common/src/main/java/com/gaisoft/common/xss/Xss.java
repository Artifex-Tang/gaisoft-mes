package com.gaisoft.common.xss;

import com.gaisoft.common.xss.XssValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy={XssValidator.class})
public @interface Xss {
    public String message() default "不允许任何脚本运行";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
