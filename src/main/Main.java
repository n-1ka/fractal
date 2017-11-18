package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import fractal.Fractal;
import fractal.FractalEvaluator;
import fractal.FractalListener;
import mandelbrot.DummyFractalFunction;
import mandelbrot.DummyFractalDepthColorer;
import mandelbrot.MandelbrotFractalEvaluator;
import math.Mfloat;
import math.Number;

public class Main extends JFrame implements ActionListener, FractalListener {

	// Fractal constants
	private static final int PIXEL_SIZE = 2;
	private static final int MAX_DEPTH = 1000;
	private static final Mfloat DEFAULT_X = Number.buildFloat(0.0);
    private static final Mfloat DEFAULT_Y = Number.buildFloat(0.0);
    private static final Mfloat DEFAULT_VIEW_SIZE = Number.buildFloat(4.1);

	// Zoom constant
	private static final double MIN_ZOOM = 0.03;
	
	// Canvas constants
	private static final int CANVAS_WIDTH = 1200;
	private static final int CANVAS_HEIGHT = 700;

	private static final int FLOAT_INPUT_SIZE = 14;

	private JTextField pixelSizeField;
	private JButton updateButton;

	private JTextField fractalXField;
	private JTextField fractalYField;
	private JTextField fractalViewSizeField;
	private JButton updateCoordinatesButton;
    private JButton resetZoomButton;

	private Fractal fractal;

	private int tryParseInteger(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	private void update() {
	    Mfloat x, y, size;
	    x = y = size = null;

		if (fractal != null) {
			try {
                x = Number.buildFloat(fractalXField.getText());
                y = Number.buildFloat(fractalYField.getText());
                size = Number.buildFloat(fractalViewSizeField.getText());
            } catch (Exception e) {
			    e.printStackTrace();
			    // Don't update size
            }

            fractal.setPixelSize(tryParseInteger(pixelSizeField.getText(), PIXEL_SIZE));

            if (x != null && y != null && size != null) {
                fractal.setViewSize(size);
                fractal.setCenterX(x);
                fractal.setCenterY(y);
            }
		}
	}

	private void resetFractalZoom() {
	    fractal.setCenterX(DEFAULT_X);
        fractal.setCenterY(DEFAULT_Y);
        fractal.setViewSize(DEFAULT_VIEW_SIZE);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == updateButton || source == updateCoordinatesButton) {
			update();
        } else if (source == resetZoomButton) {
            resetFractalZoom();
        }
    }
	
	private static FractalEvaluator buildFractalEvaluator() {
		return new MandelbrotFractalEvaluator(
				MAX_DEPTH,
				new DummyFractalDepthColorer(), 
				new DummyFractalFunction());
	}
	
	private static Fractal buildFractal() {
		return new Fractal(DEFAULT_X, DEFAULT_Y, DEFAULT_VIEW_SIZE, PIXEL_SIZE, buildFractalEvaluator());
	}
	
	private Component buildCenterUI() {
		fractal = buildFractal();
		fractal.addFractalListener(this);
		new CanvasZoomable(MIN_ZOOM, fractal, fractal);
		return fractal;
	}
	
	private Component buildTopUI() {
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

        res.add(new JLabel("View area size: "));
        fractalViewSizeField = new JTextField(FLOAT_INPUT_SIZE);
        res.add(fractalViewSizeField);

        updateCoordinatesButton = new JButton("Update");
        updateCoordinatesButton.addActionListener(this);
        res.add(updateCoordinatesButton);

		return res;
	}
	
	private Component buildBottomUI() {
		JPanel res = new JPanel();
		res.setLayout(new FlowLayout());
		
		res.add(new JLabel("Pixel size: "));
		pixelSizeField = new JTextField(5);
		pixelSizeField.setText(String.valueOf(PIXEL_SIZE));
		res.add(pixelSizeField);
		
		updateButton = new JButton("Update");
		updateButton.addActionListener(this);
		res.add(updateButton);

		return res;
	}

    @Override
    public void fractalUpdated(Fractal f) {
        updateUI();
    }

	private void updateUI() {
        fractalXField.setText(fractal.getCenterX().toString());
        fractalYField.setText(fractal.getCenterY().toString());
        fractalViewSizeField.setText(fractal.getViewSize().toString());
    }
	
	private Main() {
		setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		setLayout(new BorderLayout());

		add(buildCenterUI(), BorderLayout.CENTER);
		add(buildTopUI(), BorderLayout.NORTH);
		add(buildBottomUI(), BorderLayout.SOUTH);

		updateUI();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            main.setVisible(true);
        });
	}

}
