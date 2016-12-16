package randomTerrainGen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JComponent;

public class Component extends JComponent
		implements MouseListener, MouseWheelListener, MouseMotionListener, KeyListener {

	boolean speedUp = false;

	Color groundclr = Color.green;
	Color barrierclr = Color.gray;
	// Block ground = new Block(0, 560, 5000, 20, barrierclr);
	// Block barrier1 = new Block(0, 0, 10, 560, barrierclr);
	// Block barrier2 = new Block(4990, 0, 10, 560, barrierclr);
	ArrayList<Block> blocks = new ArrayList<Block>();
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	Point mousePoint = new Point();
	double zoomAmount = .2;
	HashSet<Integer> keysPressed = new HashSet<Integer>();
	int translateX = 0;
	int translateY = 0;
	Human hmn = new Human();

	ArrayList<Tree> trees = new ArrayList<Tree>();
	ArrayList<Berry> berries = new ArrayList<Berry>();
	ArrayList<House> houses = new ArrayList<House>();
	ArrayList<Human> humans = new ArrayList<Human>();

	ArrayList<Human> toAdd = new ArrayList<Human>();
	ArrayList<Human> toRemove = new ArrayList<Human>();
	int tick = 1000;
	int time = 30;
	Rectangle screenRect = new Rectangle(0, 0, 1000, 600);
	boolean shouldDraw = true;

	public Component() {

		hmn.setComp(this);
		Human hmn2 = new Human();
		hmn2.x = hmn.x + Helper.randInt(-9000, 19000);
		hmn.sex = true;
		hmn2.sex = false;
		humans.add(hmn);
		humans.add(hmn2);

		for (int i = 0; i < 100; i++) {
			humans.add(new Human());
		}

		generateMountains2();
		// blocks.add(ground);
		// blocks.add(barrier1);
		// blocks.add(barrier2);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform old = g2.getTransform();
		toAdd.clear();
		toRemove.clear();
		g.setColor(getSkyColor());
		g.fillRect(0, 0, 1000, 600);
		g2.scale(zoomAmount, zoomAmount);
		g2.translate(translateX, translateY);

			for (House house : houses) {
				house.draw(g2);
			}
		
		for (Block block : blocks) {
			block.draw(g2);
		}
		if (shouldDraw) {
			for (Tree tree : trees) {
				tree.draw(g2);
			}
		}
		for (Human human : humans) {
			human.draw(g2, shouldDraw);
		}
		if (shouldDraw) {
			for (Berry berry : berries) {
				berry.draw(g2);
			}
		}
		villageDetector();
		humans.addAll(toAdd);
		humans.removeAll(toRemove);
		controlStuff();
		tick--;
		if (tick <= 0) {
			onTick();
			tick = 120;
		}

		g2.setTransform(old);

	}

	public void analyse() {
		int countInHouse = 0;
		for (Human human : humans) {
			if (human.shouldDraw == false)
				countInHouse++;
		}
		System.out.println("----------");
		System.out.println("There are a total of " + humans.size() + " humans");
		System.out.println("A total of " + countInHouse + " humans are in their houses");

	}

	public Color getSkyColor() {
		if (time < 50 || time > 150)
			return new Color(0, 0, 40);
		else
			return new Color(0, 0, 90);
	}

	public void onTick() {
		continueToPlaceFlora();
		dayCycle();
	}

	public void dayCycle() {
		time++;
		if (time > 200)
			time = 0;
	}

	public void continueToPlaceFlora() {
		for (Block block : blocks) {
			// one in every 10 blocks should have a tree, or a berry bush
			if (Helper.randInt(0, 100) == 1) {
				if (Helper.randInt(0, 1) == 1) {
					if (trees.size() < 250)
						trees.add(new Tree(block.rect.x + 5, block.rect.y));
				} else {
					if (berries.size() < 250)
						berries.add(new Berry(block.rect.x + 5, block.rect.y));
				}
			}

		}
	}

	public void controlStuff() {

		if (isKeyPressed(KeyEvent.VK_SHIFT)) {
			if (isKeyPressed(KeyEvent.VK_A))
				translateX += 5;
			if (isKeyPressed(KeyEvent.VK_D))
				translateX -= 5;
			if (isKeyPressed(KeyEvent.VK_W))
				translateY += 5;
			if (isKeyPressed(KeyEvent.VK_S))
				translateY -= 5;
		} else {
			if (isKeyPressed(KeyEvent.VK_A))
				translateX++;
			if (isKeyPressed(KeyEvent.VK_D))
				translateX--;
			if (isKeyPressed(KeyEvent.VK_W))
				translateY++;
			if (isKeyPressed(KeyEvent.VK_S))
				translateY--;
		}
	}

	public void generateMountains() {
		for (int i = 0; i < 3000; i++) {
			Block blocktemp = new Block(Helper.randInt(0, 240) * 20, 0, 20, 20, groundclr);

			boolean shouldGoDown = true;
			while (shouldGoDown) {
				for (Block block : blocks) {
					if (new Rectangle(blocktemp.rect.x, blocktemp.rect.y + 20, 20, 20).intersects(block.rect)
							|| blocktemp.rect.y >= 540) {
						shouldGoDown = false;
					}
				}
				if (shouldGoDown) {
					blocktemp.rect = new Rectangle(blocktemp.rect.x, blocktemp.rect.y + 20, 20, 20);

				}
			}
			blocks.add(blocktemp);
			System.out.println("Added " + i + " blocks");
		}

	}

	public void villageDetector() {
		Color clr = new Color(Helper.randInt(0, 255), Helper.randInt(0, 255), Helper.randInt(0, 255));
		for (House house : houses) {
			ArrayList<House> village = new ArrayList<House>();
			village.add(house);
			for (House house2 : houses) {
				if (house != house2)
					if (Math.abs(house.x - house2.x) < 100) {
						village.add(house2);

					}
			}
			if (village.size() > 2) {

				for (House house3 : village) {

					if (house3.clr == Color.white) {
						house3.clr = clr;
						for (Human human : humans) {
							if (house3.creatorGenome.equals(human.genome))
								human.clr = clr;
						}
					}
				}
			}
		}
	}

	// the current code for generating
	public void generateMountains2() {
		int lastHeight;
		int height = Helper.randInt(300, 305);
		for (int i = 0; i < 20000 / 20; i++) {
			blocks.add(new Block((i * 20) - 10000, height, 20, 20, groundclr));
			lastHeight = height;
			height = lastHeight + Helper.randInt(-5, 5);

		}
		System.out.println("Initial line");
		ArrayList<Block> temp = new ArrayList<Block>();
		int count = 1;
		for (Block block : blocks) {
			int y = block.rect.y;

			while (y < 540) {
				y += 20;
				// temp.add(new Block(block.rect.x, y, 20, 20, groundclr));
			}
			System.out.println("Filling in blocks. " + count + " completed");
			count++;
			// one in every 10 blocks should have a tree, or a berry bush
			if (Helper.randInt(0, 2) == 1) {
				if (Helper.randInt(0, 1) == 1)
					trees.add(new Tree(block.rect.x + 5, block.rect.y));
				else
					berries.add(new Berry(block.rect.x + 5, block.rect.y));
			}

		}
		blocks.addAll(temp);
	}

	public void smoothMountains() {
		Collection removequeue = new ArrayList<Block>();
		for (Block myblock : blocks) {
			if (!hasBlocksAbove(myblock))
				for (Block myblock2 : blocks) {
					if (myblock2.rect.x == myblock.rect.x + 20) {
						if (myblock2.rect.y < myblock.rect.y - 80) {
							removequeue.add(myblock2);
						}
					}
				}
		}
		blocks.removeAll(removequeue);
	}

	public boolean hasBlocksAbove(Block block) {

		for (Block block2 : blocks) {
			if (block2 != block) {
				if (block.rect.x == block2.rect.x)
					if (block.rect.y > block2.rect.y)
						return false;
			}
		}

		return true;
	}

	public boolean isKeyPressed(int key) {
		if (keysPressed.contains(key))
			return true;
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		/*
		 * if (zoomAmount > .23000 || e.getWheelRotation() > 0) if (zoomAmount <
		 * 1.100) zoomAmount += e.getPreciseWheelRotation() / 10; if (zoomAmount
		 * < .2) zoomAmount = .2; if (zoomAmount > 1) zoomAmount = 1;
		 */
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keysPressed.add(e.getKeyCode());
		switch (e.getKeyCode()) {
		case KeyEvent.VK_EQUALS:
			zoomAmount += .2;

			break;
		case KeyEvent.VK_MINUS:
			zoomAmount -= .2;

			break;
		case KeyEvent.VK_Q:
			analyse();
			break;
		case KeyEvent.VK_SPACE:
			shouldDraw = !shouldDraw;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.remove(e.getKeyCode());

	}
}
