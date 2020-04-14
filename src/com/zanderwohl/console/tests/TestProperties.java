package com.zanderwohl.console.tests;

import com.zanderwohl.util.Properties;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

public class TestProperties {

    @Test
    public void testToMap(){
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, String> result;

        result = Properties.toMap("");
        assertEquals(map, result);

        map.put("Hello","World");
        result = Properties.toMap("Hello=World\n");
        assertEquals(map, result);

        map.put("Another","Entry");
        result = Properties.toMap("Hello=World\nAnother=Entry\n");
        assertEquals(map, result);
    }

    @Test
    public void testToString(){
        HashMap<String, String> map = new HashMap<>();
        String result;
        map.put("Hello", "World");
        result = Properties.toString(map);
        assertEquals("Hello=World\n", result);

        map.put("Second", "Entry");
        result = Properties.toString(map);
        assertEquals("Hello=World\nSecond=Entry\n", result);
    }

    @Test
    public void testToStringBlankKey(){
        HashMap<String, String> map = new HashMap<>();
        String result;
        map.put("", "value");
        result = Properties.toString(map);
        assertEquals("=value\n", result);
    }

    @Test
    public void testToStringBlankValue(){
        HashMap<String, String> map = new HashMap<>();
        String result;
        map.put("key", "");
        result = Properties.toString(map);
        assertEquals("key=\n", result);
    }

}
