package perspectiveDrawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy extends Pixel {
	Component comp;
	double speed = 1;
	int amountHit = 0;
	ArrayList<Rectangle> columns = new ArrayList<Rectangle>();
	BufferedImage enemyImg = new Image("img/ghost.png").img;
	BufferedImage enemyAngryImg = new Image("img/ghostAngry.png").img;
	int hitTimer = 0;
	int health = 100;

	public Enemy(double xpos, double ypos, Color clr, Component myComp) {
		super(xpos, ypos, clr);
		comp = myComp;
	}

	public void drawColumns(Graphics2D g2) {

		for (Rectangle current : columns) {
			// g2.fill(current);
			if (hitTimer <= 0)
				g2.drawImage(comp.getSlice(enemyImg, columns.indexOf(current), columns.size()), current.x, current.y,
						current.width, current.height, null);
			else
				g2.drawImage(comp.getSlice(enemyAngryImg, columns.indexOf(current), columns.size()), current.x,
						current.y, current.width, current.height, null);
		}
		if (hitTimer > 0)
			hitTimer--;
	}

	public void move() {
		amountHit = 0;
		columns.clear();
		if (x > comp.player.x + 10) {
			x -= speed;
			rect = new Rectangle2D.Double(x, y, 5, 5);
			for (Pixel pixel : comp.pixels) {
				if (pixel != this) {
					if (pixel.rect.intersects(rect))
						x += speed;
				}
			}
		} else if (x < comp.player.x - 10) {
			x += speed;
			rect = new Rectangle2D.Double(x, y, 5, 5);
			for (Pixel pixel : comp.pixels) {
				if (pixel != this) {
					if (pixel.rect.intersects(rect))
						x -= speed;
				}
			}
		}
		if (y > comp.player.y + 10) {
			y -= speed;
			rect = new Rectangle2D.Double(x, y, 5, 5);
			for (Pixel pixel : comp.pixels) {
				if (pixel != this) {
					if (pixel.rect.intersects(rect))
						y += speed;
				}
			}
		} else if (y < comp.player.y - 10) {
			y += speed;
			rect = new Rectangle2D.Double(x, y, 5, 5);
			for (Pixel pixel : comp.pixels) {
				if (pixel != this) {
					if (pixel.rect.intersects(rect))
						y -= speed;
				}
			}
		}

	}
}
