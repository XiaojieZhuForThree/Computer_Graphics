package Tetris;
import java.awt.*;
import java.awt.event.*;

public class Tetris extends Frame {
	public static void main(String[] args) {
		new Tetris();
	}
//	constructor
	Tetris() {
		super("Tetris");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		add("Center", new getTetris());
		setSize(800, 600);
		setVisible(true);
	}
}

class getTetris extends Canvas {
//	set the integers for the lengths and center of the canvas
	int maxX, maxY, minMaxXY, xCenter, yCenter;
	boolean show = false;	// to determine if the pause should be shown
	float square; // to determine the unit length of one square
	
	// to determine the coordinates for each object
	float xA, yA, xB, yB, xC, yC, xD, yD, xE, yE, xF, yF, xG, yG;
	float xH, yH, xI, yI, xJ, yJ, xK, yK, xL, yL, xM, yM;
	
	// to determine the coordinates for the cursor
	int mouse1x, mouse1y, mouse2x, mouse2y;
	
	// initialization function to get the initial value and center for the frame
	void initgr() {
		Dimension d = getSize();
		maxX = d.width - 1;
		maxY = d.height - 1;
		minMaxXY = Math.min(maxX, maxY);
		xCenter = maxX / 2;
		yCenter = maxY / 2;
	}

	int iX(float x) {
		return Math.round(x);
	}

	int iY(float y) {
		return maxY - Math.round(y);
	}
	
	getTetris() {
		// add the MouseListener to get the function of the QUIT function
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				mouse1x = event.getX();
				mouse1y = event.getY();
				if ((mouse1x < iX(xK + square * 4)) && (mouse1x > iX(xK)) && (mouse1y < iY(yK - square * (float) 1.5))
						&& (mouse1y > iY(yK))) {
					System.exit(0);
				}
			}
		});

		// use the mouseMotionListener to implement the function to show the PAUSE button
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent event) {
				mouse2x = event.getX();
				mouse2y = event.getY();
				if ((mouse2x < iX(xA + square * 10)) && (mouse2x > iX(xA)) && (mouse2y < iY(yA - square * 20)) && (mouse2y > iY(yA))) {
					show = true;
					repaint();
				} else {
					show = false;
					repaint();
				}
			}
		});
	}
	public void paint(Graphics g) {
		initgr();
		square = (float) (minMaxXY / 30);	// customarily set the length to be the minMaxXY / 30
		
		//coordinates for the main area
		xA = (float) xCenter - square * 5;
		yA = (float) yCenter + square * 10;
		
		// coordinates for the upper right small rectangle
		xB = (float) xCenter + square * 6;
		yB = yA;
		
		// coordinates for the pause button	
		xC = (float) ((float) xCenter - square * 2.5);
		yC = (float) yCenter + square;
		xM = (float) (xC + 0.9 * square);
		yM = (float) (yC - 1.4 * square);
		
		// coordinates for the yellow wedge
		xD = (float) xCenter + square;
		yD = (float) yCenter - square * 9;
		
		// coordinates for the blue L
		xE = (float) xCenter + square * 3;
		yE = (float) yCenter - square * 9;
		
		// coordinates for the red reversed L
		xF = xB + square;
		yF = yB - (float) 1.5 * square;
		
		// coordinates for the green cube
		xG = (float) xCenter - square;
		yG = (float) yCenter + square * 6;
		
		// coordinates for the strings
		xI = (float) xCenter + square * 6;
		yI = (float) yCenter - square;
		xH = xI;
		yH = yI + square * 2;
		xJ = xH;
		yJ = yI - 2 * square;
		
		// coordinates for the pause button
		xK = (float) xCenter + square * 6;
		yK = (float) yCenter - square * (float) 8.5;
		xL = (float) (xK + 0.9 * square);
		yL = (float) (yK - 1.1 * square);

		// draw all the necessary rectangles
		g.drawRect(iX(xA), iY(yA), (int) (square * 10), (int) (square * 20));
		g.drawRect(iX(xB), iY(yB), (int) (square * 5), (int) (square * 3));
		g.drawRect(iX(xK), iY(yK), (int) (square * 4), (int) (square * (float) 1.5));

		
		// draw the needed shapes
		drawWedge(g, iX(xD), iY(yD), (int) (square));
		drawL(g, iX(xE), iY(yE), (int) (square));
		drawReverseL(g, iX(xF), iY(yF), (int) (square));
		drawCube(g, iX(xG), iY(yG), (int) (square));

		// draw the strings
		Font f = new Font("Dialog", Font.BOLD, (int) square);
		g.setFont(f);
		g.drawString("Level:      1", iX(xH), iY(yH));
		g.drawString("Lines:      0", iX(xI), iY(yI));
		g.drawString("QUIT", iX(xL), iY(yL));
		f = new Font("Dialog", Font.BOLD, (int) (square));
		g.setFont(f);
		g.drawString("Score:      0", iX(xJ), iY(yJ));
		
		// draw the pause button, determined by the value of show
		if (show) {
			g.setColor(Color.BLUE);
			g.drawRect(iX(xC), iY(yC), (int) (square * 5), (int) (square * 2));
			f = new Font("Dialog", Font.BOLD, (int) (square));
			g.setColor(Color.BLUE);
			g.drawString("PAUSE", iX(xM), iY(yM));			
		}

	}
	
	// functions to draw all the shapes
	void drawWedge(Graphics g, int x, int y, int square) {
		int[][] vertices = new int[4][2];
		vertices[0] = new int[] { x, y };
		vertices[1] = new int[] { x + square, y };
		vertices[2] = new int[] { x + square, y - square };
		vertices[3] = new int[] { x + 2 * square, y - square };
		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.yellow);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}

	void drawL(Graphics g, int x, int y, int square) {
		int[][] vertices = new int[4][2];
		vertices[0] = new int[] { x, y };
		vertices[1] = new int[] { x + square, y };
		vertices[2] = new int[] { x + square, y - square };
		vertices[3] = new int[] { x + square, y - 2 * square };
		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.blue);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}

	void drawReverseL(Graphics g, int x, int y, int square) {
		int[][] vertices = new int[4][2];
		vertices[0] = new int[] { x, y };
		vertices[1] = new int[] { x + square, y };
		vertices[2] = new int[] { x + 2 * square, y };
		vertices[3] = new int[] { x + 2 * square, y - square };
		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.red);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}

	void drawCube(Graphics g, int x, int y, int square) {
		int[][] vertices = new int[4][2];
		vertices[0] = new int[] { x, y };
		vertices[1] = new int[] { x + square, y };
		vertices[2] = new int[] { x + square, y - square };
		vertices[3] = new int[] { x, y - square };
		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.green);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}
}
