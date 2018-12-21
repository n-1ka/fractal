package repository;

import fractal.FractalDepthPainter;
import fractal.painter.InitialFractalDepthPainter;
import fractal.painter.GrayFractalDepthPainter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepthPaintersRepository {

    private static class Painter {
        String name;
        FractalDepthPainter painter;

        public Painter(String name, FractalDepthPainter painter) {
            this.name = name;
            this.painter = painter;
        }
    }

    private static final List<Painter> DEPTH_PAINTERS;
    private static final List<String> NAMES;
    private static final List<FractalDepthPainter> PAINTERS;
    private static final Map<String, FractalDepthPainter> MAP;

    static {
        DEPTH_PAINTERS = new ArrayList<>();
        DEPTH_PAINTERS.add(new Painter("default", new InitialFractalDepthPainter()));
        DEPTH_PAINTERS.add(new Painter("gray", new GrayFractalDepthPainter()));

        NAMES = new ArrayList<>();
        PAINTERS = new ArrayList<>();
        MAP = new HashMap<>();
        for (Painter painter : DEPTH_PAINTERS) {
            String name = painter.name;
            FractalDepthPainter value = painter.painter;

            NAMES.add(name);
            PAINTERS.add(value);
            MAP.put(name, value);
        }
    }

    private static DepthPaintersRepository instance;

    private DepthPaintersRepository() {}

    public static DepthPaintersRepository getInstance() {
        if (instance == null) {
            synchronized (DepthPaintersRepository.class) {
                instance = new DepthPaintersRepository();
            }
        }

        return instance;
    }


    public int getSize() {
        return PAINTERS.size();
    }

    public List<String> getDepthPainterNames() {
        return new ArrayList<>(NAMES);
    }

    public String getDepthPainterName(int index) {
        return NAMES.get(index);
    }

    public FractalDepthPainter getPainter(int index) {
        return PAINTERS.get(index);
    }

    public FractalDepthPainter getDepthPainter(String name) {
        return MAP.get(name);
    }

}
