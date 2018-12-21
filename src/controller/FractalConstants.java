package controller;

import fractal.FractalFunction;
import mandelbrot.MandelbrotFractalFunction;
import math.CircleArea;
import math.Number;
import repository.DepthPaintersRepository;

class FractalConstants {

    static final int INITIAL_MAX_DEPTH = 100;

    static final int INITIAL_EDGE = 2;

//    static final FractalFunction INITIAL_FRACTAL_FUNCTION = new JuliaFractalFunction(Number.buildComplex(-0.4, 0.6));
    static final FractalFunction INITIAL_FRACTAL_FUNCTION = new MandelbrotFractalFunction();

    static final CircleArea INITIAL_AREA = new CircleArea(
            Number.buildFloat(0.0),
            Number.buildFloat(0.0),
            Number.buildFloat(4.0));

    static final DepthPaintersRepository PAINTERS_REPOSITORY = DepthPaintersRepository.getInstance();

    static final int INITIAL_PIXEL_SCALE = 2;

    static final int IMAGE_REFRESH_RATE_MS = 500;

}
