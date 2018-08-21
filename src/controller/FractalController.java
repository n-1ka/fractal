package controller;

import fractal.worker.FractalWorker;
import fractal.worker.FractalWorkerMulti;
import mandelbrot.MandelbrotFractalEvaluator;
import math.CircleArea;
import math.Mfloat;
import math.Number;
import repository.DepthPaintersRepository;
import view.main_frame.MainFrame;
import view.main_frame.MainFrameEventListener;

import javax.swing.*;

import static controller.FractalConstants.INITIAL_AREA;
import static controller.FractalConstants.INITIAL_FRACTAL_FUNCTION;
import static controller.FractalConstants.INITIAL_MAX_DEPTH;

public final class FractalController implements MainFrameEventListener {

    private FractalWorker worker;
    private MainFrame frame;
    private DepthPaintersRepository paintersRepository;
    private FractalImageController imageController;
    private int currentPainterIndex;

    public FractalController(FractalWorkerMulti worker, MainFrame frame,
                             DepthPaintersRepository paintersRepository, FractalImageController imageController) {
        this.worker = worker;
        this.frame = frame;
        this.paintersRepository = paintersRepository;
        this.imageController = imageController;
        this.currentPainterIndex = 0;

        worker.setEvaluator(new MandelbrotFractalEvaluator(
                INITIAL_MAX_DEPTH,
                paintersRepository.getPainter(currentPainterIndex),
                INITIAL_FRACTAL_FUNCTION
        ));

        frame.addListener(this);
        initFields();
    }

    private void initFields() {
        // Coordinates
        CircleArea fractalArea = imageController.getFractalArea();
        int pixels = imageController.getPixels();

        SwingUtilities.invokeLater(() -> {
            frame.setXField(fractalArea.getCenterX().toString());
            frame.setYField(fractalArea.getCenterY().toString());
            frame.setFractalViewSizeField(fractalArea.getDiameter().toString());

            frame.setPixelsField(String.valueOf(pixels));
        });
    }

    @Override
    public void updateClicked() {
        SwingUtilities.invokeLater(() -> {
            try {
                int pixels = Integer.parseInt(frame.getPixelsField());
                imageController.setPixels(pixels);
            } catch (NumberFormatException e) {
                System.out.println(String.format("Pixel field format error: %s", e));
            }
        });
    }

    private void updateCircleArea(CircleArea area) {
        frame.setXField(area.getCenterX().toString());
        frame.setYField(area.getCenterY().toString());
        frame.setFractalViewSizeField(area.getDiameter().toString());
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
                updateCircleArea(INITIAL_AREA);
            } catch (NumberFormatException e) {
                System.out.println(String.format("Number format error: %s", e));
            }
        });
    }

    @Override
    public void fractalPainterChanged(String painterName) {
        System.out.println("Painter changed to : " + painterName);
        // TODO: Implement
    }

}
