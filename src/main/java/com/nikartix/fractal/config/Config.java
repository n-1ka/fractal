package com.nikartix.fractal.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nikartix.fractal.config.model.FractalConfig;
import com.nikartix.fractal.config.model.ThreadingConfig;

public class Config {

    @JsonProperty("threading")
    private ThreadingConfig threadingConfig;

    @JsonProperty("fractal")
    private FractalConfig fractalConfig;

    public ThreadingConfig getThreadingConfig() {
        return threadingConfig;
    }

    public FractalConfig getFractalConfig() {
        return fractalConfig;
    }

}
