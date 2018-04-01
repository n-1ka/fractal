package mandelbrot;

import fractal.worker.FractalFunction;
import math.Mcomplex;

public class DummyFractalFunction implements FractalFunction {
	
	@Override
	public Mcomplex evaluate(Mcomplex value, Mcomplex add) {
		return value.mul(value).add(add);
	}

}
