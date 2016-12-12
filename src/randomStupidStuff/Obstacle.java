package randomStupidStuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Obstacle {
	int danger;
	Color color;
	double x, y;
	Rectangle rect;
	boolean doesOssilate = false;
	boolean isGoingRight = true;
	int amountRight = 200;
	public Obstacle(int dangernum, Color clr, int xpos, int ypos) {
		color = clr;
		danger = dangernum;
		x = xpos;
		y = ypos;
		rect = new Rectangle((int)x, (int)y, 20, 20);
	}

	public void upAndDown() {
		if(isGoingRight){
			if(x > 700 - amountRight){
				x -= .2;
			}else
				isGoingRight = false;
		}else{
			if(x < 700){
				x+= .2;
			}else
				isGoingRight = true;
		}
		rect = new Rectangle((int)x, (int)y, 20, 20);
	}

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect((int)x, (int)y, 20, 20);
		if(doesOssilate)
		upAndDown();
		
	}
}
