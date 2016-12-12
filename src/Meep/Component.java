package Meep;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

public class Component extends JComponent implements KeyListener {
	Player player;
	Player bestPlayer;
	public Component() {

		player = new Player(new int[] { 0, 0 }, 1);
		player.myComp = this;
	}

	@Override
	public void paintComponent(Graphics g) {
		player.move();
		player.draw(g);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
