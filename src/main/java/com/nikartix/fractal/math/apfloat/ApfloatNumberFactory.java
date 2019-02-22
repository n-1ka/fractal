package com.nikartix.fractal.math.apfloat;

import com.nikartix.fractal.math.Mcomplex;
import com.nikartix.fractal.math.Mfloat;
import com.nikartix.fractal.math.NumberFactory;

public class ApfloatNumberFactory implements NumberFactory {

    @Override
    public Mfloat buildFloat(double f) {
        return new ApfloatFloat(f);
    }

    @Override
    public Mfloat buildFloat(String f) throws NumberFormatException {
        return new ApfloatFloat(f);
    }

    @Override
    public Mcomplex buildComplex(Mfloat real, Mfloat imag) {
        return new ApfloatComplex(real, imag);
    }

    @Override
    public Mcomplex buildComplex(double real, double imag) {
        return new ApfloatComplex(buildFloat(real), buildFloat(imag));
    }

}
