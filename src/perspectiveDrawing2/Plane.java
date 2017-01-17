package perspectiveDrawing2;

public class Plane {
	double a,b,c;
	Point3D orig;
	public Plane(double A,double B,double C, Point3D point) {
		a = A;
		b = B;
		c = C;
		orig = point;
	}
}
