package circularGravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class Player {
	double x, y;
	double velX = 0, velY = 0;
	Polygon myShape = new Polygon();
	Planet nearestPlanet = null;
	Component comp;
	boolean isGrounded = false;

	public Player(double posx, double posy, Component myComp) {
		x = posx;
		y = posy;
		myShape.addPoint(0, 0);
		myShape.addPoint(0, 20);
		myShape.addPoint(20, 20);
		myShape.addPoint(20, 0);
		comp = myComp;
	}

	public Shape setShapeAngle() {
		Polygon temp = myShape;
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);

		at.rotate(-getRotationToNearestPlanet());
		// else
		// at.rotate(Math.toDegrees(getRotationToNearestPlanet()));

		// System.out.println(angle);
		at.translate(-10, -10);

		Shape temp2 = at.createTransformedShape(temp);

		return temp2;
	}

	public void rotateToNearest() {
		for (Planet planet : comp.planets) {
			if (nearestPlanet == null) {
				nearestPlanet = planet;
			} else if ((Math.abs(nearestPlanet.centre.getX() - x)
					- nearestPlanet.radius) > (Math.abs(planet.centre.getX() - x) - planet.radius)) {
				nearestPlanet = planet;
			}
		}
	}

	public void gravity() {

		velX += nearestPlanet.gravity * -Math.sin(getRotationToNearestPlanet());
		velY += nearestPlanet.gravity * -Math.cos(getRotationToNearestPlanet());

	}

	public void move() {
		if (!isGrounded)
			rotateToNearest();

		Area area1 = new Area(setShapeAngle());
		Area area2 = new Area(nearestPlanet.rect);

		area1.intersect(area2);
		if (area1.isEmpty()) {
			gravity();
			isGrounded = false;
		} else {
			velX = 0;
			velY = 0;

			isGrounded = true;
		}

	}

	public double getRotationToNearestPlanet() {
		double out = 0.0;
		if (nearestPlanet != null) {
			double xDiff = x - nearestPlanet.centre.getX();
			double yDiff = y - nearestPlanet.centre.getY();
			out = Math.atan2(xDiff, yDiff);
			System.out.println(out);
		}
		return out;
	}

	public void walkRight() {
		velX = 3 * Math.sin(getRotationToNearestPlanet() + 90);
		velY = 3 * Math.cos(getRotationToNearestPlanet() + 90);
		attachToPlanet();
	}

	public void walkLeft() {
		velX = 3 * Math.sin(getRotationToNearestPlanet() - 90);
		velY = 3 * Math.cos(getRotationToNearestPlanet() - 90);
		attachToPlanet();
	}

	public void jump() {
		velX += 5 * Math.sin(getRotationToNearestPlanet());
		velY += 5 * Math.cos(getRotationToNearestPlanet());
	}

	public void attachToPlanet() {
		if (isGrounded) {
			x = nearestPlanet.centre.getX() + Math.sin(getRotationToNearestPlanet()) * (nearestPlanet.radius + 10);
			y = nearestPlanet.centre.getY() + Math.cos(getRotationToNearestPlanet()) * (nearestPlanet.radius + 10);
		}
	}

	public void finalizeMovement() {
		x += velX;
		y += velY;
	}

	public void draw(Graphics g) {
		move();
		g.setColor(Color.yellow);
		Graphics2D g2 = (Graphics2D) g;
		g2.fill(setShapeAngle());
		// g2.drawLine((int) x, (int) y, (int) (x + (20 *
		// Math.sin(getRotationToNearestPlanet()))), (int) (y + (20 *
		// Math.cos(getRotationToNearestPlanet()))));
	}
}
