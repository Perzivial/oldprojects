package perspectiveDrawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;

public class Player {
	public Component comp;
	double angle = 0;
	double x;
	double y;
	private int resolution = 100;
	private int focalLength = 500;
	public static final int WALL_HEIGHT = 500;

	public Player(double xpos, double ypos, double ang, Component myComp) {
		x = xpos;
		y = ypos;
		angle = ang;
		comp = myComp;
	}

	public void draw(Graphics g) {
		drawRays(g);
		g.setColor(Color.black);
		g.fillRect((int) x, (int) y, 10, 10);
		g.setColor(Color.red);
		g.drawLine((int) x + 5, (int) y + 5, ((int) x + 5) + (int) (5 * Math.sin(Math.toRadians(angle))),
				((int) y + 5) + (int) (5 * Math.cos(Math.toRadians(angle))));

	}

	public void goForward() {
		x += 1 * Math.sin(Math.toRadians(angle));
		y += 1 * Math.cos(Math.toRadians(angle));
		for (Pixel pixel : comp.pixels) {
			if (new Rectangle2D.Double(x, y, 10, 10).intersects(pixel.rect))
				goBackward();
		}
	}

	public void goBackward() {
		x -= 1 * Math.sin(Math.toRadians(angle));
		y -= 1 * Math.cos(Math.toRadians(angle));
		for (Pixel pixel : comp.pixels) {
			if (new Rectangle2D.Double(x, y, 10, 10).intersects(pixel.rect))
				goForward();
		}
	}

	public void rotateLeft() {
		angle -= 5;
	}

	public void rotateRight() {
		angle += 5;
	}
	
	public Point2D getPixelIntersect(Line2D line, Pixel pixel) {
		Line2D sideLine1 = new Line2D.Double(pixel.x, pixel.y, pixel.x + 10, pixel.y);
		Line2D sideLine2 = new Line2D.Double(pixel.x, pixel.y + 10, pixel.x + 10, pixel.y + 10);
		Line2D sideLine3 = new Line2D.Double(pixel.x, pixel.y, pixel.x, pixel.y + 10);
		Line2D sideLine4 = new Line2D.Double(pixel.x + 10, pixel.y, pixel.x, pixel.y + 10);
		Point2D side1Point = lineIntersectPoint(line,sideLine1);
		Point2D side2Point = lineIntersectPoint(line,sideLine2);
		Point2D side3Point = lineIntersectPoint(line,sideLine3);
		Point2D side4Point = lineIntersectPoint(line,sideLine4);
		if(side1Point != null && side2Point!=null){
			if(side1Point.distance(line.getP1()) < side2Point.distance(line.getP1()))
				return side1Point;
			else 
				return side2Point;
		}else if(side3Point != null && side4Point!=null){
			if(side3Point.distance(line.getP1()) < side4Point.distance(line.getP1()))
				return side3Point;
			else 
				return side4Point;
		}
		
		else if(side1Point != null && side3Point!=null){
			if(side1Point.distance(line.getP1()) < side3Point.distance(line.getP1()))
				return side1Point;
			else 
				return side3Point;
		}else if(side1Point != null && side4Point!=null){
			if(side1Point.distance(line.getP1()) < side4Point.distance(line.getP1()))
				return side1Point;
			else 
				return side4Point;
		}
		else if(side1Point != null && side3Point!=null){
			if(side1Point.distance(line.getP1()) < side3Point.distance(line.getP1()))
				return side1Point;
			else 
				return side3Point;
		}else if(side2Point != null && side4Point!=null){
			if(side2Point.distance(line.getP1()) < side4Point.distance(line.getP1()))
				return side2Point;
			else 
				return side4Point;
		}
		return null;
	}
	public Double getDist(Line2D line, Rectangle2D rect, double step){
		double angle = -Math.atan2(line.getY1()-line.getY2(), line.getX1()-line.getX2());
		Line2D lineSeg = new Line2D.Double(line.getP1(),new Point2D.Double(line.getX1()+(step * Math.sin(angle)),line.getY1()+(step * Math.cos(angle))));
		Line2D rectSide1 = new Line2D.Double(rect.getX(),rect.getY(),rect.getX()+10,rect.getY());
		Line2D rectSide2 = new Line2D.Double(rect.getX(),rect.getY()+10,rect.getX()+10,rect.getY()+10);
		Line2D rectSide3 = new Line2D.Double(rect.getX(),rect.getY(),rect.getX(),rect.getY()+10);
		Line2D rectSide4 = new Line2D.Double(rect.getX()+10,rect.getY(),rect.getX()+10,rect.getY()+10);
//		if(line.intersects(rect)){
//		while(lineSeg.intersects(rect)){
//			lineSeg.setLine(line.getX1(), line.getX2(), lineSeg.getX2()+(step*Math.sin(angle)), lineSeg.getY2()+(step*Math.cos(angle)));
//
//			
//			
//		}
//	
//		return new Point2D.Double(lineSeg.getX2(),lineSeg.getY2());
//		}
		Point2D point1 =  lineIntersectPoint(line,rectSide1);
		Point2D point2 =  lineIntersectPoint(line,rectSide1);
		Point2D point3 =  lineIntersectPoint(line,rectSide1);
		Point2D point4 =  lineIntersectPoint(line,rectSide1);
		
		double lowestDist = Double.POSITIVE_INFINITY;
		if(line.intersectsLine(rectSide1))
			lowestDist = line.getP1().distance(point1);
		if(line.intersectsLine(rectSide2))
			if( line.getP1().distance(point2)<lowestDist)
			lowestDist = line.getP1().distance(point2);
		if(line.intersectsLine(rectSide3))
			if( line.getP1().distance(point3)<lowestDist)
			lowestDist = line.getP1().distance(point3);
		if(line.intersectsLine(rectSide4))
			if( line.getP1().distance(point4)<lowestDist)
			lowestDist = line.getP1().distance(point4);
		
		return lowestDist;
		
	}
	public Point2D getPointRectIntersectPoint(Line2D line, Rectangle2D rect, Graphics2D g){
		
		Area area = new Area(line);
		//area.intersect(new Area(rect));
		g.setColor(Color.red);
		g.fill(area);
		return new Point2D.Double(area.getBounds2D().getX(), area.getBounds2D().getY());
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

			Line2D line = new Line2D.Double(x + 5 + (5 * Math.sin(Math.toRadians(angle))),
					y + 5 + (5 * Math.cos(Math.toRadians(angle))),
					x + 5 + (focalLength * Math.sin(Math.toRadians(tempangle))),
					y + 5 + (focalLength * Math.cos(Math.toRadians(tempangle))));
			Graphics2D g2 = (Graphics2D) g.create();
			//g2.draw(line);

			for (Pixel pixel : comp.pixels) {
				if (line.intersects(pixel.rect)) {
					//pixel.dist = line.getP1().distance(new
					//Point2D.Double(pixel.x + 5, pixel.y + 5)) + 1;
					//getPointRectIntersectPoint(line, pixel.rect,(Graphics2D)g.create());
					pixel.dist = Double.MAX_VALUE;
					try{
					pixel.dist = getDist(line,pixel.rect,.1);
					System.out.println(pixel.dist);
					g2.draw(new Line2D.Double(line.getP1(), new Point2D.Double(line.getX1()+ (pixel.dist*Math.sin(Math.toRadians(tempangle))),line.getX1()+ (pixel.dist*Math.cos(Math.toRadians(tempangle))))));
					}catch(Exception e){
						
					}
				}
			}
			// comp.sortPixelsByDistance();
			Collections.sort(comp.pixels);
			// Collections.reverse(comp.pixels);
			for (Pixel pixel : comp.pixels) {
				if (line.intersects(pixel.rect)) {
					Graphics2D g3 = (Graphics2D) g.create();
					Line2D sideLine1 = new Line2D.Double(pixel.x, pixel.y, pixel.x + 10, pixel.y);
					Line2D sideLine2 = new Line2D.Double(pixel.x, pixel.y + 10, pixel.x + 10, pixel.y + 10);
					Line2D sideLine3 = new Line2D.Double(pixel.x, pixel.y, pixel.x, pixel.y + 10);
					Line2D sideLine4 = new Line2D.Double(pixel.x + 10, pixel.y, pixel.x, pixel.y + 10);
					if (line.intersectsLine(sideLine1)) {
						double tempX = lineIntersectPoint(line, sideLine1).getX();
						double tempY = lineIntersectPoint(line, sideLine1).getY();

						Graphics2D g4 = (Graphics2D) g.create();
						//g4.setColor(Color.red);
						//g4.fillRect((int) tempX - 1, (int) tempY - 1, 2, 2);
						//System.out.println(line.getP1().distance(lineIntersectPoint(line, sideLine1)));
					}

					Point2D intersectFaceUp = lineIntersectPoint(line, sideLine1);
					Point2D intersectFaceDown = lineIntersectPoint(line, sideLine1);
					Point2D intersectFaceLeft = lineIntersectPoint(line, sideLine3);
					Point2D intersectFaceRight = lineIntersectPoint(line, sideLine4);

					if (line.intersectsLine(sideLine1) && line.intersectsLine(sideLine2))
						g3.setColor(pixel.color);
					else if (line.intersectsLine(sideLine3) && line.intersectsLine(sideLine4))
						g3.setColor(pixel.color.darker());
					else if (line.intersectsLine(sideLine1) && line.intersectsLine(sideLine3)) {
						if (intersectFaceUp.distance(line.getP1()) < intersectFaceLeft.distance(line.getP1()))
							g3.setColor(pixel.color);
						else
							g3.setColor(pixel.color.darker());
					} else if (line.intersectsLine(sideLine1) && line.intersectsLine(sideLine4)) {
						if (intersectFaceUp.distance(line.getP1()) < intersectFaceRight.distance(line.getP1()))
							g3.setColor(pixel.color);
						else
							g3.setColor(pixel.color.darker());
					} else if (line.intersectsLine(sideLine2) && line.intersectsLine(sideLine3)) {
						if (intersectFaceDown.distance(line.getP1()) < intersectFaceLeft.distance(line.getP1()))
							g3.setColor(pixel.color);
						else
							g3.setColor(pixel.color.darker());
					} else if (line.intersectsLine(sideLine2) && line.intersectsLine(sideLine4)) {
						if (intersectFaceDown.distance(line.getP1()) < intersectFaceRight.distance(line.getP1()))
							g3.setColor(pixel.color);
						else
							g3.setColor(pixel.color.darker());
					} else
						g3.setColor(pixel.color);

					g3.translate(0, 350);
					g3.translate(0, -pixel.dist);
					try{
					g3.translate(0, -((WALL_HEIGHT / ((int) pixel.dist / 5)) / 2));
					g3.fillRect((int) ((double) 1000 / (double) resolution) * i, 50 + (int) pixel.dist,
							(1000 / resolution), WALL_HEIGHT / ((int) pixel.dist / 5));
					}catch(java.lang.ArithmeticException e){
						
					}
					
					// g3.fillRect((int) ((double) 1000 / (double) resolution) *
					// i, 50 + (int) pixel.dist, (1000/resolution),350 - (int)
					// pixel.dist);

					break;
				}
			}

			/*
			 * Pixel closestPixel = null; for (Pixel pixel : comp.pixels) { if
			 * (closestPixel == null) closestPixel = pixel; if
			 * (closestPixel.dist > pixel.dist) closestPixel = pixel; } try { if
			 * (line.intersects(closestPixel.rect)) {
			 * g.setColor(closestPixel.color); g.fillRect((int) ((double) 1000 /
			 * (double) resolution) * i, 50 + (int) closestPixel.dist, 100, 350
			 * - (int) closestPixel.dist); } } catch (Exception e) {
			 * 
			 * }
			 */
			/*
			 * for (Pixel pixel : comp.pixels) { if
			 * (line.intersects(pixel.rect)) { double distance =
			 * line.getP1().distance(new Point2D.Double(pixel.x+5,pixel.y+5)) +
			 * 1;
			 * 
			 * AffineTransform form = new AffineTransform();
			 * 
			 * //Graphics2D g3 = (Graphics2D) g.create(); // try{ g.setColor(new
			 * Color(pixel.color.getRed()+ Helper.randInt(-5,
			 * 5),pixel.color.getGreen()+ Helper.randInt(-5,
			 * 5),pixel.color.getBlue() + Helper.randInt(-5, 5)));
			 * }catch(Exception e){ g.setColor(pixel.color); } int posx = 0;
			 * //System.out.println(distance); if(distance < lowestDist){
			 * 
			 * lowestDist = distance; System.out.println("fasf"); } break; }
			 * 
			 * 
			 * } if(lowestDist != Double.POSITIVE_INFINITY)
			 * g.fillRect((int)((double)1000/(double)resolution)*i, 50+
			 * (int)lowestDist, 100, 350 - (int)lowestDist);
			 */
		}
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
}
