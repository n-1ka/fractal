package fractal.new_worker;

import java.util.List;

public abstract class SplittableTask<E, EV, S extends Task<EV>> extends Task<E> {

    public abstract List<S> splitTask();

}
