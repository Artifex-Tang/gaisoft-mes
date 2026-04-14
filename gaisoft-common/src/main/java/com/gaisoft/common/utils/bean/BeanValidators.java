package com.gaisoft.common.utils.bean;

import java.util.Set;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

public class BeanValidators {
    public static void validateWithException(Validator validator, Object object, Class<?> ... groups) throws ConstraintViolationException {
        Set constraintViolations = validator.validate(object, (Class[])groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
