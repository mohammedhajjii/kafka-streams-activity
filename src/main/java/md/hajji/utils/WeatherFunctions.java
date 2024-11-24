package md.hajji.utils;


import md.hajji.models.Weather;
import md.hajji.models.WeatherStationState;
import org.apache.kafka.streams.KeyValue;

public class WeatherFunctions {

    /**
     * cast a weather string like "station,temperature,humidity" to a Weather instance
     * @param weatherString: weather string like "station,temperature,humidity"
     * @return : Weather instance
     */
    public static Weather from(String weatherString){
        String[] tokens = weatherString.split(",");
        return new Weather(
                tokens[0],
                Double.parseDouble(tokens[1]),
                Double.parseDouble(tokens[2])
        );
    }

    /**
     * map a Weather instance into another by changing temperature unit from cellulitis to Fahrenheit
     * @param weather : weather object
     * @return : new {@code Weather} object with temperature expressed in Fahrenheit unit.
     */
    public static Weather mapToFahrenheit(Weather weather){
        double fahrenheit = (weather.temperature() * 9/5) + 32;
        return new Weather(weather.station(), fahrenheit, weather.humidity());
    }

    /**
     * return a boolean value based on Weather instance temperature
     * used to filter weather instances if the temperature is under 30 Cel
     * @param weather: weather object
     * @return : {@code true} if temperature is over 30 Cel, else {@code false}
     */
    public static boolean filterWeatherByTemperature(Weather weather){
        return weather.temperature() > 30.0;
    }

    /**
     * log in the console key and value of a kafka record for a special debugging stage
     * @param key: record key
     * @param value: record value
     * @param stage: Debugging stage
     */
    public static void log(String key, Object value, DebuggingStage stage){
        System.out.println("[" + stage + "]: {key: " + key + ", value: " + value + "}");
    }

    /**
     * summarize a final state with s simple string contains important infos
     * @param station: station name
     * @param state: {@code WeatherStationState} instance that describe the final state of given station
     * @return : {@code KeyValue} pair with station as a key and weatherState summary.
     */
    public static KeyValue<String, String> summarizeState(String station, WeatherStationState state){
        return new KeyValue<>(station, state.summarize(station));
    }

    /**
     * weather state aggregator for aggregation function
     * @param station : record key
     * @param weather: record value as {@code Weather} instance
     * @param state: the current {@code WeatherStationState} object that describe current state of aggregator.
     * @return : the new {@code WeatherStationState} object that describe new state of aggregator.
     */
    public static WeatherStationState weatherStateAggregator(
            String station,
            Weather weather,
            WeatherStationState state){
        return state.update(weather);
    }

}
