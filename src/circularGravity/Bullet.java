package circularGravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Bullet {
	double x;
	double y;
	double velX = 0;
	double velY = 0;
	int lifeTime = 500;
	public Bullet(double xpos, double ypos, double xvel, double yvel) {
		x = xpos;
		y = ypos;
		velX = xvel;
		velY = yvel;
	}
	public void draw(Graphics g){
		lifeTime --;
		gravity();
		g.setColor(Color.red);
		g.fillRect((int)x-1, (int)y-1, 2, 2);
		x += velX;
		y += velY;
	}
	public void gravity() {
		// Point2D point = new Point2D.Double(x + 10, y + 40);
		// velX -= (nearestPlanet.gravity/(distanceToNearestPlanet()/2)) *
		// Math.sin(getRotationToNearestPlanet());
		// velY -= (nearestPlanet.gravity/(distanceToNearestPlanet()/2)) *
		// Math.cos(getRotationToNearestPlanet());
		for (Planet planet : Player.comp.planets) {

			velX -= (planet.gravity / (distanceToPlanet(planet) / 2))/5 * Math.sin(getRotationToPlanet(planet));
			velY -= (planet.gravity / (distanceToPlanet(planet) / 2))/5 * Math.cos(getRotationToPlanet(planet));
		}

	}
	public double distanceToPlanet(Planet planet){
		return new Point2D.Double(x,y).distance(planet.centre)-planet.radius;
	}
	public double getRotationToPlanet(Planet planet) {
		double out = 0.0;

		double xDiff = x - planet.centre.getX();
		double yDiff = y - planet.centre.getY();
		out = Math.atan2(xDiff, yDiff);

		return out;
	}
}
