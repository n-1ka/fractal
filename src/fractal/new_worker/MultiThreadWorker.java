package fractal.new_worker;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiThreadWorker<V, S extends Task<V>, T extends SplittableTask<V, S>> implements Worker <V, T> {

    private ConcurrentLinkedDeque<Worker<V, S>> workers;
    private BlockingQueue<S> subTaskQueue;

    public MultiThreadWorker(BlockingQueue<T> taskQueue, WorkerFactory<V, S> workerFactory, int nWorkers) {
        this.subTaskQueue = new LinkedBlockingQueue<>();
        this.workers = new ConcurrentLinkedDeque<>();

        for (int i = 0; i < nWorkers; i++) {
            workers.push(workerFactory.buildWorker(subTaskQueue));
        }
    }

    @Override
    public void executeTask(T task) {

    }

    @Override
    public void stopAllTasks() {

    }

    @Override
    public void addTaskListener(TaskListener<V, T> listener) {

    }

    @Override
    public void removeTaskListener(TaskListener<V, T> listener) {

    }

}
