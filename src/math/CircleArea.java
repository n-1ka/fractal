package math;

public class CircleArea {

    private Mfloat centerX, centerY, diameter;

    public CircleArea(Mfloat centerX, Mfloat centerY, Mfloat diameter) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.diameter = diameter;
    }

    public Mfloat getCenterX() {
        return centerX;
    }

    public Mfloat getCenterY() {
        return centerY;
    }

    public Mfloat getDiameter() {
        return diameter;
    }

}
