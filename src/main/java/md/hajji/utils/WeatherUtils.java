package md.hajji.utils;


import md.hajji.models.Weather;
import md.hajji.models.WeatherState;
import org.apache.kafka.streams.KeyValue;

public class WeatherUtils {

    public static Weather from(String weatherString){
        String[] tokens = weatherString.split(",");
        return new Weather(
                tokens[0],
                Double.parseDouble(tokens[1]),
                Double.parseDouble(tokens[2])
        );
    }

    public static Weather mapToFahrenheit(Weather weather){
        double fahrenheit = (weather.temperature() * 9/5) + 32;
        return new Weather(weather.station(), fahrenheit, weather.humidity());
    }

    public static boolean filterWeatherByTemperature(Weather weather){
        return weather.temperature() > 30.0;
    }

    public static void log(String key, Object value, DebuggingStage stage){
        System.out.println("[" + stage + "]: {key: " + key + ", value: " + value + "}");
    }

    public static KeyValue<String, String> summarizeState(String station, WeatherState state){
        double meanTemperature = state.temperatures() / state.count();
        double meanHumidity = state.humidities() / state.count();
        String newValue = String.format("Station: %s, MT: %.2f, MH: %.2f", station, meanTemperature, meanHumidity);
        return new KeyValue<>(station, newValue);
    }

    public static WeatherState weatherStateAggregator(
            String station,
            Weather weather,
            WeatherState state){
        return state.update(weather);
    }

}
