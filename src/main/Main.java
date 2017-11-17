package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import mandelbrot.DummyFractalFunction;
import mandelbrot.DummyFractalDepthColorer;
import mandelbrot.MandelbrotFractalEvaluator;

public class Main extends JFrame implements ActionListener {
	
	// Fractal constants
	private static final int PIXEL_SIZE = 2;
	private static final int MAX_DEPTH = 1000;
	
	// Zoom constant
	private static final double MIN_ZOOM = 0.03;
	
	// Canvas constants
	private static final int CANVAS_WIDTH = 1200;
	private static final int CANVAS_HEIGHT = 700;
	
	private JTextField pixelSizeField;
	private JButton updateButton;
	
	private Fractal fractal;

	private int tryParseInteger(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	private void update() {
		if (fractal != null) {
			fractal.setPixelSize(tryParseInteger(pixelSizeField.getText(), PIXEL_SIZE));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == updateButton) {
			update();
		}
	}
	
	private static FractalEvaluator buildFractalEvaluator() {
		return new MandelbrotFractalEvaluator(
				MAX_DEPTH,
				new DummyFractalDepthColorer(), 
				new DummyFractalFunction());
	}
	
	private static Fractal buildFractal() {
		return new Fractal(PIXEL_SIZE, buildFractalEvaluator());
	}
	
	private Component buildCenterUI() {
		fractal = buildFractal();
		new CanvasZoomable(MIN_ZOOM, fractal, fractal);
		return fractal;
	}
	
	private Component buildTopUI() {
		JPanel res = new JPanel();
		res.setLayout(new FlowLayout());
		
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
	
	private Main() {
		setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		setLayout(new BorderLayout());

		add(buildCenterUI(), BorderLayout.CENTER);
		add(buildTopUI(), BorderLayout.NORTH);
		add(buildBottomUI(), BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            main.setVisible(true);
        });
	}


}
