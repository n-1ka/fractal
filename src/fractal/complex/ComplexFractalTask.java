package fractal.complex;

import math.Mcomplex;
import math.Mfloat;
import math.Number;
import math.RectArea;
import worker.task.AbstractAsyncTask;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ComplexFractalTask extends AbstractAsyncTask<BufferedImage, BufferedImage> {

    private BufferedImage image;
    private ComplexFractalEvaluator evaluator;
    private RectArea area;

    private int subTaskHeight;
    private int subTaskWidth;

    public ComplexFractalTask(BufferedImage image,
                              ComplexFractalEvaluator evaluator,
                              RectArea area,
                              int subTaskHeight,
                              int subTaskWidth) {
        this.image = image;
        this.evaluator = evaluator;
        this.area = area;

        this.subTaskHeight = subTaskHeight;
        this.subTaskWidth = subTaskWidth;
    }

    public BufferedImage getImage() {
        return image;
    }

    public ComplexFractalEvaluator getEvaluator() {
        return evaluator;
    }

    public RectArea getArea() {
        return area;
    }

    public int getSubTaskHeight() {
        return subTaskHeight;
    }

    public int getSubTaskWidth() {
        return subTaskWidth;
    }

    private ComplexFractalTask copy(Consumer<ComplexFractalTask> applyFn) {
        ComplexFractalTask res = new ComplexFractalTask(
                image, evaluator, area,
                subTaskHeight, subTaskWidth
        );
        applyFn.accept(res);
        return res;
    }

    public ComplexFractalTask setImage(BufferedImage image) {
        return copy(task -> task.image = image);
    }

    public ComplexFractalTask setEvaluator(ComplexFractalEvaluator evaluator) {
        return copy(task -> task.evaluator = evaluator);
    }

    public ComplexFractalTask setArea(RectArea area) {
        return copy(task -> task.area = area);
    }

    public ComplexFractalTask setSubTaskHeight(int subTaskHeight) {
        return copy(task -> task.subTaskHeight = subTaskHeight);
    }

    public ComplexFractalTask setSubTaskWidth(int subTaskWidth) {
        return copy(task -> task.subTaskWidth = subTaskWidth);
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

    private static class SubImage {
        private int x, y, w, h;
        private BufferedImage image;
        private RectArea area;

        public SubImage(int x, int y, int w, int h, BufferedImage image, RectArea area) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.image = image;
            this.area = area;
        }
    }

    private RectArea getSubArea(int x, int y, int w, int h) {
        y = image.getHeight() - y - h;

        Mfloat areaW = area.getWidth().div(image.getWidth());
        Mfloat areaH = area.getHeight().div(image.getHeight());

        Mfloat areaX0 = area.getX0().add(areaW.mul(x));
        Mfloat areaY0 = area.getY0().add(areaH.mul(y));
        Mfloat areaX1 = areaX0.add(areaW.mul(w));
        Mfloat areaY1 = areaY0.add(areaH.mul(h));

        return new RectArea(
                areaX0, areaX1,
                areaY0, areaY1
        );
    }

    private List<SubImage> generateSubImages() {
        List<SubImage> res = new ArrayList<>();

        int width = image.getWidth();
        int height = image.getHeight();
        int dx = subTaskWidth;
        int dy = subTaskHeight;

        int x = 0;
        while (x < width) {
            int y = 0;
            while (y < height) {
                int w = Math.min(dx, width - x);
                int h = Math.min(dy, height - y);

                BufferedImage subimage = image.getSubimage(x, y, w, h);
                RectArea subArea = getSubArea(x, y, w, h);

                res.add(new SubImage(x, y, w, h, subimage, subArea));

                y += dy;
            }
            x += dx;
        }

        return res;
    }

    @Override
    protected List<AbstractAsyncTask<BufferedImage, ?>> splitTask() {
        List<AbstractAsyncTask<BufferedImage, ?>> subTasks = new ArrayList<>();

        for (SubImage subImage : generateSubImages()) {
            subTasks.add(new ComplexFractalTask(subImage.image, evaluator, subImage.area, subTaskHeight, subTaskWidth));
        }

        return subTasks;
    }

    @Override
    protected BufferedImage joinSubResults(List<BufferedImage> results) {
        return image;
    }

}
