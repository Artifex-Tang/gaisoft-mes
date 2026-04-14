package com.gaisoft.common.exception.user;

import com.gaisoft.common.exception.user.UserException;

public class UserPasswordRetryLimitExceedException
extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitExceedException(int retryLimitCount, int lockTime) {
        super("user.password.retry.limit.exceed", new Object[]{retryLimitCount, lockTime});
    }
}
