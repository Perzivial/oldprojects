package perspectiveDrawing2;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import perspectiveDrawing.Pixel;

public class Vector {
	private double a;
	private double b;
	private double c;
	Point3D A;
	Point3D B;

	public Vector(Point3D p1, Point3D p2) {
		a = p2.x - p1.x;
		b = p2.y - p1.y;
		c = p2.z - p1.z;
		A = p1;
		B = p2;
	}

	public Vector( Point3D p1,double x, double y, double z) {
		A = p1;
		a = x;
		b = y;
		c = z;
	}

	public Vector(double x, double y, double z) {
		a = x;
		b = y;
		c = z;
	}

	public Point3D getPointAtDist(double t) {

		Point3D out = new Point3D(A.x + (a * t), A.y + (b * t), A.z + (c * t));
		System.out.println(c);
		return out;
	}

	public double dotProduct(Vector that) {
		Vector3d vect = new Vector3d(a, b, c);

		Vector3d othervect = new Vector3d(that.a, that.b, that.c);

		return vect.dot(othervect);
	}

	public boolean intersectPlane(Plane plane) {
		if (new Vector(plane.a, plane.b, plane.c).dotProduct(this) != 0) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return a + ", " + b + ", " + c;
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public double getC() {
		return c;
	}
}
