package main;

import fractal.worker.FractalDepthPainter;
import fractal.worker.FractalWorker;
import fractal.worker.FractalWorkerMulti;
import fractal.worker.FractalWorkerSingle;
import mandelbrot.DummyFractalDepthPainter;
import mandelbrot.DummyFractalFunction;
import mandelbrot.GrayFractalDepthPainter;
import mandelbrot.MandelbrotFractalEvaluator;
import math.CircleArea;
import math.Number;
import view.FractalPanel;
import view.MainFrame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

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
                worker,
                depthPainters);

		SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.pack();
            mainFrame.setVisible(true);
        });
	}

}
