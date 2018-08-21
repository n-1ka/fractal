package controller;

import fractal.worker.FractalWorkerMulti;
import view.ImagePanel;
import view.main_frame.MainFrame;

import javax.swing.*;

import static controller.FractalConstants.INITIAL_AREA;
import static controller.FractalConstants.PAINTERS_REPOSITORY;

public final class Main {

    private static FractalWorkerMulti buildFractalWorker() {
        FractalWorkerMulti worker = new FractalWorkerMulti(4, 4);
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

        ImagePanel imagePanel = new ImagePanel();
        MainFrame mainFrame = buildMainFrame(imagePanel);
        FractalImageController imageController = new FractalImageController(worker, INITIAL_AREA, imagePanel);

        new FractalController(worker, mainFrame, PAINTERS_REPOSITORY, imageController);
    }

}
