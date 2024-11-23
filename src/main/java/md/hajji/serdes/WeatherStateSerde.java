package md.hajji.serdes;

import md.hajji.models.WeatherState;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;


public class WeatherStateSerde implements Serde<WeatherState> {

    private final WeatherStateSerializer weatherStateSerializer = new WeatherStateSerializer();
    private final WeatherStateDeserializer weatherStateDeserializer = new WeatherStateDeserializer();

    private static final WeatherStateSerde INSTANCE = new WeatherStateSerde();

    @Override
    public Serializer<WeatherState> serializer() {
        return weatherStateSerializer;
    }

    @Override
    public Deserializer<WeatherState> deserializer() {
        return weatherStateDeserializer;
    }

}



class WeatherStateSerializer implements Serializer<WeatherState> {
    @Override
    public byte[] serialize(String s, WeatherState state) {
        return SerdesUtils.write(state);
    }
}

class WeatherStateDeserializer implements Deserializer<WeatherState> {

    @Override
    public WeatherState deserialize(String s, byte[] bytes) {
        return SerdesUtils.read(bytes, WeatherState.class);
    }
}