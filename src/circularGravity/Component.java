package circularGravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JComponent;

public class Component extends JComponent implements KeyListener {
	Planet start = new Planet(500, 300, 100, .1, Color.cyan);
	Planet planet2 = new Planet(200, 300, 50, .1, Color.red);
	Player person = new Player(900, 100, this);
	HashSet<Integer> keysPressed = new HashSet<Integer>();
	ArrayList<Planet> planets = new ArrayList<Planet>();

	public Component() {
		planets.add(start);
		planets.add(planet2);
	}

	@Override
	public void paintComponent(Graphics g) {
		for (Planet planet : planets) {
			planet.draw(g);
		}
		person.draw(g);

			
			if (keysPressed.contains(KeyEvent.VK_A))
				person.walkRight();
			if (keysPressed.contains(KeyEvent.VK_D))
				person.walkLeft();

		if (keysPressed.contains(KeyEvent.VK_W)) {
			person.jump();
		}else if(person.isGrounded){
			person.attachToPlanet();
		}
		person.finalizeMovement();
		
	}

	public boolean iskeyDown(int key) {
		if (keysPressed.contains(key))
			return true;
		return false;
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
