package com.nikartix.fractal.worker;

import com.nikartix.fractal.worker.task.Task;

import java.util.concurrent.*;

public class MultiThreadWorker extends Thread implements Worker {

    private BlockingQueue<Task> taskQueue;
    private BlockingQueue<Task> allSubtask;
    private BlockingDeque<ThreadWorker> workers;

    public MultiThreadWorker(BlockingQueue<Task> taskQueue, int nThreads) {
        this.taskQueue = taskQueue;
        this.allSubtask = new LinkedBlockingQueue<>();
        this.workers = new LinkedBlockingDeque<>();

        for (int i = 0; i < nThreads; i++) {
            this.workers.add(new ThreadWorker(this.allSubtask));
        }
    }

    public MultiThreadWorker(int nThreads) {
        this(new LinkedBlockingQueue<>(), nThreads);
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
            allSubtask.addAll(taskQueue.take().split());
        }
    }

    @Override
    public void run() {
        try {
            mainLoop();
        } catch (InterruptedException e) {
            System.out.println(String.format("MultiThreadWorker: '%s' Thread is interrupted", getName()));
        }
    }

    @Override
    public synchronized void start() {
        this.workers.forEach(Thread::start);
        super.start();
    }

    @Override
    public void interrupt() {
        this.workers.forEach(Thread::interrupt);
        super.interrupt();
    }

    public void joinAll() throws InterruptedException {
        for (Thread worker : workers) {
            worker.join();
        }

        this.join();
    }


}
