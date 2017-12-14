package fractal;

import math.Mcomplex;
import math.Mfloat;
import math.Number;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FractalWorker extends Thread {

    private volatile boolean running = true;

    private int xSize, ySize;
    private double doneRatio;
    private Mfloat x0, x1, y0, y1;
    private Color[][] grid;
    private FractalEvaluator evaluator;
    private List<FractalWorkerListener> listeners;

    public FractalWorker(FractalEvaluator evaluator, Mfloat x0, Mfloat x1, Mfloat y0, Mfloat y1, int xSize, int ySize) {
        this.evaluator = evaluator;
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.xSize = xSize;
        this.ySize = ySize;
        this.doneRatio = 0.0;
        this.grid = new Color[xSize][ySize];
        this.listeners = new ArrayList<>();
    }

    public void addFractalWorkerListener(FractalWorkerListener l) {
        listeners.add(l);
    }

    public void removeFractalWorkerListener(FractalWorkerListener l) {
        listeners.remove(l);
    }

    public Color getColorAt(int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[x].length)
            return Color.WHITE;
        else
            return grid[x][y];
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public void terminate() {
        running = false;
    }

    public double getDoneRatio() {
        return doneRatio;
    }

    private void notifyFinish() {
        for (FractalWorkerListener l : listeners) {
            l.fractalWorkerFinished(this);
        }
    }

    private Color evaluate(int x, int y) throws InterruptedException {
        if (!running) {
            throw new InterruptedException("Interrupted by client.");
        }

        return evaluator.evaluate(getComplexAt(x, y));
    }

    private Mcomplex getComplexAt(int x, int y) {
        double xMul = x / (double) getXSize();
        double yMul = y / (double) getYSize();
        Mfloat real = x0.add(x1.sub(x0).mul(Number.buildFloat(xMul)));
        Mfloat imag = y0.add(y1.sub(y0).mul(Number.buildFloat(yMul)));
        return Number.buildComplex(real, imag);
    }

    @Override
    public void run() {
        int x, y;
        x = getXSize();
        y = getYSize();

        try {
            doneRatio = 0.0;
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    grid[i][j] = evaluate(i, j);
                }
                doneRatio = (i + 1) / (double) x;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        notifyFinish();
    }

}
