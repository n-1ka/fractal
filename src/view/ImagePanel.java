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
        this.image = img;
        setSize(img.getWidth(this), img.getHeight(this));
        repaint();
    }

    public Image getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        if (img == image) {
            System.out.println("UPDATE");
            setSize(w, h);
            return true;
        }
        return false;
    }


}
