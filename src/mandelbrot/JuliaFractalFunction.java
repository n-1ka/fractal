package mandelbrot;

import fractal.worker.FractalFunction;
import math.Mcomplex;

import java.util.Iterator;

public class JuliaFractalFunction implements FractalFunction  {

    private Mcomplex c;

    public JuliaFractalFunction(Mcomplex c) {
        this.c = c;
    }

    @Override
    public Iterator<Mcomplex> evaluate(Mcomplex value) {
        return new Iterator<Mcomplex>() {
            Mcomplex prev = value;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Mcomplex next() {
                prev = prev.mul(prev).add(c);
                return prev;
            }
        };
    }
}
