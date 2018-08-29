package fractal.new_worker;

import java.util.List;

public interface SplittableTask<E, S extends Task<E>> extends Task<E> {

    List<S> splitTask();

}
