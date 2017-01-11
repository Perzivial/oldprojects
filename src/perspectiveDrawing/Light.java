package perspectiveDrawing;

import java.awt.Color;
import java.awt.Point;

public class Light extends Pixel {

	int radius;
	Point point;
	public Light(double xpos, double ypos, Color clr, int rad) {
		super(xpos, ypos, clr);
		point = new Point((int)xpos+5,(int)ypos+5);
		radius = rad;
	}

}
