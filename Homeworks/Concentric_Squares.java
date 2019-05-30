// Triangles.java: This program draws 50 triangles inside each other.
import java.awt.*;
import java.awt.event.*;

public class Concentric_Squares extends Frame {
   public static void main(String[] args) {new Concentric_Squares();}

   Concentric_Squares() {
      super("Concentric squares: 50 squares inside each other");
      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      setSize(600, 400);
      add("Center", new concentricSquares());
      setVisible(true);
   }
}

class concentricSquares extends Canvas {
   int maxX, maxY, minMaxXY, xCenter, yCenter;

   void initgr() {
      Dimension d = getSize();
      maxX = d.width - 1; maxY = d.height - 1;
      minMaxXY = Math.min(maxX, maxY);
      xCenter = maxX / 2; yCenter = maxY / 2;
   }

   int iX(float x) {return Math.round(x);}

   int iY(float y) {return maxY - Math.round(y);}

   public void paint(Graphics g) {
      initgr();
      float side = 0.95F * minMaxXY, sideHalf = 0.5F * side, 
            xA, yA, xB, yB, xC, yC, xD, yD, 
            xA1, yA1, xB1, yB1, xC1, yC1, xD1, yD1;
      xA = xCenter - sideHalf; yA = yCenter - sideHalf;
      xB = xCenter + sideHalf; yB = yA;
      xC = xB; yC = yCenter + sideHalf;
      xD = xA; yD = yC;
      for (int i = 0; i < 50; i++) {
         g.drawLine(iX(xA), iY(yA), iX(xB), iY(yB));
         g.drawLine(iX(xB), iY(yB), iX(xC), iY(yC));
         g.drawLine(iX(xC), iY(yC), iX(xD), iY(yD));
         g.drawLine(iX(xD), iY(yD), iX(xA), iY(yA));
         xA1 = (float) ((xA + xB) * 0.5); yA1 = (float) ((yA + yB) * 0.5);
         xB1 = (float) ((xB + xC) * 0.5); yB1 = (float) ((yB + yC) * 0.5);
         xC1 = (float) ((xC + xD) * 0.5); yC1 = (float) ((yC + yD) * 0.5);
         xD1 = (float) ((xD + xA) * 0.5); yD1 = (float) ((yD + yA) * 0.5);
         xA = xA1; xB = xB1; xC = xC1; xD = xD1;
         yA = yA1; yB = yB1; yC = yC1; yD = yD1;
      }
   }
}
