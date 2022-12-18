package cc.niushuai.framework.spring.boot.requestlog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json util
 *
 * @author niushuai233
 * @date: 2022/12/16 21:36
 */
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String toJsonStr(T obj) {

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toBean(String json, Class<T> clazz) {

        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
