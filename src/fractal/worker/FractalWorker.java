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
    private FractalProblem problem;
    private List<FractalWorkerListener> listeners;
    private boolean running;

    public FractalWorker(FractalEvaluator evaluator) {
        super("");
        this.evaluator = evaluator;
        this.listeners = new ArrayList<>();
        this.running = true;
    }

    public synchronized void addFractalWorkerListener(FractalWorkerListener listener) {
        this.listeners.add(listener);
    }

    private synchronized void notifyFractalWorkerListners(FractalProblem problem) {
        this.listeners.forEach((l) -> l.fractalPainted(this, problem));
    }

    public synchronized FractalEvaluator getEvaluator() {
        return evaluator;
    }

    public synchronized void setEvaluator(FractalEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public synchronized void setProblem(FractalProblem problem) {
        this.problem = problem;
    }

    public synchronized FractalProblem getProblem() {
        return problem;
    }

    @Override
    public void run() {
        FractalEvaluator evaluator = getEvaluator();
        FractalProblem problem = getProblem();
        boolean repaint = true;

        while (running) {
            if (repaint && problem != null) {
                if (paintImage(evaluator, problem)) {
                    notifyFractalWorkerListners(problem);
                }
            }

            repaint = isStateUpdated(evaluator, problem);

            if (repaint) {
                evaluator = getEvaluator();
                problem = getProblem();
            }
        }

    }

    private boolean isStateUpdated(FractalEvaluator currentEvaluator,
                                   FractalProblem currentProblem) {
        return currentEvaluator != evaluator || currentProblem != problem;
    }

    private boolean paintImage(FractalEvaluator evaluator, FractalProblem problem) {
        Area area = problem.getArea();

        Mfloat width = area.getWidth();
        Mfloat height = area.getHeight();
        BufferedImage image = problem.getImage();

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        boolean isInterrupted = false;

        for (int i = 0; i < imageWidth && !isInterrupted; i++) {
            for (int j = 0; j < imageHeight && !isInterrupted; j++) {
                Mfloat real = area.getX0().add(width.mul(i / (double) imageWidth));
                Mfloat imag = area.getY0().add(height.mul(j / (double) imageHeight));

                Mcomplex value = Number.buildComplex(real, imag);

                Color color = evaluator.evaluate(value);

                image.setRGB(i, j, color.getRGB());

                isInterrupted = isStateUpdated(evaluator, problem);
            }
        }

        return !isInterrupted;
    }

}
