package math;

public class RectArea {

    private Mfloat x0, x1, y0, y1;

    public RectArea(Mfloat x0, Mfloat x1, Mfloat y0, Mfloat y1) {
        this.x0 = x0.mini(x1);
        this.x1 = x0.maxi(x1);
        this.y0 = y0.mini(y1);
        this.y1 = y0.maxi(y1);
    }

    public Mfloat getX0() {
        return x0;
    }

    public Mfloat getX1() {
        return x1;
    }

    public Mfloat getY0() {
        return y0;
    }

    public Mfloat getY1() {
        return y1;
    }

    public Mfloat getWidth() {
        return x1.sub(x0);
    }

    public Mfloat getHeight() {
        return y1.sub(y0);
    }

    @Override
    public String toString() {
        return String.format("rect: x = [%s, %s], y = [%s, %s]",
                x0.toString(), x1.toString(), y0.toString(), y1.toString());
    }
}
