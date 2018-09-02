package test;

import fractal.new_worker.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadWorkerTest {

    private static class CountingTask extends SplittableTask<Integer, Integer, Task<Integer>> {

        private Integer start, end;

        public CountingTask(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        public Integer execute() {
            Integer res = 0;

            while (!isInterrupted()) {
                if (start > end) {
                    return res;
                }
                res += this.start++;
            }

            // Was interrupted
            return null;
        }

        @Override
        public List<Task<Integer>> splitTask() {
            int mid = (this.end - start) / 2;
            return Arrays.asList(
                    new CountingTask(start, mid),
                    new CountingTask(mid + 1, end)
            );
        }

    }

    private static <E, T extends Task<E>> E run(Worker<E, T> worker, T task) throws InterruptedException {
        Semaphore wait = new Semaphore(0);
        AtomicReference<E> res = new AtomicReference<>();

        TaskListener<E> listener = (finishedTask, value) -> {
            if (task == finishedTask) {
                res.set(value);
                wait.release();
            }
        };

        task.addTaskListener(listener);
        worker.executeTask(task);
        wait.acquire();

        task.removeTaskListener(listener);

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
        MultiThreadWorker<Integer, Task<Integer>, CountingTask> multiWorker = new MultiThreadWorker<>(
                ThreadWorker::new,
                2
        );
        worker.start();
        multiWorker.start();

        int from = 0;
        int to = 10000;
        System.out.println(run(multiWorker, new CountingTask(from, to)));
        System.out.println(sum(from, to));

        worker.interrupt();
        worker.join();

        System.out.println("Bye!");
    }

}
