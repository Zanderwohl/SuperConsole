package com.zanderwohl.tests;


import com.zanderwohl.util.Properties;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TestProperties {

    @Test
    public void testToMap(){
        assertEquals("", "");
    }

    @Test
    public void testToString(){
        HashMap<String, String> map = new HashMap<>();
        String result;
        map.put("Hello","World");
        result = Properties.toString(map);
        assertEquals("Hello=World\n", result);
        map.put("Second","Entry");
        result = Properties.toString(map);
        assertEquals("Hello=World\nSecond=Entry\n", result);
    }

}
