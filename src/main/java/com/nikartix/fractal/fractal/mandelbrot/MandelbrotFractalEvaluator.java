package com.nikartix.fractal.fractal.mandelbrot;

import com.nikartix.fractal.fractal.FractalDepthPainter;
import com.nikartix.fractal.fractal.FractalFunction;
import com.nikartix.fractal.fractal.complex.ComplexFractalEvaluator;
import com.nikartix.fractal.math.Mcomplex;
import com.nikartix.fractal.math.Mfloat;

import java.awt.*;
import java.util.Iterator;

public class MandelbrotFractalEvaluator implements ComplexFractalEvaluator {
	
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

		return painter.generateColor(depth, maxDepth);
	}

}
