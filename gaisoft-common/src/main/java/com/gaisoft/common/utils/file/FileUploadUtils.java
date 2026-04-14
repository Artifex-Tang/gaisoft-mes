package com.gaisoft.common.utils.file;

import com.gaisoft.common.config.RuoYiConfig;
import com.gaisoft.common.exception.file.FileNameLengthLimitExceededException;
import com.gaisoft.common.exception.file.FileSizeLimitExceededException;
import com.gaisoft.common.exception.file.InvalidExtensionException;
import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.file.MimeTypeUtils;
import com.gaisoft.common.utils.uuid.Seq;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtils {
    public static final long DEFAULT_MAX_SIZE = 0x3200000L;
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;
    private static String defaultBaseDir = RuoYiConfig.getProfile();

    public static void setDefaultBaseDir(String defaultBaseDir) {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir() {
        return defaultBaseDir;
    }

    public static final String upload(MultipartFile file) throws IOException {
        try {
            return FileUploadUtils.upload(FileUploadUtils.getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public static final String upload(String baseDir, MultipartFile file) throws IOException {
        try {
            return FileUploadUtils.upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension) throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        int fileNamelength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNamelength > 100) {
            throw new FileNameLengthLimitExceededException(100);
        }
        FileUploadUtils.assertAllowed(file, allowedExtension);
        String fileName = FileUploadUtils.extractFilename(file);
        String absPath = FileUploadUtils.getAbsoluteFile(baseDir, fileName).getAbsolutePath();
        file.transferTo(Paths.get(absPath, new String[0]));
        return FileUploadUtils.getPathFileName(baseDir, fileName);
    }

    public static final String extractFilename(MultipartFile file) {
        return StringUtils.format("{}/{}_{}.{}", DateUtils.datePath(), FilenameUtils.getBaseName((String)file.getOriginalFilename()), Seq.getId("UPLOAD"), FileUploadUtils.getExtension(file));
    }

    public static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);
        if (!desc.exists() && !desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return desc;
    }

    public static final String getPathFileName(String uploadDir, String fileName) throws IOException {
        int dirLastIndex = RuoYiConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        return "/profile/" + currentDir + "/" + fileName;
    }

    public static final void assertAllowed(MultipartFile file, String[] allowedExtension) throws FileSizeLimitExceededException, InvalidExtensionException {
        long size = file.getSize();
        if (size > 0x3200000L) {
            throw new FileSizeLimitExceededException(50L);
        }
        String fileName = file.getOriginalFilename();
        String extension = FileUploadUtils.getExtension(file);
        if (allowedExtension != null && !FileUploadUtils.isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension, fileName);
            }
            if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension, fileName);
            }
            if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension, fileName);
            }
            if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION) {
                throw new InvalidExtensionException.InvalidVideoExtensionException(allowedExtension, extension, fileName);
            }
            throw new InvalidExtensionException(allowedExtension, extension, fileName);
        }
    }

    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (!str.equalsIgnoreCase(extension)) continue;
            return true;
        }
        return false;
    }

    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension((String)file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }
}
