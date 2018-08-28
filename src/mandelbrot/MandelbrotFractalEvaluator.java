package mandelbrot;

import java.awt.Color;
import java.util.Iterator;

import fractal.worker.FractalFunction;
import fractal.worker.FractalDepthPainter;
import fractal.worker.FractalEvaluator;
import math.Mcomplex;
import math.Mfloat;

public class MandelbrotFractalEvaluator implements FractalEvaluator {
	
	private int maxDepth;
	private Mfloat edge;
	private FractalDepthPainter painter;
	private FractalFunction function;
	
	public MandelbrotFractalEvaluator(int maxDepth,
									  Mfloat edge,
									  FractalDepthPainter painter,
									  FractalFunction function) {
		this.maxDepth = maxDepth;
		this.painter = painter;
		this.function = function;
		this.edge = edge;
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

		int depth = 0;
		Iterator<Mcomplex> sequence = function.evaluate(value);

		while (sequence.hasNext()) {
			Mcomplex next = sequence.next();
            depth++;

			if (depth >= maxDepth || squareAbs(next).compareTo(edgeSquared) > 0) {
				break;
			}
		}

		if (depth == maxDepth)
            System.out.println("YES");
		return painter.generateColor(depth, maxDepth);
	}

}
