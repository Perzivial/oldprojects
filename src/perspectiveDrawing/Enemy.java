package perspectiveDrawing;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class Enemy extends Pixel {
	Component comp;

	public Enemy(double xpos, double ypos, Color clr, Component myComp) {
		super(xpos, ypos, clr);
		comp = myComp;
	}

	public void move() {
		if(x > comp.player.x +5)
			x -= .001;
		else if(x < comp.player.x -5)
			x += .001;		
		if(y > comp.player.y +5)
			y -= .001;
		else if(x < comp.player.y -5)
			y += .001;
		rect = new Rectangle2D.Double(x,y,10,10);
	}
}
