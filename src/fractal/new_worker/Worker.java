package fractal.new_worker;

public interface Worker <V, T extends Task<V>> {

    void executeTask(T task);

    void stopAllTasks();

    void start();

    void interrupt();

}
