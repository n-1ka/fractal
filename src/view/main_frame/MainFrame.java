package view.main_frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class MainFrame extends JFrame implements ActionListener {

    private static final int FLOAT_INPUT_SIZE = 16;

    private JButton moveButton;
    private JButton resetZoomButton;
    private JTextField fractalXField;
    private JTextField fractalYField;
    private JTextField fractalViewSizeField;
    private JTextField pixelScaleField;
    private JTextField fractalDepthField;
    private JTextField fractalEdgeField;
    private JComboBox fractalPainterDropdown;
    private JButton updateButton;

    private JPanel middlePanel;
    private List<MainFrameEventListener> listeners;

    public MainFrame(JPanel middlePanel, List<String> depthPainters) {
        this.middlePanel = middlePanel;
        this.listeners = new ArrayList<>();

        add(buildCenterUI());
        add(buildNorthUI(), BorderLayout.NORTH);
        add(buildSouthUI(depthPainters), BorderLayout.SOUTH);
    }

    public void addListener(MainFrameEventListener listener) {
        listeners.add(listener);
    }

    public void setXField(String value) {
        setFieldValue(fractalXField, value);
    }

    public String getXField() {
        return getFieldValue(fractalXField);
    }

    public void setYField(String value) {
        setFieldValue(fractalYField, value);
    }

    public String getYField() {
        return getFieldValue(fractalYField);
    }

    public void setFractalViewSizeField(String value) {
        setFieldValue(fractalViewSizeField, value);
    }

    public String getFractalViewSizeField() {
        return getFieldValue(fractalViewSizeField);
    }

    public void setPixelScaleField(String value) {
        setFieldValue(pixelScaleField, value);
    }

    public String getPixelScaleField() {
        return getFieldValue(pixelScaleField);
    }

    public void setFractalDepthField(String value) {
        setFieldValue(fractalDepthField, value);
    }

    public String getFractalDepthField() {
        return getFieldValue(fractalDepthField);
    }

    public void setFractalEdgeField(String value) {
        setFieldValue(fractalEdgeField, value);
    }

    public String getFractalEdgeField() {
        return getFieldValue(fractalEdgeField);
    }

    public void setCurrentDepthPainterName(String painterName) {
        fractalPainterDropdown.setSelectedItem(painterName);
    }

    public String getCurrentDepthPainterName() {
        return (String) fractalPainterDropdown.getSelectedItem();
    }

    private void notifyUpdateClicked() {
        listeners.forEach(MainFrameEventListener::updateClicked);
    }

    private void notifyRezetZoomClicked() {
        listeners.forEach(MainFrameEventListener::resetZoomClicked);
    }

    private void notifyMoveClicked() {
        listeners.forEach(MainFrameEventListener::moveClicked);
    }

    private void notifyPainterChanged(String painterName) {
        listeners.forEach(l -> l.fractalPainterChanged(painterName));
    }

    private void notifySaveImage(File file) {
        listeners.forEach(l -> l.saveImage(file));
    }

    private String getFieldValue(JTextField field) {
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
        return middlePanel;
    }

    private Component buildNorthUI() {
        JPanel res = new JPanel();
        res.setLayout(new FlowLayout());

        res.add(new JLabel("Center coordinates (x, y): "));
        fractalXField = new JTextField(FLOAT_INPUT_SIZE);
        fractalYField = new JTextField(FLOAT_INPUT_SIZE);
        res.add(fractalXField);
        res.add(fractalYField);

        res.add(new JLabel("View getArea size: "));
        fractalViewSizeField = new JTextField(FLOAT_INPUT_SIZE);
        res.add(fractalViewSizeField);

        moveButton = new JButton("Move");
        moveButton.addActionListener(this);
        res.add(moveButton);

        resetZoomButton = new JButton("Reset Zoom");
        resetZoomButton.addActionListener(this);
        res.add(resetZoomButton);

        JButton saveButton = new JButton("Save image");
        saveButton.addActionListener(actionEvent -> {
            JFileChooser saveFileChooser = new JFileChooser();
            saveFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

            int returnValue = saveFileChooser.showOpenDialog(MainFrame.this);
            if (returnValue != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File selectedFile = saveFileChooser.getSelectedFile();

            if (selectedFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "File already exists, overwrite?",
                        "Overwrite file?", JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            notifySaveImage(selectedFile);
        });
        res.add(saveButton);

        return res;
    }

    private JComboBox buildFractalPainterDropdown(List<String> depthPainters) {
        JComboBox<String> res = new JComboBox<>();
        String selectedItem = null;
        for (String name : depthPainters) {
            if (selectedItem == null) {
                selectedItem = name;
            }

            res.addItem(name);
        }
        res.setSelectedItem(selectedItem);

        res.addActionListener(this);
        return res;
    }

    private Component buildSouthUI(List<String> depthPainters) {
        JPanel res = new JPanel();
        res.setLayout(new FlowLayout());

        res.add(new JLabel("Pixel Scale: "));
        pixelScaleField = new JTextField(10);
        res.add(pixelScaleField);

        res.add(new JLabel("Depth: "));
        fractalDepthField = new JTextField(5);
        res.add(fractalDepthField);

        res.add(new JLabel("Edge: "));
        fractalEdgeField = new JTextField(5);
        res.add(fractalEdgeField);

        res.add(new JLabel("Coloring: "));
        fractalPainterDropdown = buildFractalPainterDropdown(depthPainters);
        res.add(fractalPainterDropdown);

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        res.add(updateButton);

        return res;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == moveButton) {
            notifyMoveClicked();
        } if (source == resetZoomButton) {
            notifyRezetZoomClicked();
        } else if (source == updateButton) {
            notifyUpdateClicked();
        } else if (source == fractalPainterDropdown) {
            String painter = (String) fractalPainterDropdown.getSelectedItem();
            notifyPainterChanged(painter);
        }
    }

}
