package perspectiveDrawing2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JFrame;

public class Player {
	public static final int VIEW_DISTANCE = 200;
	public static final double RESOLUTION_X = 60;
	public static final double RESOLUTION_Y = 25;
	double x, y, z;
	double yaw = 135;
	double pitch = 50;
	JFrame frame;
	public static Component comp;
	
	public Player(double xpos, double ypos) {
		x = xpos;
		y = ypos;
		z = 0;
	}

	public void castRays(Graphics2D g2) {
		int cell_width = (int) ((double) frame.getWidth() / RESOLUTION_X);
		int cell_height = (int) ((double) frame.getHeight() / RESOLUTION_Y);

		double downAngle = -45;
		Line2D yawLine = new Line2D.Double(x + 2.5, y + 2.5, x + 2.5 + (100 * Math.sin(Math.toRadians(yaw))),
				y + 2.5 + (100 * Math.cos(Math.toRadians(yaw))));
		g2.setColor(Color.yellow);

		g2.draw(yawLine);

		g2.drawRect((int) x - 2, (int) y - 2, 4, 4);
		for (int vertical = 0; vertical < RESOLUTION_Y; vertical++) {
			g2.drawString(Integer.toString(vertical), 5, (int) ((frame.getHeight() / RESOLUTION_Y)*vertical));
			for (int horizontal = 0; horizontal < RESOLUTION_X; horizontal++) {
				double angle2D = yaw - (135 / 2) + ((135 / RESOLUTION_X) * (double) horizontal);
				double angle3D = pitch - 45 + ((90 / RESOLUTION_X) * (double) vertical);
				angle3D /= 10;
				System.out.println(angle3D);
				Vector vect = new Vector(new Point3D(x + 2.5, y + 2.5, z - .5), Math.sin(Math.toRadians(angle2D)),
						Math.cos(Math.toRadians(angle2D)), Math.sin(angle3D));
				Line2D line = new Line2D.Double(vect.A.x, vect.A.y,
						vect.A.x + (Player.VIEW_DISTANCE * Math.sin(Math.toRadians(angle2D))),
						vect.A.y + (Player.VIEW_DISTANCE * Math.cos(Math.toRadians(angle2D))));
				// g2.setColor(Color.red);
				//g2.draw(line);

				for (Block block : comp.blocks) {
					if (block.intersects(vect, angle2D,z)) {
						if(block.whichColor(line))
						g2.setColor(Color.cyan);
						else
							g2.setColor(Color.blue);
						//System.out.println((int) ((double) frame.getWidth() / RESOLUTION_X) * horizontal);
						g2.drawRect(((int) ((double) frame.getWidth() / RESOLUTION_X) * horizontal)+cell_width,
								((int) ((double) frame.getHeight() / RESOLUTION_Y) * vertical ) - cell_height, cell_width, cell_height);
						
					}

				}

			}

		}
	}

	public void walkForward() {
		x += 4 * Math.sin(Math.toRadians(yaw));
		y += 4 * Math.cos(Math.toRadians(yaw));
	}

	public void walkBackward() {
		x -= 4 * Math.sin(Math.toRadians(yaw));
		y -= 4 * Math.cos(Math.toRadians(yaw));
	}

	public void walkRight() {
		x += 2 * Math.sin(Math.toRadians(yaw - 90));
		y += 2 * Math.cos(Math.toRadians(yaw - 90));
	}

	public void walkLeft() {
		x -= 2 * Math.sin(Math.toRadians(yaw - 90));
		y -= 2 * Math.cos(Math.toRadians(yaw - 90));
	}

	public void turnLeft() {
		yaw += 5;
	}

	public void turnRight() {
		yaw -= 5;
	}
	public void jump(){
		z += 1;
	}
}
