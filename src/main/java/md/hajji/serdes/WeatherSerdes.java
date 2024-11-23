package md.hajji.serdes;

public class WeatherSerdes{

    public static final WeatherSerde WEATHER_SERDE = new WeatherSerde();
    public static final WeatherStateSerde WEATHER_STATE_SERDE = new WeatherStateSerde();

    public static WeatherSerde weatherSerde(){
        return WEATHER_SERDE;
    }

    public static WeatherStateSerde weatherStateSerde(){
        return WEATHER_STATE_SERDE;
    }

}


