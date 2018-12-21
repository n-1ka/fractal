package worker.task;

import java.util.Collections;
import java.util.List;

public interface Task {

    /**
     * Runs a task.
     */
    void run();

    /**
     * Interrupts a task.
     * If task is already finished interrupting it will do nothing.
     */
    void interrupt();

    /**
     * Returns list of subtask that if all executed
     *  is equal to executing the whole task.
     * Default implementation returns itself in a list.
     * @return Subtask
     */
    default List<Task> split() {
        return Collections.singletonList(this);
    }

}

