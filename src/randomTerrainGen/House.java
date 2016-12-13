package randomTerrainGen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class House {
	int x,y;
	String creatorGenome;
	public House(String gene, int posx,int posy) {
		x = posx;
		y = posy;
		creatorGenome = gene;
	}
	public void draw(Graphics g){
		g.setColor(Color.white);
		g.fillRect(x,y -20, 20, 20);
		g.setColor(Color.red);
		Polygon poly = new Polygon();
		poly.addPoint(x, y-20);
		poly.addPoint(x+10, y-30);
		poly.addPoint(x+20, y-20);
		g.fillPolygon(poly);
	}
}
