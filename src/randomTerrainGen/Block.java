package randomTerrainGen;

import java.awt.*;


public class Block {
	Rectangle rect;
	Color color;
	public Block(int x, int y, int w,int h, Color clr) {
		rect = new Rectangle(x,y,w,h);
		color = clr;
	}
	public void draw(Graphics2D g2){
		g2.setColor(color);
		g2.fill(rect);
	}
}
