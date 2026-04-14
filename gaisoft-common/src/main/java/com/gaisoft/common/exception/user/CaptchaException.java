package com.gaisoft.common.exception.user;

import com.gaisoft.common.exception.user.UserException;

public class CaptchaException
extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super("user.jcaptcha.error", (Object[])null);
    }
}
