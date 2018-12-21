package worker.task;

public interface ObservableTask<E> {

    void addListener(TaskListener<E> listener);

    void removeListener(TaskListener<E> listener);

}
