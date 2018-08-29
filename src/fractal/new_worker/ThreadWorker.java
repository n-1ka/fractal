package fractal.new_worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadWorker <V, T extends Task<V>> extends Thread implements Worker<V, T> {

    private BlockingQueue<T> taskQueue;
    private ConcurrentLinkedQueue<TaskListener<V, T>> taskListeners;
    private T currentTask;

    public ThreadWorker(BlockingQueue<T> taskQueue) {
        this.taskQueue = taskQueue;
        this.taskListeners = new ConcurrentLinkedQueue<>();
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

    @Override
    public void addTaskListener(TaskListener<V, T> listener) {
        taskListeners.add(listener);
    }

    @Override
    public void removeTaskListener(TaskListener<V, T> listener) {
        taskListeners.remove(listener);
    }

    protected void notifyTaskCompleted(T task, V value) {
        taskListeners.forEach(l -> l.onTaskCompleted(task, value));
    }

    protected void runTask(T task) {
        V value = task.execute();

        if (value != null) {
            notifyTaskCompleted(task, value);
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
