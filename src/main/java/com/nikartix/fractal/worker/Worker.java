package com.nikartix.fractal.worker;

import com.nikartix.fractal.worker.task.Task;

public interface Worker {

    /**
     * Worker executes task asynchronously.
     * Worker must be running for task to be executed.
     * @param task - Task
     */
    void execute(Task task);

    /**
     * Worker is started.
     * It can be interrupted after this.
     */
    void start();

    /**
     * Worker is interrupted.
     * Worker is in finished state, can't be restarted.
     */
    void interrupt();

}
