package mandelbrot;

import fractal.worker.FractalFunction;
import math.Mcomplex;

public class DummyFractalFunction implements FractalFunction {
	
	@Override
	public Mcomplex evaluate(Mcomplex prevResult, Mcomplex value) {
		return prevResult.mul(prevResult).add(value);
	}

}
