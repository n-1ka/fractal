package controller;

import fractal.worker.FractalWorker;
import fractal.worker.FractalWorkerMulti;
import mandelbrot.DummyFractalFunction;
import mandelbrot.MandelbrotFractalEvaluator;
import math.CircleArea;
import math.RectArea;
import repository.DepthPaintersRepository;
import view.main_frame.MainFrame;
import view.main_frame.MainFrameEventListener;

public final class FractalController implements MainFrameEventListener {

    private static final int INITIAL_MAX_DEPTH = 100;
    private static final DummyFractalFunction INITIAL_FRACTAL_FUNCTION = new DummyFractalFunction();

    private FractalWorker worker;
    private MainFrame frame;
    private DepthPaintersRepository paintersRepository;
    private int currentPainterIndex;

    public FractalController(FractalWorkerMulti worker, MainFrame frame, DepthPaintersRepository paintersRepository) {
        this.worker = worker;
        this.frame = frame;
        this.paintersRepository = paintersRepository;
        this.currentPainterIndex = 0;

        worker.setEvaluator(new MandelbrotFractalEvaluator(
                INITIAL_MAX_DEPTH,
                paintersRepository.getPainter(currentPainterIndex),
                INITIAL_FRACTAL_FUNCTION
        ));

        frame.addListener(this);
        initFields();
    }

    private void initFields() {
        RectArea area = worker.getArea();
        // TODO: Implement
    }

    @Override
    public void updateClicked() {
        System.out.println("Update clicked!");
        // TODO: Implement
    }

    @Override
    public void resetZoomClicked() {
        System.out.println("Zoom clicked!");
        // TODO: Implement
    }

    @Override
    public void fractalPainterChanged(String painterName) {
        System.out.println("Painter changed to : " + painterName);
        // TODO: Implement
    }

}
