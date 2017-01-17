package perspectiveDrawing2;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

import perspectiveDrawing.Enemy;

public class Block {
	int x, y, z;
	double dist;

	public Block(int xpos, int ypos, int zpos) {
		x = xpos;
		y = ypos;
		z = zpos;
	}

	public boolean contains(Point3D point) {
		if (point.x >= x && point.x <= x + 10)
			if (point.y >= y && point.y <= y + 10)
				if (point.z >= z && point.z <= z + 10)
					return true;
		return false;
	}

	public boolean intersects(Vector vect, double angle2D, double offset) {

		Line2D line = new Line2D.Double(vect.A.x, vect.A.y,
				vect.A.x + (Player.VIEW_DISTANCE * Math.sin(Math.toRadians(angle2D))),
				vect.A.y + (Player.VIEW_DISTANCE * Math.cos(Math.toRadians(angle2D))));
		Rectangle rect = new Rectangle(x, y, 10, 10);
		if (line.intersects(rect)) {

			double z = getDistToRect(line, rect) * vect.getC();

			z -= offset;
			if (z >= this.z && z <= this.z + 10)
				return true;

		}
		return false;
	}

	public double getDistToRect(Line2D line, Rectangle rect) {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		Line2D sideLine1 = new Line2D.Double(rect.x, rect.y, rect.x + 10, rect.y);
		Line2D sideLine2 = new Line2D.Double(rect.x, rect.y + 10, rect.x + 10, rect.y + 10);
		Line2D sideLine3 = new Line2D.Double(rect.x, rect.y, rect.x, rect.y + 10);
		Line2D sideLine4 = new Line2D.Double(rect.x + 10, rect.y, rect.x + 10, rect.y + 10);
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
			return Player.VIEW_DISTANCE;
		}
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

	public boolean whichColor(Line2D line) {
		Rectangle rect = new Rectangle(x, y, 10, 10);
		Line2D sideLine1 = new Line2D.Double(rect.x, rect.y, rect.x + 10, rect.y);
		Line2D sideLine2 = new Line2D.Double(rect.x, rect.y + 10, rect.x + 10, rect.y + 10);
		Line2D sideLine3 = new Line2D.Double(rect.x, rect.y, rect.x, rect.y + 10);
		Line2D sideLine4 = new Line2D.Double(rect.x + 10, rect.y, rect.x + 10, rect.y + 10);
		Point2D intersectFaceUp = lineIntersect(line, sideLine1);
		Point2D intersectFaceDown = lineIntersect(line, sideLine1);
		Point2D intersectFaceLeft = lineIntersect(line, sideLine3);
		Point2D intersectFaceRight = lineIntersect(line, sideLine4);
		// g3.setColor(Color.BLACK);

		// BufferedImage toDraw = img;

		if (line.intersectsLine(sideLine1) && line.intersectsLine(sideLine2)) {
			return true;

		} else if (line.intersectsLine(sideLine3) && line.intersectsLine(sideLine4))
			return false;
		else if (line.intersectsLine(sideLine1) && line.intersectsLine(sideLine3)) {
			if (lineIntersect(line, sideLine1).distance(line.getP1()) < lineIntersect(line, sideLine3)
					.distance(line.getP1()))
				return true;
			else
				return false;
		} else if (line.intersectsLine(sideLine1) && line.intersectsLine(sideLine4)) {
			if (lineIntersect(line, sideLine1).distance(line.getP1()) < lineIntersect(line, sideLine4)
					.distance(line.getP1()))
				return true;
			else
				return false;
		} else if (line.intersectsLine(sideLine2) && line.intersectsLine(sideLine3)) {
			if (lineIntersect(line, sideLine2).distance(line.getP1()) < lineIntersect(line, sideLine3)
					.distance(line.getP1()))
				return true;
			else
				return false;
		} else if (line.intersectsLine(sideLine2) && line.intersectsLine(sideLine4)) {
			if (lineIntersect(line, sideLine2).distance(line.getP1()) < lineIntersect(line, sideLine4)
					.distance(line.getP1()))
				return true;
			else
				return false;
		} else
			return true;

	}
}
