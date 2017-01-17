package perspectiveDrawing2;

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
import java.awt.geom.Line2D;
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
	JFrame frame;
	Block block = new Block(200, 200, 0);
	Vector vect = new Vector(new Point3D(0, 0, 0), new Point3D(5, 1, .5));
	Player player = new Player(200, 200);
	HashSet<Integer> keys = new HashSet<Integer>();
	ArrayList<Block> blocks = new ArrayList<Block>();

	public Component() {
		player.comp = this;
		blocks.add(block);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(blocks.get(0).x, blocks.get(0).y, 10, 10);
		Graphics2D g2 = (Graphics2D) g;

		player.castRays(g2);
		getInput();

	}

	public void getInput() {
		if (keys.contains(KeyEvent.VK_W))
			player.walkForward();
		if (keys.contains(KeyEvent.VK_S))
			player.walkBackward();
		if (keys.contains(KeyEvent.VK_A))
			player.walkLeft();
		if (keys.contains(KeyEvent.VK_D))
			player.walkRight();
		if (keys.contains(KeyEvent.VK_LEFT))
			player.turnLeft();
		if (keys.contains(KeyEvent.VK_RIGHT))
			player.turnRight();
		if(keys.contains(KeyEvent.VK_SPACE))
			player.jump();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys.add(e.getKeyCode());

	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.remove(e.getKeyCode());
	}
}
