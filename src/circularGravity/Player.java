package circularGravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Player {
	double x, y;
	double velX = 0, velY = 0;
	Polygon myShape = new Polygon();
	Planet nearestPlanet = null;
	public static Component comp;
	boolean isGrounded = false;
	int snapCoolDown = 2;
	double angle = 0;

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
		if (!isGrounded) {
			for (Planet planet : comp.planets) {
				if (nearestPlanet == null) {
					nearestPlanet = planet;
				} //else if ((Math.abs(nearestPlanet.centre.getX() - x)
						//- nearestPlanet.radius) > (Math.abs(planet.centre.getX() - x) - planet.radius)) {
				else if(Math.abs(new Point2D.Double(x,y).distance(nearestPlanet.centre)) - nearestPlanet.radius> Math.abs(new Point2D.Double(x,y).distance(planet.centre) ) -planet.radius){	
				nearestPlanet = planet;
					snapCoolDown = -1;
				}
			}
		}
	}

	public void gravity() {
		Point2D point = new Point2D.Double(x + 10, y + 40);
		velX -= (nearestPlanet.gravity/(distanceToNearestPlanet()/2)) * Math.sin(getRotationToNearestPlanet());
		velY -= (nearestPlanet.gravity/(distanceToNearestPlanet()/2)) * Math.cos(getRotationToNearestPlanet());

	}
	
	public double distanceToNearestPlanet(){
		return bottom().distance(nearestPlanet.centre)-nearestPlanet.radius;
	}
	public Point2D bottom(){
		return new Point2D.Double((x )+Math.sin(getRotationToNearestPlanet())*-10, (y)+Math.cos(getRotationToNearestPlanet())*-10);
	}
	public void move() {
		if (!isGrounded)
			rotateToNearest();
	
		Area area1 = new Area(setShapeAngle());
		Area area2 = new Area(nearestPlanet.rect);

		area1.intersect(area2);
		if (distanceToNearestPlanet() > 5) {
			if (snapCoolDown <= 0)
			gravity();
			isGrounded = false;
		} else if (snapCoolDown <= 0) {
			if(!comp.iskeyDown(KeyEvent.VK_W))
			attachToPlanet();
			velX = 0;
			velY = 0;
			isGrounded = true;
		}
		if (snapCoolDown >= 0)
			snapCoolDown--;
		
	}

	public double getRotationToNearestPlanet() {
		double out = 0.0;
		if (nearestPlanet != null) {
			double xDiff = x - nearestPlanet.centre.getX();
			double yDiff = y - nearestPlanet.centre.getY();
			out = Math.atan2(xDiff, yDiff);

		}
		return out;
	}

	public void walkRight() {
		if (isGrounded) {
			velX = 7 * Math.sin(getRotationToNearestPlanet() + 90);
			velY = 7 * Math.cos(getRotationToNearestPlanet() + 90);
		}
	}

	public void walkLeft() {
		if (isGrounded) {
			velX = 7 * Math.sin(getRotationToNearestPlanet() - 90);
			velY = 7 * Math.cos(getRotationToNearestPlanet() - 90);
		}
	}

	public void jump() {

		if (isGrounded) {
			velX += 5 * Math.sin(getRotationToNearestPlanet());
			velY += 5 * Math.cos(getRotationToNearestPlanet());
			snapCoolDown = 2;
		}
	}

	public void attachToPlanet() {
		if (isGrounded && snapCoolDown <= 0 && !comp.iskeyDown(KeyEvent.VK_W)) {
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
		g2.setColor(Color.red);
			
		g2.fill(new Rectangle2D.Double(bottom().getX()-2, bottom().getY()-2, 4, 4));
	
	}
}
