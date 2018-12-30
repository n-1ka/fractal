package com.nikartix.fractal.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ZoomableImagePanel extends ImagePanel {

    private Integer x0, y0, x1, y1;
    private Color zoomColor;
    private List<ImageZoomListener> listeners;

    public ZoomableImagePanel(Image image, Color zoomColor) {
        super(image);
        x0 = y0 = x1= y1 = null;
        this.zoomColor = zoomColor;
        this.listeners = new ArrayList<>();

        MouseListener mouseListener = new MouseListener();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    public ZoomableImagePanel(Color zoomColor) {
        this(null, zoomColor);
    }

    public Color getZoomColor() {
        return zoomColor;
    }

    public void setZoomColor(Color zoomColor) {
        this.zoomColor = zoomColor;
    }

    public void addImageZoomListener(ImageZoomListener listener) {
        this.listeners.add(listener);
    }

    private class Zoom {
        int x, y, diameter;
    }

    private Zoom calculateZoom(int x0, int y0, int x1, int y1) {
        Zoom res = new Zoom();
        int radius = Math.max(Math.abs(x1 - x0), Math.abs(y1 - y0));
        res.x = x0 - radius;
        res.y = y0 - radius;
        res.diameter = radius * 2;
        return res;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (x0 != null && y0 != null) {
            Zoom zoom = calculateZoom(x0, y0, x1, y1);

            Color oldColor = g.getColor();
            g.setColor(zoomColor);
            g.drawOval(zoom.x, zoom.y, zoom.diameter, zoom.diameter);
            g.setColor(oldColor);
        }
    }

    private class MouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            x0 = x1 = e.getX();
            y0 = y1 = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (x0 != null && x1 != null && y0 != null && y1 != null) {
                Zoom zoom = calculateZoom(x0, y0, x1, y1);
                x0 = x1 = y0 = y1 = null;
                repaint();

                listeners.forEach(l -> {
                    l.imageZoomed(zoom.x, zoom.y, zoom.diameter, ZoomableImagePanel.this);
                });
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            x0 = x1 = y0 = y1 = null;
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (x0 != null && y0 != null) {
                x1 = e.getX();
                y1 = e.getY();
                repaint();
            }
        }
    }

}
