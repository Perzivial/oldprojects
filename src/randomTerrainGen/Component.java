package randomTerrainGen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JComponent;

public class Component extends JComponent
		implements MouseListener, MouseWheelListener, MouseMotionListener, KeyListener {
	Color groundclr = Color.green;
	Block ground = new Block(0, 560, 5000, 20, groundclr);
	ArrayList<Block> blocks = new ArrayList<Block>();
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	Point mousePoint = new Point();
	double zoomAmount = .2;
	HashSet<Integer> keysPressed = new HashSet<Integer>();
	int translateX = 0;
	int translateY = 0;
	public Component() {
		blocks.add(ground);
		generateMountains2();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(translateX, translateY);
		g2.scale(zoomAmount, zoomAmount);

		for (Block block : blocks) {
			block.draw(g2);
		}
		if (isKeyPressed(KeyEvent.VK_A))
			translateX++;
		if (isKeyPressed(KeyEvent.VK_D))
			translateX--;
		if (isKeyPressed(KeyEvent.VK_W))
			translateY++;
		if (isKeyPressed(KeyEvent.VK_S))
			translateY--;
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

	// the current code for generating
	public void generateMountains2() {
		int lastHeight;
		int height = Helper.randInt(300, 305);
		for (int i = 0; i < 5000 / 20; i++) {
			blocks.add(new Block(i * 20, height, 20, 20, groundclr));
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
				temp.add(new Block(block.rect.x, y, 20, 20, groundclr));
			}
			System.out.println("Filling in blocks. " + count + " completed");
			count++;
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

		if (zoomAmount > .23000 || e.getWheelRotation() > 0)
			if (zoomAmount < 1.100)
				zoomAmount += e.getPreciseWheelRotation() / 10;
		if (zoomAmount < .2)
			zoomAmount = .2;
		if (zoomAmount > 1)
			zoomAmount = 1;
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

	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.remove(e.getKeyCode());

	}
}
