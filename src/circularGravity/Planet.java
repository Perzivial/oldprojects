package circularGravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class Planet {
	Point2D centre;
	double radius;
	Color color;
	double gravity;
	RoundRectangle2D rect;
	public Planet(double x,double y,double rad,double grav, Color clr) {
	centre = new Point2D.Double(x,y);
	radius = rad;
	color = clr;
	gravity = grav;
	rect =  new RoundRectangle2D.Double(x-rad,y-rad,rad*2,rad*2,rad*2,rad*2);
	}
	
	public void draw(Graphics g){
		g.setColor(color);
		g.drawOval((int)(centre.getX()-radius), (int)(centre.getY()-radius), (int)radius*2, (int) radius*2);
	}
}
