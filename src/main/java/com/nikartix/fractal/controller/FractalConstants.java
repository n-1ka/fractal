package com.nikartix.fractal.controller;

import com.nikartix.fractal.fractal.FractalFunction;
import com.nikartix.fractal.fractal.mandelbrot.MandelbrotFractalFunction;
import com.nikartix.fractal.repository.DepthPaintersRepository;

class FractalConstants {

//    static final FractalFunction INITIAL_FRACTAL_FUNCTION = new JuliaFractalFunction(Number.buildComplex(-0.4, 0.6));
    static final FractalFunction INITIAL_FRACTAL_FUNCTION = new MandelbrotFractalFunction();

    static final DepthPaintersRepository PAINTERS_REPOSITORY = DepthPaintersRepository.getInstance();

}
