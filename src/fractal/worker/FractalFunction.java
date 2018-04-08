package fractal.worker;

import math.Mcomplex;

public interface FractalFunction {
	
	Mcomplex evaluate(Mcomplex value, Mcomplex add);

}
