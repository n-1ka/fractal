package test;

import fractal.new_worker.*;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadWorkerTest {

    private static class CountingTask implements Task<Integer> {

        private Integer start, end;
        private boolean isRunning;

        public CountingTask(Integer start, Integer end) {
            this.start = start;
            this.end = end;
            this.isRunning = true;
        }

        @Override
        public Integer execute() {
            Integer res = 0;

            while (isRunning) {
                if (start > end) {
                    return res;
                }
                res += this.start++;
            }

            // Was interrupted
            return null;
        }

        @Override
        public void interrupt() {
            isRunning = false;
        }

        @Override
        public int getJobId() {
            return 0;
        }

    }

    private static <E, T extends Task<E>> E run(Worker<E, T> worker, T task) throws InterruptedException {
        Semaphore wait = new Semaphore(0);
        AtomicReference<E> res = new AtomicReference<>();

        TaskListener<E, T> listener = (finishedTask, value) -> {
            if (task == finishedTask) {
                res.set(value);
                System.out.println("RES = " + String.valueOf(value));
                wait.release();
            }
        };

        worker.addTaskListener(listener);
        worker.executeTask(task);
        wait.acquire();

        worker.removeTaskListener(listener);

        return res.get();
    }

    private static int sum(int from, int to) {
        int res = 0;
        for (int i = from; i <= to; i++) {
            res += i;
        }
        return res;
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadWorker<Integer, CountingTask> worker = new ThreadWorker<>();
        worker.start();

        int from = 0;
        int to = 10000;
        System.out.println(run(worker, new CountingTask(from, to)));
        System.out.println(sum(from, to));

        worker.interrupt();
        worker.join();

        System.out.println("Bye!");
    }

}
