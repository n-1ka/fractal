package controller;

import fractal.FractalWorkerListener;
import fractal.worker.FractalWorker;
import math.CircleArea;
import util.MathUtil;
import view.ImagePanel;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FractalImageController implements ComponentListener, FractalWorkerListener {

    private FractalWorker worker;
    private BufferedImage currentImage;
    private CircleArea fractalArea;
    private ImagePanel imagePanel;
    private int pixels;

    private List<FractalAreaUpdateListener> areaUpdateListeners;

    public FractalImageController(FractalWorker worker, CircleArea fractalArea, ImagePanel imagePanel, int pixels) {
        this.worker = worker;
        this.currentImage = null;
        this.fractalArea = fractalArea;
        this.imagePanel = imagePanel;
        this.pixels = pixels;
        this.areaUpdateListeners = new ArrayList<>();

        imagePanel.addComponentListener(this);
        worker.addFractalWorkerListener(this);
    }

    public void addFractalImageUpdateListener(FractalAreaUpdateListener listener) {
        areaUpdateListeners.add(listener);
    }

    private void notifyFractalImageUpdate(CircleArea area) {
        areaUpdateListeners.forEach(l -> l.fractalAreaUpdated(area));
    }

    public CircleArea getFractalArea() {
        return fractalArea;
    }

    public void setFractalArea(CircleArea fractalArea) {
        this.fractalArea = fractalArea;
        updateImage(true);
        notifyFractalImageUpdate(fractalArea);
    }

    public int getPixels() {
        return pixels;
    }

    public void setPixels(int pixels) {
        this.pixels = pixels;
        updateImage(true);
    }

    private void updateImage(boolean force) {
        int width = imagePanel.getWidth();
        int height = imagePanel.getHeight();
        int imagePixels = width * height;
        double ratio = Math.sqrt(pixels / (double) imagePixels);
        width = (int) (width * ratio);
        height = (int) (height * ratio);

        if (width > 0 && height > 0 &&
                (force || currentImage == null ||
                        currentImage.getWidth() != width || currentImage.getHeight() != height)) {
            currentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            worker.setImage(currentImage);
            worker.setArea(MathUtil.squareToRect(fractalArea, width / (double) height));
        }
    }

    @Override
    public void fractalUpdated(FractalWorker worker, BufferedImage image) {
        if (image == currentImage) {
            SwingUtilities.invokeLater(() -> imagePanel.setImage(currentImage));
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateImage(false);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        updateImage(false);
    }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }

}
