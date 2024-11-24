package md.hajji.serdes;

public class WeatherSerdes{

    /**
     * {@code WeatherSerde} instance
     */
    public static final WeatherSerde WEATHER_SERDE = new WeatherSerde();
    /**
     * {@code WeatherStationStateSerde} instance
     */
    public static final WeatherStationStateSerde WEATHER_STATION_STATE_SERDE =
            new WeatherStationStateSerde();

    /**
     * get {@code WeatherSerde} instance
     * @return {@code WeatherSerde} instance
     */
    public static WeatherSerde weatherSerde(){
        return WEATHER_SERDE;
    }

    /**
     * get {@code WeatherStationStateSerde} instance
     * @return {@code WeatherStationStateSerde} object
     */
    public static WeatherStationStateSerde weatherStateSerde(){
        return WEATHER_STATION_STATE_SERDE;
    }

}


