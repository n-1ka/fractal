package fractal.worker;

import fractal.FractalWorkerListener;
import math.Mfloat;
import math.RectArea;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class FractalWorkerMulti extends Thread implements FractalWorker, FractalWorkerListener {

    private FractalWorkerSingle[][] workers;
    private FractalEvaluator evaluator;
    private BufferedImage image;
    private RectArea area;
    private Collection<FractalWorkerListener> listeners;
    private BlockingQueue<UpdateTask> taskUpdateQueue;
    private boolean isRunning;

    private class UpdateTask {
        private FractalEvaluator evaluator;
        private BufferedImage image;
        private RectArea area;

        public UpdateTask(FractalEvaluator evaluator, BufferedImage image, RectArea area) {
            this.evaluator = evaluator;
            this.image = image;
            this.area = area;
        }

        public boolean isValid() {
            return evaluator != null && image != null && area != null
                    && image.getWidth() > 0 && image.getHeight() > 0;
        }
    }

    private static <T> void forEach2D(T[][] m, Consumer<? super T> consumer) {
        for (T[] aM : m) {
            for (T anAM : aM) {
                consumer.accept(anAM);
            }
        }
    }

    public FractalWorkerMulti(int nRows, int nCols, FractalEvaluator evaluator) {
        this.workers = new FractalWorkerSingle[nRows][nCols];
        this.evaluator = evaluator;
        this.image = null;
        this.area = null;
        this.listeners = new ConcurrentLinkedQueue<>();
        this.taskUpdateQueue = new LinkedBlockingQueue<>();
        this.isRunning = true;

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                FractalWorkerSingle worker = new FractalWorkerSingle(this.evaluator);
                worker.addFractalWorkerListener(this);
                workers[i][j] = worker;
                worker.start();
            }
        }
    }
    public FractalWorkerMulti(int nRows, int nCols) {
        this(nRows, nCols, null);
    }

    @Override
    public void fractalUpdated(FractalWorker worker, BufferedImage image) {
        listeners.forEach(l -> {
            l.fractalUpdated(this, this.image);
        });
    }

    @Override
    public void addFractalWorkerListener(FractalWorkerListener listener) {
        listeners.add(listener);
    }

    @Override
    public FractalEvaluator getEvaluator() {
        return evaluator;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public RectArea getArea() {
        return area;
    }

    @Override
    public void setEvaluator(FractalEvaluator evaluator) {
        this.evaluator = evaluator;
        updateTask();
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
        updateTask();
    }

    @Override
    public void setArea(RectArea area) {
        this.area = area;
        updateTask();
    }

    private void updateTask() {
        taskUpdateQueue.clear();
        taskUpdateQueue.add(new UpdateTask(evaluator, image, area));
    }

    private void updateImage(BufferedImage image) {
        if (workers.length == 0)
            return;
        int width = image.getWidth();
        int height = image.getHeight();
        int rows = workers.length;
        int cols = workers[0].length;
        int w = width/cols;
        int h = height/rows;

        if (w <= 0 || h <= 0) {
            return;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                workers[i][j].setImage(image.getSubimage(j * w, i * h, w, h));
            }
        }
    }

    private void updateArea(RectArea area) {
        if (workers.length == 0)
            return;

        int rows = workers.length;
        int cols = workers[0].length;
        Mfloat w = area.getWidth().div(cols);
        Mfloat h = area.getHeight().div(rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Mfloat x0 = area.getX0().add(w.mul(j));
                Mfloat y0 = area.getY0().add(h.mul(i));
                RectArea areaIJ = new RectArea(
                        x0, x0.add(w),
                        y0, y0.add(h)
                );
                workers[i][j].setArea(areaIJ);
            }
        }
    }

    private void updateEvaluator(FractalEvaluator evaluator) {
        forEach2D(workers, worker -> worker.setEvaluator(evaluator));
    }

    @Override
    public void run() {
        try {
            while (isRunning && !isInterrupted()) {
                UpdateTask task = taskUpdateQueue.take();
                if (task.isValid()) {
                    updateEvaluator(task.evaluator);
                    updateImage(task.image);
                    updateArea(task.area);
                }
            }
        } catch (InterruptedException e) {
            // Quit
        }
    }
}
