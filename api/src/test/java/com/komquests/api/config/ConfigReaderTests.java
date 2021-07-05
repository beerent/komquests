package com.komquests.api.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigReaderTests {

    @Test
    public void configReaderReturnsValueFromKey() {
        String configContents = "key=value";
        ConfigReader configReader = new ConfigReader(configContents);
        String value = configReader.getValue("key");

        assertEquals(value, "value");
    }

    @Test
    public void configReaderReturnsValueFromKeyWithMultiplePairs() {
        String configContents = "key1=value1\nkey2=value2";
        ConfigReader configReader = new ConfigReader(configContents);
        String value = configReader.getValue("key2");

        assertEquals(value, "value2");
    }

    @Test
    public void configReaderGracefullyReadsLineWithNoValue() {
        String configContents = "key1=value1\nkey2";
        ConfigReader configReader = new ConfigReader(configContents);
        String value = configReader.getValue("key2");

        assertNull(value);
    }

    @Test
    public void configReaderReturnsNullIfNoValueFound() {
        String configContents = "key1=value1\nkey2=value2";
        ConfigReader configReader = new ConfigReader(configContents);
        String value = configReader.getValue("key3");

        assertNull(value);
    }
}
