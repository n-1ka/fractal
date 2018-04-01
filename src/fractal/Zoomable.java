package fractal;

public interface Zoomable {
	
	/**
	 * Zooms 2 dimensional object into some
	 * 	inner rectangular section.
	 * @param x - Zoom center x coordinate in pixels.
	 * @param y - Zoom center y coordinate in pixels.
	 * @param scale - How much to scale the object (1 will not change, 2 will zoom twice etc.)
	 */
	void zoom(int x, int y, double scale);

}
