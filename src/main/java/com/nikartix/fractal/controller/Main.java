package com.nikartix.fractal.controller;

import com.nikartix.fractal.config.Config;
import com.nikartix.fractal.config.ConfigLoader;
import com.nikartix.fractal.config.model.FractalConfig;
import com.nikartix.fractal.config.model.FractalParams;
import com.nikartix.fractal.config.model.ThreadingConfig;
import com.nikartix.fractal.fractal.complex.ComplexFractalTask;
import com.nikartix.fractal.view.ImagePanel;
import com.nikartix.fractal.view.ZoomableImagePanel;
import com.nikartix.fractal.view.main_frame.MainFrame;
import com.nikartix.fractal.worker.MultiThreadWorker;
import com.nikartix.fractal.worker.Worker;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.nikartix.fractal.controller.FractalConstants.*;

public final class Main {

    private static Config config;

    private static Worker buildFractalWorker() {
        Worker worker = new MultiThreadWorker(config.getThreadingConfig().getnThreads());
//        Worker com.nikartix.fractal.worker = new ThreadWorker();
        worker.start();
        return worker;
    }

    private static ComplexFractalTask buildInitialTask() {
        ThreadingConfig threadingConfig = config.getThreadingConfig();

        return new ComplexFractalTask(
                null,
                null,
                null,
                threadingConfig.getTaskWindowHeight(),
                threadingConfig.getTaskWindowWidth()
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

    private static void runFractal() {
        Worker worker = buildFractalWorker();
        FractalConfig fractalConfig = config.getFractalConfig();
        FractalParams params = fractalConfig.getParams();

        ZoomableImagePanel imagePanel = new ZoomableImagePanel(Color.RED);
        MainFrame mainFrame = buildMainFrame(imagePanel);

        Timer timer = new Timer(params.getImageRefreshRateMs(), (e) -> imagePanel.refreshImage());
        timer.setRepeats(true);
        timer.start();

        FractalWorkerController workerController = new FractalWorkerController(worker, buildInitialTask());
        FractalImageController imageController = new FractalImageController(
                workerController,
                params.getInitialArea().toCircleArea(),
                imagePanel,
                params.getPixelScale()
        );

        FractalZoomController zoomController = new FractalZoomController(imageController);
        imagePanel.addImageZoomListener(zoomController);

        new FractalController(mainFrame, workerController, PAINTERS_REPOSITORY, imageController, params);
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> res = new HashMap<>();

        for (String arg : args) {
            if (arg.startsWith("--") && arg.contains("=")) {
                String[] split = arg.split("=", 1);
                String key = split[0].substring(2);
                String value = split[1];
                res.put(key, value);
            }
        }

        return res;
    }

    private static Config loadConfig(String[] args) {
        Map<String, String> argsMap = parseArgs(args);
        String configPath = argsMap.getOrDefault("config", "config.yaml");

        return ConfigLoader.getInstance(configPath).getConfig();
    }

    public static void main(String[] args) {
        config = loadConfig(args);
        runFractal();
    }

}
