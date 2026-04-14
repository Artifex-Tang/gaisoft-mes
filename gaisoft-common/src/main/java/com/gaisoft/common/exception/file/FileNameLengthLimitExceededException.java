package com.gaisoft.common.exception.file;

import com.gaisoft.common.exception.file.FileException;

public class FileNameLengthLimitExceededException
extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
