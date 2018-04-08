package main;

import fractal.FractalWorkerListener;
import fractal.worker.FractalDepthPainter;
import fractal.worker.FractalEvaluator;
import fractal.worker.FractalProblem;
import fractal.worker.FractalWorker;
import mandelbrot.DummyFractalDepthPainter;
import mandelbrot.DummyFractalFunction;
import mandelbrot.GrayFractalDepthPainter;
import mandelbrot.MandelbrotFractalEvaluator;
import math.Area;
import math.Mfloat;
import math.Number;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame implements ActionListener, FractalWorkerListener {

	// Fractal constants
	private static final int DEFAULT_PIXEL_SIZE = 2;
	private static final int DEFAULT_DEPTH = 1000;
	private static final Mfloat DEFAULT_X = Number.buildFloat(0.0);
    private static final Mfloat DEFAULT_Y = Number.buildFloat(0.0);
    private static final Mfloat DEFAULT_VIEW_SIZE = Number.buildFloat(4.1);

	// Canvas constants
	private static final int CANVAS_WIDTH = 1200;
	private static final int CANVAS_HEIGHT = 700;

	// Input size for Mfloats
	private static final int FLOAT_INPUT_SIZE = 16;

	// Depth painters
    private static final String DEFAULT_PAINTER_NAME = "Default";
    private static final FractalDepthPainter DEFAULT_PAINTER = new DummyFractalDepthPainter();
    private static final Map<String, FractalDepthPainter> DEPTH_PAINTERS;

    static {
        DEPTH_PAINTERS = new HashMap<>();
        DEPTH_PAINTERS.put(DEFAULT_PAINTER_NAME, DEFAULT_PAINTER);
        DEPTH_PAINTERS.put("Gray", new GrayFractalDepthPainter());
    }

	private JTextField pixelSizeField;
	private JTextField fractalDepthField;
	private JTextField fractalEdgeField;
	private JComboBox fractalPainterDropdown;
	private JButton updateButton;

	private JTextField fractalXField;
	private JTextField fractalYField;
	private JTextField fractalViewSizeField;
	private JButton updateCoordinatesButton;
    private JButton resetZoomButton;

    private Mfloat fractalX, fractalY, fractalViewSize;
    private int depth, pixelSize;
    private BufferedImage currentImage;
    private FractalWorker worker;

	@Override
	public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == updateButton || source == updateCoordinatesButton) {
//			updateFractal();
        } else if (source == resetZoomButton) {
//            resetFractalZoom();
        } else if (source == fractalPainterDropdown) {
//            String painterName = (String) fractalPainterDropdown.getSelectedItem();
//            FractalDepthPainter painter = DEPTH_PAINTERS.get(painterName);
//            if (painter != null) {
//                evaluator.setDepthPainter(painter);
//                canvas.repaint();
//            }
        }
    }
	
	private Component buildCenterUI() {
		return new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(currentImage, 0, 0, this);
			}
		};
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

        res.add(new JLabel("View getArea size: "));
        fractalViewSizeField = new JTextField(FLOAT_INPUT_SIZE);
        res.add(fractalViewSizeField);

        updateCoordinatesButton = new JButton("Update");
        updateCoordinatesButton.addActionListener(this);
        res.add(updateCoordinatesButton);

		return res;
	}

    private JComboBox buildFractalPainterDropdown() {
        JComboBox<String> res = new JComboBox<>();
        for (String name : DEPTH_PAINTERS.keySet()) {
            res.addItem(name);
        }
        res.setSelectedItem(DEFAULT_PAINTER_NAME);

        res.addActionListener(this);
        return res;
    }

	private Component buildBottomUI() {
		JPanel res = new JPanel();
		res.setLayout(new FlowLayout());
		
		res.add(new JLabel("Pixel size: "));
		pixelSizeField = new JTextField(5);
		pixelSizeField.setText(String.valueOf(DEFAULT_PIXEL_SIZE));
		res.add(pixelSizeField);

		res.add(new JLabel("Depth: "));
		fractalDepthField = new JTextField(5);
		fractalDepthField.setText(String.valueOf(DEFAULT_DEPTH));
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

	private FractalEvaluator buildEvaluator() {
	    return new MandelbrotFractalEvaluator(
	            DEFAULT_DEPTH,
                DEFAULT_PAINTER,
                new DummyFractalFunction()
        );
    }

    private Area getCurrentArea() {
        return new Area(
                Number.buildFloat(-2.0), Number.buildFloat(2.0),
                Number.buildFloat(-2.0), Number.buildFloat(2.0)
        );
    }

    private FractalProblem buildProblem() {
	    return new FractalProblem(
	            new BufferedImage(1300, 1000, BufferedImage.TYPE_INT_RGB),
	            getCurrentArea()
        );
    }

    private void startWorker() {
	    worker = new FractalWorker(buildEvaluator());
	    worker.setProblem(buildProblem());
	    worker.addFractalWorkerListener(this);
	    worker.start();
    }

    private void initFields() {
	    fractalX = DEFAULT_X;
	    fractalY = DEFAULT_Y;
	    fractalViewSize = DEFAULT_VIEW_SIZE;
	    depth = DEFAULT_DEPTH;
	    pixelSize = DEFAULT_PIXEL_SIZE;
    }

	private Main() {
		setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		setLayout(new BorderLayout());

		initFields();

		add(buildCenterUI(), BorderLayout.CENTER);
		add(buildTopUI(), BorderLayout.NORTH);
		add(buildBottomUI(), BorderLayout.SOUTH);

		startWorker();
    }

    public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            main.pack();
            main.setVisible(true);
        });
	}

    @Override
    public void fractalPainted(FractalWorker worker, FractalProblem problem) {
        currentImage = problem.getImage();
        repaint();
    }

}
