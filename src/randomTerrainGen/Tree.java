package randomTerrainGen;

import java.awt.Color;
import java.awt.Graphics;

public class Tree {
	int x;
	int y;

	public Tree(int posx, int posy) {
		x = posx;
		y = posy;
	}

	public void draw(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillRect(x-3, y-20, 6, 20);
		g.setColor(Color.green);
		g.fillOval(x, y-30, 15, 15);
		g.fillOval(x-15, y-30, 15, 15);
		g.fillOval(x-7, y-40, 15, 15);
	}
}
