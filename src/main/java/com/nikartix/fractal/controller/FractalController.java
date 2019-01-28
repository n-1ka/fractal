package com.nikartix.fractal.controller;

import com.nikartix.fractal.config.model.FractalParams;
import com.nikartix.fractal.fractal.FractalDepthPainter;
import com.nikartix.fractal.fractal.FractalFunction;
import com.nikartix.fractal.fractal.mandelbrot.MandelbrotFractalEvaluator;
import com.nikartix.fractal.math.CircleArea;
import com.nikartix.fractal.math.Mfloat;
import com.nikartix.fractal.math.Number;
import com.nikartix.fractal.repository.DepthPaintersRepository;
import com.nikartix.fractal.view.main_frame.MainFrame;
import com.nikartix.fractal.view.main_frame.MainFrameEventListener;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.nikartix.fractal.controller.FractalConstants.*;

public final class FractalController implements MainFrameEventListener {

    private MainFrame frame;
    private FractalWorkerController workerController;
    private DepthPaintersRepository paintersRepository;
    private FractalImageController imageController;
    private MandelbrotFractalEvaluator currentEvaluator;
    private FractalParams params;

    private static MandelbrotFractalEvaluator buildEvaluator(int maxDepth, Mfloat edge,
                                                             FractalDepthPainter painter,
                                                             FractalFunction function) {
        return new MandelbrotFractalEvaluator(maxDepth, edge, painter, function);
    }

    public FractalController(MainFrame frame, FractalWorkerController workerController,
                             DepthPaintersRepository paintersRepository, FractalImageController imageController,
                             FractalParams params) {
        this.frame = frame;
        this.workerController = workerController;
        this.paintersRepository = paintersRepository;
        this.imageController = imageController;
        this.params = params;

        MandelbrotFractalEvaluator initialEvaluator = buildEvaluator(
                params.getMaxDepth(),
                Number.buildFloat(params.getEdge()),
                paintersRepository.getPainter(0),
                INITIAL_FRACTAL_FUNCTION
        );
        this.imageController.addFractalImageUpdateListener(this::updateFrameCircleAreaLabels);

        updateEvaluator(initialEvaluator);

        frame.addListener(this);
        initFields();
    }

    private void updateEvaluator(MandelbrotFractalEvaluator evaluator) {
        this.currentEvaluator = evaluator;
        this.workerController.updateEvaluator(evaluator);
    }

    private void initFields() {
        // Coordinates
        CircleArea fractalArea = imageController.getFractalArea();
        double pixelScale = imageController.getPixelScale();

        int depth = currentEvaluator.getDepth();
        Mfloat edge = currentEvaluator.getEdge();

        SwingUtilities.invokeLater(() -> {
            frame.setXField(fractalArea.getCenterX().toString());
            frame.setYField(fractalArea.getCenterY().toString());
            frame.setFractalViewSizeField(fractalArea.getDiameter().toString());

            frame.setPixelScaleField(String.valueOf(pixelScale));
            frame.setFractalDepthField(String.valueOf(depth));
            frame.setFractalEdgeField(String.valueOf(edge));
        });
    }

    @Override
    public void updateClicked() {
        SwingUtilities.invokeLater(() -> {
            try {
                double pixelScale = Double.parseDouble(frame.getPixelScaleField());
                int depth = Integer.parseInt(frame.getFractalDepthField());
                Mfloat edge = Number.buildFloat(frame.getFractalEdgeField());
                FractalDepthPainter depthPainter = paintersRepository.getDepthPainter(
                        frame.getCurrentDepthPainterName()
                );

                MandelbrotFractalEvaluator newEvaluator = buildEvaluator(depth, edge, depthPainter,
                        INITIAL_FRACTAL_FUNCTION        // TODO: com.nikartix.fractal.fractal function input
                );

                imageController.setPixelScale(pixelScale);
                updateEvaluator(newEvaluator);
            } catch (NumberFormatException e) {
                System.out.println(String.format("Number field format error: %s", e));
            }
        });
    }

    private void updateFrameCircleAreaLabels(CircleArea area) {
        frame.setXField(area.getCenterX().toString());
        frame.setYField(area.getCenterY().toString());
        frame.setFractalViewSizeField(area.getDiameter().toString());
    }

    private void updateCircleArea(CircleArea area) {
        updateFrameCircleAreaLabels(area);
        imageController.setFractalArea(area);
    }

    @Override
    public void moveClicked() {
        SwingUtilities.invokeLater(() -> {
            try {
                Mfloat centerX = Number.buildFloat(frame.getXField());
                Mfloat centerY = Number.buildFloat(frame.getYField());
                Mfloat diameter = Number.buildFloat(frame.getFractalViewSizeField());
                CircleArea area = new CircleArea(centerX, centerY, diameter);
                updateCircleArea(area);
            } catch (NumberFormatException e) {
                System.out.println(String.format("Number format error: %s", e));
            }
        });
    }

    @Override
    public void resetZoomClicked() {
        SwingUtilities.invokeLater(() -> {
            try {
                updateCircleArea(params.getInitialArea().toCircleArea());
            } catch (NumberFormatException e) {
                System.out.println(String.format("Number format error: %s", e));
            }
        });
    }

    @Override
    public void fractalPainterChanged(String painterName) {
        // Useless for now
    }

    private String fileExtension(String fileName) {
        int periondIndex = fileName.lastIndexOf('.');
        if (periondIndex > 0) {
            return fileName.substring(periondIndex + 1);
        } else {
            return "";
        }
    }

    @Override
    public void saveImage(File file) {
        new Thread(() -> {
            try {
                BufferedImage image = imageController.getCurrentImage();
                switch (fileExtension(file.getName())) {
                    case "png":
                        ImageIO.write(image, "png", file);
                        break;
                    case "jpg": case "jpeg":
                        ImageIO.write(image, "jpg", file);
                        break;
                    default:
                        System.out.println("Invalid file extension");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
