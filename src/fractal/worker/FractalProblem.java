package fractal.worker;

import math.Area;

import java.awt.image.BufferedImage;

public class FractalProblem {

    private BufferedImage image;
    private Area area;

    public FractalProblem(BufferedImage image, Area area) {
        this.image = image;
        this.area = area;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Area getArea() {
        return area;
    }


}
