package com.nikartix.fractal.util;

import com.nikartix.fractal.math.Mfloat;
import com.nikartix.fractal.math.RectArea;
import com.nikartix.fractal.math.CircleArea;

public class MathUtil {

    public static int ceilDiv(int dividend, int divisor) {
        return (dividend + divisor - 1) / divisor;
    }

    /**
     * Converts circle area int a rectangular area,
     *  given a width to height ratio {whRatio}
     *  so that the resulting rectangular area contains
     *  the square area and at least
     * @param whRatio - width/height ratio of a resulting rectangle.
     * @return Rectangle that contains passed circle.
     */
    public static RectArea squareToRect(CircleArea circleArea, double whRatio) {
        Mfloat diameter = circleArea.getDiameter();
        Mfloat width = diameter;
        Mfloat height = diameter;

        if (whRatio > 1.0) {
            width = diameter.mul(whRatio);
        } else {
            height = diameter.div(whRatio);
        }

        Mfloat x0 = circleArea.getCenterX().sub(width.div(2.0));
        Mfloat y0 = circleArea.getCenterY().sub(height.div(2.0));

        return new RectArea(
                x0, x0.add(width),
                y0, y0.add(height)
        );
    }

}
