package fractal.worker;

import java.awt.Color;

public interface FractalDepthPainter {
	
	Color generateColor(int depth, int maxDepth);

}
