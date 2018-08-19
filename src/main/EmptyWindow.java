package main;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class EmptyWindow extends JFrame implements ComponentListener {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmptyWindow frame = new EmptyWindow();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setSize(100, 100);
            frame.addComponentListener(frame);
            frame.setVisible(true);
        });
    }

    @Override
    public void componentResized(ComponentEvent e) {
        try {
            Thread.sleep(2);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentShown(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }
}
