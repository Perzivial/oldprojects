package perspectiveDrawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class Pixel implements Comparable{
	double x;
	double y;
	Color color;
	Rectangle2D rect;
	double dist = 0;
	double z = 0;
	public Pixel(double xpos, double ypos,Color clr) {
		x = xpos;
		y = ypos;
		color = clr;
		rect = new Rectangle2D.Double(x,y,10,10);
	}
	public void draw(Graphics g){
		g.setColor(color);
		g.fillRect((int)x, (int)y, 10, 10);
	}
	@Override
	public int compareTo(Object o) {
		Pixel oPixel = (Pixel) o;
		return (int)(dist-oPixel.dist);
	}
}
