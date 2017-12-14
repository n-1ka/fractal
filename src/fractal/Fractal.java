package fractal;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import math.Mcomplex;
import math.Mfloat;
import math.Number;

import javax.swing.*;

public class Fractal extends Canvas implements Zoomable, FractalWorkerListener {
	
	private static final Mfloat DEFAULT_X  = Number.buildFloat(0.0);
	private static final Mfloat DEFAULT_Y  = Number.buildFloat(0.0);
	private static final Mfloat DEFAULT_VS = Number.buildFloat(4.1);

	private Mfloat x, y, viewSize;
	private boolean zoomed, workerDone;
	private int pixelSize;
	private FractalEvaluator evaluator;
	private FractalWorker currentWorker;
	private List<FractalListener> listeners;
	
	public Fractal(Mfloat x, Mfloat y, Mfloat viewSize,
				   int pixelSize, FractalEvaluator evaluator) {
		this.x = x;
		this.y = y;
		this.viewSize = viewSize;
		this.pixelSize = pixelSize;
		this.evaluator = evaluator;
		this.listeners = new ArrayList<>();
		this.currentWorker = null;
		this.zoomed = false;
		this.workerDone = false;
	}
	
	public Fractal(int pixelSize, FractalEvaluator evaluator) {
		this(DEFAULT_X, DEFAULT_Y, DEFAULT_VS, pixelSize, evaluator);
	}
	
	public void setPixelSize(int pixelSize) {
		this.pixelSize = pixelSize;
		fractalUpdated();
	}

	public Mfloat getCenterX() {
		return x;
	}

	public void setCenterX(Mfloat x) {
		this.x = x;
		fractalUpdated();
	}

	public Mfloat getCenterY() {
		return y;
	}

	public void setCenterY(Mfloat y) {
		this.y = y;
		fractalUpdated();
	}

	public Mfloat getViewSize() {
		return viewSize;
	}

	public void setViewSize(Mfloat viewSize) {
		this.viewSize = viewSize;
		fractalUpdated();
	}

	public void addFractalListener(FractalListener l) {
		this.listeners.add(l);
	}

	public void removeFractalListener(FractalListener l) {
		this.listeners.remove(l);
	}

	public int getPixelSize() {
		return pixelSize;
	}
	
	public void setFractalEvaluator(FractalEvaluator evaluator) {
		this.evaluator = evaluator;
		fractalUpdated();
	}

	public FractalEvaluator getFractalEvaluator() {
		return evaluator;
	}

	private int ceilDiv(int dividend, int divisor) {
		return (dividend + divisor - 1) / divisor;
	}

	private FractalWorker buildFractalWorker() {
	    int w = getWidth();
        int h = getHeight();
        int xSize = ceilDiv(w, pixelSize);
		int ySize = ceilDiv(h, pixelSize);

        double xMul, yMul;
        Mfloat x0, x1, y0, y1;

        xMul = yMul = 1.0;
        if (w > h) {
            xMul = w / (double) h;
        } else {
            yMul = h / (double) w;
        }

        Mfloat xViewSize = viewSize.mul(Number.buildFloat(xMul/2));
        Mfloat yViewSize = viewSize.mul(Number.buildFloat(yMul/2));

        x0 = x.sub(xViewSize);
        x1 = x.add(xViewSize);
        y0 = y.sub(yViewSize);
        y1 = y.add(yViewSize);

        return new FractalWorker(getFractalEvaluator(), x0, x1, y0, y1, xSize, ySize);
    }

    private boolean rerunWorker() {
        int currentWidth = ceilDiv(getWidth(), pixelSize);
        int currentHeight = ceilDiv(getHeight(), pixelSize);

        return currentWorker == null ||
                zoomed ||
                currentWorker.getXSize() != currentWidth ||
                currentWorker.getYSize() != currentHeight;
    }

    private void stopWorker() {
        try {
            if (currentWorker != null) {
                currentWorker.terminate();
                currentWorker.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentWorker = null;
    }

	private void runWorker() {
		if (rerunWorker()) {
		    stopWorker();

			currentWorker = buildFractalWorker();
			currentWorker.addFractalWorkerListener(this);
			workerDone = false;
			zoomed = false;
			currentWorker.start();
		}
	}

	@Override
	public void fractalWorkerFinished(FractalWorker worker) {
        SwingUtilities.invokeLater(Fractal.this::repaint);
	}

	@Override
	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();

		runWorker();
        for (int i=0; i<width; i += pixelSize) {
			for (int j=0; j<height; j += pixelSize) {
				drawPixel(i, j, g);
			}
		}
	}
	
	private void drawRect(Graphics g, int x, int y, int width, int height) {
	    g.fillRect(x, y, width, height);
	}
	
	private void drawPixel(int x, int y, Graphics g) {
		int workerX = x / pixelSize;
		int workerY = (getHeight() - y) / pixelSize;	// Need to revert Y axis
        Color color = currentWorker.getColorAt(workerX, workerY);

		g.setColor(color);
		drawRect(g, x, y, pixelSize, pixelSize);
		g.setColor(Color.WHITE);
	}
	
	private Mcomplex getPixelValue(double x, double y) {
		double w = getWidth();
		double h = getHeight();
		double cx = x - w/2;
		double cy = h/2 - y;

		double minSize = Math.min(w, h);
		Mfloat ratio = viewSize.div(Number.buildFloat(minSize));

		Mfloat real = Number.buildFloat(cx).mul(ratio);
		Mfloat imag = Number.buildFloat(cy).mul(ratio);
		return Number.buildComplex(this.x.add(real), this.y.add(imag));
	}

	@Override
	public void zoom(double x, double y, double scale) {
		x = x - 0.5;
		y = 0.5 - y;

		double w = getWidth();
		double h = getHeight();

		Mfloat width, height;
		if (h < w) {
			height = viewSize;
			width = height.mul(Number.buildFloat(w / h));
		} else {
			width = viewSize;
			height = width.mul(Number.buildFloat(h / w));
		}
		
		this.x = this.x.add(width.mul(Number.buildFloat(x)));
		this.y = this.y.add(height.mul(Number.buildFloat(y)));
		this.viewSize = viewSize.mul(Number.buildFloat(scale));

		zoomed = true;
		fractalUpdated();
	}

	private void fractalUpdated() {
		repaint();
		for (FractalListener l : listeners) {
			l.fractalUpdated(this);
		}
	}

}
