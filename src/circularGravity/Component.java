package circularGravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JComponent;

public class Component extends JComponent implements KeyListener {
	Planet start = new Planet(500, 300, 100, .1, Color.cyan);
	Planet planet2 = new Planet(200, 300, 50, .1, Color.red);
	Planet planet3 = new Planet(-450,-100, 500, .1, Color.magenta);
	Player person = new Player(900, 100, this);
	HashSet<Integer> keysPressed = new HashSet<Integer>();
	ArrayList<Planet> planets = new ArrayList<Planet>();
	double angle = 0;

	public Component() {
		planets.add(start);
		planets.add(planet2);
		//planets.add(planet3);

	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		

		g2.translate(-person.x, -person.y);
		g2.translate(500, 200);
		AffineTransform old = g2.getTransform();
		old.translate(person.x+10, person.y + 10);
		old.rotate(angle);
		old.translate(-(person.x-10), -(person.y + 10));

		g2.setTransform(old);
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
		} else if (person.isGrounded) {
			person.attachToPlanet();
		}
		person.finalizeMovement();
		
		if(iskeyDown(KeyEvent.VK_RIGHT)){
			angle += .02;
		}
		if(iskeyDown(KeyEvent.VK_LEFT)){
			angle -= .02;
		}

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
