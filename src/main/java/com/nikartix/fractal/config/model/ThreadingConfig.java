package com.nikartix.fractal.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThreadingConfig {

    @JsonProperty("n_threads")
    private int nThreads;

    @JsonProperty("task_window_height")
    private int taskWindowHeight;

    @JsonProperty("task_window_width")
    private int taskWindowWidth;

    public int getnThreads() {
        return nThreads;
    }

    public int getTaskWindowHeight() {
        return taskWindowHeight;
    }

    public int getTaskWindowWidth() {
        return taskWindowWidth;
    }

}
