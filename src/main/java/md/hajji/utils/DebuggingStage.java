package md.hajji.utils;

/**
 * distinct the debugging state
 */
public enum DebuggingStage {

    READ, // for the first time we read from weather-data topic
    CONVERT, // after weather string has converted to weather instance
    FILTER, // after remove records witch a temp < 30 cel
    MAP_TO_FAHRENHEIT, // after mapping temperature of a record from cel to Fah
    AGGREGATE, // after applying aggregation function
    SUMMARIZE // after convert final state to a string
}
