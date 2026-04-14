package com.gaisoft.common.exception.user;

import com.gaisoft.common.exception.user.UserException;

public class BlackListException
extends UserException {
    private static final long serialVersionUID = 1L;

    public BlackListException() {
        super("login.blocked", (Object[])null);
    }
}
