package md.hajji.serdes;

import md.hajji.models.Weather;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;


public class WeatherSerde implements Serde<Weather> {

    private final WeatherSerializer weatherSerializer = new WeatherSerializer();
    private final WeatherDeserializer weatherDeserializer = new WeatherDeserializer();

    @Override
    public Serializer<Weather> serializer() {
        return weatherSerializer;
    }

    @Override
    public Deserializer<Weather> deserializer() {
        return weatherDeserializer;
    }
}

class WeatherSerializer implements Serializer<Weather> {


    @Override
    public byte[] serialize(String s, Weather weather) {
        return SerdesUtils.write(weather);
    }
}

class WeatherDeserializer implements Deserializer<Weather> {

    @Override
    public Weather deserialize(String s, byte[] bytes) {
        return SerdesUtils.read(bytes, Weather.class);
    }
}