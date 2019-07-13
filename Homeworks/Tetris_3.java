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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Tetris.Tetris_3.getTetris3;

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

	JButton startButton;
	Panel panel;

	int fixedSF = 1, fixedLC = 20, fixedSpeed = 1, fixedHF = 20, fixedWF = 10, fixedSize = 10;

	boolean start = false;
	getTetris3 tetris;

	Set<Integer> pool = new HashSet<>();
	List<Integer> selectPool = new ArrayList<>();

	public void setPanel() {
		startButton = new JButton("Start the Game After Setup");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				start = true;
				tetris.repaint();
			}
		});

		scoringFactor = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		scoringFactor.setMajorTickSpacing(1);
		scoringFactor.setPaintTicks(true);
		sfLabel = new JLabel("Scoring Factor: 1");
		scoringFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (start) {
					scoringFactor.setValue(fixedSF);
				} else {
					sfLabel.setText(String.valueOf("Scoring Factor: " + scoringFactor.getValue()));
					fixedSF = scoringFactor.getValue();
					tetris.repaint();
				}

			}
		});

		levelClimber = new JSlider(JSlider.HORIZONTAL, 10, 50, 20);
		levelClimber.setMajorTickSpacing(5);
		levelClimber.setPaintTicks(true);
		lcLabel = new JLabel("Level-up Required Lines: 20");
		levelClimber.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (start) {
					levelClimber.setValue(fixedLC);
				} else {
					lcLabel.setText(String.valueOf("Level-up Required Lines: " + levelClimber.getValue()));
					fixedLC = levelClimber.getValue();
					tetris.repaint();
				}

			}
		});

		speedFactor = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		speedFactor.setMajorTickSpacing(1);
		speedFactor.setPaintTicks(true);
		spLabel = new JLabel("Speed Factor: 0.1");
		speedFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (start) {
					speedFactor.setValue(fixedSpeed);
				} else {
					spLabel.setText(String.valueOf("Speed Factor: " + ((float) speedFactor.getValue() / 10)));
					fixedSpeed = speedFactor.getValue();
					tetris.repaint();
				}

			}
		});

		heightFactor = new JSlider(JSlider.HORIZONTAL, 15, 25, 20);
		heightFactor.setMajorTickSpacing(1);
		heightFactor.setPaintTicks(true);
		heightLabel = new JLabel("Current Hight: 20 squares");
		heightFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (start) {
					heightFactor.setValue(fixedHF);
				} else {
					heightLabel.setText(String.valueOf("Current Hight: " + heightFactor.getValue() + " squares"));
					fixedHF = heightFactor.getValue();
					tetris.repaint();
				}

			}
		});
		widthFactor = new JSlider(JSlider.HORIZONTAL, 5, 20, 10);
		widthFactor.setMajorTickSpacing(1);
		widthFactor.setPaintTicks(true);
		widthLabel = new JLabel("Current width: 10 squares");
		widthFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (start) {
					widthFactor.setValue(fixedWF);
				} else {
					widthLabel.setText(String.valueOf("Current width: " + widthFactor.getValue() + " squares"));
					fixedWF = widthFactor.getValue();
					tetris.repaint();
				}

			}
		});

		sizeFactor = new JSlider(JSlider.HORIZONTAL, 10, 14, 10);
		sizeFactor.setMajorTickSpacing(1);
		sizeFactor.setPaintTicks(true);
		sizeLabel = new JLabel("Current size: 1X");
		sizeFactor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (start) {
					sizeFactor.setValue(fixedSize);
				}
				sizeLabel.setText(String.valueOf("Current size: " + (float) sizeFactor.getValue() / 10 + "X"));
				fixedSize = sizeFactor.getValue();
				tetris.repaint();
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
		panel.add(new JLabel("Select the additional shape(s)"));
		Panel holder = new Panel();
		for (int i = 1; i < 9; i++) {
			JCheckBox x = new JCheckBox(String.valueOf(i));
			int value = i;
			x.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (!start) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							pool.add(value + 6);
						} else {
							pool.remove(value + 6);
						}
						selectPool = new ArrayList<>();
						for (int j : pool) {
							selectPool.add(j);
						}
					}
				}
			});

			holder.add(x);
		}
		holder.setMaximumSize(new Dimension(500, 20));
		holder.setLayout(new BoxLayout(holder, BoxLayout.LINE_AXIS));
		panel.add(holder);
		panel.add(startButton);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
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
		for (int i = 0; i < 7; i++) {
			pool.add(i);
			selectPool.add(i);
		}
		tetris = new getTetris3();
		setPanel();
		add(tetris, BorderLayout.CENTER);
		add(panel, BorderLayout.WEST);
		setSize(800, 600);
		setVisible(true);
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
		int n1 = selectPool.get(rand.nextInt(selectPool.size()));
		int n2 = selectPool.get(rand.nextInt(selectPool.size()));
		float delay = 700F, prevDelay = 700F;
		List<Square> nextCoord = new ArrayList<>();
		int score = 0, minus = 0, M, N;
		int level = 1, previousLevel = 1;
		int removedRows = 0, prevRemovedRows = 0;
		float S;

		Timer timer = new Timer((int) delay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				checkValid(n1, xDraw, yDraw - square, rotate);
				boolean coincidence = newcheckCoincidence(nextCoord, prevs);
				if (!coincidence && newcheckInside() && !show) {
					count += 1;
					repaint();
				} else if ((coincidence || !newcheckInside()) && !show) {
					prevDraws.add(new int[] { n1, count, leftMove, rotate });
					n1 = n2;
					n2 = selectPool.get(rand.nextInt(selectPool.size()));
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

			if (!start) {
				M = scoringFactor.getValue();
				N = levelClimber.getValue();
				S = ((float) speedFactor.getValue() / 10);
				width = widthFactor.getValue();
				height = heightFactor.getValue();
				size = sizeFactor.getValue();
			}
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
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					if (event.getButton() == MouseEvent.BUTTON1 && show == false && start) {
						checkValid(n1, xDraw - square, yDraw, rotate);
						if (!newcheckCoincidence(nextCoord, prevs) && newcheckInside()) {
							leftMove++;
							repaint();
						}
					} else if (event.getButton() == MouseEvent.BUTTON3 && show == false && start) {
						checkValid(n1, xDraw + square, yDraw, rotate);
						if (!newcheckCoincidence(nextCoord, prevs) && newcheckInside()) {
							leftMove--;
							repaint();
						}
					}
				}

				public void mousePressed(MouseEvent event) {
					mouse1x = event.getX();
					mouse1y = event.getY();
					if ((mouse1x < iX(xK + square * 4)) && (mouse1x > iX(xK))
							&& (mouse1y < iY(yK - square * (float) 1.5)) && (mouse1y > iY(yK))) {
						System.exit(0);
					} else if (event.getButton() == MouseEvent.BUTTON2 && show == false) {
						prevDelay = timer.getDelay();
						timer.setDelay(50);
					}
				}

				public void mouseReleased(MouseEvent event) {
					timer.setDelay((int) prevDelay);
				}
			});

			addMouseWheelListener(new MouseAdapter() {
				public void mouseWheelMoved(MouseWheelEvent event) {
					if (event.getWheelRotation() < 0 && !show) {
						checkValid(n1, xDraw, yDraw, (rotate + 1) % 4);
						if (!newcheckCoincidence(nextCoord, prevs) && newcheckInside() && start) {
							rotate = (rotate + 1) % 4;
							repaint();
						}
					} else if (event.getWheelRotation() > 0 && !show) {
						checkValid(n1, xDraw, yDraw, (rotate - 1) % 4);
						if (!newcheckCoincidence(nextCoord, prevs) && newcheckInside() && start) {
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
						for (Square coord : nextCoord) {
							int X = coord.X;
							int Y = coord.Y;
							fillPoly(X, Y);
							inside = inside || insidePolygon(p, pol);
						}
						if (!inside) {
							stillIn = false;
						} else if (inside && !stillIn && start) {
							int n3 = selectPool.get(rand.nextInt(selectPool.size()));
							rotate = 0;
							checkValid(n3, xDraw, yDraw - square, rotate);
							while (n3 == n1 || n3 == n2 || newcheckCoincidence(nextCoord, prevs) || !newcheckInside()) {
								n3 = selectPool.get(rand.nextInt(selectPool.size()));
								checkValid(n3, xDraw, yDraw - square, rotate);
							}
							n1 = n3;
							score -= M * level;
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
			float y = p.y;
			for (int i = 0; i < n; i++) {
				if (pol[j].y <= y && y < pol[i].y && Tools2D.area2(pol[j], pol[i], p) > 0
						|| pol[i].y <= y && y < pol[j].y && Tools2D.area2(pol[i], pol[j], p) > 0)
					b = !b;
				j = i;
			}
			return b;
		}

		public void paint(Graphics g) {
			if (start) {
				timer.start();
			}
			initgr();
			prevs = new HashSet<>();
			square = (float) (minMaxXY / (40 - size)); // customarily set the length to be the minMaxXY /
														// 30
			removedRows = 0;
			level = 1;
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

			// coordinates for the draw place
			xDraw = xCenter - ((width - 10) / 2 + leftMove) * square;
			yDraw = yCenter + (height - 11 - count) * square;

			// draw all the necessary rectangles
			g.drawRect(iX(xA), iY(yA), (int) (square * width), (int) (square * height));
			g.drawRect(iX(xB), iY(yB), (int) (square * 5), (int) (square * 3));
			g.drawRect(iX(xK), iY(yK), (int) (square * 4), (int) (square * (float) 1.5));

			Font f = new Font("Dialog", Font.BOLD, (int) square);
			g.setFont(f);
			float x = square, y = yCenter - 11 * square;
			for (int i = 1; i < 9; i++) {
				g.drawString(String.valueOf(i), iX(x), iY(y));
				drawMoveShapes(g, i + 6, x, y - 2 * square, 0);
				if (i == 2 || i == 3) {
					x += 4 * square;
				} else if (i == 4) {
					x += 2 * square;
				} else {
					x += 3 * square;
				}

			}

			// draw the previous staying shapes
			for (int[] info : prevDraws) {
				int n = info[0];
				int oldCount = info[1];
				int oldLeftMove = info[2];
				int oldRotate = info[3];
				checkValid(n, xCenter - ((width - 10) / 2 + oldLeftMove) * square,
						yCenter + (height - 11 - oldCount) * square, oldRotate);
				storePrevSquares();
				checkErase();
			}

			for (int i = 1; i <= removedRows; i++) {
				if (i > prevRemovedRows) {
					score += level * M;
					prevRemovedRows = i;
				}
				if (i % N == 0) {
					level++;
				}
			}
			if (level > previousLevel) {
				delay /= (1 + level * S);
				previousLevel = level;
				prevDelay = delay;
				timer.setDelay((int) delay);
			}

			for (Square square : prevs) {
				drawSquare(g, square);
			}

			// draw the shape at the next box
			drawStableShapes(g, n2);
			// draw the current falling shape
			drawMoveShapes(g, n1, xDraw, yDraw, rotate);

			// draw the strings
			g.setFont(f);
			g.drawString("Level:      " + String.valueOf(level), iX(xH), iY(yH));
			g.drawString("Lines:      " + String.valueOf(removedRows), iX(xI), iY(yI));
			g.drawString("QUIT", iX(xL), iY(yL));

			g.setFont(f);
			g.drawString("Score:      " + String.valueOf(score), iX(xJ), iY(yJ));
			g.drawString("All Changes Will Be IGNORED After Game Starts!!!", 0, iY((yA + square)));
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
			} else if (n1 == 7) {
				nextGrayTri(iX(xDraw - square), iY(yDraw), (int) square, rotate);
			} else if (n1 == 8) {
				nextGrassTri(iX(xDraw - square), iY(yDraw), (int) square, rotate);
			} else if (n1 == 9) {
				nextPinkTri(iX(xDraw - square), iY(yDraw + square), (int) square, rotate);
			} else if (n1 == 10) {
				nextOyellowBi(iX(xDraw - square), iY(yDraw), (int) square, rotate);
			} else if (n1 == 11) {
				nextLightGrayBi(iX(xDraw), iY(yDraw), (int) square, rotate);
			} else if (n1 == 12) {
				nextCoolGrayTri(iX(xDraw - square), iY(yDraw), (int) square, rotate);
			} else if (n1 == 13) {
				nextDeepSeaBlueSingular(iX(xDraw), iY(yDraw + square), (int) square, rotate);
			} else {
				nextWarmBlueTri(iX(xDraw - square), iY(yDraw), (int) square, rotate);
			}
		}

		public void drawStableShapes(Graphics g, int n2) {
			if (n2 == 0) {
				nextYellowWedge(iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 1) {
				nextPurpleReverseWedge(iX(xB + 2F * square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 2) {
				nextBlueReverseL(iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 3) {
				nextRedReverseL(iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 4) {
				nextGreenCube(iX(xB + 1.5F * square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 5) {
				nextOrangeHill(iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 6) {
				nextCyanBar(iX(xB + 0.5F * square), iY(yB - square), (int) square, 0);
			} else if (n2 == 7) {
				nextGrayTri(iX(xB + 1.5F * square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 8) {
				nextGrassTri(iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 9) {
				nextPinkTri(iX(xB + square), iY(yB - square), (int) square, 0);
			} else if (n2 == 10) {
				nextOyellowBi(iX(xB + 1.5F * square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 11) {
				nextLightGrayBi(iX(xB + 2 * square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 12) {
				nextCoolGrayTri(iX(xB + square), iY(yB - 1.5F * square), (int) square, 0);
			} else if (n2 == 13) {
				nextDeepSeaBlueSingular(iX(xB + 2 * square), iY(yB - square), (int) square, 0);
			} else {
				nextWarmBlueTri(iX(xB + square), iY(yB - square), (int) square, 0);
			}
			for (Square i : nextCoord) {
				drawSquare(g, i);
			}
		}

		// function to draw the current moving shape
		void drawMoveShapes(Graphics g, int n1, float xDraw, float yDraw, int rotate) {
			checkValid(n1, xDraw, yDraw, rotate);
			for (Square i : nextCoord) {
				drawSquare(g, i);
			}
		}

		// function to draw the next shape in the fixed next shape box

		// function to store the coordinates of previous fixed drawn squares

		void storePrevSquares() {
			int min = Integer.MAX_VALUE;
			for (Square vertex : nextCoord) {
				prevs.add(new Square(vertex.X, vertex.Y, vertex.color));
				min = Math.min(min, vertex.Y);
			}
			if (min <= yCenter - (int) square * (height - 10)) {
				System.exit(0);
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

		// function to detect if the collision occurs

		boolean newcheckCoincidence(List<Square> vertices, Set<Square> prevs) {
			boolean coincidence = false;
			for (Square cur : vertices) {
				for (Square prev : prevs) {
					if (cur.X == prev.X && cur.Y == prev.Y) {
						coincidence = true;
						break;
					}
				}
			}
			return coincidence;
		}

		// function to check whether the boundary values are inside the mainarea box
		boolean newcheckInside() {
			boolean isInside = true;
			for (Square i : nextCoord) {
				int x = i.X;
				int y = i.Y;
				isInside = isInside && (x >= xCenter - (width - 5) * (int) square
						&& x + (int) square <= xCenter + 5 * (int) square && y >= yCenter - (int) square * (height - 10)
						&& y + (int) square <= yCenter + (int) square * 10);
			}
			return isInside;
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
				nextCoord.add(new Square(x, y, Color.YELLOW));
				nextCoord.add(new Square(x + square, y, Color.YELLOW));
				nextCoord.add(new Square(x + square, y - square, Color.YELLOW));
				nextCoord.add(new Square(x + 2 * square, y - square, Color.YELLOW));
			} else {
				nextCoord.add(new Square(x + square, y, Color.YELLOW));
				nextCoord.add(new Square(x + square, y - square, Color.YELLOW));
				nextCoord.add(new Square(x, y - square, Color.YELLOW));
				nextCoord.add(new Square(x, y - 2 * square, Color.YELLOW));

			}
		}

		void nextPurpleReverseWedge(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0 || rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x, y, Color.MAGENTA));
				nextCoord.add(new Square(x + square, y, Color.MAGENTA));
				nextCoord.add(new Square(x, y - square, Color.MAGENTA));
				nextCoord.add(new Square(x - square, y - square, Color.MAGENTA));

			} else {
				nextCoord.add(new Square(x, y, Color.MAGENTA));
				nextCoord.add(new Square(x, y - square, Color.MAGENTA));
				nextCoord.add(new Square(x + square, y - square, Color.MAGENTA));
				nextCoord.add(new Square(x + square, y - 2 * square, Color.MAGENTA));

			}
		}

		void nextBlueReverseL(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new Square(x, y - square, Color.BLUE));
				nextCoord.add(new Square(x, y, Color.BLUE));
				nextCoord.add(new Square(x + square, y, Color.BLUE));
				nextCoord.add(new Square(x + 2 * square, y, Color.BLUE));

			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new Square(x, y, Color.BLUE));
				nextCoord.add(new Square(x, y - square, Color.BLUE));
				nextCoord.add(new Square(x, y - 2 * square, Color.BLUE));
				nextCoord.add(new Square(x + square, y - 2 * square, Color.BLUE));

			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x + 2 * square, y, Color.BLUE));
				nextCoord.add(new Square(x + 2 * square, y - square, Color.BLUE));
				nextCoord.add(new Square(x + square, y - square, Color.BLUE));
				nextCoord.add(new Square(x, y - square, Color.BLUE));

			} else {
				nextCoord.add(new Square(x, y, Color.BLUE));
				nextCoord.add(new Square(x + square, y, Color.BLUE));
				nextCoord.add(new Square(x + square, y - square, Color.BLUE));
				nextCoord.add(new Square(x + square, y - 2 * square, Color.BLUE));

			}
		}

		void nextRedReverseL(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new Square(x, y, Color.RED));
				nextCoord.add(new Square(x + square, y, Color.RED));
				nextCoord.add(new Square(x + 2 * square, y, Color.RED));
				nextCoord.add(new Square(x + 2 * square, y - square, Color.RED));

			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new Square(x, y, Color.RED));
				nextCoord.add(new Square(x + square, y, Color.RED));
				nextCoord.add(new Square(x, y - square, Color.RED));
				nextCoord.add(new Square(x, y - 2 * square, Color.RED));

			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x, y, Color.RED));
				nextCoord.add(new Square(x, y - square, Color.RED));
				nextCoord.add(new Square(x + square, y - square, Color.RED));
				nextCoord.add(new Square(x + 2 * square, y - square, Color.RED));

			} else {
				nextCoord.add(new Square(x + square, y, Color.RED));
				nextCoord.add(new Square(x + square, y - square, Color.RED));
				nextCoord.add(new Square(x + square, y - 2 * square, Color.RED));
				nextCoord.add(new Square(x, y - 2 * square, Color.RED));

			}
		}

		void nextGreenCube(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			nextCoord.add(new Square(x, y, Color.GREEN));
			nextCoord.add(new Square(x + square, y, Color.GREEN));
			nextCoord.add(new Square(x + square, y - square, Color.GREEN));
			nextCoord.add(new Square(x, y - square, Color.GREEN));

		}

		void nextOrangeHill(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new Square(x, y, Color.ORANGE));
				nextCoord.add(new Square(x + square, y, Color.ORANGE));
				nextCoord.add(new Square(x + square, y - square, Color.ORANGE));
				nextCoord.add(new Square(x + 2 * square, y, Color.ORANGE));

			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new Square(x, y, Color.ORANGE));
				nextCoord.add(new Square(x, y - square, Color.ORANGE));
				nextCoord.add(new Square(x + square, y - square, Color.ORANGE));
				nextCoord.add(new Square(x, y - 2 * square, Color.ORANGE));

			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x + square, y, Color.ORANGE));
				nextCoord.add(new Square(x + square, y - square, Color.ORANGE));
				nextCoord.add(new Square(x, y - square, Color.ORANGE));
				nextCoord.add(new Square(x + 2 * square, y - square, Color.ORANGE));

			} else {
				nextCoord.add(new Square(x + square, y, Color.ORANGE));
				nextCoord.add(new Square(x + square, y - square, Color.ORANGE));
				nextCoord.add(new Square(x, y - square, Color.ORANGE));
				nextCoord.add(new Square(x + square, y - 2 * square, Color.ORANGE));

			}
		}

		void nextCyanBar(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0 || rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x, y, Color.CYAN));
				nextCoord.add(new Square(x + square, y, Color.CYAN));
				nextCoord.add(new Square(x + 2 * square, y, Color.CYAN));
				nextCoord.add(new Square(x + 3 * square, y, Color.CYAN));

			} else {
				nextCoord.add(new Square(x + square, y, Color.CYAN));
				nextCoord.add(new Square(x + square, y + square, Color.CYAN));
				nextCoord.add(new Square(x + square, y + 2 * square, Color.CYAN));
				nextCoord.add(new Square(x + square, y + 3 * square, Color.CYAN));

			}
		}

		public void nextGrayTri(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new Square(x, y, Color.GRAY));
				nextCoord.add(new Square(x + square, y, Color.GRAY));
				nextCoord.add(new Square(x + square, y - square, Color.GRAY));

			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new Square(x, y, Color.GRAY));
				nextCoord.add(new Square(x + square, y, Color.GRAY));
				nextCoord.add(new Square(x, y - square, Color.GRAY));

			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x, y, Color.GRAY));
				nextCoord.add(new Square(x, y - square, Color.GRAY));
				nextCoord.add(new Square(x + square, y - square, Color.GRAY));

			} else {
				nextCoord.add(new Square(x + square, y, Color.GRAY));
				nextCoord.add(new Square(x + square, y - square, Color.GRAY));
				nextCoord.add(new Square(x, y - square, Color.GRAY));

			}
		}

		public void nextGrassTri(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new Square(x + square, y, new Color(0xb7, 0xe1, 0xa1)));
				nextCoord.add(new Square(x + 2 * square, y, new Color(0xb7, 0xe1, 0xa1)));
				nextCoord.add(new Square(x, y - square, new Color(0xb7, 0xe1, 0xa1)));

			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new Square(x, y, new Color(0xb7, 0xe1, 0xa1)));
				nextCoord.add(new Square(x, y + square, new Color(0xb7, 0xe1, 0xa1)));
				nextCoord.add(new Square(x + square, y - square, new Color(0xb7, 0xe1, 0xa1)));

			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x + 2 * square, y, new Color(0xb7, 0xe1, 0xa1)));
				nextCoord.add(new Square(x, y - square, new Color(0xb7, 0xe1, 0xa1)));
				nextCoord.add(new Square(x + square, y - square, new Color(0xb7, 0xe1, 0xa1)));

			} else {
				nextCoord.add(new Square(x, y + square, new Color(0xb7, 0xe1, 0xa1)));
				nextCoord.add(new Square(x + square, y, new Color(0xb7, 0xe1, 0xa1)));
				nextCoord.add(new Square(x + square, y - square, new Color(0xb7, 0xe1, 0xa1)));

			}
		}

		public void nextPinkTri(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0 || rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x, y, Color.PINK));
				nextCoord.add(new Square(x + square, y, Color.PINK));
				nextCoord.add(new Square(x + 2 * square, y, Color.PINK));

			} else {
				nextCoord.add(new Square(x + square, y, Color.PINK));
				nextCoord.add(new Square(x + square, y + square, Color.PINK));
				nextCoord.add(new Square(x + square, y - square, Color.PINK));
			}
		}

		public void nextOyellowBi(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0 || rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x, y, new Color(0xfd, 0xb9, 0x15)));
				nextCoord.add(new Square(x + square, y - square, new Color(0xfd, 0xb9, 0x15)));
			} else {
				nextCoord.add(new Square(x + square, y, new Color(0xfd, 0xb9, 0x15)));
				nextCoord.add(new Square(x, y - square, new Color(0xfd, 0xb9, 0x15)));
			}
		}

		public void nextLightGrayBi(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0 || rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x, y, Color.LIGHT_GRAY));
				nextCoord.add(new Square(x, y - square, Color.LIGHT_GRAY));
			} else {
				nextCoord.add(new Square(x, y + square, Color.LIGHT_GRAY));
				nextCoord.add(new Square(x + square, y + square, Color.LIGHT_GRAY));
			}
		}

		public void nextCoolGrayTri(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0) {
				nextCoord.add(new Square(x, y, new Color(0x95, 0xa3, 0xa6)));
				nextCoord.add(new Square(x + square, y - square, new Color(0x95, 0xa3, 0xa6)));
				nextCoord.add(new Square(x + 2 * square, y, new Color(0x95, 0xa3, 0xa6)));
			} else if (rotate == 1 || rotate == -3) {
				nextCoord.add(new Square(x, y + square, new Color(0x95, 0xa3, 0xa6)));
				nextCoord.add(new Square(x + square, y, new Color(0x95, 0xa3, 0xa6)));
				nextCoord.add(new Square(x, y - square, new Color(0x95, 0xa3, 0xa6)));
			} else if (rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x + square, y, new Color(0x95, 0xa3, 0xa6)));
				nextCoord.add(new Square(x, y - square, new Color(0x95, 0xa3, 0xa6)));
				nextCoord.add(new Square(x + 2 * square, y - square, new Color(0x95, 0xa3, 0xa6)));
			} else {
				nextCoord.add(new Square(x, y, new Color(0x95, 0xa3, 0xa6)));
				nextCoord.add(new Square(x + square, y + square, new Color(0x95, 0xa3, 0xa6)));
				nextCoord.add(new Square(x + square, y - square, new Color(0x95, 0xa3, 0xa6)));
			}
		}

		public void nextDeepSeaBlueSingular(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			nextCoord.add(new Square(x, y, new Color(0x01, 0x54, 0x82)));
		}

		public void nextWarmBlueTri(int x, int y, int square, int rotate) {
			nextCoord = new ArrayList<>();
			if (rotate == 0 || rotate == 2 || rotate == -2) {
				nextCoord.add(new Square(x, y + square, new Color(0x4b, 0x57, 0xdb)));
				nextCoord.add(new Square(x + square, y, new Color(0x4b, 0x57, 0xdb)));
				nextCoord.add(new Square(x + 2 * square, y - square, new Color(0x4b, 0x57, 0xdb)));
			} else {
				nextCoord.add(new Square(x, y - square, new Color(0x4b, 0x57, 0xdb)));
				nextCoord.add(new Square(x + square, y, new Color(0x4b, 0x57, 0xdb)));
				nextCoord.add(new Square(x + 2 * square, y + square, new Color(0x4b, 0x57, 0xdb)));
			}
		}
	}
}
