package com.nikartix.fractal.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FractalParams {

    @JsonProperty("max_depth")
    private int maxDepth;

    private double edge;

    @JsonProperty("initial_area")
    private FractalArea initialArea;

    @JsonProperty("pixel_scale")
    private double pixelScale;

    @JsonProperty("image_refresh_rate_ms")
    private int imageRefreshRateMs;

    public int getMaxDepth() {
        return maxDepth;
    }

    public double getEdge() {
        return edge;
    }

    public FractalArea getInitialArea() {
        return initialArea;
    }

    public double getPixelScale() {
        return pixelScale;
    }

    public int getImageRefreshRateMs() {
        return imageRefreshRateMs;
    }

}
