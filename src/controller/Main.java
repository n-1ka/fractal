package controller;

import fractal.worker.FractalDepthPainter;
import fractal.worker.FractalWorker;
import fractal.worker.FractalWorkerMulti;
import mandelbrot.DummyFractalDepthPainter;
import mandelbrot.GrayFractalDepthPainter;
import math.CircleArea;
import math.Number;
import repository.DepthPaintersRepository;
import view.FractalPanel;
import view.main_frame.MainFrame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public final class Main {

    private static final Map<String, FractalDepthPainter> DEPTH_PAINTERS = new HashMap<>();
    private static final CircleArea INITIAL_AREA = new CircleArea(
            Number.buildFloat(0.0),
            Number.buildFloat(0.0),
            Number.buildFloat(4.0));
    private static final DepthPaintersRepository PAINTERS_REPOSITORY = DepthPaintersRepository.getInstance();

    static {
        DEPTH_PAINTERS.put("default", new DummyFractalDepthPainter());
        DEPTH_PAINTERS.put("grey", new GrayFractalDepthPainter());
    }


    private static FractalWorkerMulti buildFractalWorker() {
        FractalWorkerMulti worker = new FractalWorkerMulti(4, 4);
        worker.start();
        return worker;
    }

    private static MainFrame buildMainFrame(FractalWorker worker) {
        MainFrame mainFrame = new MainFrame(new FractalPanel(worker, INITIAL_AREA),
                PAINTERS_REPOSITORY.getDepthPainterNames());

        SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.pack();
            mainFrame.setVisible(true);
        });

        return mainFrame;
    }

    public static void main(String[] args) {
        FractalWorkerMulti worker = buildFractalWorker();
        MainFrame mainFrame = buildMainFrame(worker);
        new FractalController(worker, mainFrame, PAINTERS_REPOSITORY);
    }

}
