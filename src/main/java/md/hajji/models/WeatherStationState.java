package md.hajji.models;

public record WeatherStationState(
        long count,
        double temperatures,
        double humidities
) {

    /**
     * initialize a new state
     * @return : {@code WeatherStationState} instance
     */
    public static WeatherStationState initialize(){
        return new WeatherStationState(0L, 0.0, 0.0);
    }

    /**
     * emit new {@code WeatherStationStata} computed from {@code Weather} instance
     * @param weather: {@code Weather} instance
     * @return  new {@code WeatherStationStata}
     */
    public WeatherStationState update(Weather weather){
        return new WeatherStationState(
                count + 1,
                temperatures + weather.temperature(),
                humidities + weather.humidity()
        );
    }


    /**
     * summarize a {@code WeatherStationState} instance by computing mean of temperature and
     * humidity mean
     * @param station : appropriate station name
     * @return state summary as {@code String}
     */
    public String summarize(String station){
        return String.format(
                "station: %s, MT: %.2f Fah, MH: %.2f %%",
                station,
                temperatures / count,
                humidities / count
        );
    }


}

