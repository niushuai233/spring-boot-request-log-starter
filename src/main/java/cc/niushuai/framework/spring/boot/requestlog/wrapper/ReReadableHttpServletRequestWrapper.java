package cc.niushuai.framework.spring.boot.requestlog.wrapper;

import cc.niushuai.framework.spring.boot.requestlog.util.IoUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 使body可重复读的 request wrapper
 *
 * @author niushuai
 * @date 2022/11/9 11:33
 */
public class ReReadableHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public ReReadableHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        body = IoUtil.readBytes(request.getInputStream());
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {

            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

}
