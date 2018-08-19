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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FractalWorkerSingle extends Thread implements FractalWorker {

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
    private boolean running;
    private BlockingQueue<Task> taskUpdateQueue;

    public FractalWorkerSingle(FractalEvaluator evaluator) {
        this.image = null;
        this.evaluator = evaluator;
        this.area = null;
        this.listeners = new ArrayList<>();
        this.running = true;
        this.taskUpdateQueue = new LinkedBlockingQueue<>();
    }

    public synchronized void addFractalWorkerListener(FractalWorkerListener listener) {
        this.listeners.add(listener);
    }

    private void notifyFractalWorkerListeners(BufferedImage image) {
        this.listeners.forEach((l) -> l.fractalUpdated(this, image));
    }

    public FractalEvaluator getEvaluator() {
        return evaluator;
    }

    public BufferedImage getImage() {
        return image;
    }

    public RectArea getArea() {
        return area;
    }

    public void setEvaluator(FractalEvaluator evaluator) {
        this.evaluator = evaluator;
        updateTask();
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        updateTask();
    }

    public void setArea(RectArea area) {
        this.area = area;
        updateTask();
    }

    private void updateTask() {
        taskUpdateQueue.clear();
        taskUpdateQueue.add(new Task(image, evaluator, area));
    }

    @Override
    public void run() {
        try {

            while (running && !isInterrupted()) {
                Task task = taskUpdateQueue.take();
                if (task.isValid()) {
                    if (paintImage(task)) {
                        notifyFractalWorkerListeners(image);
                    }
                }
            }

        } catch (InterruptedException e) {
            // Finish
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

                isInterrupted = taskUpdateQueue.size() > 0;
            }
        }

        return !isInterrupted;
    }

}
