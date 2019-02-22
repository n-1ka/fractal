package com.nikartix.fractal.fractal.painter;

import java.awt.*;

public class GrayFractalDepthPainter extends ColorGradientFractalDepthPainter {

    private static final int DEPTH_REPEAT = 300;

    @Override
    Color[] getColors() {
        return new Color[] {
                Color.BLACK,
                Color.WHITE
        };
    }

    @Override
    int getGradientSize() {
        return 20;
    }

    @Override
    Color getTerminalColor() {
        return Color.BLACK;
    }

}
