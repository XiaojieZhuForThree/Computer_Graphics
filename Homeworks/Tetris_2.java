package Tetris;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import javax.swing.Timer;

public class Tetris_2 extends Frame {
	public static void main(String[] args) {
		new Tetris_2();

	}

//	constructor
	Tetris_2() {
		super("Tetris_2");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		add("Center", new getTetris2());
		setSize(800, 600);
		setVisible(true);
	}
}

class getTetris2 extends Canvas {
	// set the integers for the lengths and center of the canvas
	int maxX, maxY, minMaxXY, xCenter, yCenter;
	boolean show = false, update = false; // to determine if the pause should be shown
	float square; // to determine the unit length of one square

	// to determine the coordinates for each object
	float xA, yA, xB, yB, xC, yC, xD, yD, xE, yE, xF, yF, xG, yG;
	float xH, yH, xI, yI, xJ, yJ, xK, yK, xL, yL, xM, yM;
	float xDraw, yDraw;
	float testYDraw, bottomLine;
	int leftMove = 0;
	int count = 0;
	int rotate = 0;
	// to determine the coordinates for the cursor
	int mouse1x, mouse1y, mouse2x, mouse2y;
	Set<int[]> prevDraws = new HashSet<>();
	Set<int[]> prevSquares;
	int[][] vertices = new int[4][2];
	// random number generator
	Random rand = new Random();

	// Obtain a number between [0 - 6].
	int n1 = rand.nextInt(7);
	int n2 = rand.nextInt(7);
	int delay = 1000;
	int xMin, xMax, yMin, yMax;
	int[][] nextCoord = new int[4][2];

	Timer timer = new Timer(delay, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			checkValid(n1, xDraw, yDraw - square, rotate);
			boolean coincidence = checkCoincidence(nextCoord, prevSquares);
			if (((n1 == 6 && yDraw > bottomLine - square) || (n1 != 6 && yDraw > bottomLine)) && !coincidence
					&& !show) {
				count += 1;
				repaint();
			} else if ((coincidence || yDraw <= bottomLine) && !show) {
				prevDraws.add(new int[] { n1, count, leftMove, rotate });
				n1 = n2;
				n2 = rand.nextInt(7);
				count = 0;
				leftMove = 0;
				rotate = 0;
				repaint();
			}
		}
	});

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

	getTetris2() {
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

			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1 && show == false) {
					checkValid(n1, xDraw - square, yDraw, rotate);
					if (!checkCoincidence(nextCoord, prevSquares)) {
						leftMove++;
						repaint();
					}
				} else if (event.getButton() == MouseEvent.BUTTON3 && show == false) {
					checkValid(n1, xDraw + square, yDraw, rotate);
					if (!checkCoincidence(nextCoord, prevSquares)) {
						leftMove--;
						repaint();
					}
				}
			}
		});

		addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent event) {
				if (event.getWheelRotation() < 0 && !show) {
					checkValid(n1, xDraw, yDraw, (rotate + 1) % 4);
					if (!(checkCoincidence(nextCoord, prevSquares)) && checkInside()) {
						rotate = (rotate + 1) % 4;
						repaint();
					}

				} else if (event.getWheelRotation() > 0 && !show) {
					checkValid(n1, xDraw, yDraw, (rotate - 1) % 4);
					if (!(checkCoincidence(nextCoord, prevSquares)) && checkInside()) {
						rotate = (rotate - 1) % 4;
						repaint();
					}
				}
			}
		});

		// use the mouseMotionListener to implement the function to show the PAUSE
		// button
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent event) {
				mouse2x = event.getX();
				mouse2y = event.getY();
				if ((mouse2x < iX(xA + square * 10)) && (mouse2x > iX(xA)) && (mouse2y < iY(yA - square * 20))
						&& (mouse2y > iY(yA))) {
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
		timer.start();
		initgr();
		prevSquares = new HashSet<>();
		square = (float) (minMaxXY / 30); // customarily set the length to be the minMaxXY / 30

		// coordinates for the main area
		xA = (float) xCenter - square * 5;
		yA = (float) yCenter + square * 10;

		// coordinates for the upper right small rectangle
		xB = (float) xCenter + square * 6;
		yB = yA;

		// coordinates for the pause button box
		xC = (float) ((float) xCenter - square * 2.5);
		yC = (float) yCenter + square;
		xM = (float) (xC + 0.9 * square);
		yM = (float) (yC - 1.4 * square);

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
		bottomLine = yCenter - 9 * square;

		xDraw = xCenter - leftMove * square;
		checkValid(n1, xDraw, yDraw, rotate);
		if (xMax > xCenter + 4 * square) {
			xDraw -= square;
			leftMove++;
		} else if (xMin < xCenter - 5 * square) {
			xDraw += square;
			leftMove--;
		}
		yDraw = yCenter + (9 - count) * square;
		// coordinates for the draw place

		// draw all the necessary rectangles
		g.drawRect(iX(xA), iY(yA), (int) (square * 10), (int) (square * 20));
		g.drawRect(iX(xB), iY(yB), (int) (square * 5), (int) (square * 3));
		g.drawRect(iX(xK), iY(yK), (int) (square * 4), (int) (square * (float) 1.5));

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
		// draw the shapes
		for (int[] info : prevDraws) {
			int n = info[0];
			int oldCount = info[1];
			int oldLeftMove = info[2];
			int oldRotate = info[3];
			drawMoveShapes(g, n, xCenter - oldLeftMove * square, yCenter + (9 - oldCount) * square, oldRotate);
			leaveMarks();
		}
		drawStableShapes(g, n2);
		drawMoveShapes(g, n1, xDraw, yDraw, rotate);
	}

	void checkValid(int n1, float xDraw, float yDraw, int rotate) {
		if (n1 == 0) {
			nextYellowWedge(iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 1) {
			nextPurpleReverseWedge(iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 2) {
			nextBlueReverseL(iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 3) {
			nextRedReverseL(iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 4) {
			nextGreenCube(iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 5) {
			nextOrangeHill(iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else {
			nextCyanBar(iX(xDraw - 2 * square), iY(yDraw + square), (int) square, rotate);
		}
	}

	void leaveMarks() {
		for (int[] vertex : vertices) {
			prevSquares.add(new int[] { vertex[0], vertex[1] });
		}
	}

	void drawMoveShapes(Graphics g, int n1, float xDraw, float yDraw, int rotate) {
		if (n1 == 0) {
			drawYellowWedge(g, iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 1) {
			drawPurpleReverseWedge(g, iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 2) {
			drawBlueReverseL(g, iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 3) {
			drawRedReverseL(g, iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 4) {
			drawGreenCube(g, iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else if (n1 == 5) {
			drawOrangeHill(g, iX(xDraw - square), iY(yDraw), (int) square, rotate);
		} else {
			drawCyanBar(g, iX(xDraw - 2 * square), iY(yDraw + square), (int) square, rotate);
		}
	}

	void drawStableShapes(Graphics g, int n2) {
		if (n2 == 0) {
			drawYellowWedge(g, iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
		} else if (n2 == 1) {
			drawPurpleReverseWedge(g, iX(xB + 2F * square), iY(yB - 1.5F * square), (int) square, 0);
		} else if (n2 == 2) {
			drawBlueReverseL(g, iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
		} else if (n2 == 3) {
			drawRedReverseL(g, iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
		} else if (n2 == 4) {
			drawGreenCube(g, iX(xB + 1.5F * square), iY(yB - 1.5F * square), (int) square, 0);
		} else if (n2 == 5) {
			drawOrangeHill(g, iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
		} else {
			drawCyanBar(g, iX(xB + 0.5F * square), iY(yB - square), (int) square, 0);
		}
	}

	boolean checkCoincidence(int[][] vertices, Set<int[]> prevSquares) {
		boolean coincidence = false;
		for (int[] coord : vertices) {
			for (int[] prev : prevSquares) {
				if (coord[0] == prev[0] && coord[1] == prev[1]) {
					coincidence = true;
					break;
				}
			}
		}
		return coincidence;
	}

	boolean checkInside() {
		return (xMin >= xCenter - (int) square * 5 && xMax <= xCenter + (int) square * 5
				&& yMin >= yCenter - (int) square * 10 && yMax >= yCenter - (int) square * 10);
	}

	// functions to draw all the shapes
	void drawYellowWedge(Graphics g, int x, int y, int square, int rotate) {
		if (rotate == 0 || rotate == 2 || rotate == -2) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x + square, y };
			vertices[2] = new int[] { x + square, y - square };
			vertices[3] = new int[] { x + 2 * square, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == 3 || rotate == -3 || rotate == -1) {
			vertices[0] = new int[] { x + square, y };
			vertices[1] = new int[] { x + square, y - square };
			vertices[2] = new int[] { x, y - square };
			vertices[3] = new int[] { x, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}
		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.YELLOW);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);
		}
	}

	void nextYellowWedge(int x, int y, int square, int rotate) {
		if (rotate == 0 || rotate == 2 || rotate == -2) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x + square, y };
			nextCoord[2] = new int[] { x + square, y - square };
			nextCoord[3] = new int[] { x + 2 * square, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == 3 || rotate == -3 || rotate == -1) {
			nextCoord[0] = new int[] { x + square, y };
			nextCoord[1] = new int[] { x + square, y - square };
			nextCoord[2] = new int[] { x, y - square };
			nextCoord[3] = new int[] { x, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}
	}

	void drawPurpleReverseWedge(Graphics g, int x, int y, int square, int rotate) {
		if (rotate == 0 || rotate == 2 || rotate == -2) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x + square, y };
			vertices[2] = new int[] { x, y - square };
			vertices[3] = new int[] { x - square, y - square };
			xMin = x - square;
			xMax = x + square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == 3 || rotate == -3 || rotate == -1) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x, y - square };
			vertices[2] = new int[] { x + square, y - square };
			vertices[3] = new int[] { x + square, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}
		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.MAGENTA);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);
		}
	}

	void nextPurpleReverseWedge(int x, int y, int square, int rotate) {
		if (rotate == 0 || rotate == 2 || rotate == -2) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x + square, y };
			nextCoord[2] = new int[] { x, y - square };
			nextCoord[3] = new int[] { x - square, y - square };
			xMin = x - square;
			xMax = x + square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == 3 || rotate == -3 || rotate == -1) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x, y - square };
			nextCoord[2] = new int[] { x + square, y - square };
			nextCoord[3] = new int[] { x + square, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}
	}

	void drawBlueReverseL(Graphics g, int x, int y, int square, int rotate) {
		if (rotate == 0) {
			vertices[0] = new int[] { x, y - square };
			vertices[1] = new int[] { x, y };
			vertices[2] = new int[] { x + square, y };
			vertices[3] = new int[] { x + 2 * square, y };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == -3) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x, y - square };
			vertices[2] = new int[] { x, y - 2 * square };
			vertices[3] = new int[] { x + square, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		} else if (rotate == 2 || rotate == -2) {
			vertices[0] = new int[] { x + 2 * square, y };
			vertices[1] = new int[] { x + 2 * square, y - square };
			vertices[2] = new int[] { x + square, y - square };
			vertices[3] = new int[] { x, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 3 || rotate == -1) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x + square, y };
			vertices[2] = new int[] { x + square, y - square };
			vertices[3] = new int[] { x + square, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}
		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.BLUE);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);
		}
	}

	void nextBlueReverseL(int x, int y, int square, int rotate) {
		if (rotate == 0) {
			nextCoord[0] = new int[] { x, y - square };
			nextCoord[1] = new int[] { x, y };
			nextCoord[2] = new int[] { x + square, y };
			nextCoord[3] = new int[] { x + 2 * square, y };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == -3) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x, y - square };
			nextCoord[2] = new int[] { x, y - 2 * square };
			nextCoord[3] = new int[] { x + square, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		} else if (rotate == 2 || rotate == -2) {
			nextCoord[0] = new int[] { x + 2 * square, y };
			nextCoord[1] = new int[] { x + 2 * square, y - square };
			nextCoord[2] = new int[] { x + square, y - square };
			nextCoord[3] = new int[] { x, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 3 || rotate == -1) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x + square, y };
			nextCoord[2] = new int[] { x + square, y - square };
			nextCoord[3] = new int[] { x + square, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}
	}

	void drawRedReverseL(Graphics g, int x, int y, int square, int rotate) {
		if (rotate == 0) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x + square, y };
			vertices[2] = new int[] { x + 2 * square, y };
			vertices[3] = new int[] { x + 2 * square, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == -3) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x + square, y };
			vertices[2] = new int[] { x, y - square };
			vertices[3] = new int[] { x, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		} else if (rotate == 2 || rotate == -2) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x, y - square };
			vertices[2] = new int[] { x + square, y - square };
			vertices[3] = new int[] { x + 2 * square, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 3 || rotate == -1) {
			vertices[0] = new int[] { x + square, y };
			vertices[1] = new int[] { x + square, y - square };
			vertices[2] = new int[] { x + square, y - 2 * square };
			vertices[3] = new int[] { x, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}

		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.RED);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);
		}
	}

	void nextRedReverseL(int x, int y, int square, int rotate) {
		if (rotate == 0) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x + square, y };
			nextCoord[2] = new int[] { x + 2 * square, y };
			nextCoord[3] = new int[] { x + 2 * square, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == -3) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x + square, y };
			nextCoord[2] = new int[] { x, y - square };
			nextCoord[3] = new int[] { x, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		} else if (rotate == 2 || rotate == -2) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x, y - square };
			nextCoord[2] = new int[] { x + square, y - square };
			nextCoord[3] = new int[] { x + 2 * square, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 3 || rotate == -1) {
			nextCoord[0] = new int[] { x + square, y };
			nextCoord[1] = new int[] { x + square, y - square };
			nextCoord[2] = new int[] { x + square, y - 2 * square };
			nextCoord[3] = new int[] { x, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}
	}

	void drawGreenCube(Graphics g, int x, int y, int square, int rotate) {
		vertices[0] = new int[] { x, y };
		vertices[1] = new int[] { x + square, y };
		vertices[2] = new int[] { x + square, y - square };
		vertices[3] = new int[] { x, y - square };
		xMin = x;
		xMax = x + square;
		yMin = y - square;
		yMax = y;
		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.GREEN);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}

	void nextGreenCube(int x, int y, int square, int rotate) {
		nextCoord[0] = new int[] { x, y };
		nextCoord[1] = new int[] { x + square, y };
		nextCoord[2] = new int[] { x + square, y - square };
		nextCoord[3] = new int[] { x, y - square };
		xMin = x;
		xMax = x + square;
		yMin = y - square;
		yMax = y;
	}

	void drawOrangeHill(Graphics g, int x, int y, int square, int rotate) {
		if (rotate == 0) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x + square, y };
			vertices[2] = new int[] { x + square, y - square };
			vertices[3] = new int[] { x + 2 * square, y };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == -3) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x, y - square };
			vertices[2] = new int[] { x + square, y - square };
			vertices[3] = new int[] { x, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		} else if (rotate == 2 || rotate == -2) {
			vertices[0] = new int[] { x + square, y };
			vertices[1] = new int[] { x + square, y - square };
			vertices[2] = new int[] { x, y - square };
			vertices[3] = new int[] { x + 2 * square, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 3 || rotate == -1) {
			vertices[0] = new int[] { x + square, y };
			vertices[1] = new int[] { x + square, y - square };
			vertices[2] = new int[] { x, y - square };
			vertices[3] = new int[] { x + square, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}

		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.ORANGE);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);
		}
	}

	void nextOrangeHill(int x, int y, int square, int rotate) {
		if (rotate == 0) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x + square, y };
			nextCoord[2] = new int[] { x + square, y - square };
			nextCoord[3] = new int[] { x + 2 * square, y };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 1 || rotate == -3) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x, y - square };
			nextCoord[2] = new int[] { x + square, y - square };
			nextCoord[3] = new int[] { x, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		} else if (rotate == 2 || rotate == -2) {
			nextCoord[0] = new int[] { x + square, y };
			nextCoord[1] = new int[] { x + square, y - square };
			nextCoord[2] = new int[] { x, y - square };
			nextCoord[3] = new int[] { x + 2 * square, y - square };
			xMin = x;
			xMax = x + 2 * square;
			yMin = y - square;
			yMax = y;
		} else if (rotate == 3 || rotate == -1) {
			nextCoord[0] = new int[] { x + square, y };
			nextCoord[1] = new int[] { x + square, y - square };
			nextCoord[2] = new int[] { x, y - square };
			nextCoord[3] = new int[] { x + square, y - 2 * square };
			xMin = x;
			xMax = x + square;
			yMin = y - 2 * square;
			yMax = y;
		}
	}

	void drawCyanBar(Graphics g, int x, int y, int square, int rotate) {
		if (rotate == 0 || rotate == 2 || rotate == -2) {
			vertices[0] = new int[] { x, y };
			vertices[1] = new int[] { x + square, y };
			vertices[2] = new int[] { x + 2 * square, y };
			vertices[3] = new int[] { x + 3 * square, y };
			xMin = x;
			xMax = x + 3 * square;
			yMin = y;
			yMax = y;
		} else if (rotate == 1 || rotate == 3 || rotate == -1 || rotate == -3) {
			vertices[0] = new int[] { x + square, y };
			vertices[1] = new int[] { x + square, y - square };
			vertices[2] = new int[] { x + square, y - 2 * square };
			vertices[3] = new int[] { x + square, y - 3 * square };
			xMin = x + square;
			xMax = x + square;
			yMin = y - 3 * square;
			yMax = y;
		}
		for (int[] coord : vertices) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.CYAN);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}

	void nextCyanBar(int x, int y, int square, int rotate) {
		if (rotate == 0 || rotate == 2 || rotate == -2) {
			nextCoord[0] = new int[] { x, y };
			nextCoord[1] = new int[] { x + square, y };
			nextCoord[2] = new int[] { x + 2 * square, y };
			nextCoord[3] = new int[] { x + 3 * square, y };
			xMin = x;
			xMax = x + 3 * square;
			yMin = y;
			yMax = y;
		} else if (rotate == 1 || rotate == 3 || rotate == -1 || rotate == -3) {
			nextCoord[0] = new int[] { x + square, y };
			nextCoord[1] = new int[] { x + square, y - square };
			nextCoord[2] = new int[] { x + square, y - 2 * square };
			nextCoord[3] = new int[] { x + square, y - 3 * square };
			xMin = x + square;
			xMax = x + square;
			yMin = y - 3 * square;
			yMax = y;
		}
	}

}
