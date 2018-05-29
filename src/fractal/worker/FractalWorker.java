package fractal.worker;

import fractal.FractalWorkerListener;
import math.RectArea;
import math.Mcomplex;
import math.Mfloat;
import math.Number;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FractalWorker extends Thread {

    private class Task {
        private BufferedImage image;
        private FractalEvaluator evaluator;
        private RectArea area;

        Task(BufferedImage image, FractalEvaluator evaluator, RectArea area) {
            this.image = image;
            this.evaluator = evaluator;
            this.area = area;
        }

        boolean isValid() {
            return image != null && evaluator != null && area != null;
        }
    }

    private FractalEvaluator evaluator;
    private BufferedImage image;
    private RectArea area;
    private List<FractalWorkerListener> listeners;
    private Task currentTask;
    private boolean running;

    public FractalWorker(FractalEvaluator evaluator) {
        this.image = null;
        this.evaluator = evaluator;
        this.area = null;
        this.listeners = new ArrayList<>();
        this.running = true;

        this.currentTask = new Task(null, evaluator, null);
    }

    public synchronized void addFractalWorkerListener(FractalWorkerListener listener) {
        this.listeners.add(listener);
    }

    private synchronized void notifyFractalWorkerListeners(BufferedImage image) {
        this.listeners.forEach((l) -> l.fractalPainted(this, image));
    }

    public synchronized FractalEvaluator getEvaluator() {
        return evaluator;
    }

    public synchronized BufferedImage getImage() {
        return image;
    }

    public synchronized RectArea getArea() {
        return area;
    }

    public synchronized void setEvaluator(FractalEvaluator evaluator) {
        this.evaluator = evaluator;
        updateTask();
    }

    public synchronized void setImage(BufferedImage image) {
        this.image = image;
        updateTask();
    }

    public synchronized void setArea(RectArea area) {
        this.area = area;
        updateTask();
    }

    private synchronized void updateTask() {
        currentTask = new Task(image, evaluator, area);
    }

    private synchronized Task getCurrentTask() {
        return currentTask;
    }

    @Override
    public void run() {
        Task task = getCurrentTask();

        boolean repaint = true;

        while (running) {
            if (repaint && task.isValid()) {
                if (paintImage(task)) {
                    notifyFractalWorkerListeners(image);
                }
            }

            repaint = (task != getCurrentTask());

            if (repaint) {
                task = getCurrentTask();
            }
        }

    }

    private boolean paintImage(Task task) {
        RectArea area = task.area;

        Mfloat width = area.getWidth();
        Mfloat height = area.getHeight();

        int imageWidth = task.image.getWidth();
        int imageHeight = task.image.getHeight();
        boolean isInterrupted = false;

        for (int i = 0; i < imageWidth && !isInterrupted; i++) {
            for (int j = 0; j < imageHeight && !isInterrupted; j++) {
                Mfloat real = area.getX0().add(width.mul(i / (double) imageWidth));
                Mfloat imag = area.getY0().add(height.mul(j / (double) imageHeight));

                Mcomplex value = Number.buildComplex(real, imag);

                Color color = task.evaluator.evaluate(value);

                task.image.setRGB(i, j, color.getRGB());

                isInterrupted = task != getCurrentTask();
            }
        }

        return !isInterrupted;
    }

}
