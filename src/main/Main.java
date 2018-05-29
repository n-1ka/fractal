package main;

import fractal.worker.FractalDepthPainter;
import fractal.worker.FractalWorker;
import mandelbrot.DummyFractalDepthPainter;
import mandelbrot.DummyFractalFunction;
import mandelbrot.GrayFractalDepthPainter;
import mandelbrot.MandelbrotFractalEvaluator;
import math.CircleArea;
import math.Number;
import view.MainFrame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static FractalWorker buildFractalWorker() {
        return new FractalWorker(
                new MandelbrotFractalEvaluator(
                        100,
                        new DummyFractalDepthPainter(),
                        new DummyFractalFunction()
                )
        );
    }

    public static void main(String[] args) {
        FractalWorker worker = buildFractalWorker();
        worker.start();

        Map<String, FractalDepthPainter> depthPainters = new HashMap<>();
        depthPainters.put("default", new DummyFractalDepthPainter());
        depthPainters.put("grey", new GrayFractalDepthPainter());

        MainFrame mainFrame = new MainFrame(worker,
                new CircleArea(
                        Number.buildFloat(0.0),
                        Number.buildFloat(0.0),
                        Number.buildFloat(4.0)),
                depthPainters);

		SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.pack();
            mainFrame.setVisible(true);
        });
	}

}
