package com.nikartix.fractal.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nikartix.fractal.math.CircleArea;
import com.nikartix.fractal.math.Number;

public class FractalArea {

    @JsonProperty("center_x")
    private double centerX;

    @JsonProperty("center_y")
    private double centerY;

    private double diameter;

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getDiameter() {
        return diameter;
    }

    public CircleArea toCircleArea() {
        return new CircleArea(
                Number.buildFloat(centerX),
                Number.buildFloat(centerY),
                Number.buildFloat(diameter)
        );
    }

}
