package perspectiveDrawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Player {
	public Component comp;
	double angle = 0;
	double x;
	double y;
	private double resolution = 200;
	private int focalLength = 1000;
	public static final int WALL_HEIGHT = 80;
	public static BufferedImage img = new Image("img/wall.png").img;
	int health = 100;
	BufferedImage shotgun = new Image("img/pistol.png").getScaledInstance(200, 50);
	boolean queueShot = false;
	int damage = 25;
	int shotCoolDown = 15;
	int coolDown = 0;

	public Player(double xpos, double ypos, double ang, Component myComp) {
		x = xpos;
		y = ypos;
		angle = ang;
		comp = myComp;
	}

	public void draw(Graphics g) {
		Graphics gSep = g.create();
		Graphics2D g2 = (Graphics2D) g;
		for (Pixel pixel : comp.pixels) {
			if (pixel instanceof Enemy) {
				((Enemy) pixel).drawColumns(g2);
			}
		}
		g2.translate(1000, 0);
		g2.scale(-1, 1);
		if(comp.isKeyDown(KeyEvent.VK_Q)){
		g.setColor(Color.white);
		g.fillRect((int) x, (int) y, 5, 5);
		g.setColor(Color.red);
		g.drawLine((int) x + 2, (int) y + 2, ((int) x + 2) + (int) (50 * Math.sin(Math.toRadians(angle))),
				((int) y + 2) + (int) (50 * Math.cos(Math.toRadians(angle))));
		}
		drawWeapon(g);
		g.setColor(Color.red);
		for (Pixel pixel : comp.pixels) {
			if (pixel instanceof Enemy)
				((Enemy) pixel).move();
		}
		if (coolDown > 0)
			coolDown--;
		damage();
		drawHealthBar(gSep);
	}
		
	public void drawHealthBar(Graphics g){
		g.setColor(Color.green);
		g.fillRect(20, 510, health * 2, 50);
	}
	
	public void damage(){
		boolean shouldApply = false;
		for(Pixel pixel:comp.pixels){
			if(pixel instanceof Enemy){
				if(pixel.dist < 11)
					shouldApply = true;
			}
		}
			if(shouldApply){
				health --;
			}else if(health < 100)
				health ++;
				
	}
	
	public void drawWeapon(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		if(coolDown == shotCoolDown-2){
			g2.setColor(Color.white);
			g2.fillOval(410, 350, 150, 150);
		}
		g2.drawImage(shotgun, 400-coolDown*2, 400+coolDown, 200+coolDown, 200, null);
	}

	public void shoot() {
		ArrayList<Pixel> toRemove = new ArrayList<Pixel>();
		if (coolDown <= 0) {
			Sound shot = new Sound("sound/gunshot.wav");
			shot.play();
			for (Pixel pixel : comp.pixels) {

				if (pixel instanceof Enemy) {
					RoundRectangle2D rectmy = new RoundRectangle2D.Double(460, 300, 70, 200, 100, 200);
					boolean hasHit = false;
					for (Rectangle rect2 : ((Enemy) pixel).columns) {
						if (rectmy.intersects(rect2)) {
							if (!hasHit) {
								((Enemy) pixel).health -= damage;
								((Enemy) pixel).hitTimer = 5;
								((Enemy) pixel).playHurtSound();
								if (((Enemy) pixel).health <= 0){
									toRemove.add(pixel);
									((Enemy) pixel).playDeadSound();
								}
								hasHit = true;
							}
						}
					}
				}

			}

			comp.pixels.removeAll(toRemove);

			coolDown = shotCoolDown;
		}
	}

	public void goForward() {
		x += 2 * Math.sin(Math.toRadians(angle));
		y += 2 * Math.cos(Math.toRadians(angle));
		for (Pixel pixel : comp.pixels) {
			if (new Rectangle2D.Double(x, y, 5, 5).intersects(pixel.rect))
				if(!(pixel instanceof Enemy))
				goBackward();
		}
	}

	public void goBackward() {
		x -= 2 * Math.sin(Math.toRadians(angle));
		y -= 2 * Math.cos(Math.toRadians(angle));
		for (Pixel pixel : comp.pixels) {
			if (new Rectangle2D.Double(x, y, 5, 5).intersects(pixel.rect))
				if(!(pixel instanceof Enemy))
				goForward();
		}
	}

	public void rotateLeft() {
		angle -= 5;
	}

	public void rotateRight() {
		angle += 5;
	}

	public void walkLeft() {
		x += 1 * Math.sin(Math.toRadians(angle - 90));
		y += 1 * Math.cos(Math.toRadians(angle - 90));
		for (Pixel pixel : comp.pixels) {
			if (new Rectangle2D.Double(x, y, 5, 5).intersects(pixel.rect))
				if(!(pixel instanceof Enemy))
				walkRight();
		}
	}

	public void walkRight() {
		x += 1 * Math.sin(Math.toRadians(angle + 90));
		y += 1 * Math.cos(Math.toRadians(angle + 90));
		for (Pixel pixel : comp.pixels) {
			if (new Rectangle2D.Double(x, y, 5, 5).intersects(pixel.rect))
				if(!(pixel instanceof Enemy))
				walkLeft();
		}
	}

	public Point2D getPixelIntersect(Line2D line, Pixel pixel) {
		Line2D sideLine1 = new Line2D.Double(pixel.x, pixel.y, pixel.x + 10, pixel.y);
		Line2D sideLine2 = new Line2D.Double(pixel.x, pixel.y + 10, pixel.x + 10, pixel.y + 10);
		Line2D sideLine3 = new Line2D.Double(pixel.x, pixel.y, pixel.x, pixel.y + 10);
		Line2D sideLine4 = new Line2D.Double(pixel.x + 10, pixel.y, pixel.x, pixel.y + 10);
		Point2D side1Point = lineIntersectPoint(line, sideLine1);
		Point2D side2Point = lineIntersectPoint(line, sideLine2);
		Point2D side3Point = lineIntersectPoint(line, sideLine3);
		Point2D side4Point = lineIntersectPoint(line, sideLine4);
		if (side1Point != null && side2Point != null) {
			if (side1Point.distance(line.getP1()) < side2Point.distance(line.getP1()))
				return side1Point;
			else
				return side2Point;
		} else if (side3Point != null && side4Point != null) {
			if (side3Point.distance(line.getP1()) < side4Point.distance(line.getP1()))
				return side3Point;
			else
				return side4Point;
		}

		else if (side1Point != null && side3Point != null) {
			if (side1Point.distance(line.getP1()) < side3Point.distance(line.getP1()))
				return side1Point;
			else
				return side3Point;
		} else if (side1Point != null && side4Point != null) {
			if (side1Point.distance(line.getP1()) < side4Point.distance(line.getP1()))
				return side1Point;
			else
				return side4Point;
		} else if (side1Point != null && side3Point != null) {
			if (side1Point.distance(line.getP1()) < side3Point.distance(line.getP1()))
				return side1Point;
			else
				return side3Point;
		} else if (side2Point != null && side4Point != null) {
			if (side2Point.distance(line.getP1()) < side4Point.distance(line.getP1()))
				return side2Point;
			else
				return side4Point;
		}
		return null;
	}

	public Double getDist(Line2D line, Rectangle2D rect, double step) {
		double angle = -Math.atan2(line.getY1() - line.getY2(), line.getX1() - line.getX2());
		Line2D lineSeg = new Line2D.Double(line.getP1(),
				new Point2D.Double(line.getX1() + (step * Math.sin(angle)), line.getY1() + (step * Math.cos(angle))));
		Line2D rectSide1 = new Line2D.Double(rect.getX(), rect.getY(), rect.getX() + 10, rect.getY());
		Line2D rectSide2 = new Line2D.Double(rect.getX(), rect.getY() + 10, rect.getX() + 10, rect.getY() + 10);
		Line2D rectSide3 = new Line2D.Double(rect.getX(), rect.getY(), rect.getX(), rect.getY() + 10);
		Line2D rectSide4 = new Line2D.Double(rect.getX() + 10, rect.getY(), rect.getX() + 10, rect.getY() + 10);
		// if(line.intersects(rect)){
		// while(lineSeg.intersects(rect)){
		// lineSeg.setLine(line.getX1(), line.getX2(),
		// lineSeg.getX2()+(step*Math.sin(angle)),
		// lineSeg.getY2()+(step*Math.cos(angle)));
		//
		//
		//
		// }
		//
		// return new Point2D.Double(lineSeg.getX2(),lineSeg.getY2());
		// }
		Point2D point1 = lineIntersectPoint(line, rectSide1);
		Point2D point2 = lineIntersectPoint(line, rectSide1);
		Point2D point3 = lineIntersectPoint(line, rectSide1);
		Point2D point4 = lineIntersectPoint(line, rectSide1);

		double lowestDist = Double.POSITIVE_INFINITY;
		if (line.intersectsLine(rectSide1))
			lowestDist = line.getP1().distance(point1);
		if (line.intersectsLine(rectSide2))
			if (line.getP1().distance(point2) < lowestDist)
				lowestDist = line.getP1().distance(point2);
		if (line.intersectsLine(rectSide3))
			if (line.getP1().distance(point3) < lowestDist)
				lowestDist = line.getP1().distance(point3);
		if (line.intersectsLine(rectSide4))
			if (line.getP1().distance(point4) < lowestDist)
				lowestDist = line.getP1().distance(point4);

		return lowestDist;

	}

	public Point2D getPointRectIntersectPoint(Line2D line, Rectangle2D rect, Graphics2D g) {

		Area area = new Area(line);
		// area.intersect(new Area(rect));
		g.setColor(Color.red);
		g.fill(area);
		return new Point2D.Double(area.getBounds2D().getX(), area.getBounds2D().getY());
	}

	public double getDistToRect(Line2D line, Pixel pixel) {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		Line2D sideLine1 = new Line2D.Double(pixel.x, pixel.y, pixel.x + 10, pixel.y);
		Line2D sideLine2 = new Line2D.Double(pixel.x, pixel.y + 10, pixel.x + 10, pixel.y + 10);
		Line2D sideLine3 = new Line2D.Double(pixel.x, pixel.y, pixel.x, pixel.y + 10);
		Line2D sideLine4 = new Line2D.Double(pixel.x + 10, pixel.y, pixel.x + 10, pixel.y + 10);
		Point2D point1 = lineIntersect(line, sideLine1);
		Point2D point2 = lineIntersect(line, sideLine2);
		Point2D point3 = lineIntersect(line, sideLine3);
		Point2D point4 = lineIntersect(line, sideLine4);
		ArrayList<Double> dists = new ArrayList<Double>();
		if (point1 != null)
			dists.add(line.getP1().distance(point1));
		if (point2 != null)
			dists.add(line.getP1().distance(point2));
		if (point3 != null)
			dists.add(line.getP1().distance(point3));
		if (point4 != null)
			dists.add(line.getP1().distance(point4));
		Collections.sort(dists);
		try {
			return dists.get(0);
		} catch (Exception e) {
			return focalLength;
		}
	}

	public Point2D getCollisionPointOnRect(Line2D line, Pixel pixel) {
		Line2D sideLine1 = new Line2D.Double(pixel.x, pixel.y, pixel.x + 10, pixel.y);
		Line2D sideLine2 = new Line2D.Double(pixel.x, pixel.y + 10, pixel.x + 10, pixel.y + 10);
		Line2D sideLine3 = new Line2D.Double(pixel.x, pixel.y, pixel.x, pixel.y + 10);
		Line2D sideLine4 = new Line2D.Double(pixel.x + 10, pixel.y, pixel.x + 10, pixel.y + 10);
		Point2D point1 = lineIntersect(line, sideLine1);
		Point2D point2 = lineIntersect(line, sideLine2);
		Point2D point3 = lineIntersect(line, sideLine3);
		Point2D point4 = lineIntersect(line, sideLine4);
		double dist1 = 0;
		double dist2 = 0;
		double dist3 = 0;
		double dist4 = 0;
		if (point1 != null)
			dist1 = line.getP1().distance(point1);
		if (point2 != null)
			dist2 = line.getP1().distance(point2);
		if (point3 != null)
			dist3 = line.getP1().distance(point3);
		if (point4 != null)
			dist4 = line.getP1().distance(point4);
		double trueDist = getDistToRect(line, pixel);
		if (dist1 == trueDist)
			return point1;
		if (dist2 == trueDist)
			return point2;
		if (dist3 == trueDist)
			return point2;
		if (dist4 == trueDist)
			return point3;
		return null;
	}

	// draws a specified number of rays to hit stuff and draw them accordingly
	public void drawRays(Graphics g) {

		double addAngle = 0;
		for (int i = 0; i < resolution; i++) {
			double lowestDist = Double.POSITIVE_INFINITY;
			double tempangle = angle - 45 - 22.5;
			addAngle += (double) 135 / (double) resolution;
			// tempangle += (i*resolution/360);
			tempangle += addAngle;
			// tempangle += (resolution/135)*i;

			Line2D line = new Line2D.Double(
					x + 2.5 /* + (5 * Math.sin(Math.toRadians(angle))) */,
					y + 2.5 /* + (5 * Math.cos(Math.toRadians(angle))) */,
					x + 2.5 + (focalLength * Math.sin(Math.toRadians(tempangle))),
					y + 2.5 + (focalLength * Math.cos(Math.toRadians(tempangle))));
			Graphics2D g2 = (Graphics2D) g.create();
			g2.translate(1000, 0);
			g2.scale(-1, 1);
			// g2.draw(line);

			for (Pixel pixel : comp.pixels) {
				if (line.intersects(pixel.rect)) {
					// pixel.dist = line.getP1().distance(new
					// Point2D.Double(pixel.x + 5, pixel.y + 5)) + 1;
					// getPointRectIntersectPoint(line,
					// pixel.rect,(Graphics2D)g.create());
					pixel.dist = Double.MAX_VALUE;
					try {
						// pixel.dist = getDist(line,pixel.rect,.1);
						// TODO the distance

						pixel.dist = getDistToRect(line, pixel);
						pixel.z = -pixel.dist / 2;
						if (pixel instanceof Enemy)
							pixel.z = line.getP1().distance(new Point2D.Double(pixel.x + 5, pixel.y + 5));
						// pixel.z = (-pixel.dist
						// *Math.sin(Math.toRadians(90)))/Math.sin(Math.toRadians(tempangle));
						// pixel.z =(pixel.dist * -Math.cos(addAngle));
						// pixel.dist = pixel.dist *
						// Math.cos(Math.toRadians(tempangle));
						// System.out.println(pixel.dist);
						// System.out.println(pixel.dist);
						// g2.translate(200, 200);
						// g2.draw(new Line2D.Double(line.getP1(),
						// new Point2D.Double(line.getX1() + (pixel.dist *
						// Math.sin(Math.toRadians(tempangle))),
						// line.getX1() + (pixel.dist *
						// Math.cos(Math.toRadians(tempangle))))));

						Line2D sideLine1 = new Line2D.Double(pixel.x, pixel.y, pixel.x + 10, pixel.y);
						Point2D point = lineIntersect(line, sideLine1);

						// g2.draw(shape);
						// g2.fillRect(point.getX(), (int)line2.getY2(),5, 5);
					} catch (Exception e) {

					}
					/*
					 * FlatteningPathIterator it = new
					 * FlatteningPathIterator(line.getPathIterator(g2.
					 * getTransform()),1); float[] coords=new float[2];
					 * ArrayList<Point2D> points = new ArrayList<Point2D>();
					 * while (!it.isDone()) {
					 * 
					 * it.currentSegment(coords); int x=(int)coords[0]; int
					 * y=(int)coords[1]; points.add(new Point2D.Double(x,y));
					 * it.next();
					 * 
					 * } for(Point2D pointTemp : points){
					 * System.out.println(pointTemp.getX() + " ," +
					 * pointTemp.getY()); g2.setColor(Color.red);
					 * g2.drawRect((int)pointTemp.getX()-1,(int)pointTemp.getY()
					 * -1,2,2); }
					 */

				}
			}
			// comp.sortPixelsByDistance();
			try{
			Collections.sort(comp.pixels);
			}catch(Exception e){
				System.out.println("an error occured while sorting the pixel array");
			}
			// Collections.reverse(comp.pixels);
			for (Pixel pixel : comp.pixels) {

				if (line.intersects(pixel.rect)) {
					Graphics2D g3 = (Graphics2D) g.create();
					Line2D sideLine1 = new Line2D.Double(pixel.x, pixel.y, pixel.x + 10, pixel.y);
					Line2D sideLine2 = new Line2D.Double(pixel.x, pixel.y + 10, pixel.x + 10, pixel.y + 10);
					Line2D sideLine3 = new Line2D.Double(pixel.x, pixel.y, pixel.x, pixel.y + 10);
					Line2D sideLine4 = new Line2D.Double(pixel.x + 10, pixel.y, pixel.x + 10, pixel.y + 10);

					if (line.intersectsLine(sideLine1)) {
						double tempX = lineIntersectPoint(line, sideLine1).getX();
						double tempY = lineIntersectPoint(line, sideLine1).getY();

						Graphics2D g4 = (Graphics2D) g.create();
						// g4.setColor(Color.red);
						// g4.fillRect((int) tempX - 1, (int) tempY - 1, 2, 2);
						// System.out.println(line.getP1().distance(lineIntersectPoint(line,
						// sideLine1)));
					}

					Point2D intersectFaceUp = lineIntersect(line, sideLine1);
					Point2D intersectFaceDown = lineIntersect(line, sideLine1);
					Point2D intersectFaceLeft = lineIntersect(line, sideLine3);
					Point2D intersectFaceRight = lineIntersect(line, sideLine4);
					// g3.setColor(Color.BLACK);

					g.setColor(pixel.color);
					// BufferedImage toDraw = img;
					if (!(pixel instanceof Enemy)) {
						if (line.intersectsLine(sideLine1) && line.intersectsLine(sideLine2)) {
							g.setColor(pixel.color);

						} else if (line.intersectsLine(sideLine3) && line.intersectsLine(sideLine4))
							g.setColor(pixel.color.darker());
						else if (line.intersectsLine(sideLine1) && line.intersectsLine(sideLine3)) {
							if (lineIntersect(line, sideLine1).distance(line.getP1()) < lineIntersect(line, sideLine3)
									.distance(line.getP1()))
								g.setColor(pixel.color);
							else
								g.setColor(pixel.color.darker());
						} else if (line.intersectsLine(sideLine1) && line.intersectsLine(sideLine4)) {
							if (lineIntersect(line, sideLine1).distance(line.getP1()) < lineIntersect(line, sideLine4)
									.distance(line.getP1()))
								g.setColor(pixel.color);
							else
								g.setColor(pixel.color.darker());
						} else if (line.intersectsLine(sideLine2) && line.intersectsLine(sideLine3)) {
							if (lineIntersect(line, sideLine2).distance(line.getP1()) < lineIntersect(line, sideLine3)
									.distance(line.getP1()))
								g.setColor(pixel.color);
							else
								g.setColor(pixel.color.darker());
						} else if (line.intersectsLine(sideLine2) && line.intersectsLine(sideLine4)) {
							if (lineIntersect(line, sideLine2).distance(line.getP1()) < lineIntersect(line, sideLine4)
									.distance(line.getP1()))
								g.setColor(pixel.color);
							else
								g.setColor(pixel.color.darker());
						} else
							g.setColor(pixel.color);
					}
					// g3.setColor(pixel.color);
					int red = g.getColor().getRed() - (int) pixel.dist;
					int green = g.getColor().getGreen() - (int) pixel.dist;
					int blue = g.getColor().getBlue() - (int) pixel.dist;
					if (red > 255)
						red = 255;
					if (red < 0)
						red = 0;
					if (green > 255)
						green = 255;
					if (green < 0)
						green = 0;
					if (blue > 255)
						blue = 255;
					if (blue < 0)
						blue = 0;
					g3.setColor(new Color(red, green, blue));
					g3.translate(0, 100);
					// System.out.println(pixel.z);
					pixel.z = Math.abs(pixel.z);

					try {
						// TODO draws the collumns of the pixels
						// g3.translate(0, -((WALL_HEIGHT / ((int) pixel.dist /
						// 5)) / 2));

						g3.translate(0, 300);
						// int level = 100;
						// System.out.println(level);
						g3.translate(0, -((WALL_HEIGHT * WALL_HEIGHT / pixel.z)));
						if (!(pixel instanceof Enemy)) {
							g3.fillRect((int) ((double) 1000 / (double) resolution) * i,
									(int) ((WALL_HEIGHT * WALL_HEIGHT / pixel.z)) / 2, (int) (1000 / resolution),
									(int) (WALL_HEIGHT * WALL_HEIGHT / pixel.z));
						} else {
							((Enemy) pixel).amountHit++;
							((Enemy) pixel).columns.add(new Rectangle((int) ((double) 1000 / (double) resolution) * i,
									(int) (400 + -((WALL_HEIGHT * WALL_HEIGHT / pixel.z))
											+ ((int) ((WALL_HEIGHT * WALL_HEIGHT / pixel.z)) / 2)),
									(int) (1000 / resolution), (int) (WALL_HEIGHT * WALL_HEIGHT / pixel.z)));
						}
						Point2D myPoint = getCollisionPointOnRect(line, pixel);

						g3.setColor(Color.red);
						// g3.fillRect((int)myPoint.getX()-1,
						// (int)myPoint.getY()-1, 2, 2);
						// g3.translate(200, 200);
						g3.setColor(Color.green);
						// g3.draw(sideLine4);
					} catch (Exception e) {

					}

					// g3.fillRect((int) ((double) 1000 / (double) resolution) *
					// i, 50 + (int) pixel.dist, (1000/resolution),350 - (int)
					// pixel.dist);

					if (!(pixel instanceof Enemy))
						break;
				}

			}

		}
	}

	public BufferedImage drawSlicedImage(Line2D sideLine, Line2D ray, BufferedImage image, Graphics2D g) {
		int percent = (int) ((lineIntersect(sideLine, ray).distance(sideLine.getP1())
				/ sideLine.getP1().distance(sideLine.getP2())) * 100);
		return comp.getSlice(image, (int) ((resolution / 100) * (percent)), (int) resolution);
	}

	public Point2D lineIntersectPoint(Line2D line1, Line2D line2) {
		double x1 = line1.getX1();
		double x2 = line1.getX2();
		double y1 = line1.getY1();
		double y2 = line1.getY2();

		double x3 = line2.getX1();
		double x4 = line2.getX2();
		double y3 = line2.getY1();
		double y4 = line2.getY2();
		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

		if (d == 0)
			return null;

		double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;

		double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

		return new Point2D.Double(xi, yi);

	}

	public static Point2D lineIntersect(Line2D line1, Line2D line2) {
		double x1 = line1.getX1();
		double x2 = line1.getX2();
		double y1 = line1.getY1();
		double y2 = line1.getY2();

		double x3 = line2.getX1();
		double x4 = line2.getX2();
		double y3 = line2.getY1();
		double y4 = line2.getY2();

		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (denom == 0.0) { // Lines are parallel.
			return null;
		}
		double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
		double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
		if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
			// Get the intersection point.
			return new Point2D.Double((int) (x1 + ua * (x2 - x1)), (int) (y1 + ua * (y2 - y1)));
		}

		return null;
	}
}
