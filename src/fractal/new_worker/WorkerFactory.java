package fractal.new_worker;

import fractal.new_worker.task.Task;

import java.util.concurrent.BlockingQueue;

public interface WorkerFactory {

    Worker buildWorker(BlockingQueue<Task> taskQueue);

}
