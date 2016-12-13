package randomTerrainGen;

import java.awt.Color;
import java.awt.Graphics;

public class Berry {
	int x, y;
	public Berry(int posx,int posy) {
		x = posx;
		y = posy;
	}
	public void draw(Graphics g){
		g.setColor(Color.green.darker());
		g.fillOval(x-2, y-6, 10, 10);
		g.fillOval(x, y-6, 10, 10);
		g.setColor(Color.red);
		g.fillOval(x + 1, y - 3, 1, 1);
		g.fillOval(x + 4, y - 3, 1, 1);
	}
}
