package circularGravity;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy {
	double x, y,velX,velY;
	int lifetime = 1200;
	public Enemy(double xpos, double ypos, double xvel, double yvel) {
		x = xpos;
		y = ypos;
		velX = xvel;
		velY = yvel;
	}
	public void draw(Graphics g){
		g.setColor(Color.green);
		g.fillOval((int)x-10, (int)y-10, 20, 20);
		x += velX;
		y += velY;
		lifetime --;
	}
}
