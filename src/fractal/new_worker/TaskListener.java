package fractal.new_worker;

public interface TaskListener <E> {

    void onTaskCompleted(Task<E> task, E value);

}
