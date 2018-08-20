package controller;

import fractal.worker.FractalDepthPainter;
import fractal.worker.FractalWorker;
import fractal.worker.FractalWorkerMulti;
import mandelbrot.DummyFractalDepthPainter;
import mandelbrot.DummyFractalFunction;
import mandelbrot.GrayFractalDepthPainter;
import mandelbrot.MandelbrotFractalEvaluator;
import math.CircleArea;
import math.Number;
import view.FractalPanel;
import view.main_frame.MainFrame;
import view.main_frame.MainFrameEventListener;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public final class Main implements MainFrameEventListener {

    private static FractalWorker buildFractalWorker() {
        MandelbrotFractalEvaluator evaluator = new MandelbrotFractalEvaluator(
                100,
                new DummyFractalDepthPainter(),
                new DummyFractalFunction()
        );

        FractalWorkerMulti worker = new FractalWorkerMulti(3, 3, evaluator);
        worker.start();
        return worker;
    }

    public static void main(String[] args) {
        Main main = new Main();
        FractalWorker worker = buildFractalWorker();

        Map<String, FractalDepthPainter> depthPainters = new HashMap<>();
        depthPainters.put("default", new DummyFractalDepthPainter());
        depthPainters.put("grey", new GrayFractalDepthPainter());

        CircleArea circleArea = new CircleArea(
                Number.buildFloat(0.0),
                Number.buildFloat(0.0),
                Number.buildFloat(4.0));

        MainFrame mainFrame = new MainFrame(
                new FractalPanel(worker, circleArea),
                depthPainters);

        mainFrame.addListener(main);

		SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.pack();
            mainFrame.setVisible(true);
        });
	}

    @Override
    public void updateClicked() {
        System.out.println("Update clicked!");
    }

    @Override
    public void resetZoomClicked() {
        System.out.println("Zoom clicked!");
    }

    @Override
    public void fractalPainterChanged(FractalDepthPainter depthPainter) {
        System.out.println("Painter changed to : " + depthPainter.toString());
    }

}
