package md.hajji;

import md.hajji.models.WeatherStationState;
import md.hajji.serdes.WeatherSerdes;
import md.hajji.utils.DebuggingStage;
import md.hajji.utils.WeatherFunctions;
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

    //kafka streams properties values:
    static final String APPLICATION_ID = "weather-analyser-application";
    static final String BOOTSTRAP_SERVERS = "localhost:9092";
    static final String INPUT_TOPIC = "weather-data";
    static final String OUTPUT_TOPIC = "station-averages";

    public static void main(String[] args) {

        // define kafka streams properties:
        Properties kafkaProps = new Properties();
        kafkaProps.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        kafkaProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        kafkaProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        kafkaProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // get StreamsBuilder instance:
        StreamsBuilder builder = new StreamsBuilder();

        // start consumer weather string like "station,temperature,humidity"
        //  with String seres for both key and value:
        builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String()))
                // log results of read stage:
                .peek((key, value) -> WeatherFunctions.log(key, value, DebuggingStage.READ))
                // convert every weather string record to Weather instance
                .mapValues(WeatherFunctions::from)
                // log results for Convert stage:
                .peek((key, value) -> WeatherFunctions.log(key, value, DebuggingStage.CONVERT))
                // drop down weather instances with temperature under 30 cel:
                .filter((_, weather) -> WeatherFunctions.filterWeatherByTemperature(weather))
                // log results for Filter stage
                .peek((key, value) -> WeatherFunctions.log(key, value, DebuggingStage.FILTER))
                // convert weather temperature to Fahrenheit unit:
                .mapValues(WeatherFunctions::mapToFahrenheit)
                // log results for Transform stage:
                .peek((key, value) -> WeatherFunctions.log(key, value, DebuggingStage.MAP_TO_FAHRENHEIT))
                // group weather record by station name
                // specifying also serde for both station name as key and weather record as value
                .groupBy((_, weather) -> weather.station(), Grouped.with(Serdes.String(), WeatherSerdes.weatherSerde()))
                // perform aggregation function for calculating sum of humidities and temperatures
                // by station name
                .aggregate(
                        // get new state for every station:
                        WeatherStationState::initialize,
                        // inject aggregator function:
                        WeatherFunctions::weatherStateAggregator,
                        // use String and weatherStateSerde as serde for station name and weatherStationState
                        Materialized.with(Serdes.String(), WeatherSerdes.weatherStateSerde())
                )
                // convert KTable to KStream:
                .toStream()
                // log results of aggregation stage:
                .peek((key, value) -> WeatherFunctions.log(key, value, DebuggingStage.AGGREGATE))
                // convert each final state to string summary:
                .map(WeatherFunctions::summarizeState)
                // log results for summarize stage:
                .peek((key, value) -> WeatherFunctions.log(key, value, DebuggingStage.SUMMARIZE))
                // write results to output topic: station-averages:
                .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));



        // define kakfka streams object with appropriate builder and properties:
        KafkaStreams streams = new KafkaStreams(builder.build(), kafkaProps);
        //start kafkaStreams process:
        streams.start();
        // add nice way to shuting down kafka streams process:
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


    }
}
