package perspectiveDrawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JComponent;

public class Component extends JComponent implements KeyListener {

	Player player = new Player(0, 5, 90, this);
	ArrayList<Pixel> pixels = new ArrayList<Pixel>();
	HashSet<Integer> keysPressed = new HashSet<Integer>();
	public Component() {
		pixels.add(new Pixel(20, 5, Color.ORANGE));
		pixels.add(new Pixel(20, 20, Color.DARK_GRAY));
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.gray);
		g.fillRect(0, 400, 1000, 200);
		player.draw(g);
		movePlayer();
		drawPixels(g);
	}

	public void drawPixels(Graphics g) {
		for (Pixel pixel : pixels) {
			pixel.draw(g);
		}
	}
	public boolean isKeyDown(int key){
		if(keysPressed.contains(key))
			return true;
		return false;
					
	}
	public void movePlayer(){
		if(isKeyDown(KeyEvent.VK_W))
			player.goForward();
		else if (isKeyDown(KeyEvent.VK_S))
			player.goBackward();
		if(isKeyDown(KeyEvent.VK_A))
			player.rotateLeft();
		if (isKeyDown(KeyEvent.VK_D))
			player.rotateRight();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keysPressed.add(e.getKeyCode());
		// TODO Auto-generated method stub
		/*
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.goForward();
			break;
		case KeyEvent.VK_S:
			player.goBackward();
			break;
		case KeyEvent.VK_A:
			player.rotateLeft();
			break;
		case KeyEvent.VK_D:
			player.rotateRight();
			break;
		}
		*/
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.remove(e.getKeyCode());
	}
	public void sortPixelsByDistance(){
		ArrayList<Pixel> tempPixels = new ArrayList<Pixel>();
		for(Pixel pixel: pixels){
			if(tempPixels.size() == 0)
				tempPixels.add(pixel);
			else{
				if(tempPixels.get(tempPixels.size() - 1).dist > pixel.dist)
					tempPixels.add(pixel);
				else{
					try{
					tempPixels.add(tempPixels.size() - 2,pixel);
					}catch(Exception e){
						tempPixels.add(0,pixel);
					}
				}
			}
		}
	}
}
