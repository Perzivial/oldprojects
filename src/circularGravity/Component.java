package circularGravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JComponent;

public class Component extends JComponent implements KeyListener,MouseListener,MouseMotionListener {
	Planet start = new Planet(500, 300, 100, 10, Color.cyan);
	Planet planet2 = new Planet(200, 300, 50, 5, Color.red);
	Planet planet3 = new Planet(-450,-100, 500, .1, Color.magenta);
	Player person = new Player(500, 150, this);
	HashSet<Integer> keysPressed = new HashSet<Integer>();
	ArrayList<Planet> planets = new ArrayList<Planet>();
	double angle = 0;
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	public Component() {

		planet2.orbitPlanet = start;
		planet2.orbitDistance = 300;
		planet3.orbitSpeed = 2;
		//planets.add(start);
		//planets.add(planet2);
		for(int i = 0; i < 100; i ++){
			 addPlanetSafely();
		}
		//planets.add(planet3);

	}

	@Override
	public void paintComponent(Graphics g) {
	
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.translate(-person.x, -person.y);
		g2.translate(500, 200);
		AffineTransform old = g2.getTransform();
		
		old.translate(person.x+10, person.y + 10);
		old.rotate(angle);
		old.translate(-(person.x-10), -(person.y + 10));

		g2.setTransform(old);
		g2.translate(-person.x, -person.y);
		g2.scale(2, 2);
		for (Planet planet : planets) {

			planet.draw(g);
			
		}
		
		person.draw(g);
		ArrayList<Bullet> removeBullet = new ArrayList<Bullet>();
		for(Bullet bullet:bullets){
			bullet.draw(g2);
			if(bullet.lifeTime<= 0)
				removeBullet.add(bullet);
		}
		bullets.removeAll(removeBullet);

		//System.out.println((new Point2D.Double((person.x+10)*Math.sin(person.angle), (person.y+10)*Math.sin(person.angle)).distance(person.nearestPlanet.centre)));
		if (keysPressed.contains(KeyEvent.VK_A))
			person.walkRight();
		if (keysPressed.contains(KeyEvent.VK_D))
			person.walkLeft();

		if (keysPressed.contains(KeyEvent.VK_W)) {
			person.jump();
		}
		person.finalizeMovement();
		
		if(iskeyDown(KeyEvent.VK_RIGHT)){
			//angle += .02;
			person.shootingAngle -= .1;
		}
		if(iskeyDown(KeyEvent.VK_LEFT)){
			//angle -= .02;
			person.shootingAngle += .1;			
		}if(iskeyDown(KeyEvent.VK_UP)){
			bullets.add(new Bullet(person.x,person.y,person.velX + 5*Math.sin(person.angle + person.shootingAngle),person.velY +5*Math.cos( person.angle + person.shootingAngle)));
		}
		
	}
	
	public void addPlanetSafely(){
		Planet newPlanet = new Planet((double)Helper.randInt(-1000,1000),(double)Helper.randInt(-1000,1000),(double)Helper.randInt(50, 100), (double)(Helper.randInt(10,30))/10,Helper.randomrainbowcolor(.6f));
		boolean shouldAdd = true;
		for(Planet planet:planets){
			if(newPlanet.centre.distance(planet.centre) < planet.radius *2+ newPlanet.radius*2)
				shouldAdd = false;
		}
		if(shouldAdd)
		planets.add(newPlanet);
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
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		double angle = Math.atan2(e.getY()-person.y, e.getX()-person.x);
		//System.out.println(Math.toDegrees(angle));
		
	}
}
