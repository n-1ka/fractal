package view.main_frame;

import java.io.File;

public interface MainFrameEventListener {

    void updateClicked();

    void moveClicked();

    void resetZoomClicked();

    void fractalPainterChanged(String painterName);

    void saveImage(File file);

}
