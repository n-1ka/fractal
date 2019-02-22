package com.nikartix.fractal.fractal;

import com.nikartix.fractal.math.Mcomplex;

import java.util.Iterator;

public interface FractalFunction {
	
	Iterator<Mcomplex> evaluate(Mcomplex value);

}
