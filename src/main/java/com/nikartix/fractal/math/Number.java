package com.nikartix.fractal.math;

import com.nikartix.fractal.math.java.JavaNumberFactory;

public class Number {

	private static NumberFactory factory = new JavaNumberFactory();

	public static Mfloat buildFloat(double f) {
		return factory.buildFloat(f);
	}

	public static Mfloat buildFloat(String f) throws NumberFormatException {
        return factory.buildFloat(f);
	}
	
	public static Mcomplex buildComplex(Mfloat real, Mfloat imag) {
		return factory.buildComplex(real, imag);
	}
	
	public static Mcomplex buildComplex(double real, double imag) {
		return factory.buildComplex(real, imag);
	}

}
