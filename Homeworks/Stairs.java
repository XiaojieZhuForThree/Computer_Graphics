import java.io.*;

public class Stairs {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length != 3) {
			System.out.println("Supply n (> 0), alpha (in degrees)\n" + "and a filename as program arguments.\n");
			System.exit(1);
		}
		int n = 0;
		double alphaDeg = 0;
		try {
			n = Integer.valueOf(args[0]).intValue();
			alphaDeg = Double.valueOf(args[1]).doubleValue();
			if (n <= 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			System.out.println("n must be an integer > 0");
			System.out.println("alpha must be a real number");
			System.exit(1);
		}
		new Stair(n, alphaDeg * Math.PI / 180, args[2]);
	}

}

class Point3D {
	float x, y, z;

	Point3D(double x, double y, double z) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}
}

class Stair {
	FileWriter fw;

	Stair(int n, double alpha, String fileName) throws IOException {
		fw = new FileWriter(fileName);
		Point3D[] P = new Point3D[9];
		double a = 3.5;
		double b = 1;
		P[1] = new Point3D(2 * a, -b, 0);
		P[2] = new Point3D(2 * a, b, 0);
		P[3] = new Point3D(1, b, 0);
		P[4] = new Point3D(1, -b, 0);
		P[5] = new Point3D(2 * a, -b, 0.2);
		P[6] = new Point3D(2 * a, b, 0.2);
		P[7] = new Point3D(1, b, 0.2);
		P[8] = new Point3D(1, -b, 0.2);
		for (int k = 0; k < n; k++) { // Beam k:
			double phi = k * alpha, cosPhi = Math.cos(phi), sinPhi = Math.sin(phi);
			int m = 10 * k;
			for (int i = 1; i <= 8; i++) {
				double x = P[i].x, y = P[i].y;
				float x1 = (float) (x * cosPhi - y * sinPhi), y1 = (float) (x * sinPhi + y * cosPhi),
						z1 = (float) (P[i].z + k);
				fw.write((m + i) + " " + x1 + " " + y1 + " " + z1 + "\r\n");
			}
			double x = P[1].x, y = 0;
			float x1 = (float) (x * cosPhi - y * sinPhi), y1 = (float) (x * sinPhi + y * cosPhi);
			fw.write((m + 9) + " " + x1 + " " + y1 + " " + (0.1 + k) + "\r\n");
			fw.write((m + 10) + " " + x1 + " " + y1 + " " + (k + 6) + "\r\n");
		}
		int count = 10 * n + 1;
		int nn = 20;
		int height = n + 6;
		double delta = 2 * Math.PI / nn;
		Point3D cyOri = new Point3D(1, 0, 0);
		for (int x = 0; x < nn; x++) {
			double calpha = x * delta, ccosa = Math.cos(calpha), csina = Math.sin(calpha);
			double cx = cyOri.x, cy = cyOri.y;
			float x1 = (float) (cx * ccosa - cy * csina), y1 = (float) (cx * csina + cy * ccosa);
			fw.write((count + x) + " " + x1 + " " + y1 + " " + 0 + "\r\n");
			fw.write((count + 20 + x) + " " + x1 + " " + y1 + " " + height + "\r\n");
		}

		fw.write("Faces:\r\n");
		for (int k = 0; k < n; k++) {
			int m = 10 * k;
			face(m, 1, 2, 6, 5);
			face(m, 4, 8, 7, 3);
			face(m, 5, 6, 7, 8);
			face(m, 1, 4, 3, 2);
			face(m, 2, 3, 7, 6);
			face(m, 1, 5, 8, 4);
			fw.write((m + 9) + " " + (m + 10) + ".\r\n");
		}
		for (int k = 0; k < n - 1; k++) {
			int m = 10 * k;
			fw.write((m + 10) + " " + (m + 20) + ".\r\n");
		}

		for (int t = 0; t < nn - 1; t++) {
			face(count, t, t + 1, t + 21, t + 20);
			// fw.write((count + t + 20) + " " + (count + t + 21) + ".\r\n");
		}
		face(count, 19, 0, 20, 39);
		for (int w = 0; w < nn - 1; w++) {
			fw.write((count + w + 20) + " ");
		}
		fw.write((count + 39) + ".\r\n");
		fw.close();
	}

	void face(int m, int a, int b, int c, int d) throws IOException {
		a += m;
		b += m;
		c += m;
		d += m;
		fw.write(a + " " + b + " " + c + " " + d + ".\r\n");
	}
}
