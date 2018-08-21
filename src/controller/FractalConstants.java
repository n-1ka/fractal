package controller;

import fractal.worker.FractalFunction;
import mandelbrot.DummyFractalFunction;
import math.CircleArea;
import math.Number;
import repository.DepthPaintersRepository;

class FractalConstants {

    static final int INITIAL_MAX_DEPTH = 100;

    static final FractalFunction INITIAL_FRACTAL_FUNCTION = new DummyFractalFunction();

    static final CircleArea INITIAL_AREA = new CircleArea(
            Number.buildFloat(0.0),
            Number.buildFloat(0.0),
            Number.buildFloat(4.0));

    static final DepthPaintersRepository PAINTERS_REPOSITORY = DepthPaintersRepository.getInstance();

}
