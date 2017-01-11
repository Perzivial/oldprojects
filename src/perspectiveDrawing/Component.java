package perspectiveDrawing;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Component extends JComponent implements KeyListener {
	String tempServerName = "localhost";
	String tempPort = "";
	Socket client = null;
	DataInputStream in = null;
	private ServerSocket serverSocket;
	Socket server = null;

	GhostPlayer otherguy = new GhostPlayer(Integer.MAX_VALUE, Integer.MAX_VALUE, Color.red, this);
	Player player = new Player(0, 5, 0, this);
	ArrayList<Pixel> pixels = new ArrayList<Pixel>();
	HashSet<Integer> keysPressed = new HashSet<Integer>();
	BufferedImage ground = new Image("img/ground.png").img;
	BufferedImage deathImage = new Image("img/ghostscream.png").img;

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
	public static final int STATE_MULTIPLAYER_SETUP = 3;
	public static final int STATE_SETUP_SERVER = 4;
	public static final int STATE_SETUP_CLIENT = 5;
	int resetCounter = 60;
	boolean menuCursor = true;
	boolean shouldSpawn = true;
	boolean isEditingPort = true;

	public Component() {
		// pixels.add(new Pixel(20, 5, Color.ORANGE));
		// pixels.add(new Pixel(20, 20, Color.green));

		placePixelsBasedOnTextFile();
		// connectToServer();
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
			player.x = Integer.MAX_VALUE;
			g.drawImage(deathImage, 0, 0, 1000, 600, null);
			resetCounter--;
			if (resetCounter <= 0) {
				reset();
				if (server == null && client == null)
					state = STATE_MENU;
				else
					pixels.add(otherguy);
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
		case STATE_MULTIPLAYER_SETUP:
			g.setColor(Color.white);
			g.drawString("Please press 1 to setup a server, or 2 to join one", 10, 50);
			break;
		case STATE_SETUP_SERVER:
			g.setColor(Color.white);
			g.drawString("Port num: " + tempPort, 10, 50);
			g.drawString("Press enter when ready", 10, 150);
			break;
		case STATE_SETUP_CLIENT:
			g.setColor(Color.white);
			g.drawString("Port num: " + tempPort, 10, 50);
			g.drawString("Server name: " + tempServerName, 10, 100);
			g.drawString("Press enter when ready", 10, 200);
			break;
		}
		if (client != null) {
			if (!player.isShooting)
				sendToClient((int) player.x + "," + (int) player.y);
			else {
				boolean shoulddamage = false;

				RoundRectangle2D rectmy = new RoundRectangle2D.Double(460, 300, 70, 200, 100, 200);
				for (Rectangle rect : otherguy.columns) {
					if (rectmy.intersects(rect)) {
						shoulddamage = true;
					}
				}
				if (!shoulddamage)
					sendToClient((int) player.x + "," + (int) player.y + ",1");
				else
					sendToClient((int) player.x + "," + (int) player.y + ",2");
				player.isShooting = false;
			}
			String info = readFromClient();
			otherguy.x = Double.parseDouble(info.split(",")[0]);
			otherguy.y = Double.parseDouble(info.split(",")[1]);
			System.out.println(info.split(",").length);
			if (info.split(",").length > 2) {
				System.out.println(info.split(",")[2]);
				if (info.split(",")[2].equals("2"))
					player.health -= player.damage;
				otherguy.shotTimer = 1;
				System.out.println("shot");
			}
		}
		///
		if (server != null) {
			if (!player.isShooting)
				sendToServer((int) player.x + "," + (int) player.y);
			else {
				boolean shoulddamage = false;

				RoundRectangle2D rectmy = new RoundRectangle2D.Double(460, 300, 70, 200, 100, 200);
				for (Rectangle rect : otherguy.columns) {
					if (rectmy.intersects(rect)) {
						shoulddamage = true;
					}
				}
				if (!shoulddamage)
					sendToServer((int) player.x + "," + (int) player.y + ",1");
				else
					sendToServer((int) player.x + "," + (int) player.y + ",2");
				player.isShooting = false;
			}
			String info = readFromServer();
			otherguy.x = Double.parseDouble(info.split(",")[0]);
			otherguy.y = Double.parseDouble(info.split(",")[1]);
			if (info.split(",").length == 3) {
				System.out.println(info.split(",")[2]);
				if (info.split(",")[2].equals("2"))
					player.health -= player.damage;
				otherguy.shotTimer = 1;
				System.out.println("shot");
			}
		}
		otherguy.columns.clear();
	///
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
		if (shouldSpawn) {
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
					spawnTimer = spawnFrequency - player.score * 5;
					if (spawnTimer <= 20)
						spawnTimer = 20;

				} else {
					spawnTimer--;
				}
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
			if (e.getKeyCode() == KeyEvent.VK_M)
				state = STATE_MULTIPLAYER_SETUP;
			break;
		case STATE_CONTROLS:
			state = STATE_MENU;
			break;
		case STATE_MULTIPLAYER_SETUP:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_1:
				state = STATE_SETUP_SERVER;
				break;
			case KeyEvent.VK_2:
				state = STATE_SETUP_CLIENT;
				break;
			case KeyEvent.VK_ESCAPE:
				state = STATE_MENU;
				break;
			}
			break;
		case STATE_SETUP_SERVER:
			if (Character.isDigit(e.getKeyChar())) {
				tempPort += e.getKeyChar();
			}
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
				if (tempPort.length() > 0)
					tempPort = tempPort.substring(0, tempPort.length() - 1);
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				run(Integer.parseInt(tempPort));
			break;
		case STATE_SETUP_CLIENT:
			if (isEditingPort) {
				if (Character.isDigit(e.getKeyChar())) {
					tempPort += e.getKeyChar();
				}
			} else {
				if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE)
					tempServerName += e.getKeyChar();
			}
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {

				if (isEditingPort) {
					if (tempPort.length() > 0)
						tempPort = tempPort.substring(0, tempPort.length() - 1);
				} else {
					if (tempServerName.length() > 0)
						tempServerName = tempServerName.substring(0, tempServerName.length() - 1);
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
				isEditingPort = !isEditingPort;
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				connectToServer(tempServerName, Integer.parseInt(tempPort));
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

	public void connectToServer(String serverName, int port) {
		shouldSpawn = false;
		try {
			// System.out.println("Connecting to " + serverName + " on port " +
			// port);
			client = new Socket(serverName, port);

			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);

			out.writeUTF("Hello from " + client.getLocalSocketAddress());
			InputStream inFromServer = client.getInputStream();
			in = new DataInputStream(inFromServer);

			System.out.println("Server says " + in.readUTF());
			reset();
			pixels.add(otherguy);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendToClient(String outStr) {
		OutputStream outToServer;
		try {
			outToServer = client.getOutputStream();

			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF(outStr);
			System.out.println("The size of the outgoing info is " + out.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendToServer(String outStr) {
		OutputStream outToClient;
		try {
			outToClient = server.getOutputStream();

			DataOutputStream out = new DataOutputStream(outToClient);
			out.writeUTF(outStr);

			System.out.println("The size of the outgoing info is " + out.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readFromServer() {
		InputStream inFromServer;
		try {
			inFromServer = server.getInputStream();
			in = new DataInputStream(inFromServer);
			return in.readUTF();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return "";
	}

	public String readFromClient() {
		InputStream inFromClient;
		try {
			inFromClient = client.getInputStream();
			in = new DataInputStream(inFromClient);
			return in.readUTF();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return "";
	}

	public void run(int port) {
		shouldSpawn = false;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(10000);

			System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
			server = serverSocket.accept();

			System.out.println("Just connected to " + server.getRemoteSocketAddress());
			in = new DataInputStream(server.getInputStream());

			System.out.println(in.readUTF());
			DataOutputStream out = new DataOutputStream(server.getOutputStream());
			out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress());
			// server.close();
			reset();
			pixels.add(otherguy);
		} catch (SocketTimeoutException s) {
			System.out.println("Socket timed out!");

		} catch (IOException e) {
			e.printStackTrace();

		}

	}
}
