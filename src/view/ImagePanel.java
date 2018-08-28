package view;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image image;
    private int version;

    public ImagePanel(Image image) {
        this.image = image;
        this.version = 0;

        if (image != null) {
            setSize(image.getWidth(this), image.getWidth(this));
        }
    }

    public ImagePanel() {
        this(null);
    }

    public void setImage(Image img) {
        this.image = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        repaint();
    }

    public Image getImage() {
        return image;
    }

    public void refreshImage() {
        image.flush();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }

}
