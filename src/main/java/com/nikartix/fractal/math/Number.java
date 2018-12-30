package com.nikartix.fractal.math;

import com.nikartix.fractal.math.apfloat.ApfloatFloat;

public class Number {
	
	public static Mfloat buildFloat(double f) {
		return new ApfloatFloat(f);
	}

	public static Mfloat buildFloat(String f) throws NumberFormatException {
        return new ApfloatFloat(f);
	}
	
	public static Mcomplex buildComplex(Mfloat real, Mfloat imag) {
		return new MfloatComplex(real, imag);
	}
	
	public static Mcomplex buildComplex(double real, double imag) {
		return new MfloatComplex(buildFloat(real), buildFloat(imag));
	}

	public static Mcomplex buildComplex(String real, String imag) throws NumberFormatException {
		return new MfloatComplex(buildFloat(real), buildFloat(imag));
	}

}
