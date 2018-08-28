package controller;

import fractal.worker.FractalWorkerMulti;
import view.ImagePanel;
import view.ZoomableImagePanel;
import view.main_frame.MainFrame;

import javax.swing.*;

import java.awt.*;

import static controller.FractalConstants.*;

public final class Main {

    private static FractalWorkerMulti buildFractalWorker() {
        FractalWorkerMulti worker = new FractalWorkerMulti(6, 6);
        worker.start();
        return worker;
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
        FractalWorkerMulti worker = buildFractalWorker();

        ZoomableImagePanel imagePanel = new ZoomableImagePanel(Color.RED);
        MainFrame mainFrame = buildMainFrame(imagePanel);

        Timer timer = new Timer(IMAGE_REFRESH_RATE_MS, (e) -> imagePanel.refreshImage());
        timer.setRepeats(true);
        timer.start();

        FractalImageController imageController = new FractalImageController(worker, INITIAL_AREA, imagePanel, INITIAL_PIXEL_SCALE);

        new FractalController(worker, mainFrame, PAINTERS_REPOSITORY, imageController);
        new FractalZoomController(imagePanel, imageController);
    }

}
