package com.nikartix.fractal.test;

import com.nikartix.fractal.worker.MultiThreadWorker;
import com.nikartix.fractal.worker.task.AbstractAsyncTask;
import com.nikartix.fractal.worker.task.TaskListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadWorkerTest {

    private static class CountingTask extends AbstractAsyncTask<Long, Long> {

        private Long start, end;

        public CountingTask(Long start, Long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Long runTask() {
            Long res = 0L;

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
        protected List<AbstractAsyncTask<Long, ?>> splitTask() {
            List<AbstractAsyncTask<Long, ?>> res = new ArrayList<>();
            long step = 1000L;
            long from = start;

            while (from < end) {
                long to = Math.min(from + step, end);
                res.add(new CountingTask(from, to));
                from = to + 1;
            }

            return res;
        }

        @Override
        protected Long joinSubResults(List<Long> results) {
            Long res = 0L;
            for (Long subSum : results) {
                res += subSum;
            }
            return res;
        }

    }

    private static <E, V> E run(MultiThreadWorker worker, AbstractAsyncTask<E, V> task) throws InterruptedException {
        Semaphore wait = new Semaphore(0);
        AtomicReference<E> res = new AtomicReference<>();

        TaskListener<E> listener = (finishedTask, value) -> {
            if (task == finishedTask) {
                res.set(value);
                wait.release();
            }
        };

        task.addListener(listener);
        worker.execute(task);
        wait.acquire();

        task.removeListener(listener);

        return res.get();
    }

    private static long sum(long from, long to) {
        long res = 0L;
        for (long i = from; i <= to; i++) {
            res += i;
        }
        return res;
    }

    public static void main(String[] args) throws InterruptedException {
        MultiThreadWorker multiWorker = new MultiThreadWorker(10);
        multiWorker.start();

        long from = 0;
        long to = 100000000L;
        long workerTime0 = System.currentTimeMillis();
        Long workerRes = run(multiWorker, new CountingTask(from, to));
        long workerTime1 = System.currentTimeMillis();

        long singlThreadTime0 = System.currentTimeMillis();
        Long actualSum = sum(from, to);
        long singlThreadTime1 = System.currentTimeMillis();

        System.out.println(workerRes);
        System.out.println(actualSum);
        System.out.println("Result: " + (actualSum.equals(workerRes)));

        System.out.println();
        System.out.println("Worker time: " + (workerTime1 - workerTime0));
        System.out.println("Single thread time: " + (singlThreadTime1 - singlThreadTime0));

        multiWorker.interrupt();
        multiWorker.joinAll();

        System.out.println("Bye!");
    }

}
