package com.nikartix.fractal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;

public class ConfigLoader {

    private static volatile ConfigLoader instance;

    private String configPath;
    private Config cachedConfig;

    private ConfigLoader(String configPath) {
        this.configPath = configPath;
    }

    public static ConfigLoader getInstance(String configPath) {
        if (instance == null) {
            synchronized (ConfigLoader.class) {
                instance = new ConfigLoader(configPath);
            }
        }

        return instance;
    }

    private Config loadConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(new File(configPath), Config.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Config getConfig() {
        if (cachedConfig == null) {
            synchronized (this) {
                cachedConfig = loadConfig();
            }
        }

        return cachedConfig;
    }

    public static void debug() {
        Config config = ConfigLoader.getInstance("src/main/resources/config.yaml").getConfig();
        System.out.println(config);
        System.out.println(ReflectionToStringBuilder.toString(config, ToStringStyle.MULTI_LINE_STYLE));
    }

}
