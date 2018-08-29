package fractal.new_worker;

public interface Task <E> {

    E execute();

    int getJobId();

    void interrupt();

    default Interrupter getInterrupter() {
        return Task.this::interrupt;
    }

}

