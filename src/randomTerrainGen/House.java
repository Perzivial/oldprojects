package randomTerrainGen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class House {
	int x,y;
	String creatorGenome;
	int levels = 1;
	Color clr = Color.white;
	public House(String gene, int posx,int posy) {
		x = posx;
		y = posy;
		creatorGenome = gene;
	}
	public void draw(Graphics g){
		int offset = 20 * levels;
		g.setColor(clr);
		g.fillRect(x,y -offset, 20, offset+10);
		g.setColor(Color.red);
		Polygon poly = new Polygon();
		poly.addPoint(x, y-offset);
		poly.addPoint(x+10, y-offset-10);
		poly.addPoint(x+20, y-offset);
		g.fillPolygon(poly);
	}
}
