package com.gaisoft.common.exception.user;

import com.gaisoft.common.exception.base.BaseException;

public class UserException
extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
