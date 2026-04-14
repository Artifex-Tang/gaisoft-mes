package com.gaisoft.common.utils.file;

import com.gaisoft.common.config.RuoYiConfig;
import com.gaisoft.common.utils.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtils {
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] getImage(String imagePath) {
        InputStream is = ImageUtils.getFile(imagePath);
        try {
            byte[] byArray = IOUtils.toByteArray((InputStream)is);
            return byArray;
        }
        catch (Exception e) {
            log.error("图片加载异常 {}", (Throwable)e);
            byte[] byArray = null;
            return byArray;
        }
        finally {
            IOUtils.closeQuietly((Closeable)is);
        }
    }

    public static InputStream getFile(String imagePath) {
        try {
            byte[] result = ImageUtils.readFile(imagePath);
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        }
        catch (Exception e) {
            log.error("获取图片异常 {}", (Throwable)e);
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] readFile(String url) {
        InputStream in = null;
        try {
            if (url.startsWith("http")) {
                URL urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.setConnectTimeout(30000);
                urlConnection.setReadTimeout(60000);
                urlConnection.setDoInput(true);
                in = urlConnection.getInputStream();
            } else {
                String localPath = RuoYiConfig.getProfile();
                String downloadPath = localPath + StringUtils.substringAfter(url, "/profile");
                in = new FileInputStream(downloadPath);
            }
            byte[] result = IOUtils.toByteArray((InputStream)in);
            IOUtils.closeQuietly((Closeable)in);
            return result;
        }
        catch (Exception e) {
            log.error("获取文件路径异常 {}", (Throwable)e);
            byte[] byArray = null;
            return byArray;
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }
}
