package fractal.worker;

import fractal.FractalWorkerListener;
import math.RectArea;

import java.awt.image.BufferedImage;

public interface FractalWorker {

    void addFractalWorkerListener(FractalWorkerListener listener);

    FractalEvaluator getEvaluator();

    BufferedImage getImage();

    RectArea getArea();

    void setEvaluator(FractalEvaluator evaluator);

    void setImage(BufferedImage image);

    void setArea(RectArea area);

}
