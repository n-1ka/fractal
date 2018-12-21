package mandelbrot;

import fractal.worker.task.AbstractAsyncTask;
import fractal.FractalEvaluator;
import math.Mcomplex;
import math.Mfloat;
import math.Number;
import math.RectArea;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MandelbrotFractalTask extends AbstractAsyncTask<BufferedImage, BufferedImage> {

    private BufferedImage image;
    private FractalEvaluator evaluator;
    private RectArea area;

    private int splitRowSize;
    private int splitColSize;

    public MandelbrotFractalTask(BufferedImage image,
                                 FractalEvaluator evaluator,
                                 RectArea area,
                                 int splitRowSize,
                                 int splitColSize) {
        this.image = image;
        this.evaluator = evaluator;
        this.area = area;

        this.splitRowSize = splitRowSize;
        this.splitColSize = splitColSize;
    }

    public BufferedImage getImage() {
        return image;
    }

    public FractalEvaluator getEvaluator() {
        return evaluator;
    }

    public RectArea getArea() {
        return area;
    }

    public int getSplitRowSize() {
        return splitRowSize;
    }

    public int getSplitColSize() {
        return splitColSize;
    }

    private MandelbrotFractalTask copy(Consumer<MandelbrotFractalTask> applyFn) {
        MandelbrotFractalTask res = new MandelbrotFractalTask(
                image, evaluator, area,
                splitRowSize, splitColSize
        );
        applyFn.accept(res);
        return res;
    }

    public MandelbrotFractalTask setImage(BufferedImage image) {
        return copy(task -> task.image = image);
    }

    public MandelbrotFractalTask setEvaluator(FractalEvaluator evaluator) {
        return copy(task -> task.evaluator = evaluator);
    }

    public MandelbrotFractalTask setArea(RectArea area) {
        return copy(task -> task.area = area);
    }

    public MandelbrotFractalTask setSplitRowSize(int splitRowSize) {
        return copy(task -> task.splitRowSize = splitRowSize);
    }

    public MandelbrotFractalTask setSplitColSize(int splitColSize) {
        return copy(task -> task.splitColSize = splitColSize);
    }

    @Override
    public BufferedImage runTask() {
        Mfloat width = area.getWidth();
        Mfloat height = area.getHeight();

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        boolean isInterrupted = false;

        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.RED);
        graphics.drawRect(1, 1, imageWidth-1, imageHeight-1);

        for (int i = 0; i < imageWidth && !isInterrupted; i++) {
            for (int j = 0; j < imageHeight && !isInterrupted; j++) {
                Mfloat real = area.getX0().add(width.mul(i / (double) imageWidth));
                Mfloat imag = area.getY0().add(height.mul(j / (double) imageHeight));

                Mcomplex value = Number.buildComplex(real, imag);

                Color color = evaluator.evaluate(value);

                image.setRGB(i, (imageHeight - j - 1), color.getRGB());

                isInterrupted = isInterrupted();
            }
        }

        if (!isInterrupted) {
            return image;
        } else {
            return null;
        }
    }

    @Override
    protected List<AbstractAsyncTask<BufferedImage, ?>> splitTask() {
        List<AbstractAsyncTask<BufferedImage, ?>> subTasks = new ArrayList<>();

        int width = image.getWidth();
        int height = image.getHeight();
        int rows = Math.min(splitRowSize, height);
        int cols = Math.min(splitColSize, width);
        int w = width/cols;
        int h = height/rows;

        Mfloat areaWidth = area.getWidth().div(cols);
        Mfloat areaHeight = area.getHeight().div(rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BufferedImage subImage = image.getSubimage(j * w, (rows - i - 1) * h, w, h);

                Mfloat areaX0 = area.getX0().add(areaWidth.mul(j));
                Mfloat areaY0 = area.getY0().add(areaHeight.mul(i));
                RectArea areaIJ = new RectArea(
                        areaX0, areaX0.add(areaWidth),
                        areaY0, areaY0.add(areaHeight)
                );

                subTasks.add(new MandelbrotFractalTask(subImage, evaluator, areaIJ, splitRowSize, splitColSize));
            }
        }

        return subTasks;
    }

    @Override
    protected BufferedImage joinSubResults(List<BufferedImage> results) {
        return image;
    }

}
