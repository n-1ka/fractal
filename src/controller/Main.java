package controller;

import fractal.worker.MultiThreadWorker;
import fractal.worker.Worker;
import mandelbrot.MandelbrotFractalTask;
import view.ImagePanel;
import view.ZoomableImagePanel;
import view.main_frame.MainFrame;

import javax.swing.*;
import java.awt.*;

import static controller.FractalConstants.*;

public final class Main {

    private static Worker buildFractalWorker() {
        Worker worker = new MultiThreadWorker(8);
        worker.start();
        return worker;
    }

    private static MandelbrotFractalTask buildInitialTask() {
        return new MandelbrotFractalTask(
                null,
                null,
                null,
                10,
                10
        );
    }

    private static MainFrame buildMainFrame(ImagePanel imagePanel) {
        MainFrame mainFrame = new MainFrame(imagePanel, PAINTERS_REPOSITORY.getDepthPainterNames());

        SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.pack();
            mainFrame.setVisible(true);
        });

        return mainFrame;
    }

    public static void main(String[] args) {
        Worker worker = buildFractalWorker();

        ZoomableImagePanel imagePanel = new ZoomableImagePanel(Color.RED);
        MainFrame mainFrame = buildMainFrame(imagePanel);

        Timer timer = new Timer(IMAGE_REFRESH_RATE_MS, (e) -> imagePanel.refreshImage());
        timer.setRepeats(true);
        timer.start();

        FractalWorkerController workerController = new FractalWorkerController(worker, buildInitialTask());
        FractalImageController imageController = new FractalImageController(workerController, INITIAL_AREA, imagePanel, INITIAL_PIXEL_SCALE);

        new FractalController(mainFrame, workerController, PAINTERS_REPOSITORY, imageController);
        new FractalZoomController(imagePanel, imageController);
    }

}
