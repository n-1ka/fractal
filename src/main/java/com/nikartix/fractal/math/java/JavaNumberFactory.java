package com.nikartix.fractal.math.java;

import com.nikartix.fractal.math.Mcomplex;
import com.nikartix.fractal.math.Mfloat;
import com.nikartix.fractal.math.NumberFactory;

public class JavaNumberFactory implements NumberFactory {

    @Override
    public Mfloat buildFloat(double f) {
        return new JavaFloat(f);
    }

    @Override
    public Mfloat buildFloat(String f) throws NumberFormatException {
        return new JavaFloat(Double.valueOf(f));
    }

    @Override
    public Mcomplex buildComplex(Mfloat real, Mfloat imag) {
        return new MfloatComplex(real, imag);
    }

    @Override
    public Mcomplex buildComplex(double real, double imag) {
        return new MfloatComplex(buildFloat(real), buildFloat(imag));
    }

}
