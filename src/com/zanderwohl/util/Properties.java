package com.zanderwohl.util;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Properties {

    public static Map<String, String> toMap(String input){
        Map<String, String> properties = new HashMap<>();

        String[] lines = input.split("\n");
        for(String line: lines){
            String[] pair = line.split("=");
            properties.put(pair[0].trim(), pair[1].trim());
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

}
