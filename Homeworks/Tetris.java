import java.awt.*;
import java.awt.event.*;

public class Tetris extends Frame {
	public static void main(String[] args) {
		new Tetris();
	}

	Tetris() {
		super("Tetris");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		add("Center", new getTetris());
		setSize(600, 400);
		setVisible(true);
	}
}

class getTetris extends Canvas {
	int maxX, maxY, minMaxXY, xCenter, yCenter;

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

	public void paint(Graphics g) {
		initgr();
		float square = (float) (minMaxXY / 30);
		float xA, yA, xB, yB, xC, yC, xD, yD, xE, yE, xF, yF, xG, yG;
		float xH, yH, xI, yI, xJ, yJ, xK, yK, xL, yL, xM, yM;
		xA = (float) xCenter - square * 5;
		yA = (float) yCenter - square * 10;
		xB = (float) xCenter + square * 6;
		yB = yA;
		xC = (float) ((float) xCenter - square * 2.5);
		yC = (float) yCenter - square;
		xD = (float) xCenter + square;
		yD = (float) yCenter + square * 9;
		xE = (float) xCenter + square * 3;
		yE = (float) yCenter + square * 9;
		xF = xB + square;
		yF = yB + (float) 1.5 * square;
		xG = (float) xCenter - square;
		yG = (float) yCenter - square * 6;
		xI = (float) xCenter + square * 6;
		yI = (float) yCenter + square;
		xH = xI;
		yH = yI - square * 2;
		xJ = xH;
		yJ = yI + 2 * square;
		xK = (float) xCenter + square * 6;
		yK = (float) yCenter + square * (float)8.5;
		xL = (float) (xK + 0.9 * square);
		yL = (float) (yK + 1.1 * square);
		xM = (float) (xC + 0.9 * square);
		yM = (float) (yC + 1.4 * square);
		g.drawRect(iX(xA), iX(yA), iX(square * 10), iX(square * 20));
		g.drawRect(iX(xB), iX(yB), iX(square * 5), iX(square * 3));
		g.drawRect(iX(xK), iX(yK), iX(square * 4), iX(square * (float)1.5));
		g.setColor(Color.BLUE);
		g.drawRect(iX(xC), iX(yC), iX(square * 5), iX(square * 2));
		drawWedge(g, iX(xD), iX(yD), (int) (square));
		drawL(g, iX(xE), iX(yE), (int) (square));
		drawReverseL(g, iX(xF), iX(yF), (int) (square));
		drawSquare(g, iX(xG), iX(yG), (int) (square));
		
		Font f = new Font("Dialog", Font.BOLD, (int)square);
		g.setFont(f);
		g.drawString("Level:      1", iX(xH), iX(yH));
		g.drawString("Lines:      0", iX(xI), iX(yI));
		g.drawString("QUIT", iX(xL), iX(yL));
		f = new Font("Dialog", Font.BOLD, (int)(square*0.999));
		g.setFont(f);
		g.drawString("Score:      0", iX(xJ), iX(yJ));
		f = new Font("Dialog", Font.BOLD, (int)(square));
		g.setColor(Color.BLUE);
		g.drawString("PAUSE", iX(xM), iX(yM));

	}

	void drawWedge(Graphics g, int x, int y, int square) {
		int[][] vertex = new int[4][2];
		vertex[0] = new int[] { x, y };
		vertex[1] = new int[] { x + square, y };
		vertex[2] = new int[] { x + square, y - square };
		vertex[3] = new int[] { x + 2 * square, y - square };
		for (int[] coord : vertex) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.yellow);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}

	void drawL(Graphics g, int x, int y, int square) {
		int[][] vertex = new int[4][2];
		vertex[0] = new int[] { x, y };
		vertex[1] = new int[] { x + square, y };
		vertex[2] = new int[] { x + square, y - square };
		vertex[3] = new int[] { x + square, y - 2 * square };
		for (int[] coord : vertex) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.blue);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}

	void drawReverseL(Graphics g, int x, int y, int square) {
		int[][] vertex = new int[4][2];
		vertex[0] = new int[] { x, y };
		vertex[1] = new int[] { x + square, y };
		vertex[2] = new int[] { x + 2 * square, y };
		vertex[3] = new int[] { x + 2 * square, y - square };
		for (int[] coord : vertex) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.red);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}

	void drawSquare(Graphics g, int x, int y, int square) {
		int[][] vertex = new int[4][2];
		vertex[0] = new int[] { x, y };
		vertex[1] = new int[] { x + square, y };
		vertex[2] = new int[] { x + square, y - square };
		vertex[3] = new int[] { x, y - square };
		for (int[] coord : vertex) {
			int X = coord[0];
			int Y = coord[1];
			g.setColor(Color.green);
			g.fillRect(X, Y, square, square);
			g.setColor(Color.black);
			g.drawRect(X, Y, square, square);

		}
	}
}
