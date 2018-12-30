package com.nikartix.fractal.math.apfloat;

import com.nikartix.fractal.math.Mfloat;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class ApfloatFloat implements Mfloat {

    private Apfloat value;

    public ApfloatFloat(Apfloat value) {
        this.value = value;
    }

    public ApfloatFloat(String value) {
        this(new Apfloat(value));
    }

    public ApfloatFloat(double value) {
        this(String.format("%f", value));
    }

    @Override
    public Mfloat add(Mfloat f) {
        return new ApfloatFloat(value.add(((ApfloatFloat) f).value));
    }

    @Override
    public Mfloat sub(Mfloat f) {
        return new ApfloatFloat(value.subtract(((ApfloatFloat) f).value));
    }

    @Override
    public Mfloat mul(Mfloat f) {
        return new ApfloatFloat(value.multiply(((ApfloatFloat) f).value));
    }

    @Override
    public Mfloat div(Mfloat f) {
        return new ApfloatFloat(value.divide(((ApfloatFloat) f).value));
    }

    @Override
    public Mfloat maxi(Mfloat f) {
        Apfloat value = ((ApfloatFloat) f).value;

        if (this.value.compareTo(value) > 0) {
            return this;
        } else {
            return f;
        }
    }

    @Override
    public Mfloat mini(Mfloat f) {
        Apfloat value = ((ApfloatFloat) f).value;

        if (this.value.compareTo(value) < 0) {
            return this;
        } else {
            return f;
        }
    }

    @Override
    public Mfloat add(double f) {
        return add(new ApfloatFloat(f));
    }

    @Override
    public Mfloat sub(double f) {
        return sub(new ApfloatFloat(f));
    }

    @Override
    public Mfloat mul(double f) {
        return mul(new ApfloatFloat(f));
    }

    @Override
    public Mfloat div(double f) {
        return div(new ApfloatFloat(f));
    }

    @Override
    public Mfloat maxi(double f) {
        return maxi(new ApfloatFloat(f));
    }

    @Override
    public Mfloat mini(double f) {
        return mini(new ApfloatFloat(f));
    }

    @Override
    public Mfloat abs() {
        return new ApfloatFloat(ApfloatMath.abs(value));
    }

    @Override
    public int compareTo(Mfloat mfloat) {
        return this.value.compareTo(((ApfloatFloat) mfloat).value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
