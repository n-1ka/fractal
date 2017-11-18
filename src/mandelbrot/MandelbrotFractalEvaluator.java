package mandelbrot;

import java.awt.Color;

import fractal.FractalFunction;
import fractal.FractalDepthColorer;
import fractal.FractalEvaluator;
import math.Mcomplex;
import math.Mfloat;
import math.Number;

public class MandelbrotFractalEvaluator implements FractalEvaluator {
	
	public final static double EDGE = 2.0;
	
	private int maxDepth;
	private Mfloat edge;
	private FractalDepthColorer colorer;
	private FractalFunction function;
	
	public MandelbrotFractalEvaluator(int maxDepth,
										FractalDepthColorer colorer,
										FractalFunction function) {
		this.maxDepth = maxDepth;
		this.colorer = colorer;
		this.function = function;
		this.edge = Number.buildFloat(EDGE);
	}
	
	private Mfloat squareAbs(Mcomplex complex) {
		Mfloat real = complex.real();
		Mfloat imag = complex.imag();
		return real.mul(real).add(imag.mul(imag));
	}
	
	@Override
	public Color evaluate(Mcomplex value) {
		Mfloat edgeSquared = edge.mul(edge);
		Mcomplex v = Number.buildComplex(0, 0);

		int depth = 0;
		while (depth < maxDepth && 
				squareAbs(v).compareTo(edgeSquared) < 0) {
			v = function.evaluate(v, value);
			depth++;
		}

		return colorer.generateColor(depth, maxDepth);
	}

}
