package cc.niushuai.framework.spring.boot.requestlog.util;

/**
 * 字符串工具类
 *
 * @author niushuai233
 * @date: 2022/12/16 21:36
 */
public class StrUtil {


    public static final String DOT = ".";
    public static final String AMP = "&";
    public static final String EQUAL = "=";

    public static String join(String conjunction, Object... params) {

        if (null == conjunction) {
            throw new IllegalArgumentException("conjunction required");
        }

        if (null == params || params.length == 0) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        for (Object param : params) {
            buffer.append(param).append(conjunction);
        }
        return buffer.substring(0, buffer.length() - conjunction.length() - 1);
    }

    public static boolean isEmpty(String obj) {
        return null == obj || obj.length() == 0;
    }

    public static String repeat(String str, int length) {

        if (length < 0) {
            return str;
        }
        String base = "";
        for (int i = 0; i < length; i++) {
            base += str;
        }
        return base;
    }
}
