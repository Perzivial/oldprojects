package circularGravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class Planet {
	Point2D centre;
	double radius;
	Color color;
	double gravity;
	RoundRectangle2D rect;
	Planet orbitPlanet = null;
	double orbitDistance = 100;
	double angle = 0;
	double orbitSpeed = .005;
	public Planet(double x, double y, double rad, double grav, Color clr) {
		centre = new Point2D.Double(x, y);
		radius = rad;
		color = clr;
		gravity = grav;
		rect = new RoundRectangle2D.Double(x - rad, y - rad, rad * 2, rad * 2, rad * 2, rad * 2);
	}

	public void draw(Graphics g) {
		g.setColor(color);

		if (orbitPlanet != null) {
			double x = centre.getX();
			double y = centre.getY();
			
			x = orbitPlanet.centre.getX() + Math.sin(angle)* (orbitPlanet.radius + orbitDistance);
			y = orbitPlanet.centre.getY() +Math.cos(angle) * (orbitPlanet.radius + orbitDistance);
			
			centre.setLocation(x, y);
			
			angle += orbitSpeed;
			
		}
		rect = new RoundRectangle2D.Double( centre.getX() - radius,  centre.getY() - radius, radius * 2, radius * 2, radius * 2, radius * 2);
		//g.drawOval((int) (centre.getX() - radius), (int) (centre.getY() - radius), (int) radius * 2, (int) radius * 2);

		Graphics2D g2 = (Graphics2D) g;
		g2.fill(rect);

	}
}
