package fractal.new_worker;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Task <E> {

    private boolean isInterrupted;
    private CopyOnWriteArrayList<TaskListener<E>> listeners;

    public Task() {
        this.isInterrupted = false;
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public abstract E execute();

    public final void notifyTaskExecuted(E value) {
        if (value != null) {
            listeners.forEach(l -> l.onTaskCompleted(this, value));
        }
    }

    public final void addTaskListener(TaskListener<E> listener) {
        listeners.addIfAbsent(listener);
    }

    public final void removeTaskListener(TaskListener<E> listener) {
        listeners.remove(listener);
    }

    public final void interrupt() {
        this.isInterrupted = true;
    }

    public final boolean isInterrupted(){
        return isInterrupted;
    }

}

