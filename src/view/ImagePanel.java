package view;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image image;

    public ImagePanel(Image image) {
        this.image = image;

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

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }

}
