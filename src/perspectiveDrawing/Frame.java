package perspectiveDrawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.Method;

import javax.swing.JButton;
// Take a look in the JRE (Java Runtime Environment) Library classes.jar for javax.swing in Eclipse IDE
// You will see all the .class files for each swing class, like JFrame, JComponent etc. (compiled Java bytecode)
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Class that contains the main method for the program and creates the frame
 * containing the component.
 */
@SuppressWarnings("deprecation")
public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();
	JFrame frame;
	private final static int ONE_SECOND = 1000;
	private static Timer timer = null;
	private int screensizeChangeBuffer = 0;
	Component theComponent = new Component();

	public Frame(String s) throws Exception {

		super(s);
	
		/*
		 * JPanel p = new JPanel(); label = new JLabel("Key Listener!");
		 * p.add(label); add(p); addKeyListener(this); setSize(200, 100);
		 * setVisible(true);
		 */
		frame = new JFrame();
		frame.add(theComponent);
		frame.setSize(1000, 600);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Spare Management System");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theComponent.addKeyListener(theComponent);
		// theComponent.addMouseListener(theComponent);
		// theComponent.addMouseMotionListener(theComponent);
		// theComponent.addMouseWheelListener(theComponent);
		theComponent.setFocusable(true);

		frame.getContentPane().setBackground(new Color(0,0,30));
		theComponent.setDoubleBuffered(true);
		theComponent.setVisible(true);

		frame.setVisible(true);
		Font myFont = new Font("Lucida", Font.PLAIN, 25);
		theComponent.setFont(myFont);

		setLayout(new FlowLayout()); // set the layout manager

		timer = new Timer(ONE_SECOND / 30, new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				theComponent.repaint();

			}
		});
	}

	public static void main(String[] args) throws Exception {
		new Frame("Spare Management System");
		timer.start();

	}

}
