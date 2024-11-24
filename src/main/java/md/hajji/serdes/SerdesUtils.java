package md.hajji.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;

public class SerdesUtils {

    /**
     * {@code ObjectMapper} instance used for serialization and deserialization
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * write an object as bytes
     * util for byte serialization
     * @param source : serialized object
     * @return {@code byte[]}
     */
    @SneakyThrows({JsonProcessingException.class})
    public static byte[] write(Object source){
        return OBJECT_MAPPER.writeValueAsBytes(source);
    }

    /**
     * deserialize an object of type T
     * @param source: byte array that represent the encoded object
     * @param clazz: the class of the result object
     * @return an object of type {@code T}
     * @param <T> : type parameter that represent the class destination
     */
    @SneakyThrows({IOException.class})
    public static <T> T read(byte[] source, Class<T> clazz){
        return OBJECT_MAPPER.readValue(source, clazz);
    }

}
