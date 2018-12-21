package fractal.worker.task;

public interface TaskListener<E> {

    void onTaskCompleted(Object task, E value);

}
