package perspectiveDrawing;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Component extends JComponent implements KeyListener {

	Player player = new Player(0, 5, 0, this);
	ArrayList<Pixel> pixels = new ArrayList<Pixel>();
	HashSet<Integer> keysPressed = new HashSet<Integer>();
	BufferedImage ground = new Image("img/ground.png").img;
	BufferedImage deathImage = new Image("img/ghostScream.png").img;
	public static BufferedImage enemyImg = new Image("img/ghost.png").getScaledInstance(
			new Image("img/ghost.png").img.getWidth() / 5, new Image("img/ghost.png").img.getHeight() / 5);
	public static BufferedImage enemyAngryImg = new Image("img/ghostAngry.png").getScaledInstance(
			new Image("img/ghostAngry.png").img.getWidth() / 5, new Image("img/ghostAngry.png").img.getHeight() / 5);
	int offsetX = 0;
	int offsetY = 400;
	Point2D mousePoint = null;
	double sensitivity = 2;
	int spawnTimer = 100;
	int spawnFrequency = 200;
	JFrame frame;
	boolean shouldZoom = false;
	int state = 1;
	public static final int STATE_INGAME = 0;
	public static final int STATE_DEAD = -1;
	public static final int STATE_MENU = 1;
	public static final int STATE_CONTROLS = 2;
	int resetCounter = 60;
	boolean menuCursor = true;

	public Component() {
		// pixels.add(new Pixel(20, 5, Color.ORANGE));
		// pixels.add(new Pixel(20, 20, Color.green));

		placePixelsBasedOnTextFile();

	}

	@Override
	public void paintComponent(Graphics g) {
		switch (state) {
		case STATE_INGAME:
			if (player.health < 0) {
				state = STATE_DEAD;
			}
			Graphics2D zoom = (Graphics2D) g;
			if (shouldZoom) {
				zoom.scale(2, 2);
			}
			Graphics2D g20 = (Graphics2D) g.create();
			// g20.fill(new RoundRectangle2D.Double(460,300,70,200,100,200));
			g.setColor(Color.darkGray.darker().darker().darker());
			g.fillRect(0, 400, 1000, 200);
			drawGround(g);
			for (int i = 0; i < 200; i++) {
				Color clr = new Color(0, 20, 0, (255 / 200) * i);
				g.setColor(clr);
				// g.fillRect(0, 599 - i, 1000, 1);
			}
			for (int i = 0; i < 200; i++) {
				Color clr = new Color(0, 50, 80, i);
				g.setColor(clr);
				g.fillRect(0, 200 + i, 1000, 1);
			}
			player.drawRays(g);
			if (isKeyDown(KeyEvent.VK_SPACE)) {
				player.shoot();
			}
			player.draw(g);
			movePlayer();
			if (isKeyDown(KeyEvent.VK_Q))
				drawPixels(g);
			// g.drawImage(getSlice(player.img, 5,10), 0, 0, null);
			spawnGhost();

			break;
		case STATE_DEAD:
			g.drawImage(deathImage, 0, 0, 1000, 600, null);
			resetCounter--;
			if (resetCounter <= 0) {
				reset();
				state = STATE_MENU;
			}
			break;
		case STATE_MENU:
			g.fillRect(0, 0, 1000, 600);
			player.drawRays(g);
			player.angle += 1;
			drawMenu(g);
			break;
		case STATE_CONTROLS:
			drawControls(g);
			break;
		}
		
	}

	public void reset() {
		pixels.clear();
		player = new Player(0, 0, 0, this);
		placePixelsBasedOnTextFile();
		resetCounter = 100;
		state = STATE_INGAME;
		spawnTimer = 100;
	}

	public void drawMenu(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font(g.getFont().getFontName(), 10, 80));
		g.drawString("Spoopy", 350, 100);
		g.setFont(new Font(g.getFont().getFontName(), 10, 20));
		g.drawString("Start", 470, 200);
		g.drawString("Controls", 450, 300);
		if (menuCursor == true)
			g.fillOval(455, 188, 10, 10);
		else
			g.fillOval(435, 288, 10, 10);
	}

	public void drawControls(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font(g.getFont().getFontName(), 10, 80));
		g.drawString("Controls", 350, 100);
		g.setFont(new Font(g.getFont().getFontName(), 10, 20));
		g.drawString("W - Forward", 450, 200);
		g.drawString("S - Backward", 450, 250);
		g.drawString("Right Arrow - Turn Right", 400, 300);
		g.drawString("Left Arrow - Turn Left", 400, 350);
		g.drawString("A - Strafe Left", 450, 400);
		g.drawString("D - Strafe Right", 450, 450);
		g.drawString("Press any key to return to menu", 370, 550);
	}

	public void spawnGhost() {
		int amountGhosts = 0;
		for (Pixel pixel : pixels) {
			if (pixel instanceof Enemy)
				amountGhosts++;
		}
		if (amountGhosts < 5) {
			if (spawnTimer <= 0) {

				Enemy temp = new Enemy(player.x + Helper.randInt(-50, 50), player.y + Helper.randInt(-50, 50),
						Color.red, this);

				pixels.add(temp);
				spawnTimer = spawnFrequency;
			} else {
				spawnTimer--;
			}
		}
	}

	public boolean doesRectCollide(Rectangle2D rect) {
		for (Pixel pixel : pixels) {
			if (rect.intersects(pixel.rect)) {

				return true;
			}
		}
		return false;
	}

	public BufferedImage getSlice(BufferedImage image, int slice, int amountSlices) {

		return image.getSubimage((image.getWidth() / amountSlices) * slice, 0, (image.getWidth() / amountSlices),
				image.getHeight());
	}

	public void drawGround(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		g2.setClip(0, 400, 1000, 200);

		g2.translate(500, 500);
		g2.scale(2, 2);
		g2.translate(-500, -500);

		// g2.drawImage(ground, offsetX, offsetY, 1000,200,this);
	}

	public void drawPixels(Graphics g) {
		for (Pixel pixel : pixels) {
			pixel.draw(g);
		}
	}

	public boolean isKeyDown(int key) {
		if (keysPressed.contains(key))
			return true;
		return false;

	}

	public void movePlayer() {
		if (state == STATE_INGAME) {
			if (isKeyDown(KeyEvent.VK_W)) {
				player.goForward();
			} else if (isKeyDown(KeyEvent.VK_S))
				player.goBackward();
			if (isKeyDown(KeyEvent.VK_A)) {
				player.walkLeft();
				offsetX += 3;
			}
			if (isKeyDown(KeyEvent.VK_D)) {
				player.walkRight();
				offsetX -= 3;
			}
			if (isKeyDown(KeyEvent.VK_LEFT))
				player.rotateLeft();
			if (isKeyDown(KeyEvent.VK_RIGHT))
				player.rotateRight();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (state) {
		case STATE_MENU:
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
				menuCursor = !menuCursor;
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (menuCursor) {
					reset();
					menuCursor = true;
				} else
					state = STATE_CONTROLS;

			}
			break;
		case STATE_CONTROLS:
			state = STATE_MENU;
			break;
		}
		keysPressed.add(e.getKeyCode());
		// TODO Auto-generated method stub
		/*
		 * switch (e.getKeyCode()) { case KeyEvent.VK_W: player.goForward();
		 * break; case KeyEvent.VK_S: player.goBackward(); break; case
		 * KeyEvent.VK_A: player.rotateLeft(); break; case KeyEvent.VK_D:
		 * player.rotateRight(); break; }
		 */
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.remove(e.getKeyCode());
	}

	// this code doesnt work lol. do not use it sucks
	public void sortPixelsByDistance() {
		ArrayList<Pixel> tempPixels = new ArrayList<Pixel>();

		for (Pixel pixel : pixels) {
			if (tempPixels.size() == 0)
				tempPixels.add(pixel);
			else {
				if (tempPixels.get(tempPixels.size() - 1).dist > pixel.dist)
					tempPixels.add(pixel);
				else {
					try {
						tempPixels.add(tempPixels.size() - 1, pixel);
					} catch (Exception e) {
						tempPixels.add(0, pixel);
					}
				}
			}
		}

	}

	public void placePixelsBasedOnTextFile() {
		Scanner scan = null;
		try {
			scan = new Scanner(new File("level.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> lines = new ArrayList<String>();
		while (scan.hasNext()) {
			lines.add(scan.nextLine());
		}
		int countLine = 0;

		for (String str : lines) {
			StringTokenizer tokenizer = new StringTokenizer(str);

			int count = 0;
			while (tokenizer.hasMoreTokens()) {

				String token = tokenizer.nextToken();

				if (token.contains("1")) {
					pixels.add(new Pixel(count * 10, countLine * 10, Color.darkGray));
					// System.out.println(count);
				} else if (token.contains("2")) {
					pixels.add(new Pixel(count * 10, countLine * 10, Color.gray));
				} else if (token.contains("p")) {
					player.x = (count * 10) + 5;
					player.y = (countLine * 10) + 5;
				} else if (token.contains("e")) {
					pixels.add(new Enemy(count * 10, countLine * 10, Color.red, this));
				}
				count++;

			}

			countLine++;
		}
	}

}
