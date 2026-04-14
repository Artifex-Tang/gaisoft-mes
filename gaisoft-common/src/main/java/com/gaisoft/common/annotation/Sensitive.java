package com.gaisoft.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gaisoft.common.config.serializer.SensitiveJsonSerializer;
import com.gaisoft.common.enums.DesensitizedType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
@JacksonAnnotationsInside
@JsonSerialize(using=SensitiveJsonSerializer.class)
public @interface Sensitive {
    public DesensitizedType desensitizedType();
}
