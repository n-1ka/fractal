package fractal.new_worker.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractAsyncTask<E, V> implements Task, ObservableTask<E> {

    private List<TaskListener<E>> listeners;
    private boolean interrupted;

    private class SubtaskObserver implements TaskListener<V> {
        private int nTasks;
        private AtomicInteger finished;
        private ConcurrentLinkedDeque<V> res;

        SubtaskObserver(int nTasks) {
            this.nTasks = nTasks;
            this.finished = new AtomicInteger(0);
            this.res = new ConcurrentLinkedDeque<>();
        }

        @Override
        public void onTaskCompleted(Object task, V value) {
            res.add(value);

            if (finished.incrementAndGet() == nTasks) {
                List<V> resList = new ArrayList<>(res);
                notifyTaskFinished(joinSubResults(resList));
            }
        }

    }

    public AbstractAsyncTask() {
        this.listeners = new CopyOnWriteArrayList<>();
        this.interrupted = false;
    }

    public abstract E runTask();

    protected List<AbstractAsyncTask<V, ?>> splitTask() {
        throw new RuntimeException("Not implemented.");
    }

    protected E joinSubResults(List<V> results) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void addListener(TaskListener<E> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(TaskListener<E> listener) {
        this.listeners.remove(listener);
    }

    private void notifyTaskFinished(E res) {
        listeners.forEach(listener -> listener.onTaskCompleted(this, res));
    }

    @Override
    public void run() {
        notifyTaskFinished(runTask());
    }

    @Override
    public List<Task> split() {
        List<AbstractAsyncTask<V, ?>> subTasks = splitTask();
        SubtaskObserver subtaskObserver = new SubtaskObserver(subTasks.size());
        subTasks.forEach(t -> t.addListener(subtaskObserver));

        return new ArrayList<>(subTasks);
    }

    @Override
    public void interrupt() {
        this.interrupted = true;
    }

    protected boolean isInterrupted() {
        return interrupted;
    }


}

