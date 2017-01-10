package perspectiveDrawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GhostPlayer extends Enemy {
	public static BufferedImage shoot = new Image("img/gunGhostShoot.png").getScaledInstance(133, 269);
	int shotTimer = 0;
	public GhostPlayer(double xpos, double ypos, Color clr, Component myComp) {
		super(xpos, ypos, clr, myComp);
		enemyImg = new Image("img/gunGhost.png").getScaledInstance(133, 269);
		enemyAngryImg = new Image("img/ghostAngry.png").getScaledInstance(133, 269);
	}

	@Override
	public void drawColumns(Graphics2D g2) {

		for (Rectangle current : columns) {
			try {
				if(shotTimer != 1){
				// g2.fill(current);
				if (hitTimer <= 0)
					g2.drawImage(comp.getSlice(enemyImg, columns.indexOf(current), columns.size()), current.x,
							current.y, current.width, current.height, null);
				else
					g2.drawImage(comp.getSlice(enemyAngryImg, columns.indexOf(current), columns.size()), current.x,
							current.y, current.width, current.height, null);
				}else{
					if (hitTimer <= 0)
						g2.drawImage(comp.getSlice(shoot, columns.indexOf(current), columns.size()), current.x,
								current.y, current.width, current.height, null);
					else
						g2.drawImage(comp.getSlice(shoot, columns.indexOf(current), columns.size()), current.x,
								current.y, current.width, current.height, null);
					shotTimer = 0;
				}
			} catch (Exception e) {

			}
		}
		if (hitTimer > 0)
			hitTimer--;
	}

	@Override
	public void move() {
		amountHit = 0;
		columns.clear();
		rect = new Rectangle2D.Double(x, y, 5, 5);
	}
}
