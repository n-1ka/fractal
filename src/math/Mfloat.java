package math;

public interface Mfloat extends Comparable<Mfloat> {
	
	Mfloat add(Mfloat f);

	Mfloat sub(Mfloat f);

	Mfloat mul(Mfloat f);

	Mfloat div(Mfloat f);

	Mfloat maxi(Mfloat f);

	Mfloat mini(Mfloat f);

	Mfloat add(double f);

	Mfloat sub(double f);

	Mfloat mul(double f);

	Mfloat div(double f);

	Mfloat maxi(double f);

	Mfloat mini(double f);

	Mfloat abs();

}
