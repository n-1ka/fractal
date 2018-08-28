package mandelbrot;

import fractal.worker.FractalFunction;
import math.Mcomplex;
import math.Number;

import java.util.Iterator;

public class MandelbrotFractalFunction implements FractalFunction {
	
	@Override
	public Iterator<Mcomplex> evaluate(Mcomplex value) {
	    return new Iterator<Mcomplex>() {

	    	Mcomplex prevValue = Number.buildComplex(0, 0);

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public Mcomplex next() {
				prevValue = prevValue.mul(prevValue).add(value);
				return prevValue;
			}
		};
	}

}
