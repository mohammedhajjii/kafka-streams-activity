package md.hajji.serdes;

import md.hajji.models.WeatherStationState;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;


public class WeatherStationStateSerde implements Serde<WeatherStationState> {

    // weather station state serializer
    private final WeatherStationStateSerializer
            weatherStationStateSerializer = new WeatherStationStateSerializer();
    // weather station state deserializer
    private final WeatherStationStateDeserializer weatherStationStateDeserializer =
            new WeatherStationStateDeserializer();

    /**
     * serialize {@code WeatherStationState} instance
     * @return {@code Serializer<WeatherStationState>} object
     */

    @Override
    public Serializer<WeatherStationState> serializer() {
        return weatherStationStateSerializer;
    }

    /**
     * deserialize {@code WeatherStationState} instance
     * @return Deserializer<WeatherStationState> object
     */
    @Override
    public Deserializer<WeatherStationState> deserializer() {
        return weatherStationStateDeserializer;
    }

}

/**
 * WeatherStationStateSerializer
 */
class WeatherStationStateSerializer implements Serializer<WeatherStationState> {
    @Override
    public byte[] serialize(String s, WeatherStationState state) {
        return SerdesUtils.write(state);
    }
}

/**
 * WeatherStationStateDeserializer
 */
class WeatherStationStateDeserializer implements Deserializer<WeatherStationState> {

    @Override
    public WeatherStationState deserialize(String s, byte[] bytes) {
        return SerdesUtils.read(bytes, WeatherStationState.class);
    }
}