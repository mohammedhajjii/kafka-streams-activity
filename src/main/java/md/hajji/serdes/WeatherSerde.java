package md.hajji.serdes;

import md.hajji.models.Weather;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;


public class WeatherSerde implements Serde<Weather> {

    // weatherSerializer and WeatherDeserializer instances:
    private final WeatherSerializer weatherSerializer = new WeatherSerializer();
    private final WeatherDeserializer weatherDeserializer = new WeatherDeserializer();

    /**
     * get Weather serializer
     * @return {@code WeatherSerializer} instance
     */
    @Override
    public Serializer<Weather> serializer() {
        return weatherSerializer;
    }

    /**
     * get Weather deserializer
     * @return {@code WeatherDeserializer} instance
     */
    @Override
    public Deserializer<Weather> deserializer() {
        return weatherDeserializer;
    }
}

/**
 * {@code WeatherSerializer} used for {@code Weather} Serialization
 */
class WeatherSerializer implements Serializer<Weather> {

    @Override
    public byte[] serialize(String s, Weather weather) {
        return SerdesUtils.write(weather);
    }
}

/**
 * {@code WeatherDeserializer} used for {@code Weather} Deserialization
 */
class WeatherDeserializer implements Deserializer<Weather> {

    @Override
    public Weather deserialize(String s, byte[] bytes) {
        return SerdesUtils.read(bytes, Weather.class);
    }
}