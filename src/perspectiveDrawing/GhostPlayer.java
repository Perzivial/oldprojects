package perspectiveDrawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
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
				if(shotTimer == 0){
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
					
					System.out.println("bang");
				}
			} catch (Exception e) {

			}
		}
		if(shotTimer != 0){
		shotTimer --;
		Sound shot = new Sound("sound/gunshot.wav");
		if(new Point2D.Double(comp.player.x + 2.5,comp.player.y + 2.5).distance(new Point2D.Double(this.x+2.5,this.y+2.5))>100){
			shot.reducesound();
		}
		if(new Point2D.Double(comp.player.x + 2.5,comp.player.y + 2.5).distance(new Point2D.Double(this.x+2.5,this.y+2.5))<200){
		
		shot.play();
		}
		}
		if (hitTimer > 0)
			hitTimer--;
	}

	@Override
	public void move() {
		amountHit = 0;

		rect = new Rectangle2D.Double(x, y, 5, 5);
	}
}
