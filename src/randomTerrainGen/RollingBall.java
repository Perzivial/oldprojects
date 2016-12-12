package randomTerrainGen;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class RollingBall {
	Component myComp;
	double x;
	double y;
	double velX;
	double velY;
	int rotationAmount = 0;
	RoundRectangle2D rect;

	public RollingBall(int xpos, int ypos, Component comp) {
		x = xpos;
		y = ypos;
		rect = new RoundRectangle2D.Double(x, y, 30, 30, 30, 30);
		myComp = comp;
	}

	public void move() {
		x += velX;
		y += velY;
		gravity();
		rebound();
		rect = new RoundRectangle2D.Double(x, y, 30, 30, 30, 30);
	}

	public void draw(Graphics2D g2) {
		g2.fill(rect);
	}

	public void gravity() {
		boolean shouldApply = true;
		for (Shape shape : myComp.shapes) {
			Area area1 = new Area(shape);
			area1.intersect(new Area(rect));
			if (!area1.isEmpty()) {
				shouldApply = false;
			}
		}
		if (shouldApply) {
			velY+= .01;
		}
	}
	public void rebound(){
		for (Shape shape : myComp.shapes) {
			Area area1 = new Area(shape);
			area1.intersect(new Area(rect));
			if (!area1.isEmpty()) {
				//velX *= -.5;
			
				velY *= -.5;
			}
		}
	}
}
