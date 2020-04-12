package com.zanderwohl.util;

import java.util.HashMap;
import java.util.Set;

public class Properties {

    /**
     * Takes properties file contents, key=value format, return separated, and makes it a map.
     * @param input The file's contents.
     * @return The HashMap keyed by the left string.
     */
    public static HashMap<String, String> toMap(String input){
        HashMap<String, String> properties = new HashMap<>();

        String[] lines = input.split("\n");
        for(String line: lines){
            String[] pair = line.split("=");
            if(pair.length > 1) {
                properties.put(pair[0].trim(), pair[1].trim());
            }
        }

        return properties;
    }

    /**
     * Takes a HashMap of keys and values, placing it in a return separated key=value format.
     * @param input The HashMap.
     * @return A string correctly formatted, ready to be written all at once.
     */
    public static String toString(HashMap<String, String> input){
        String properties = "";

        Set entries = input.entrySet();
        for(Object entry: entries){
            properties += entry + "\n";
        }

        return properties;
    }

    /**
     * I don't know why this exists.
     * @param properties
     * @param key
     * @return
     */
    public static String get(HashMap<String, String> properties, String key){
        return properties.get(key);
    }

}
