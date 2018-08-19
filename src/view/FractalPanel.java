package view;

import fractal.FractalWorkerListener;
import fractal.worker.FractalWorker;
import math.CircleArea;
import util.MathUtil;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

public class FractalPanel extends ImagePanel implements FractalWorkerListener, ComponentListener {

    private FractalWorker worker;
    private BufferedImage currentImage;
    private CircleArea fractalArea;

    public FractalPanel(FractalWorker worker, CircleArea fractalArea) {
        this.worker = worker;
        this.fractalArea = fractalArea;
        this.currentImage = null;
        this.worker.addFractalWorkerListener(this);
        this.addComponentListener(this);
    }

    public CircleArea getFractalArea() {
        return fractalArea;
    }

    private void updateImage() {
        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0 &&
                (currentImage == null || currentImage.getWidth() != width || currentImage.getHeight() != height)) {
            currentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            worker.setImage(currentImage);
            worker.setArea(MathUtil.squareToRect(fractalArea, width / (double) height));
        }
    }

    @Override
    public void fractalUpdated(FractalWorker worker, BufferedImage image) {
        if (image == currentImage) {
            SwingUtilities.invokeLater(() -> setImage(currentImage));
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

}
