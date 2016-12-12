package randomStupidStuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JComponent;

public class Component extends JComponent implements KeyListener, MouseWheelListener{
	JumpMan man = new JumpMan(this);
	ArrayList<Obstacle> obstList = new ArrayList<Obstacle>();
	Color bgColor = Helper.randomrainbowcolor(.7f);
	public Component() {
		man.neur1.inputs = new double[] { 0 ,0};
		man.neur1.weights = new double[] { 1 ,1};
		Obstacle obst = new Obstacle(2, new Color(63, 138, 92), 100, 300);
		Obstacle obst2 = new Obstacle(2, new Color(63, 138, 92), 300, 300);
		Obstacle obst3 = new Obstacle(2,new Color(180, 0, 89), 400, 300);
		
		Obstacle obst4 = new Obstacle(2,new Color(63, 138, 92), 850, 300);
		Obstacle obst5 = new Obstacle(2, new Color(63, 138, 92), 950, 300);
		Obstacle obst6 = new Obstacle(2, new Color(63, 138, 92), 1100, 300);
		Obstacle obst7 = new Obstacle(2, new Color(63, 138, 92), 1200, 300);
		Obstacle obst8 = new Obstacle(2, new Color(63, 138, 92), 1350, 300);
		Obstacle obst9 = new Obstacle(2, new Color(63, 138, 92), 1370, 300);
		
		
		obst3.doesOssilate = true;
		obstList.add(obst);
		obstList.add(obst2);
		obstList.add(obst3);
		obstList.add(obst4);
		obstList.add(obst5);
		obstList.add(obst6);
		obstList.add(obst7);
		obstList.add(obst8);
		obstList.add(obst9);
		obstList.add(new Obstacle(2,new Color(63, 138, 92), 1500, 300));
		obstList.add(new Obstacle(2, new Color(63, 138, 92), 1500, 280));
		obstList.add(new Obstacle(2, new Color(63, 138, 92), 1500, 260));
		
		obstList.add(new Obstacle(2, new Color(63, 138, 92), 1600, 300));
		obstList.add(new Obstacle(2, new Color(63, 138, 92), 1600, 280));
		
		for(int i = 0; i < 10; i ++){
			obstList.add(new Obstacle(2, new Color(63, 138, 92), 1900 + (i * 100) * Helper.randInt(0, 3) , 300 - (Helper.randInt(0, 20))));
		}
		
	}

	public void drawSuccess(Graphics g) {
		if (man.x > 5000) {
			if (man.hasCompleted == false)
				man.hasCompleted = true;
			man.respawn();
		}
		if (man.hasCompleted) {
			g.setColor(Color.green);
			g.drawString("SUCCESS", 400, 200);
			g.drawString(man.deaths + " deaths", 400, 250);

		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(bgColor);
		g.fillRect(0, 0, 1000, 600);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		AffineTransform old = g2.getTransform();
		if(man.x > 500)
			g2.translate(-(man.x - 500), 0);
		for (Obstacle obst : obstList) {
			obst.draw(g);
		}
		man.draw(g);
		g.setColor(new Color(63, 138, 92));
		g.fillRect(0, 320, 6000, 280);
		g2.setTransform(old);
		drawSuccess(g);
		
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}
}
