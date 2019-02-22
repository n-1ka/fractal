package com.nikartix.fractal.test;

import com.nikartix.fractal.fractal.complex.ComplexFractalTask;
import com.nikartix.fractal.fractal.painter.InitialFractalDepthPainter;
import com.nikartix.fractal.worker.MultiThreadWorker;
import com.nikartix.fractal.worker.Worker;
import com.nikartix.fractal.fractal.mandelbrot.*;
import com.nikartix.fractal.math.Number;
import com.nikartix.fractal.math.RectArea;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MandelbrotFractalTaskTest {

    private Worker worker;

    public MandelbrotFractalTaskTest(Worker worker) {
        this.worker = worker;
    }

    public void run() {
        BufferedImage image = new BufferedImage(1080, 1080, BufferedImage.TYPE_INT_RGB);

        ComplexFractalTask task = new ComplexFractalTask(image,
                new MandelbrotFractalEvaluator(
                        10000,
                        Number.buildFloat(2.0),
                        new InitialFractalDepthPainter(),
                        new MandelbrotFractalFunction()
                ),
                new RectArea(
                        Number.buildFloat(-2.0),
                        Number.buildFloat(2.0),
                        Number.buildFloat(-2.0),
                        Number.buildFloat(2.0)
                ),
                10,
                10
        );

        task.addListener(this::onFinish);

        worker.start();
        worker.execute(task);
    }

    private void onFinish(Object task, BufferedImage image) {
        System.out.println("Task finished!");
        try {
            File file = new File("/home/nika/Desktop/new_fractal.jpg");
            ImageIO.write(image, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            worker.interrupt();
        }
    }

    public static void main(String[] args) {
        new MandelbrotFractalTaskTest(new MultiThreadWorker(10)).run();
    }

}
