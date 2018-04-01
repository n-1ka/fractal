package fractal.worker;

import fractal.FractalWorkerListener;
import math.Area;
import math.Mcomplex;
import math.Mfloat;
import math.Number;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FractalWorker extends Thread {

    private FractalEvaluator evaluator;
    private BufferedImage image;
    private FractalProblem problem;
    private List<FractalWorkerListener> listeners;

    public FractalWorker(FractalEvaluator evaluator) {
        super("");
        this.evaluator = evaluator;
        this.listeners = new ArrayList<>();
    }

    public synchronized void addFractalWorkerListener(FractalWorkerListener listener) {
        this.listeners.add(listener);
    }

    private synchronized void notifyFractalWorkerListners() {
        this.listeners.forEach((l) -> l.fractalPainted(this));
    }

    public synchronized void setImage(BufferedImage image) {
        this.image = image;
    }

    public synchronized BufferedImage getImage() {
        return image;
    }

    public synchronized void setProblem(FractalProblem problem) {
        this.problem = problem;
    }

    public synchronized FractalProblem getProblem() {
        return problem;
    }

    @Override
    public void run() {
        BufferedImage image = getImage();
        FractalProblem problem = getProblem();
        boolean repaint = true;

        while (true) {
            if (repaint && image != null && problem != null) {
                if (paintImage(image, problem)) {
                    notifyFractalWorkerListners();
                }
            }

            repaint = isStateUpdated(image, problem);

            if (repaint) {
                image = getImage();
                problem = getProblem();
            }
        }

    }

    private boolean isStateUpdated(BufferedImage currentImage, FractalProblem currentProblem) {
        return currentImage != image || currentProblem != problem;
    }

    private boolean paintImage(BufferedImage image, FractalProblem problem) {
        Area area = problem.area();

        Mfloat width = area.getWidth();
        Mfloat height = area.getHeight();

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        boolean isInterrupted = false;

        for (int i = 0; i < imageWidth && !isInterrupted; i++) {
            for (int j = 0; j < imageHeight && !isInterrupted; j++) {
                Mfloat real = area.getX0().add(width.mul(j / (double) imageWidth));
                Mfloat imag = area.getY0().add(height.mul(i / (double) imageHeight));

                Mcomplex value = Number.buildComplex(real, imag);

                Color color = evaluator.evaluate(value);

                image.setRGB(j, i, color.getRGB());

                isInterrupted = isStateUpdated(image, problem);
            }
        }

        return !isInterrupted;
    }

}
