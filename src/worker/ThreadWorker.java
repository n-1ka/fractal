package worker;

import worker.task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadWorker extends Thread implements Worker {

    private BlockingQueue<Task> taskQueue;
    private Task currentTask;

    public ThreadWorker(BlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
        this.currentTask = null;
    }

    public ThreadWorker() {
        this(new LinkedBlockingQueue<>());
    }

    @Override
    public void execute(Task task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void mainLoop() throws InterruptedException {
        while (!isInterrupted()) {
            currentTask = taskQueue.take();
            currentTask.run();
        }
    }

    @Override
    public void run() {
        try {
            mainLoop();
        } catch (InterruptedException e) {
            System.out.println(String.format("ThreadWorker: '%s' Thread is interrupted", getName()));

            if (currentTask != null) {
                currentTask.interrupt();
            }
        }
    }

}
