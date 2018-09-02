package fractal.new_worker;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadWorker<V, S extends Task<V>, T extends SplittableTask<V, S>>
        extends Thread implements Worker <V, T> {

    private CopyOnWriteArrayList<Worker<V, S>> workers;
    private BlockingQueue<T> taskQueue;
    private BlockingQueue<S> subTaskQueue;

    public MultiThreadWorker(BlockingQueue<T> taskQueue, WorkerFactory<V, S> workerFactory, int nWorkers) {
        this.taskQueue = taskQueue;
        this.subTaskQueue = new LinkedBlockingQueue<>();
        this.workers = new CopyOnWriteArrayList<>();

        for (int i = 0; i < nWorkers; i++) {
            Worker<V, S> subWorker = workerFactory.buildWorker(subTaskQueue);
            subWorker.start();
            workers.add(subWorker);
        }
    }

    public MultiThreadWorker(WorkerFactory<V, S> workerFactory, int nWorkers) {
        this(new LinkedBlockingQueue<>(), workerFactory, nWorkers);
    }

    @Override
    public void executeTask(T task) {
        taskQueue.add(task);
    }

    @Override
    public void stopAllTasks() {

    }

    private void runTask(T task) {
        List<S> subTasks = task.splitTask();
        AtomicInteger tasksLeft = new AtomicInteger(subTasks.size());

        TaskListener<V> listener = (subTask, value) -> {
            if (tasksLeft.decrementAndGet() == 0) {
                task.notifyTaskExecuted(value);
            }
        };

        for (S subTask : subTasks) {
            subTask.addTaskListener(listener);
            subTaskQueue.add(subTask);
        }
    }

    private void mainLoop() throws InterruptedException {
        while (!isInterrupted()) {
            T currentTask = taskQueue.take();
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
