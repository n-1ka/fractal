package controller;

import fractal.worker.FractalDepthPainter;
import fractal.worker.FractalFunction;
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

import static controller.FractalConstants.*;

public final class FractalController implements MainFrameEventListener {

    private FractalWorker worker;
    private MainFrame frame;
    private DepthPaintersRepository paintersRepository;
    private FractalImageController imageController;
    private int currentPainterIndex;
    private final MandelbrotFractalEvaluator evaluator;

    private static MandelbrotFractalEvaluator buildEvaluator(int maxDepth, Mfloat edge,
                                                             FractalDepthPainter painter, FractalFunction function) {
        return new MandelbrotFractalEvaluator(maxDepth, edge, painter, function);
    }

    public FractalController(FractalWorkerMulti worker, MainFrame frame,
                             DepthPaintersRepository paintersRepository, FractalImageController imageController) {
        this.worker = worker;
        this.frame = frame;
        this.paintersRepository = paintersRepository;
        this.imageController = imageController;
        this.currentPainterIndex = 0;
        this.evaluator = buildEvaluator(
                INITIAL_MAX_DEPTH,
                Number.buildFloat(INITIAL_EDGE),
                paintersRepository.getPainter(currentPainterIndex),
                INITIAL_FRACTAL_FUNCTION
        );

        worker.setEvaluator(evaluator);

        frame.addListener(this);
        initFields();
    }

    private void initFields() {
        // Coordinates
        CircleArea fractalArea = imageController.getFractalArea();
        int pixels = imageController.getPixels();
        int depth = evaluator.getDepth();
        Mfloat edge = evaluator.getEdge();

        SwingUtilities.invokeLater(() -> {
            frame.setXField(fractalArea.getCenterX().toString());
            frame.setYField(fractalArea.getCenterY().toString());
            frame.setFractalViewSizeField(fractalArea.getDiameter().toString());

            frame.setPixelsField(String.valueOf(pixels));
            frame.setFractalDepthField(String.valueOf(depth));
            frame.setFractalEdgeField(String.valueOf(edge));
        });
    }

    @Override
    public void updateClicked() {
        SwingUtilities.invokeLater(() -> {
            try {
                int pixels = Integer.parseInt(frame.getPixelsField());
                int depth = Integer.parseInt(frame.getFractalDepthField());
                Mfloat edge = Number.buildFloat(frame.getFractalEdgeField());
                FractalDepthPainter depthPainter = paintersRepository.getDepthPainter(
                        frame.getCurrentDepthPainterName());

                MandelbrotFractalEvaluator newEvaluator = buildEvaluator(depth, edge, depthPainter,
                        evaluator.getFunction()     // TODO: Implement function update feature
                );

                imageController.setPixels(pixels);
                worker.setEvaluator(newEvaluator);
            } catch (NumberFormatException e) {
                System.out.println(String.format("Number field format error: %s", e));
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
