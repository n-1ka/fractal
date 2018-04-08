package fractal;

import fractal.worker.FractalProblem;
import fractal.worker.FractalWorker;

public interface FractalWorkerListener {

    void fractalPainted(FractalWorker worker, FractalProblem problem);

}
