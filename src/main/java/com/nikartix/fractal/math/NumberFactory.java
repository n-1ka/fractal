package com.nikartix.fractal.math;

public interface NumberFactory {

    Mfloat buildFloat(double f);

    Mfloat buildFloat(String f) throws NumberFormatException;

    Mcomplex buildComplex(Mfloat real, Mfloat imag);

    Mcomplex buildComplex(double real, double imag);

}
