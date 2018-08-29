package fractal.new_worker;

import java.util.concurrent.BlockingQueue;

public interface WorkerFactory <V, T extends Task<V>> {

    Worker<V, T> buildWorker(BlockingQueue<T> taskQueue);

}
