package md.hajji;

import md.hajji.models.WeatherState;
import md.hajji.serdes.WeatherSerdes;
import md.hajji.utils.DebuggingStage;
import md.hajji.utils.WeatherUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import java.util.Properties;

public class WeatherAnalyser {

    static final String APPLICATION_ID = "weather-analyser-test";
    static final String BOOTSTRAP_SERVERS = "localhost:9092";
    static final String INPUT_TOPIC = "weather-data";
    static final String OUTPUT_TOPIC = "station-averages";

    public static void main(String[] args) {

        Properties kafkaProps = new Properties();
        kafkaProps.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        kafkaProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        kafkaProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        kafkaProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());


        StreamsBuilder builder = new StreamsBuilder();

        builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String()))
                .peek((key, value) -> WeatherUtils.log(key, value, DebuggingStage.READ))
                .mapValues(WeatherUtils::from)
                .peek((key, value) -> WeatherUtils.log(key, value, DebuggingStage.CONVERT))
                .filter((_, weather) -> WeatherUtils.filterWeatherByTemperature(weather))
                .peek((key, value) -> WeatherUtils.log(key, value, DebuggingStage.FILTER))
                .mapValues(WeatherUtils::mapToFahrenheit)
                .peek((key, value) -> WeatherUtils.log(key, value, DebuggingStage.MAP_TO_FAHRENHEIT))
                .groupBy((_, weather) -> weather.station(), Grouped.with(Serdes.String(), WeatherSerdes.weatherSerde()))
                .aggregate(
                        WeatherState::initialize,
                        WeatherUtils::weatherStateAggregator,
                        Materialized.with(Serdes.String(), WeatherSerdes.weatherStateSerde())
                )
                .toStream()
                .peek((key, value) -> WeatherUtils.log(key, value, DebuggingStage.AGGREGATE))
                .map(WeatherUtils::summarizeState)
                .peek((key, value) -> WeatherUtils.log(key, value, DebuggingStage.SUMMARIZE))
                .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));



        KafkaStreams streams = new KafkaStreams(builder.build(), kafkaProps);
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


    }
}
