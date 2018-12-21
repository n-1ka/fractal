package test;

import fractal.new_worker.ThreadWorker;
import fractal.new_worker.Worker;
import mandelbrot.*;
import math.Number;
import math.RectArea;

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

        MandelbrotFractalTask task = new MandelbrotFractalTask(image,
                new MandelbrotFractalEvaluator(
                        10000,
                        Number.buildFloat(2.0),
                        new DummyFractalDepthPainter(),
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
        new MandelbrotFractalTaskTest(new ThreadWorker()).run();
    }

}
