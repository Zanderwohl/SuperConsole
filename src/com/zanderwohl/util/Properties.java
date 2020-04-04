package com.zanderwohl.util;

import java.util.HashMap;
import java.util.Set;

public class Properties {

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

    public static String toString(HashMap<String, String> input){
        String properties = "";

        Set entries = input.entrySet();
        for(Object entry: entries){
            properties += entry + "\n";
        }

        return properties;
    }

    public static String get(HashMap<String, String> properties, String key){
        return properties.get(key);
    }

}
