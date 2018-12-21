package fractal.painter;

import fractal.FractalDepthPainter;

import java.awt.*;

public abstract class ColorGradientFractalDepthPainter implements FractalDepthPainter {

    abstract Color[] getColors();

    abstract int getGradientSize();

    abstract Color getTerminalColor();

    protected Color weightedSum(double alpha, Color a, Color b) {
        alpha = Math.max(0.0, Math.min(alpha, 1.0));
        double beta = 1 - alpha;
        return new Color(
                (int) (a.getRed() * alpha + b.getRed() * beta),
                (int) (a.getGreen() * alpha + b.getGreen() * beta),
                (int) (a.getBlue() * alpha + b.getBlue() * beta)
        );
    }

    @Override
    public Color generateColor(int depth, int maxDepth) {
        if (depth == maxDepth)
            return getTerminalColor();

        Color[] colors = getColors();
        int gradientSize = getGradientSize();
        int totalSize = colors.length * gradientSize;

        int currentIndex = depth % totalSize;
        int beginColorIndex = currentIndex / gradientSize;
        int offset = currentIndex - beginColorIndex * gradientSize;

        Color beginColor = colors[beginColorIndex];
        Color endColor = colors[(beginColorIndex + 1) % colors.length];
        double beginFraction = 1.0 - offset / ((double) gradientSize);

        return weightedSum(beginFraction, beginColor, endColor);
    }

}
