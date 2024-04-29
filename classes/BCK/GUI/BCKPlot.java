package BCK.GUI;

import com.sun.java.accessibility.AccessibleContext;
import com.sun.java.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;

public class BCKPlot extends JComponent {
   int width;
   int height;
   protected Frame frame;
   protected Point currentPoint;
   protected Color currentColor;
   protected boolean blank;

   public BCKPlot(Frame var1, int var2, int var3) {
      this.frame = var1;
      this.width = var2;
      this.height = var3;
      this.currentColor = Color.white;
      this.currentPoint = new Point(2, 1);
      this.setBackground(Color.white);
      this.setForeground(Color.white);
      this.blank = true;
   }

   public void setBlank(boolean var1) {
      this.blank = var1;
   }

   public AccessibleContext getAccessibleContext() {
      return super.getAccessibleContext();
   }

   public Dimension getPreferredSize() {
      return new Dimension(this.width, this.height);
   }

   public void setPreferredSize(Dimension var1) {
      this.height = var1.height;
      this.width = var1.width;
   }

   public void paint(Graphics var1) {
   }

   public void newPoint(Point var1, Color var2) {
      this.currentPoint = var1;
      this.currentColor = var2;
      Graphics var3 = this.getGraphics();
      var3.setColor(this.currentColor);
      var3.drawLine(this.currentPoint.x, this.currentPoint.y, this.currentPoint.x, this.currentPoint.y);
   }

   public void new2by2(Point var1, Color var2) {
      this.currentPoint = var1;
      this.currentColor = var2;
      Graphics var3 = this.getGraphics();
      var3.setColor(this.currentColor);
      var3.fillRect(this.currentPoint.x, this.height - this.currentPoint.y, 2, 2);
   }

   public void new4by4(Point var1, Color var2) {
      this.currentPoint = var1;
      this.currentColor = var2;
      Graphics var3 = this.getGraphics();
      var3.setColor(this.currentColor);
      var3.fillRect(this.currentPoint.x, this.height - this.currentPoint.y, 4, 4);
   }

   public void drawPicture(int[] var1, int var2, int var3, int var4) {
      int var5 = var1.length / var2;
      Graphics var6 = this.getGraphics();
      int var7 = 0;

      for (int var9 = 0; var9 < var5; var9++) {
         for (int var10 = 0; var10 < var2; var10++) {
            float var8 = (float)var1[var7] / (float)var3;
            var6.setColor(new Color(var8, var8, var8));
            var6.fillRect(var10 * var4, var9 * var4, var4, var4);
            var7++;
         }
      }
   }

   public void drawPicture(double[] var1, int var2, int var3) {
      int var4 = var1.length / var2;
      Graphics var5 = this.getGraphics();
      int var6 = 0;
      double var8 = -100.0;

      for (int var10 = 0; var10 < var1.length; var10++) {
         if (var1[var10] > var8) {
            var8 = var1[var10];
         }
      }

      for (int var11 = 0; var11 < var4; var11++) {
         for (int var12 = 0; var12 < var2; var12++) {
            float var7 = (float)var1[var6] / (float)var8;
            var5.setColor(new Color(var7, var7, var7));
            var5.fillRect(var12 * var3, var11 * var3, var3, var3);
            var6++;
         }
      }
   }

   public void crossHair() {
      Graphics var1 = this.getGraphics();
      var1.setColor(Color.green);
      var1.drawLine(this.width / 2, 0, this.width / 2, this.height);
      var1.drawLine(0, this.height / 2, this.width, this.height / 2);
   }
}
