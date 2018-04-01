package math;

public class JavaFloat implements Mfloat {
	
	private double value;
	
	public JavaFloat(double value) {
		this.value = value;
	}
	
	public double getDouble() {
		return value;
	}

	@Override
	public Mfloat add(Mfloat f) {
		return add(((JavaFloat)f).value);
	}

	@Override
	public Mfloat sub(Mfloat f) {
		return sub(((JavaFloat)f).value);
	}

	@Override
	public Mfloat mul(Mfloat f) {
		return mul(((JavaFloat)f).value);
	}

	@Override
	public Mfloat div(Mfloat f) {
		return div(((JavaFloat)f).value);
	}

	@Override
	public Mfloat maxi(Mfloat f) {
		return maxi(((JavaFloat) f).value);
	}

	@Override
	public Mfloat mini(Mfloat f) {
		return mini(((JavaFloat) f).value);
	}

	@Override
	public Mfloat add(double f) {
		return new JavaFloat(value + f);
	}

	@Override
	public Mfloat sub(double f) {
		return new JavaFloat(value - f);
	}

	@Override
	public Mfloat mul(double f) {
		return new JavaFloat(value * f);
	}

	@Override
	public Mfloat div(double f) {
		return new JavaFloat(value / f);
	}

	@Override
	public Mfloat maxi(double f) {
		return new JavaFloat(Math.max(value, f));
	}

	@Override
	public Mfloat mini(double f) {
		return new JavaFloat(Math.min(value, f));
	}

	@Override
	public Mfloat abs() {
		return new JavaFloat(Math.abs(value));
	}

	@Override
	public int compareTo(Mfloat arg0) {
		if (arg0 == null) {
			return 1;
		} else if (this == arg0) {
			return 0;
		} 

		JavaFloat f = (JavaFloat) arg0;	// Assuming argument is JavaFloat
		return Double.compare(value, f.value);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
