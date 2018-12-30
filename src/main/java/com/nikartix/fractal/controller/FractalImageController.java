package com.nikartix.fractal.controller;

import com.nikartix.fractal.math.CircleArea;
import com.nikartix.fractal.util.MathUtil;
import com.nikartix.fractal.view.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FractalImageController implements ComponentListener, FractalWorkerListener {

    private BufferedImage currentImage;
    private FractalWorkerController workerController;
    private CircleArea fractalArea;
    private ImagePanel imagePanel;
    private double pixelScale;

    private List<FractalAreaUpdateListener> areaUpdateListeners;

    public FractalImageController(FractalWorkerController workerController, CircleArea fractalArea, ImagePanel imagePanel, double pixelScale) {
        this.currentImage = null;
        this.workerController = workerController;
        this.fractalArea = fractalArea;
        this.imagePanel = imagePanel;
        this.pixelScale = pixelScale;
        this.areaUpdateListeners = new ArrayList<>();

        imagePanel.addComponentListener(this);
        workerController.addFractalWorkerListener(this);
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
        updateImage();
        notifyFractalImageUpdate(fractalArea);
    }

    public double getPixelScale() {
        return pixelScale;
    }

    public void setPixelScale(double pixelScale) {
        this.pixelScale = pixelScale;
        updateImage();
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    private void fillImage(BufferedImage image, Color color) {
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(color);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    private void updateImage() {
        int width = (int)(imagePanel.getWidth()/pixelScale);
        int height = (int)(imagePanel.getHeight()/pixelScale);

        if (currentImage == null ||
                currentImage.getWidth() != width ||
                currentImage.getHeight() != height) {
            currentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imagePanel.setImage(currentImage);
            workerController.updateImage(currentImage);
        }

        if (width > 0 && height > 0) {
            workerController.updateArea(MathUtil.squareToRect(fractalArea, width / (double) height));
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateImage();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        updateImage();
    }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }

    @Override
    public void fractalUpdated(BufferedImage image) {
        if (image == currentImage) {
            SwingUtilities.invokeLater(() -> imagePanel.setImage(image));
        }
    }

}
