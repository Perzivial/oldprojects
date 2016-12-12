package randomStupidStuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class JumpMan {
	int x = 10;
	int y = 300;
	int velX = 1;
	double velY;
	Component myComp;
	Neuron neur1 = new Neuron(8);
	int fitness = 0;
	double[] bestweights = new double[] { 0 , 0};
	int deaths = 0;
	boolean hasCompleted = false;
	
	public JumpMan(Component comp) {
		myComp = comp;
	}

	public void draw(Graphics g) {
		gravity();
		x += velX;
		y += velY;

		g.setColor(new Color(117,255,171));
		g.fillRect(x, y, 20, 20);
		getInput();
		checkForJumpAndStop();
		checkForDeath();

	}

	public void getInput() {
		neur1.inputs[0] = 0;
		neur1.inputs[1] = 0;
		for (Obstacle obst : myComp.obstList) {
			if (new Rectangle(x + 10, y, 30, 20).intersects(obst.rect)) {
				neur1.inputs[0] = obst.danger;
			}
		}
		if(y == 300){
			neur1.inputs[1] = 3;
		}
	}

	public void checkForJumpAndStop() {
		
		if (neur1.output() == 1) {
			jump();
		}
		
		/*
		if(neur1.sum() > .9){
			stop(true);
		}else{
			stop(false);
			if(neur1.sum() > .6)
				jump();
		}
		*/
	}
	
	public void jump() {
		
		//if (y == 300) {
			velY = -5;
			myComp.bgColor = Helper.randomrainbowcolor(.7f);
		//}

	}

	public void checkForDeath() {
		for (Obstacle obst : myComp.obstList) {
			if (new Rectangle(x, y, 20, 20).intersects(obst.rect))
				respawn();
		}
	}

	public void respawn() {
	
		x = 10;
		y = 300;
		velX = 1;
		velY = 0;
		neur1.inputs[0] = 0;
		if(!hasCompleted){
		if (x > fitness) {
			fitness = x;
			int count = 0;
			for (double current : neur1.weights) {
				bestweights[count] = current;
				count++;
			}
		}
		neur1.weights[0] = bestweights[0] + new Double(Helper.randInt(-2, 2));
		neur1.weights[1] = bestweights[1] + new Double(Helper.randInt(-2, 2));
		deaths++;
		}
		
	}

	public void stop(boolean is) {
		if (is)
			velX = 0;
		else
			velX = 1;
	}

	public void gravity() {

		velY += .1;

		if (y > 300) {
			y = 300;
			velY = 0;
		}

	}
}
