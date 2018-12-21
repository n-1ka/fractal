package controller;

import worker.Worker;
import worker.task.TaskListener;
import fractal.complex.ComplexFractalEvaluator;
import fractal.complex.ComplexFractalTask;
import math.RectArea;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class FractalWorkerController implements TaskListener<BufferedImage> {

    private Worker worker;
    private ComplexFractalTask currentTask;
    private List<FractalWorkerListener> workerListeners;

    public FractalWorkerController(Worker worker,
                                   ComplexFractalTask initialTask) {
        this.worker = worker;
        this.currentTask = initialTask;
        this.workerListeners = new CopyOnWriteArrayList<>();

        this.currentTask.addListener(this);
    }

    private boolean isTaskValid(ComplexFractalTask task) {
        return task.getImage() != null && task.getEvaluator() != null && task.getArea() != null;
    }

    private void updateCurrentTask(Function<ComplexFractalTask, ComplexFractalTask> updateFn) {
        currentTask.interrupt();
        currentTask = updateFn.apply(currentTask);
        if (isTaskValid(currentTask)) {
            currentTask.addListener(this);
            worker.execute(currentTask);
        }
    }

    public void updateImage(BufferedImage image) {
        updateCurrentTask(task -> task.setImage(image));
    }

    public void updateEvaluator(ComplexFractalEvaluator evaluator) {
        updateCurrentTask(task -> task.setEvaluator(evaluator));
    }

    public void updateArea(RectArea area) {
        updateCurrentTask(task -> task.setArea(area));
    }

    public void addFractalWorkerListener(FractalWorkerListener listener) {
        this.workerListeners.add(listener);
    }

    public void removeFractalWorkerListener(FractalWorkerListener listener) {
        this.workerListeners.remove(listener);
    }

    private void notifyFractalWorkerListeners(BufferedImage image) {
        workerListeners.forEach(listener -> listener.fractalUpdated(image));
    }

    @Override
    public void onTaskCompleted(Object task, BufferedImage value) {
        if (task == currentTask) {
            notifyFractalWorkerListeners(value);
        }
    }

}

