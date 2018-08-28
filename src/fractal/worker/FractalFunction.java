package fractal.worker;

import math.Mcomplex;

import java.util.Iterator;

public interface FractalFunction {
	
	Iterator<Mcomplex> evaluate(Mcomplex value);

}
