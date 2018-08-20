package view.main_frame;

import fractal.worker.FractalDepthPainter;
import view.FractalPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public final class MainFrame extends JFrame implements ActionListener {

    private static final int FLOAT_INPUT_SIZE = 16;

    private JButton resetZoomButton;
    private JTextField fractalXField;
    private JTextField fractalYField;
    private JTextField fractalViewSizeField;
    private JTextField pixelSizeField;
    private JTextField fractalDepthField;
    private JTextField fractalEdgeField;
    private JComboBox fractalPainterDropdown;
    private JButton updateButton;

    private FractalPanel panel;
    private Map<String, FractalDepthPainter> depthPainters;
    private List<MainFrameEventListener> listeners;

    public MainFrame(FractalPanel panel, Map<String, FractalDepthPainter> depthPainters) {
        this.panel = panel;
        this.depthPainters = new HashMap<>(depthPainters);
        this.listeners = new ArrayList<>();

        add(buildCenterUI());
        add(buildNorthUI(), BorderLayout.NORTH);
        add(buildSouthUI(), BorderLayout.SOUTH);
    }

    public void addListener(MainFrameEventListener listener) {
        listeners.add(listener);
    }

    public void setXField(String value) {
        setFieldValue(fractalXField, value);
    }

    public String getXField() throws InterruptedException {
        return getFieldValue(fractalXField);
    }

    public void setYField(String value) {
        setFieldValue(fractalYField, value);
    }

    public String getYField() throws InterruptedException {
        return getFieldValue(fractalYField);
    }

    public void setFractalViewSizeField(String value) {
        setFieldValue(fractalViewSizeField, value);
    }

    public String getFractalViewSizeField() throws InterruptedException {
        return getFieldValue(fractalViewSizeField);
    }

    public void setPixelSizeField(String value) {
        setFieldValue(pixelSizeField, value);
    }

    public String getPixelSizeField() throws InterruptedException {
        return getFieldValue(pixelSizeField);
    }

    public void setFractalDepthField(String value) {
        setFieldValue(fractalDepthField, value);
    }

    public String getFractalDepthField() throws InterruptedException {
        return getFieldValue(fractalDepthField);
    }

    public void setFractalEdgeField(String value) {
        setFieldValue(fractalEdgeField, value);
    }

    private void notifyUpdateClicked() {
        listeners.forEach(MainFrameEventListener::updateClicked);
    }

    private void notifyRezetZoomClicked() {
        listeners.forEach(MainFrameEventListener::resetZoomClicked);
    }

    private void notifyPainterChanged(FractalDepthPainter depthPainter) {
        listeners.forEach(l -> l.fractalPainterChanged(depthPainter));
    }

    private String getFieldValue(JTextField field) throws InterruptedException {
//        AtomicReference<String> value = new AtomicReference<>("");
//        try {
//            SwingUtilities.invokeAndWait(() -> value.set(field.getText()));
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return value.get();
        return field.getText();
    }

    private void setFieldValue(JTextField field, String value) {
//        SwingUtilities.invokeLater(() -> field.setText(value));
        field.setText(value);
    }

    private Component buildCenterUI() {
        return panel;
    }

    private Component buildNorthUI() {
        JPanel res = new JPanel();
        res.setLayout(new FlowLayout());

        resetZoomButton = new JButton("Reset Zoom");
        resetZoomButton.addActionListener(this);
        res.add(resetZoomButton);

        res.add(new JLabel("Center coordinates (x, y): "));
        fractalXField = new JTextField(FLOAT_INPUT_SIZE);
        fractalYField = new JTextField(FLOAT_INPUT_SIZE);
        res.add(fractalXField);
        res.add(fractalYField);

        res.add(new JLabel("View getArea size: "));
        fractalViewSizeField = new JTextField(FLOAT_INPUT_SIZE);
        res.add(fractalViewSizeField);

        return res;
    }

    private JComboBox buildFractalPainterDropdown() {
        JComboBox<String> res = new JComboBox<>();
        String selectedItem = null;
        for (String name : depthPainters.keySet()) {
            if (selectedItem == null) {
                selectedItem = name;
            }

            res.addItem(name);
        }
        res.setSelectedItem(selectedItem);

        res.addActionListener(this);
        return res;
    }

    private Component buildSouthUI() {
        JPanel res = new JPanel();
        res.setLayout(new FlowLayout());

        res.add(new JLabel("Pixel size: "));
        pixelSizeField = new JTextField(5);
        res.add(pixelSizeField);

        res.add(new JLabel("Depth: "));
        fractalDepthField = new JTextField(5);
        res.add(fractalDepthField);

        res.add(new JLabel("Edge: "));
        fractalEdgeField = new JTextField(5);
        res.add(fractalEdgeField);

        res.add(new JLabel("Coloring: "));
        fractalPainterDropdown = buildFractalPainterDropdown();
        res.add(fractalPainterDropdown);

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        res.add(updateButton);

        return res;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == resetZoomButton) {
            System.out.println("RESET ZOOM!");
            notifyRezetZoomClicked();
        } else if (source == updateButton) {
            System.out.println("UPDATE!");
            notifyUpdateClicked();
        } else if (source == fractalPainterDropdown) {
            String painter = (String) fractalPainterDropdown.getSelectedItem();
            notifyPainterChanged(depthPainters.get(painter));
        }
    }

}
