package md.hajji.models;

public record WeatherState(
        long count,
        double temperatures,
        double humidities
) {

    public static WeatherState initialize(){
        return new WeatherState(0L, 0.0, 0.0);
    }

    public WeatherState update(Weather weather){
        return new WeatherState(
                count + 1,
                temperatures + weather.temperature(),
                humidities + weather.humidity()
        );
    }


}

