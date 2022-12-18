package cc.niushuai.framework.spring.boot.requestlog.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * io util
 *
 * @author niushuai233
 * @date: 2022/12/16 21:36
 */
public class IoUtil {

    public static byte[] readBytes(InputStream inputStream) throws IOException {

        return getByteArrayOutputStream(inputStream).toByteArray();
    }

    public static String read(InputStream inputStream) throws IOException {

        ByteArrayOutputStream data = getByteArrayOutputStream(inputStream);
        return data.toString(StandardCharsets.UTF_8.name());
    }

    private static ByteArrayOutputStream getByteArrayOutputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            data.write(buffer, 0, length);
        }
        data.flush();
        return data;
    }
}
