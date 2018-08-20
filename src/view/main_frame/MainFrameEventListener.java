package view.main_frame;

import fractal.worker.FractalDepthPainter;

public interface MainFrameEventListener {

    void updateClicked();

    void resetZoomClicked();

    void fractalPainterChanged(String painterName);

}
