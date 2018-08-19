package fractal;

import fractal.worker.FractalWorker;

import java.awt.image.BufferedImage;

public interface FractalWorkerListener {

    void fractalUpdated(FractalWorker worker, BufferedImage image);

}
