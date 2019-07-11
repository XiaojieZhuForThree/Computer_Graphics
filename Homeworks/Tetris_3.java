package Tetris;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Square {
	int X, Y;
	Color color;

	Square(int x, int y, Color cl) {
		X = x;
		Y = y;
		color = cl;
	}
}

class Point2D {
	float x, y;

	Point2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

}

class Tools2D {
	static float area2(Point2D a, Point2D b, Point2D c) {
		return (a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);
	}

	static float distance2(Point2D p, Point2D q) {
		float dx = p.x - q.x, dy = p.y - q.y;
		return dx * dx + dy * dy;
	}

	static boolean insideTriangle(Point2D a, Point2D b, Point2D c, Point2D p) {
		return area2(a, b, p) >= 0 && area2(b, c, p) >= 0 && area2(c, a, p) >= 0;
	}
}

public class Tetris_3 extends Frame {
	// global variables
	JSlider scoringFactor;
	JLabel sfLabel;

	JSlider levelClimber;
	JLabel lcLabel;

	JSlider speedFactor;
	JLabel spLabel;

	JSlider heightFactor;
	JLabel heightLabel;

	JSlider widthFactor;
	JLabel widthLabel;

	JSlider sizeFactor;
	JLabel sizeLabel;

	Panel panel;

	public void setPanel() {

		scoringFactor = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		scoringFactor.setMajorTickSpacing(1);
		scoringFactor.setPaintTicks(true);
		sfLabel = new JLabel("Scoring Factor: 1");
		scoringFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sfLabel.setText(String.valueOf("Scoring Factor: " + scoringFactor.getValue()));
			}
		});

		levelClimber = new JSlider(JSlider.HORIZONTAL, 20, 50, 20);
		levelClimber.setMajorTickSpacing(5);
		levelClimber.setPaintTicks(true);
		lcLabel = new JLabel("Lines Removed Required for level up: 20");
		levelClimber.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lcLabel.setText(String.valueOf("Lines Removed Required for level up: " + levelClimber.getValue()));
			}
		});

		speedFactor = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		speedFactor.setMajorTickSpacing(1);
		speedFactor.setPaintTicks(true);
		spLabel = new JLabel("Speed Factor: 0.1");
		speedFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				spLabel.setText(String.valueOf("Speed Factor: " + ((float) speedFactor.getValue() / 10)));
			}
		});

		heightFactor = new JSlider(JSlider.HORIZONTAL, 20, 30, 20);
		heightFactor.setMajorTickSpacing(2);
		heightFactor.setPaintTicks(true);
		heightLabel = new JLabel("Current Hight: 20 squares");
		heightFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				heightLabel.setText(String.valueOf("Current Hight: " + heightFactor.getValue() + " squares"));
			}
		});

		widthFactor = new JSlider(JSlider.HORIZONTAL, 10, 20, 10);
		widthFactor.setMajorTickSpacing(2);
		widthFactor.setPaintTicks(true);
		widthLabel = new JLabel("Current width: 10 squares");
		widthFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				widthLabel.setText(String.valueOf("Current width: " + widthFactor.getValue() + " squares"));
			}
		});

		sizeFactor = new JSlider(JSlider.HORIZONTAL, 10, 15, 10);
		sizeFactor.setMajorTickSpacing(1);
		sizeFactor.setPaintTicks(true);
		sizeLabel = new JLabel("Current size: 1X");
		sizeFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sizeLabel.setText(String.valueOf("Current size: " + (float) sizeFactor.getValue() / 10 + "X"));
			}
		});

		panel = new Panel();
		panel.add(sfLabel);
		panel.add(scoringFactor);
		panel.add(lcLabel);
		panel.add(levelClimber);
		panel.add(spLabel);
		panel.add(speedFactor);
		panel.add(heightLabel);
		panel.add(heightFactor);
		panel.add(widthLabel);
		panel.add(widthFactor);
		panel.add(sizeLabel);
		panel.add(sizeFactor);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	}

	public static void main(String[] args) {
		new Tetris_3();
	}

//	constructor
	Tetris_3() {
		super("Tetris_3");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		add(new getTetris3(), BorderLayout.CENTER);
		add(panel, BorderLayout.EAST);
		setSize(800, 600);
		setVisible(true);
		// setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}

	class getTetris3 extends Canvas {

		// set the integers for the lengths and center of the canvas
		int maxX, maxY, minMaxXY, xCenter, yCenter;
		boolean show = false, update = false, stillIn = false; // to determine if the pause should be shown
		float square; // to determine the unit length of one square

		// to determine the coordinates for each object
		float xA, yA, xB, yB, xC, yC, xD, yD, xE, yE, xF, yF, xG, yG;
		float xH, yH, xI, yI, xJ, yJ, xK, yK, xL, yL, xM, yM;
		float xDraw, yDraw;
		float testYDraw, bottomLine;
		int leftMove = 0;
		int count = 0;
		int rotate = 0;

		int height;
		int width;
		int size;

		// to determine the coordinates for the cursor
		int mouse1x, mouse1y, mouse2x, mouse2y;
		List<int[]> prevDraws = new ArrayList<>();
		Set<Square> prevs;

		// random number generator
		Random rand = new Random();

		Point2D[] pol = new Point2D[4];

		// Obtain a number between [0 - 6].
		int n1 = 7;
		int n2 = 7;
		float delay = 700F;
		int xMin, xMax, yMin, yMax;
		List<int[]> nextCoord = new ArrayList<>();

		int score = 0, minus = 0, M, N;
		int level = 1, previousLevel = 1;
		int removedRows = 0;
		float S;

		Timer timer = new Timer((int) delay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				checkValid(n1, xDraw, yDraw - square, rotate);
				boolean coincidence = newcheckCoincidence(nextCoord, prevs);
				if (!coincidence && checkInside() && !show) {
					count += 1;
					repaint();
				} else if ((coincidence || !checkInside()) && !show) {
					prevDraws.add(new int[] { n1, count, leftMove, rotate });
					n1 = n2;
					n2 = rand.nextInt(8);
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
			M = scoringFactor.getValue();
			N = levelClimber.getValue();
			S = ((float) speedFactor.getValue() / 10);
			width = widthFactor.getValue();
			height = heightFactor.getValue();
			size = sizeFactor.getValue();
		}

		int iX(float x) {
			return Math.round(x);
		}

		int iY(float y) {
			return maxY - Math.round(y);
		}

		float fx(int x) {
			return (float) x;
		}

		float fy(int y) {
			return (float) (maxY - y);
		}

		getTetris3() {
			// add the MouseListener to get the function of the QUIT function
			setPanel();
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent event) {
					mouse1x = event.getX();
					mouse1y = event.getY();
					if ((mouse1x < iX(xK + square * 4)) && (mouse1x > iX(xK))
							&& (mouse1y < iY(yK - square * (float) 1.5)) && (mouse1y > iY(yK))) {
						System.exit(0);
					}
				}

				public void mouseClicked(MouseEvent event) {
					if (event.getButton() == MouseEvent.BUTTON1 && show == false) {
						checkValid(n1, xDraw - square, yDraw, rotate);
						if (!newcheckCoincidence(nextCoord, prevs) && checkInside()) {
							leftMove++;
							repaint();
						}
					} else if (event.getButton() == MouseEvent.BUTTON3 && show == false) {
						checkValid(n1, xDraw + square, yDraw, rotate);
						if (!newcheckCoincidence(nextCoord, prevs) && checkInside()) {
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
						if (!newcheckCoincidence(nextCoord, prevs) && checkInside()) {
							rotate = (rotate + 1) % 4;
							repaint();
						}
					} else if (event.getWheelRotation() > 0 && !show) {
						checkValid(n1, xDraw, yDraw, (rotate - 1) % 4);
						if (!newcheckCoincidence(nextCoord, prevs) && checkInside()) {
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

					Point2D p = new Point2D(fx(mouse2x), fy(mouse2y));

					if ((mouse2x < iX(xA + square * width)) && (mouse2x > iX(xA))
							&& (mouse2y < iY(yA - square * height)) && (mouse2y > iY(yA))) {
						show = true;
						boolean inside = false;
						for (int[] coord : nextCoord) {
							int X = coord[0];
							int Y = coord[1];
							fillPoly(X, Y);
							inside = inside || insidePolygon(p, pol);
						}
						if (!inside) {
							stillIn = false;
						} else if (inside && !stillIn) {
							int n3 = rand.nextInt(8);
							rotate = 0;
							checkValid(n3, xDraw, yDraw - square, rotate);
							while (n3 == n1 || n3 == n2 || newcheckCoincidence(nextCoord, prevs) || !checkInside()) {
								n3 = rand.nextInt(8);
								checkValid(n3, xDraw, yDraw - square, rotate);
							}
							n1 = n3;
							minus += M * level;
							stillIn = true;
						}
						repaint();
					} else {
						show = false;
						repaint();
					}
				}
			});
		}

		boolean insidePolygon(Point2D p, Point2D[] pol) {
			int n = pol.length, j = n - 1;
			boolean b = false;
			float x = p.x, y = p.y;
			for (int i = 0; i < n; i++) {
				if (pol[j].y <= y && y < pol[i].y && Tools2D.area2(pol[j], pol[i], p) > 0
						|| pol[i].y <= y && y < pol[j].y && Tools2D.area2(pol[i], pol[j], p) > 0)
					b = !b;
				j = i;
			}
			return b;
		}

		public void paint(Graphics g) {
			timer.start();
			initgr();
			prevs = new HashSet<>();
			square = (float) (minMaxXY / (40 - size)); // customarily set the length to be the minMaxXY /
														// 30
			removedRows = 0;
			level = 1;
			score = 0;
			// coordinates for the main area
			xA = xCenter - square * (width - 5);
			yA = yCenter + square * (height - 10);

			// coordinates for the upper right small rectangle
			xB = (float) xCenter + square * 6;
			yB = yA;

			// coordinates for the pause button box
			xC = xA + (float) (width / 3) * square;
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

			// coordinates for the draw place
			xDraw = xCenter - leftMove * square;
			yDraw = yCenter + (9 - count) * square;

			// draw all the necessary rectangles
			g.drawRect(iX(xA), iY(yA), (int) (square * width), (int) (square * height));
			g.drawRect(iX(xB), iY(yB), (int) (square * 5), (int) (square * 3));
			g.drawRect(iX(xK), iY(yK), (int) (square * 4), (int) (square * (float) 1.5));

			// draw the previous staying shapes
			for (int[] info : prevDraws) {
				int n = info[0];
				int oldCount = info[1];
				int oldLeftMove = info[2];
				int oldRotate = info[3];
				checkValid(n, xCenter - oldLeftMove * square, yCenter + (9 - oldCount) * square, oldRotate);
				newLeaveMarks(n);
				checkErase();
			}
			for (int i = 1; i <= removedRows; i++) {
				score += level * M;
				if (i % N == 0) {
					level++;
				}
			}

			score -= minus;
			if (level > previousLevel) {
				delay /= (1 + level * S);
				previousLevel = level;
			}
			timer.setDelay((int) delay);
			for (Square square : prevs) {
				drawSquare(g, square);
			}

			// draw the shape at the next box
			drawStableShapes(g, n2);
			// draw the current falling shape
			drawMoveShapes(g, n1, xDraw, yDraw, rotate);

			// draw the strings
			Font f = new Font("Dialog", Font.BOLD, (int) square);
			g.setFont(f);
			g.drawString("Level:      " + String.valueOf(level), iX(xH), iY(yH));
			g.drawString("Lines:      " + String.valueOf(removedRows), iX(xI), iY(yI));
			g.drawString("QUIT", iX(xL), iY(yL));
			f = new Font("Dialog", Font.BOLD, (int) (square));
			g.setFont(f);
			g.drawString("Score:      " + String.valueOf(score), iX(xJ), iY(yJ));

			// draw the pause button, determined by the value of show
			if (show) {
				g.setColor(Color.BLUE);
				g.drawRect(iX(xC), iY(yC), (int) (square * 5), (int) (square * 2));
				f = new Font("Dialog", Font.BOLD, (int) (square));
				g.setColor(Color.BLUE);
				g.drawString("PAUSE", iX(xM), iY(yM));
			}
		}

		// function to get the coordinates and boundary values of the next possible move
		// of the shapes
		// the data are stored in the global array and variables

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
			} else if (n1 == 6) {
				nextCyanBar(iX(xDraw - 2 * square), iY(yDraw + square), (int) square, rotate);
			} else {
				nextGrayTri(iX(xDraw - square), iY(yDraw), (int) square, rotate);
			}
		}

		// function to store the coordinates of previous fixed drawn squares

		void newLeaveMarks(int n) {
			for (int[] vertex : nextCoord) {
				prevs.add(new Square(vertex[0], vertex[1], getColor(n)));
			}
		}

		void checkErase() {
			Set<Integer> Ys = new HashSet<>();
			Map<Integer, Integer> map = new HashMap<>();
			if (!prevs.isEmpty()) {
				for (Square square : prevs) {
					map.put(square.Y, map.getOrDefault(square.Y, 0) + 1);
				}
				for (int Y : map.keySet()) {
					if (map.get(Y) >= width) {
						Ys.add(Y);
						removedRows++;
					}
				}
				Set<Square> newSet = new HashSet<>();
				for (Square cube : prevs) {
					if (Ys.contains(cube.Y)) {
						continue;
					} else {
						int replaceY = cube.Y;
						for (int Y : Ys) {
							if (cube.Y < Y) {
								replaceY += (int) square;
							}
						}
						newSet.add(new Square(cube.X, replaceY, cube.color));
					}
				}
				prevs = newSet;
			}

		}

		// function to draw the current moving shape
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
			} else if (n1 == 6) {
				drawCyanBar(g, iX(xDraw - 2 * square), iY(yDraw + square), (int) square, rotate);
			} else {
				drawGrayTri(g, iX(xDraw - square), iY(yDraw), (int) square, rotate);
			}

		}

		// function to draw the next shape in the fixed next shape box
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
			} else if (n2 == 6) {
				drawCyanBar(g, iX(xB + 0.5F * square), iY(yB - square), (int) square, 0);
			} else {
				drawGrayTri(g, iX(xB + 1.5F * square), iY(yB - 1.5F * square), (int) square, 0);
			}
		}

		// function to detect if the collision occurs

		boolean newcheckCoincidence(List<int[]> vertices, Set<Square> prevs) {
			boolean coincidence = false;
			for (int[] coord : vertices) {
				for (Square prev : prevs) {
					if (coord[0] == prev.X && coord[1] == prev.Y) {
						coincidence = true;
						break;
					}
				}
			}
			return coincidence;
		}

		// function to check whether the boundary values are inside the mainarea box
		boolean checkInside() {
			return (xMin >= xCenter - (width - 5) * (int) square && xMax <= xCenter + 4 * (int) square
					&& yMin >= yCenter - (int) square * (height - 10) && yMax <= yCenter + (int) square * 9);
		}

		Color getColor(int n) {
			if (n == 0) {
				return Color.YELLOW;
			} else if (n == 1) {
				return Color.MAGENTA;
			} else if (n == 2) {
				return Color.BLUE;
			} else if (n == 3) {
				return Color.RED;
			} else if (n == 4) {
				return Color.GREEN;
			} else if (n == 5) {
				return Color.ORANGE;
			} else if (n == 6) {
				return Color.CYAN;
			} else {
				return Color.GRAY;
			}
		}

		void fillPoly(int X, int Y) {
			int[][] points = new int[4][2];
			points[0] = new int[] { X, Y };
			points[1] = new int[] { X + (int) square, Y };
			points[2] = new int[] { X + (int) square, Y + (int) square };
			points[3] = new int[] { X, Y + (int) square };
			int i = 0;
			for (int[] point : points) {
				pol[i] = new Point2D(fx(point[0]), fy(point[1]));
				i++;
			}
		}

		// functions to draw all the shapes
		void drawYellowWedge(Graphics g, int x, int y, int square, int rotate) {
			nextYellowWedge(x, y, square, rotate);
			for (int[] coord : nextCoord) {
				int X = coord[0];
				int Y = coord[1];
				g.setColor(Color.YELLOW);
				g.fillRect(X, Y, square, square);
				g.setColor(Color.black);
				g.drawRect(X, Y, square, square);
			}
		}

		void drawPurpleReverseWedge(Graphics g, int x, int y, int square, int rotate) {
			nextPurpleReverseWedge(x, y, square, rotate);
			for (int[] coord : nextCoord) {
				int X = coord[0];
				int Y = coord[1];
				g.setColor(Color.MAGENTA);
				g.fillRect(X, Y, square, square);
				g.setColor(Color.black);
				g.drawRect(X, Y, square, square);
			}
		}

		void drawBlueReverseL(Graphics g, int x, int y, int square, int rotate) {
			nextBlueReverseL(x, y, square, rotate);
			for (int[] coord : nextCoord) {
				int X = coord[0];
				int Y = coord[1];
				g.setColor(Color.BLUE);
				g.fillRect(X, Y, square, square);
				g.setColor(Color.black);
				g.drawRect(X, Y, square, square);
			}
		}

		void drawRedReverseL(Graphics g, int x, int y, int square, int rotate) {
			nextRedReverseL(x, y, square, rotate);
			for (int[] coord : nextCoord) {
				int X = coord[0];
				int Y = coord[1];
				g.setColor(Color.RED);
				g.fillRect(X, Y, square, square);
				g.setColor(Color.black);
				g.drawRect(X, Y, square, square);
			}
		}

		void drawGreenCube(Graphics g, int x, int y, int square, int rotate) {
			nextGreenCube(x, y, square, rotate);
			for (int[] coord : nextCoord) {
				int X = coord[0];
				int Y = coord[1];
				g.setColor(Color.GREEN);
				g.fillRect(X, Y, square, square);
				g.setColor(Color.black);
				g.drawRect(X, Y, square, square);

			}
		}

		void drawOrangeHill(Graphics g, int x, int y, int square, int rotate) {
			nextOrangeHill(x, y, square, rotate);
			for (int[] coord : nextCoord) {
				int X = coord[0];
				int Y = coord[1];
				g.setColor(Color.ORANGE);
				g.fillRect(X, Y, square, square);
				g.setColor(Color.black);
				g.drawRect(X, Y, square, square);
			}
		}

		void drawCyanBar(Graphics g, int x, int y, int square, int rotate) {
			nextCyanBar(x, y, square, rotate);
			for (int[] coord : nextCoord) {
				int X = coord[0];
				int Y = coord[1];
				g.setColor(Color.CYAN);
				g.fillRect(X, Y, square, square);
				g.setColor(Color.black);
				g.drawRect(X, Y, square, square);

			}
		}

		void drawGrayTri(Graphics g, int x, int y, int square, int rotate) {
			nextGrayTri(x, y, square, rotate);
			for (int[] coord : nextCoord) {
				int X = coord[0];
				int Y = coord[1];
				g.setColor(Color.GRAY);
				g.fillRect(X, Y, square, square);
				g.setColor(Color.black);
				g.drawRect(X, Y, square, square);
			}
		}

		void drawSquare(Graphics g, Square cube) {
			g.setColor(cube.color);
			g.fillRect(cube.X, cube.Y, (int) square, (int) square);
			g.setColor(Color.black);
			g.drawRect(cube.X, cube.Y, (int) square, (int) square);
		}

		// functions to get the coordinates and boundary values of the next move of the
		// shapes
		void nextYellowWedge(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0 || rotate == 2 || rotate == -2) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x + 2 * square, y - square });
				xMin = x;
				xMax = x + 2 * square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 1 || rotate == 3 || rotate == -3 || rotate == -1) {
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x, y - 2 * square });
				xMin = x;
				xMax = x + square;
				yMin = y - 2 * square;
				yMax = y;
			}
		}

		void nextPurpleReverseWedge(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0 || rotate == 2 || rotate == -2) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x - square, y - square });
				xMin = x - square;
				xMax = x + square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 1 || rotate == 3 || rotate == -3 || rotate == -1) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x + square, y - 2 * square });
				xMin = x;
				xMax = x + square;
				yMin = y - 2 * square;
				yMax = y;
			}
		}

		void nextBlueReverseL(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + 2 * square, y });
				xMin = x;
				xMax = x + 2 * square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x, y - 2 * square });
				nextCoord.add(new int[] { x + square, y - 2 * square });
				xMin = x;
				xMax = x + square;
				yMin = y - 2 * square;
				yMax = y;
			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new int[] { x + 2 * square, y });
				nextCoord.add(new int[] { x + 2 * square, y - square });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x, y - square });
				xMin = x;
				xMax = x + 2 * square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 3 || rotate == -1) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x + square, y - 2 * square });
				xMin = x;
				xMax = x + square;
				yMin = y - 2 * square;
				yMax = y;
			}
		}

		void nextRedReverseL(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + 2 * square, y });
				nextCoord.add(new int[] { x + 2 * square, y - square });
				xMin = x;
				xMax = x + 2 * square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x, y - 2 * square });
				xMin = x;
				xMax = x + square;
				yMin = y - 2 * square;
				yMax = y;
			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x + 2 * square, y - square });
				xMin = x;
				xMax = x + 2 * square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 3 || rotate == -1) {
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x + square, y - 2 * square });
				nextCoord.add(new int[] { x, y - 2 * square });
				xMin = x;
				xMax = x + square;
				yMin = y - 2 * square;
				yMax = y;
			}
		}

		void nextGreenCube(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			nextCoord.add(new int[] { x, y });
			nextCoord.add(new int[] { x + square, y });
			nextCoord.add(new int[] { x + square, y - square });
			nextCoord.add(new int[] { x, y - square });
			xMin = x;
			xMax = x + square;
			yMin = y - square;
			yMax = y;
		}

		void nextOrangeHill(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x + 2 * square, y });
				xMin = x;
				xMax = x + 2 * square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x, y - 2 * square });
				xMin = x;
				xMax = x + square;
				yMin = y - 2 * square;
				yMax = y;
			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x + 2 * square, y - square });
				xMin = x;
				xMax = x + 2 * square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 3 || rotate == -1) {
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x + square, y - 2 * square });
				xMin = x;
				xMax = x + square;
				yMin = y - 2 * square;
				yMax = y;
			}
		}

		void nextCyanBar(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0 || rotate == 2 || rotate == -2) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + 2 * square, y });
				nextCoord.add(new int[] { x + 3 * square, y });
				xMin = x;
				xMax = x + 3 * square;
				yMin = y;
				yMax = y;
			} else if (rotate == 1 || rotate == 3 || rotate == -1 || rotate == -3) {
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y + square });
				nextCoord.add(new int[] { x + square, y + 2 * square });
				nextCoord.add(new int[] { x + square, y + 3 * square });
				xMin = x + square;
				xMax = x + square;
				yMin = y;
				yMax = y + 3 * square;
			}
		}

		void nextGrayTri(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y - square });
				xMin = x;
				xMax = x + square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x, y - square });
				xMin = x;
				xMax = x + square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new int[] { x, y });
				nextCoord.add(new int[] { x, y - square });
				nextCoord.add(new int[] { x + square, y - square });
				xMin = x;
				xMax = x + square;
				yMin = y - square;
				yMax = y;
			} else if (rotate == 3 || rotate == -1) {
				nextCoord.add(new int[] { x + square, y });
				nextCoord.add(new int[] { x + square, y - square });
				nextCoord.add(new int[] { x, y - square });
				xMin = x;
				xMax = x + square;
				yMin = y - square;
				yMax = y;
			}
		}
	}

}
