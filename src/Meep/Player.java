package Meep;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Player {
	int x = 100;
	int y = 100;
	int velX = 0, velY = 0;
	Rectangle rect;
	int[] inputs;
	int moveIndex = 0;
	int cooldown = 0;
	Component myComp;

	public Player(int[] beforeinput, int mutationAmount) {
		ArrayList<Integer> derp = Helper.toArray(beforeinput);
		for (int i = 0; i < mutationAmount; i++) {
			if (Helper.randInt(0, 2) == 0) {
				try {
					derp.set(Helper.randInt(0, derp.size()-1), Helper.randInt(0, 4));
				} catch (Exception e) {
					System.out.println("error");
				}
			} else {
				derp.add(Helper.randInt(0, 4));
			}
		}
		inputs = new int[derp.size()];
		for (int i = 0; i < derp.size(); i++) {
			inputs[i] = derp.get(i);
		}
		System.out.println(Arrays.toString(inputs));
	}

	public void draw(Graphics g) {
		x += velX;
		y += velY;
		g.setColor(Color.white);
		rect = new Rectangle(x, y, 50, 50);
		g.fillRect(x, y, 50, 50);
	}

	public void move() {
		if (cooldown <= 0) {
			try {
				switch (inputs[moveIndex]) {
				case 1:
					velX = 1;
					break;
				case 2:
					velX = -1;
					break;
				case 3:
					velY = 1;
					break;
				case 4:
					velY = -1;
					break;
				}
				moveIndex++;
				cooldown = 10;
			} catch (Exception e) {
				reset();
			}
		} else
			cooldown--;
	}

	public void gravity() {

	}

	public void reset() {

		velX = 0;
		velY = 0;
		moveIndex = 0;
		cooldown = 0;

		if (myComp.bestPlayer == null) {
			myComp.bestPlayer = new Player(new int[inputs.length], 1);
			int count = 0;
			for(int current: inputs){
				myComp.bestPlayer.inputs[count] = current;
				count ++;
			}
		} else if (x > myComp.bestPlayer.x) {
			myComp.bestPlayer = new Player(new int[inputs.length], 1);
			int count = 0;
			for(int current: inputs){
				myComp.bestPlayer.inputs[count] = current;
				count ++;
			}
			System.out.println("overriden at " + x);
		}
		Player temp = new Player(myComp.bestPlayer.inputs, 3 * inputs.length / 2);
		inputs = temp.inputs;
		x = 100;
		y = 100;
	}
}
