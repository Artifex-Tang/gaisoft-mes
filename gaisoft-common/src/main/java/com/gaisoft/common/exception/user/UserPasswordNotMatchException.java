package com.gaisoft.common.exception.user;

import com.gaisoft.common.exception.user.UserException;

public class UserPasswordNotMatchException
extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException() {
        super("user.password.not.match", (Object[])null);
    }
}
