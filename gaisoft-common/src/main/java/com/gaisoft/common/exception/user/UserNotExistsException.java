package com.gaisoft.common.exception.user;

import com.gaisoft.common.exception.user.UserException;

public class UserNotExistsException
extends UserException {
    private static final long serialVersionUID = 1L;

    public UserNotExistsException() {
        super("user.not.exists", (Object[])null);
    }
}
