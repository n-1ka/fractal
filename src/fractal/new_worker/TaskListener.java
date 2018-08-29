package fractal.new_worker;

public interface TaskListener <V, T extends Task<V>> {

    void onTaskCompleted(T task, V value);

}
