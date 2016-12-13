package randomTerrainGen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Human {
	static Component comp;
	// gene stuff
	int eatWeight = 0;
	int treeWeight = 0;
	int buildWeight = 0;
	int multiplyWeight = 0;
	String genome = "";
	// movement stuff
	double x = Helper.randInt(10, 4990);
	double y = 20;
	double velX = 0;
	double velY = 0;
	int health = 100;
	int hunger = 50;
	int sleepDays = 0;

	public Human(int eat, int tree, int build, int steal) {
		eatWeight = eat;
		treeWeight = tree;
		buildWeight = build;
		multiplyWeight = steal;
	}

	/**
	 * The reason that im creating the genome as a string is so that i can
	 * mutate it later ;3
	 */
	public Human() {
		generateGenome();
		createStatsFromGenome();
	}

	public void draw(Graphics g) {
		move();
		g.setColor(Color.yellow);
		g.drawLine((int) x, (int) y, (int) x, (int) y - 10);
		g.fillRect((int) x - 2, (int) y - 12, 4, 4);
	}

	public void move() {

		x += velX;
		y += velY;
		gravity();
		groundPhysics();
		walkLeft();
	}

	public void gravity() {
		velY += .1;
	}

	public void walkRight() {

		Rectangle rect = new Rectangle((int) x + 2, (int) y - 4, 2, 4);
		velX = .5;
		for (Block block : comp.blocks) {
			if (rect.intersects(block.rect))
				jump();
			if(rect.intersects(block.rect))
				velX = -.5;
		}
	}
	
	public void walkLeft() {
		Rectangle rect = new Rectangle((int) x - 2, (int) y - 4, 2, 4);
		velX = -.5;
		for (Block block : comp.blocks) {
			if (rect.intersects(block.rect))
				jump();
			if(rect.intersects(block.rect))
				velX = .5;
		}
	}
	
	public void jump() {
		if (isGrounded()) {
			velY = -1;
		}
	}

	public boolean isGrounded() {
		Point point = new Point();
		point.setLocation(x, y);
		for (Block block : comp.blocks) {
			if (block.rect.contains(point))
				return true;
		}
		return false;
	}

	public void groundPhysics() {
		if (isGrounded()) {
			velY = 0;
		}
		Rectangle rect = new Rectangle((int) x - 1, (int) y - 4, 2, 4);
		for (Block block : comp.blocks) {
			if (rect.intersects(block.rect)) {
				y--;
			}
			Rectangle rectleft = new Rectangle((int) x - 1, (int) y - 4, 2, 4);
			Rectangle rectright = new Rectangle((int) x + 1, (int) y - 4, 2, 4);

		}
		
	}

	public void createStatsFromGenome() {
		for (char gene : genome.toCharArray()) {
			int num = (int) gene;
			if (num < 43)
				multiplyWeight++;
			else if (num < 73)
				treeWeight++;
			else if (num < 90)
				buildWeight++;
			else
				eatWeight++;
		}
		System.out.println(this);
	}

	@Override
	public String toString() {
		String out = "";
		out += "Eat Weight : " + eatWeight + "\n";
		out += "Tree Weight : " + treeWeight + "\n";
		out += "Build Weight : " + buildWeight + "\n";
		out += "Multiplication Weight : " + multiplyWeight + "\n";
		return out;
	}

	public void generateGenome() {
		for (int i = 0; i < 20; i++) {
			genome += (char) Helper.randInt(33, 126);
		}
		System.out.println("The genome is " + genome);
	}

	public static Component getComp() {
		return comp;
	}

	public static void setComp(Component comp) {
		Human.comp = comp;
	}
}
