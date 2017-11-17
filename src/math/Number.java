package math;

public class Number {
	
	public static Mfloat buildFloat(double f) {
		return new JavaFloat(f);
	}
	
	public static Mcomplex buildComplex(Mfloat real, Mfloat imag) {
		return new JavaComplex(real, imag);
	}
	
	public static Mcomplex buildComplex(double real, double imag) {
		return new JavaComplex(buildFloat(real), buildFloat(imag));
	}

}
