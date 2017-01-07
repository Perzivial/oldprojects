package perspectiveDrawing;

public class Side implements Comparable{
	public double dist;
	public int side;
	public Side(int whatSide, double distance) {
		side = whatSide;
		dist = distance;
	}
	@Override
	public int compareTo(Object o) {
		Side oPixel = (Side) o;
		return (int)(dist-oPixel.dist);
	}
}
