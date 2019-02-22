package com.nikartix.fractal.math.apfloat;

import com.nikartix.fractal.math.Mcomplex;
import com.nikartix.fractal.math.Mfloat;
import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;

public class ApfloatComplex implements Mcomplex {

    private Apcomplex value;

    public ApfloatComplex(Apcomplex value) {
        this.value = value;
    }

    public ApfloatComplex(Mfloat real, Mfloat imag) {
        ApfloatFloat apReal = (ApfloatFloat) real;
        ApfloatFloat apImag = (ApfloatFloat) imag;
        this.value = new Apcomplex(apReal.getValue(), apImag.getValue());
    }

    public ApfloatComplex(String real, String imag) {
        this.value = new Apcomplex(new Apfloat(real), new Apfloat(imag));
    }

    @Override
    public Mfloat real() {
        return new ApfloatFloat(value.real());
    }

    @Override
    public Mfloat imag() {
        return new ApfloatFloat(value.imag());
    }

    @Override
    public Mcomplex add(Mcomplex c) {
        Apcomplex value = ((ApfloatComplex) c).value;
        return new ApfloatComplex(this.value.add(value));
    }

    @Override
    public Mcomplex sub(Mcomplex c) {
        Apcomplex value = ((ApfloatComplex) c).value;
        return new ApfloatComplex(this.value.subtract(value));
    }

    @Override
    public Mcomplex mul(Mcomplex c) {
        Apcomplex value = ((ApfloatComplex) c).value;
        return new ApfloatComplex(this.value.multiply(value));
    }

    @Override
    public Mcomplex div(Mcomplex c) {
        Apcomplex value = ((ApfloatComplex) c).value;
        return new ApfloatComplex(this.value.divide(value));
    }
}
