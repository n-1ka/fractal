package fractal.new_worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadWorker <V, T extends Task<V>> extends Thread implements Worker<V, T> {

    private BlockingQueue<T> taskQueue;
    private T currentTask;

    public ThreadWorker(BlockingQueue<T> taskQueue) {
        this.taskQueue = taskQueue;
        this.currentTask = null;
    }

    public ThreadWorker() {
        this(new LinkedBlockingQueue<>());
    }

    @Override
    public void executeTask(T task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopAllTasks() {
        taskQueue.clear();

        if (currentTask != null) {
            currentTask.interrupt();
        }
    }

    private void runTask(T task) {
        V value = task.execute();

        if (value != null) {
            task.notifyTaskExecuted(value);
        }
    }

    private void mainLoop() throws InterruptedException {
        while (!isInterrupted()) {
            currentTask = taskQueue.take();
            runTask(currentTask);
        }
    }

    @Override
    public void run() {
        try {
            mainLoop();
        } catch (InterruptedException e) {
            System.out.println(String.format("'%s' Thread is interrupted", getName()));
        }
    }

}
