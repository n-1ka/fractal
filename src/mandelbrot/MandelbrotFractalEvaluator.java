package mandelbrot;

import java.awt.Color;

import fractal.worker.FractalFunction;
import fractal.worker.FractalDepthPainter;
import fractal.worker.FractalEvaluator;
import math.Mcomplex;
import math.Mfloat;
import math.Number;

public class MandelbrotFractalEvaluator implements FractalEvaluator {
	
	private final static double EDGE = 2.0;
	
	private int maxDepth;
	private Mfloat edge;
	private FractalDepthPainter painter;
	private FractalFunction function;
	
	public MandelbrotFractalEvaluator(int maxDepth,
									  FractalDepthPainter painter,
									  FractalFunction function) {
		this.maxDepth = maxDepth;
		this.painter = painter;
		this.function = function;
		this.edge = Number.buildFloat(EDGE);
	}
	
	private Mfloat squareAbs(Mcomplex complex) {
		Mfloat real = complex.real();
		Mfloat imag = complex.imag();
		return real.mul(real).add(imag.mul(imag));
	}

	/**
	 * @return Maximum function evaluation number.
	 */
	public int getDepth() {
		return maxDepth;
	}

	/**
	 * @return Function divergence edge.
	 */
	public Mfloat getEdge() {
		return edge;
	}

	/**
	 * @return Fractal depth painter
	 */
	public FractalDepthPainter getDepthPainter() {
		return painter;
	}

	/**
	 * @return Fractal function
	 */
	public FractalFunction getFunction() {
		return function;
	}

	@Override
	public Color evaluate(Mcomplex value) {
		Mfloat edgeSquared = edge.mul(edge);
		Mcomplex result = Number.buildComplex(0, 0);

		int depth = 0;
		while (depth < maxDepth && 
				squareAbs(result).compareTo(edgeSquared) < 0) {
			result = function.evaluate(result, value);
			depth++;
		}

		return painter.generateColor(depth, maxDepth);
	}

}
