package main;

import fractal.FractalWorkerListener;
import fractal.worker.FractalProblem;
import fractal.worker.FractalWorker;
import mandelbrot.DummyFractalDepthPainter;
import mandelbrot.DummyFractalFunction;
import mandelbrot.MandelbrotFractalEvaluator;
import math.Area;
import math.Number;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TestMain extends JFrame implements FractalWorkerListener {

    private Image img;

    public TestMain() {
        FractalWorker worker = new FractalWorker(
                new MandelbrotFractalEvaluator(
                        20,
                        new DummyFractalDepthPainter(),
                        new DummyFractalFunction()
                )
        );

        worker.setImage(new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB));
        worker.setProblem(new FractalProblem(
                new Area(
                        Number.buildFloat(-2.0), Number.buildFloat(2.0),
                        Number.buildFloat(-2.0), Number.buildFloat(2.0)
                )
        ));
        worker.addFractalWorkerListener(this);

        worker.start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TestMain main = new TestMain();
            main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            main.pack();
            main.setVisible(true);
        });
    }

    @Override
    public void fractalPainted(FractalWorker worker) {
        img = worker.getImage();
        setSize(img.getWidth(this), img.getHeight(this));
        repaint();
    }
}
