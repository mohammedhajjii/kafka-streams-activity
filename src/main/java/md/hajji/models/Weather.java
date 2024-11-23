package md.hajji.models;

public record Weather(
        String station,
        double temperature,
        double humidity
) {

    @Override
    public String toString() {
        return String.format("%s,%.2f,%.2f", station, temperature, humidity);
    }
}
