package fractal.new_worker;

import java.util.concurrent.ConcurrentHashMap;

public class CompositeInterrupter implements Interrupter {

    private ConcurrentHashMap<Integer, Interrupter> interrupters;

    private CompositeInterrupter(ConcurrentHashMap<Integer, Interrupter> interrupters) {
        this.interrupters = interrupters;
    }

    public CompositeInterrupter() {
        this(new ConcurrentHashMap<>());
    }

    private int getKey(Interrupter interrupter) {
        return System.identityHashCode(interrupter);
    }

    public void addInterrupter(Interrupter interrupter) {
        interrupters.put(getKey(interrupter), interrupter);
    }

    public void removeInterrupter(Interrupter interrupter) {
        interrupters.remove(getKey(interrupter));
    }

    @Override
    public void interrupt() {
        interrupters.values().forEach(Interrupter::interrupt);
    }
}
