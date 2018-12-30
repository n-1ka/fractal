package com.nikartix.fractal.controller;

import com.nikartix.fractal.math.CircleArea;
import com.nikartix.fractal.math.Mfloat;
import com.nikartix.fractal.math.Number;
import com.nikartix.fractal.view.ImageZoomListener;
import com.nikartix.fractal.view.ZoomableImagePanel;

public class FractalZoomController implements ImageZoomListener {

    private ZoomableImagePanel panel;
    private FractalImageController imageController;

    public FractalZoomController(ZoomableImagePanel panel, FractalImageController imageController) {
        this.panel = panel;
        this.imageController = imageController;
        this.panel.addImageZoomListener(this);
    }

    @Override
    public void imageZoomed(int x, int y, int diameter, ZoomableImagePanel panel) {
        int panelDiameter = Math.min(panel.getWidth(), panel.getHeight());
        x = x + diameter/2;
        y = y + diameter/2;

        int centerOffX = x - panel.getWidth()/2;
        // Minus because we want origin to be left bottom
        int centerOffY = -(y - panel.getHeight()/2);

        Mfloat diameterRatio = Number.buildFloat(diameter / (double) panelDiameter);
        CircleArea fractalArea = imageController.getFractalArea();

        Mfloat fractalAreaDiameter = fractalArea.getDiameter();
        Mfloat dx = fractalAreaDiameter.mul(Number.buildFloat(centerOffX/(double) panelDiameter));
        Mfloat dy = fractalAreaDiameter.mul(Number.buildFloat(centerOffY/(double) panelDiameter));
        Mfloat newX = fractalArea.getCenterX().add(dx);
        Mfloat newY = fractalArea.getCenterY().add(dy);
        Mfloat newDiameter = fractalArea.getDiameter().mul(diameterRatio);

        CircleArea newArea = new CircleArea(newX, newY, newDiameter);
        imageController.setFractalArea(newArea);
    }
}
