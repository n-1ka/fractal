package fractal.new_worker;

public interface Worker <V, T extends Task<V>> {

    void executeTask(T task);

    void stopAllTasks();

    void addTaskListener(TaskListener<V, T> listener);

    void removeTaskListener(TaskListener<V, T> listener);

}
