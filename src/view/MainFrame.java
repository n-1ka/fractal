package view;

import fractal.worker.FractalDepthPainter;
import fractal.worker.FractalEvaluator;
import fractal.worker.FractalWorker;
import fractal.worker.FractalWorkerImpl;
import math.CircleArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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

    private FractalWorker worker;
    private CircleArea initialArea;
    private FractalPanel panel;
    private Map<String, FractalDepthPainter> depthPainters;

    public MainFrame(FractalWorker worker, CircleArea initialArea, Map<String, FractalDepthPainter> depthPainters) {
        this.worker = worker;
        this.initialArea = initialArea;
        this.depthPainters = new HashMap<>(depthPainters);
        this.panel = new FractalPanel(worker, initialArea);

        add(buildCenterUI());
        add(buildNorthUI(), BorderLayout.NORTH);
        add(buildSouthUI(), BorderLayout.SOUTH);

        updateFields();
    }

    private void updateFields() {
        CircleArea fractalArea = panel.getFractalArea();

        fractalXField.setText(fractalArea.getCenterX().toString());
        fractalYField.setText(fractalArea.getCenterY().toString());
        fractalViewSizeField.setText(fractalArea.getDiameter().toString());

        FractalEvaluator evaluator = worker.getEvaluator();
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
            // TODO: Reset zoom
        } else if (source == updateButton) {
            // TODO: Update
        }
    }

}
