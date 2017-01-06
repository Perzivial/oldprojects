package randomTerrainGen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Human {
	static Component comp;
	// gene stuff
	int eatWeight = 0;
	int treeWeight = 0;
	int buildWeight = 0;
	int multiplyWeight = 0;
	String genome = "";
	Color clr = Color.yellow;
	// movement stuff
	double x = Helper.randInt(-9000, 19000);
	double y = 20;
	double velX = 0;
	double velY = 0;
	double health = 100;
	double hunger = 50;
	int sleepDays = 0;
	int updateTimer = 20;
	Tree nearestTree = null;
	Berry nearestBerry = null;
	ArrayList<String> inventory = new ArrayList<String>();
	boolean isDead = false;
	boolean sex;
	int screamCounter = 0;
	Human mate = null;
	House myHouse = null;
	int age = 0;
	boolean shouldDraw = true;

	Human target = null;

	public Human(int eat, int tree, int build, int steal) {
		eatWeight = eat;
		treeWeight = tree;
		buildWeight = build;
		multiplyWeight = steal;
	}

	/**
	 * The reason that im creating the genome as a string is so that i can
	 * mutate it later ;3
	 */
	public Human() {
		generateGenome();
		createStatsFromGenome();
		// sex = Helper.randInt(0, 1) == 0;
	}

	public Human(String gene1, String gene2) {
		generateGenome(gene1, gene2);
		createStatsFromGenome();
		// sex = Helper.randInt(0, 1) == 0;
	}

	public void draw(Graphics g, boolean shouldShow) {
		shouldDraw = true;

		if (!isDead) {
			move();
			g.setColor(clr);
		} else
			g.setColor(clr.darker());
		if (shouldShow) {
			if (shouldDraw) {
				g.drawLine((int) x, (int) y, (int) x, (int) y - 10);
				g.fillRect((int) x - 2, (int) y - 12, 4, 4);
			}
		}
		if (screamCounter > -1)
			screamCounter--;

	}

	public void move() {

		x += velX;
		y += velY;
		gravity();
		groundPhysics();
		updateRelations();

		hunger();

		if (mate == null)
			chooseAction();
		else {
			goToMate();
		}
		age++;
	}

	public void die() {
		isDead = true;
		comp.toRemove.add(this);
	}

	public void getClosestNonVillagePlayer() {
		Human temp = null;
		if(inventory.isEmpty())
		for (Human human : comp.humans) {
			if(human != this)
			if (!human.clr.equals(clr)) {
				if (Math.abs(x - human.x) < 100) {
					temp = human;
				}
			}
		}
		target = temp;
	}

	public void hunger() {

		if (hunger <= 0)
			health -= 0.01388888889;
		else
			hunger -= 0.001388888889;

		if (hunger < 30)
			eat();
	}

	public void gravity() {
		velY += .1;
	}

	public void walkRight() {

		Rectangle rect = new Rectangle((int) x + 2, (int) y - 4, 2, 4);

		if (sex == true)
			velX = .3;
		else
			velX = .5;
		for (Block block : comp.blocks) {
			if (rect.intersects(block.rect))
				jump();
			if (rect.intersects(block.rect))
				velX = -.5;
		}
	}

	public void walkLeft() {
		Rectangle rect = new Rectangle((int) x - 2, (int) y - 4, 2, 4);
		if (sex == true)
			velX = -.3;
		else
			velX = -.5;
		for (Block block : comp.blocks) {
			if (rect.intersects(block.rect))
				jump();
			if (rect.intersects(block.rect))
				velX = .5;
		}
	}

	public void jump() {
		if (isGrounded()) {
			velY = -1.2;
		}
	}

	public void chooseAction() {
		int eatNum = 0;
		int treeNum = 0;
		int buildNum = 0;
		int multiplyNum = 0;
		eatNum = 5 - Helper.amountOf("berry", inventory);
		treeNum = 2 - Helper.amountOf("log", inventory);
		buildNum = Helper.amountOf("log", inventory);
		multiplyNum = amountOfMatesInArea();

		if (nearestTree != null && nearestBerry != null) {
			if (Math.abs(nearestTree.x - x) > 200)
				treeNum -= 1;
			if (Math.abs(nearestBerry.x - x) > 200)
				eatNum -= 4;
		}
		if (myHouse != null && Math.abs(myHouse.x - x) > 300) {
			eatNum -= 3;
			treeNum -= 1;
		}
		eatNum *= eatWeight;
		treeNum *= treeWeight;
		buildNum *= buildWeight;
		multiplyNum *= multiplyWeight;

		if (!hasHouse())
			multiplyNum = Integer.MIN_VALUE;

		int homeNum = (comp.getSkyColor().getBlue() < 60 && hasHouse()) ? 15 : Integer.MIN_VALUE;
		//if (target == null) {
			switch (Helper.weigh(eatNum, treeNum, buildNum, multiplyNum, homeNum) + 1) {
			case 1:
				goToNearestBerry();
				break;
			case 2:
				goToNearestTree();
				break;
			case 3:
				buildHouse();
				break;
			case 4:
				scream();
				break;
			case 5:
				runHome();
				break;
			}
		//} //else {
			//attackTarget();
		//}
	}
	
	public void attackTarget(){
		if(x < target.x - 2)
			walkRight();
		else if(x > target.x + 2)
			walkLeft();
		else{
			attack(target);
		}
			
	}
	
	public void attack(Human human){
		human.health -= 0.6666666667;
		if(human.health < 0){
			human.die();
		}
	}
	
	public void scream() {
		screamCounter = 60;

	}

	public int amountOfMatesInArea() {
		int count = 0;
		for (Human human : comp.humans) {
			if (human.age > 2000 && human.clr.equals(clr))
				count++;
		}
		return count;
	}

	public void eat() {
		inventory.remove("berry");
		hunger += 10;
	}

	// actions
	public void findNearestTree() {

		for (Tree tree : comp.trees) {
			if (myHouse != null) {
				if (nearestTree == null && Math.abs(tree.x - myHouse.x) < 300 && Math.abs(tree.x - x) < 200)
					nearestTree = tree;
				else if (nearestTree != null && Math.abs(tree.x - x) < Math.abs(nearestTree.x - x)
						&& Math.abs(tree.x - myHouse.x) < 300 && Math.abs(tree.x - x) < 200)
					nearestTree = tree;
			} else {
				if (nearestTree == null)
					nearestTree = tree;
				else if (Math.abs(tree.x - x) < Math.abs(nearestTree.x - x))
					nearestTree = tree;
			}
		}

	}

	public void goToNearestTree() {
		if (nearestTree != null) {
			if (nearestTree.x < x - 5)
				walkLeft();
			else if (nearestTree.x > x + 5)
				walkRight();
			else {
				velX = 0;
				chopTree();
			}
		}
	}

	public void chopTree() {
		nearestTree.health--;
		if (nearestTree.health <= 0) {
			comp.trees.remove(nearestTree);
			inventory.add("log");
			nearestTree = null;
		}
	}

	// actions
	public void findNearestBerry() {

		for (Berry berry : comp.berries) {
			if (hasHouse()) {
				if (nearestBerry == null && Math.abs(berry.x - myHouse.x) < 300 && Math.abs(berry.x - x) < 300)
					nearestBerry = berry;
				else if (nearestBerry != null && Math.abs(berry.x - x) < Math.abs(nearestBerry.x - x)
						&& Math.abs(berry.x - myHouse.x) < 300 && Math.abs(berry.x - x) < 300)
					nearestBerry = berry;
			} else {
				if (nearestBerry == null)
					nearestBerry = berry;
				else if (Math.abs(berry.x - x) < Math.abs(nearestBerry.x - x))
					nearestBerry = berry;
			}
		}

	}

	public void goToNearestBerry() {
		if (nearestBerry != null) {
			if (nearestBerry.x < x - 5)
				walkLeft();
			else if (nearestBerry.x > x + 5)
				walkRight();
			else {
				velX = 0;
				pickBerry();
			}
		}
	}

	public void pickBerry() {
		comp.berries.remove(nearestBerry);
		inventory.add("berry");
		inventory.add("berry");
		nearestBerry = null;
	}

	public void buildHouse() {
		if (!hasHouse()) {
			House closeHouse = null;
			boolean shouldBuild = false;
			if (comp.houses.size() == 0)
				shouldBuild = true;
			else {
				for (House house : comp.houses) {
					if (isSimilar(house.creatorGenome) || house.clr.equals(clr)) {
						closeHouse = house;
						break;
					}
				}
				if (closeHouse != null) {
					if (Math.abs(closeHouse.x - x) < 100)
						shouldBuild = true;
					else {
						if (closeHouse.x < x)
							walkLeft();
						else
							walkRight();
					}
				} else {
					shouldBuild = true;
				}
			}
			if (shouldBuild) {
				House newHouse = new House(genome, (int) x, (int) y);
				myHouse = newHouse;
				comp.houses.add(newHouse);
				for (int i = 0; i < 3; i++) {
					inventory.remove("log");
				}
			}
		} else {
			if (x > myHouse.x + 15)
				walkLeft();
			else if (x < myHouse.x + 5)
				walkRight();
			else {
				myHouse.levels++;
				for (int i = 0; i < 3; i++) {
					inventory.remove("log");
				}
			}
		}
	}

	public void runHome() {
		if (x > myHouse.x + 15)
			walkLeft();
		else if (x < myHouse.x + 5)
			walkRight();
		else
			shouldDraw = false;
	}

	public boolean hasHouse() {
		for (House house : comp.houses) {
			if (house.creatorGenome == genome)
				return true;
		}
		return false;
	}

	public void goToMate() {
		if (x > mate.x + 2)
			walkLeft();
		else if (x < mate.x - 2)
			walkRight();

		if (Math.abs(x - mate.x) < 5) {
			multiply();
			inventory.remove("berry");
			inventory.remove("berry");
			inventory.remove("berry");
			
		}

	}

	public void multiply() {
		if (sex == true) {
			Human child = new Human(genome, mate.genome);

			child.x = x;
			child.y = y - 10;
			child.clr = clr;
			for(int i = 0; i < 5; i ++){
				child.inventory.add("log");
				child.inventory.add("berry");
			}
			comp.toAdd.add(child);
			
		}
		mate = null;
	}

	public void updateRelations() {
		updateTimer--;
		if (updateTimer <= 0) {

			findNearestTree();
			findNearestBerry();
			getClosestNonVillagePlayer();
			updateTimer = 20;
		}
		if (screamCounter > 0) {

			for (Human human : comp.humans) {

					if (human.screamCounter > 0) {
						if(human.clr.equals(clr)){
						if (mate == null)
							mate = human;
						else {
							if (Math.abs(human.x - x) < Math.abs(mate.x - x))
								mate = human;
						}
						}
					}
				
			}
		}
	}

	public boolean isGrounded() {
		Point point = new Point();
		point.setLocation(x, y);
		for (Block block : comp.blocks) {
			if (block.rect.contains(point))
				return true;
		}
		return false;
	}

	public void groundPhysics() {
		if (isGrounded()) {
			velY = 0;
		}
		Rectangle rect = new Rectangle((int) x - 1, (int) y - 4, 2, 4);
		for (Block block : comp.blocks) {
			if (rect.intersects(block.rect)) {
				y--;
			}
			Rectangle rectleft = new Rectangle((int) x - 1, (int) y - 4, 2, 4);
			Rectangle rectright = new Rectangle((int) x + 1, (int) y - 4, 2, 4);

		}

	}

	public void createStatsFromGenome() {
		for (char gene : genome.toCharArray()) {
			int num = (int) gene;
			if (num < 43)
				multiplyWeight++;
			else if (num < 73)
				treeWeight++;
			else if (num < 90)
				buildWeight++;
			else
				eatWeight++;
		}
		// System.out.println(this);
	}

	@Override
	public String toString() {
		String out = "";
		out += "Eat Weight : " + eatWeight + "\n";
		out += "Tree Weight : " + treeWeight + "\n";
		out += "Build Weight : " + buildWeight + "\n";
		out += "Multiplication Weight : " + multiplyWeight + "\n";
		return out;
	}

	public void generateGenome() {
		for (int i = 0; i < 50; i++) {
			genome += (char) Helper.randInt(33, 126);
		}
		System.out.println("The genome is " + genome);
	}

	public void generateGenome(String gene1, String gene2) {
		int splitPoint = Helper.randInt(0, gene1.length());
		String out = gene1.substring(0, splitPoint) + gene2.substring(splitPoint, gene2.length());
		char[] temp = out.toCharArray();
		int num = Helper.randInt(0, temp.length - 1);
		temp[num] = (char) Helper.randInt(33, 126);
		genome = "";
		for (char current : temp) {
			genome += current;
		}

	}

	public static Component getComp() {
		return comp;
	}

	public static void setComp(Component comp) {
		Human.comp = comp;
	}

	public boolean isSimilar(String gene2) {
		int count = 0;
		for (int i = 0; i < genome.length(); i++) {
			if (genome.charAt(i) == gene2.charAt(i))
				count++;
		}
		if (count > 5)
			return true;
		return false;
	}
}
