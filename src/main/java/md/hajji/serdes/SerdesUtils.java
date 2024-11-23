package md.hajji.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;

public class SerdesUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows({JsonProcessingException.class})
    public static byte[] write(Object source){
        return OBJECT_MAPPER.writeValueAsBytes(source);
    }

    @SneakyThrows({IOException.class})
    public static <T> T read(byte[] source, Class<T> clazz){
        return OBJECT_MAPPER.readValue(source, clazz);
    }

}
