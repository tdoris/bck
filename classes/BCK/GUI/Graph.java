package BCK.GUI;

import com.sun.java.accessibility.AccessibleContext;
import com.sun.java.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class Graph extends JComponent {
   protected short last_x;
   protected short last_y;
   protected int currentx;
   protected Vector lines = new Vector(256, 256);
   protected Color current_color = Color.black;
   protected int width;
   protected int height;
   protected Frame frame;
   protected double[] yvals;
   protected int index;
   protected double scale;
   protected int deltax;

   public Graph(Frame var1, int var2, int var3) {
      this.frame = var1;
      this.width = var2;
      this.height = var3;
      this.currentx = 0;
      this.deltax = 5;
      this.yvals = new double[var2 / this.deltax];
      this.scale = 50.0;
      this.index = 0;

      for (int var4 = 0; var4 < this.yvals.length; var4++) {
         this.yvals[var4] = (double)(this.height + 1);
      }

      this.enableEvents(16L);
      this.enableEvents(32L);
      this.setBackground(Color.white);
   }

   public AccessibleContext getAccessibleContext() {
      return super.getAccessibleContext();
   }

   public Dimension getPreferredSize() {
      return new Dimension(this.width, this.height);
   }

   public void paint(Graphics var1) {
      int var2 = 0;
      int var3 = (int)Math.ceil((double)this.height - this.scale * this.yvals[0]);
      var1.setColor(Color.white);
      var1.fillRect(0, 0, this.width, this.height);

      for (int var7 = 0; var7 < this.yvals.length; var7++) {
         if (this.yvals[var7] == (double)(this.height + 1)) {
            return;
         }

         double var4 = this.yvals[var7];
         var4 *= this.scale;
         int var6 = (int)Math.ceil((double)this.height - var4);
         var1.setColor(this.current_color);
         var1.drawLine(var2, var3, var2 + this.deltax, var6);
         var2 += this.deltax;
         var3 = var6;
      }

      this.setBackground(Color.white);
      this.repaint();
   }

   public void processMouseEvent(MouseEvent var1) {
      if (!var1.isAltDown()) {
         if (var1.getID() == 501) {
            this.last_x = (short)var1.getX();
            this.last_y = (short)var1.getY();
            return;
         }

         super.processMouseEvent(var1);
      }
   }

   public void processMouseMotionEvent(MouseEvent var1) {
      super.processMouseMotionEvent(var1);
   }

   public void newPoint(double var1) {
      this.yvals[this.index] = var1;
      this.index++;
      if (this.index == this.yvals.length) {
         this.index--;
         this.shiftLeft();
      }

      this.paint(this.getGraphics());
   }

   public void newPoint(int var1) {
      var1 *= 10;
      int var2 = this.height - var1;
      if (var2 < 0) {
         var2 = 0;
      }

      this.yvals[this.index] = (double)var2;
      this.index++;
      if (this.index == this.yvals.length) {
         this.index--;
         this.shiftLeft();
      }

      this.paint(this.getGraphics());
   }

   public void shiftLeft() {
      for (int var1 = 0; var1 < this.yvals.length - 1; var1++) {
         this.yvals[var1] = this.yvals[var1 + 1];
      }
   }

   public double getScale() {
      return this.scale;
   }

   public void zoomIn() {
      this.scale *= 2.0;
      this.paint(this.getGraphics());
   }

   public void zoomOut() {
      this.scale /= 2.0;
      this.paint(this.getGraphics());
   }
}
