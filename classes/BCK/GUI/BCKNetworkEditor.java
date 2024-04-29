package BCK.GUI;

import BCK.ANN.BCKHopfield;
import BCK.ANN.BCKKohonen;
import BCK.ANN.BCKMLP;
import BCK.ANN.BCKNeuralNetwork;
import BCK.ANN.BCKObserver;
import BCK.ANN.BCKRBF;
import com.sun.java.swing.JDesktopPane;
import com.sun.java.swing.JLayeredPane;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.Scrollable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

public class BCKNetworkEditor extends JDesktopPane implements Scrollable, BCKObserver {
   Vector images = new Vector(0);
   BCKNeuralNetwork Net;
   Vector Lines = new Vector(0);
   int MaxLines = 1000;
   int numNodes;
   int xgap;
   int firstColumn;
   int maxInColumn;
   int preferredX;
   int preferredY;
   boolean ShowLines = false;

   public BCKNetworkEditor(BCKNeuralNetwork var1) {
      this.preferredX = 1000;
      this.preferredY = 1000;
      this.setPreferredSize(new Dimension(this.preferredX, this.preferredY));
      this.setBackground(Color.black);
      this.Net = var1;
      this.maxInColumn = 10;
      this.firstColumn = 10;
      this.xgap = 100;
      this.numNodes = this.Net.getNumberOfNeurons();

      for (int var2 = 0; var2 < this.numNodes; var2++) {
         this.images.addElement(new BCKNeuronImage(var2, this, this.Net.getNeuron(var2), this.Net));
         this.add(this.getImage(var2), JLayeredPane.PALETTE_LAYER);
      }

      this.Net.registerObserver(this);
   }

   protected void finalize() {
      this.Net.removeObserver(this);
   }

   public void updateObserver() {
      if (this.Net instanceof BCKRBF) {
         this.numNodes = this.Net.getNumberOfNeurons();
         this.images = new Vector(0);
         this.removeAll();

         for (int var1 = 0; var1 < this.numNodes; var1++) {
            this.images.addElement(new BCKNeuronImage(var1, this, this.Net.getNeuron(var1), this.Net));
            this.add(this.getImage(var1), JLayeredPane.PALETTE_LAYER);
         }

         this.drawRBF((BCKRBF)this.Net);
      }

      this.drawConnections();
      this.repaint();
   }

   public void setShowLines(boolean var1) {
      this.ShowLines = var1;
      if (this.ShowLines) {
         this.drawConnections();
      }

      this.repaint();
   }

   public void drawNeurons() {
      if (this.Net instanceof BCKMLP) {
         this.drawMLP((BCKMLP)this.Net);
      } else if (this.Net instanceof BCKHopfield) {
         this.drawHopfield();
      } else if (this.Net instanceof BCKRBF) {
         this.drawRBF((BCKRBF)this.Net);
      } else if (this.Net instanceof BCKKohonen) {
         this.drawKohonen();
      }

      this.drawConnections();
      this.repaint();
   }

   public void paint(Graphics var1) {
      super.paint(var1);
      if (this.ShowLines) {
         for (int var3 = 0; var3 < this.Lines.size(); var3++) {
            Line var2 = (Line)this.Lines.elementAt(var3);
            var1.setColor(var2.color);

            for (int var4 = 0; var4 < var2.thickness; var4++) {
               var1.drawLine(var2.x1, var2.y1 + var4, var2.x2, var2.y2 + var4);
            }
         }
      }
   }

   public void drawConnection(int var1, int var2, double var3) {
      if (this.ShowLines) {
         Point var5 = this.getImage(var1).getCenter();
         Point var6 = this.getImage(var2).getCenter();
         int var8 = 1;
         var8 = (int)Math.abs(Math.floor(var3));
         var8++;
         Color var7;
         if (var3 < 0.0) {
            var7 = new Color(0.0F, 0.0F, 1.0F);
         } else {
            var7 = new Color(1.0F, 0.3F, 0.3F);
         }

         this.Lines.addElement(new Line(var5.x + 20, var5.y, var6.x - 15, var6.y, var7, var8));
      }
   }

   public void drawConnections() {
      this.Lines.removeAllElements();
      if (this.ShowLines) {
         for (int var2 = 0; var2 < this.numNodes; var2++) {
            try {
               int[] var1 = this.Net.getInputArrayIds(var2);

               for (int var3 = 0; var3 < var1.length; var3++) {
                  if (var1[var3] >= 0 && var1[var3] < this.numNodes) {
                     this.drawConnection(var1[var3], var2, this.Net.getWeight(var1[var3], var2));
                  }

                  if (this.Lines.size() > this.MaxLines) {
                     JOptionPane.showMessageDialog(bckframe.top, "Too many connections \n risk exceeding memory allocation \n", "Alert", 0);
                     this.ShowLines = false;
                     return;
                  }
               }
            } catch (Exception var4) {
               System.out.println(var4.toString());
            }
         }
      }
   }

   public void drawKohonen() {
      int var1 = this.Net.getNumInputs();
      int var2 = this.Net.getNumOutputs();
      byte var3 = 20;
      int var4 = 30;
      int var5 = 0;
      int var6 = 0;
      int var7 = (int)Math.sqrt((double)var2);

      for (int var8 = 0; var8 < var1; var8++) {
         if (var5 < var8 / this.maxInColumn) {
            var3 = 20;
            var4 += 34;
         }

         var5 = var8 / this.maxInColumn;
         this.getImage(var6).setLocation(var4, var3);
         var3 += 34;
         var6++;
      }

      if (var4 > this.preferredX) {
         this.preferredX = var4 + 100;
      }

      if (var3 > this.preferredY) {
         this.preferredY = var3;
      }

      var4 += 100;
      var3 = 20;
      int var9 = var4;

      for (int var10 = 0; var10 < var7; var10++) {
         for (int var11 = 0; var11 < var7; var11++) {
            this.getImage(var6).setLocation(var4, var3);
            var4 += 34;
            if (var4 > this.preferredX) {
               this.preferredX = var4 + 100;
            }

            var6++;
         }

         var4 = var9;
         var3 += 34;
      }

      if (var3 > this.preferredY) {
         this.preferredY = var3 + 100;
      }

      this.setPreferredSize(new Dimension(this.preferredX, this.preferredY));
   }

   public void drawMLP(BCKMLP var1) {
      byte var2 = 20;
      int var3 = 0;
      int var4 = 0;
      byte var5 = 30;

      for (int var6 = 0; var6 < var1.getNumberOfLayers(); var6++) {
         for (int var7 = 0; var7 < var1.getNumberOfNeuronsInLayer(var6); var7++) {
            if (var3 < var7 / this.maxInColumn) {
               var2 = 20;
               var5 += 34;
            }

            var3 = var7 / this.maxInColumn;
            this.getImage(var4).setLocation(var5, var2);
            var2 += 34;
            var4++;
         }

         var5 += 100;
         var2 = 20;
      }

      if (var5 > this.preferredX) {
         this.preferredX = var5 + 100;
      }

      if (var2 > this.preferredY) {
         this.preferredY = var2 + 100;
      }

      this.setPreferredSize(new Dimension(this.preferredX, this.preferredY));
   }

   public void drawRBF(BCKRBF var1) {
      int var2 = var1.getNumInputs();
      int var3 = var1.getNumOutputs();
      int var4 = 0;
      int var5 = 0;
      byte var6 = 20;
      byte var7 = 30;
      int var8 = 0;
      int var9 = var1.getNumHidden();

      for (int var10 = 0; var10 < 3; var10++) {
         if (var10 == 0) {
            var4 = var2;
         } else if (var10 == 1) {
            var4 = var9;
         } else if (var10 == 2) {
            var4 = var3;
         }

         for (int var11 = 0; var11 < var4; var11++) {
            if (var8 < var11 / this.maxInColumn) {
               var6 = 20;
               var7 += 34;
            }

            var8 = var11 / this.maxInColumn;
            this.getImage(var5).setLocation(var7, var6);
            var6 += 34;
            var5++;
         }

         var7 += 100;
         var6 = 20;
      }

      if (var7 > this.preferredX) {
         this.preferredX = var7 + 100;
      }

      if (var6 > this.preferredY) {
         this.preferredY = var6 + 100;
      }

      this.setPreferredSize(new Dimension(this.preferredX, this.preferredY));
   }

   public void drawHopfield() {
      int var1 = this.Net.getNumberOfNeurons();
      byte var2 = 20;
      byte var3 = 30;
      byte var4 = var3;
      int var5 = 0;
      int var6 = (int)Math.sqrt((double)var1);

      for (int var7 = 0; var7 < var1; var7++) {
         if (var5 < var7 / var6) {
            var2 += 60;
            var3 = var4;
         }

         var5 = var7 / var6;
         this.getImage(var7).setLocation(var3, var2);
         var3 += 60;
         if (var3 > this.preferredX) {
            this.preferredX = var3 + 100;
         }
      }

      if (var2 > this.preferredY) {
         this.preferredY = var2 + 100;
      }

      this.setPreferredSize(new Dimension(this.preferredX, this.preferredY));
   }

   public BCKNeuronImage getImage(int var1) {
      return (BCKNeuronImage)this.images.elementAt(var1);
   }

   public Dimension getPreferredScrollableViewportSize() {
      return this.getPreferredSize();
   }

   public int getScrollableUnitIncrement(Rectangle var1, int var2, int var3) {
      return 55;
   }

   public int getScrollableBlockIncrement(Rectangle var1, int var2, int var3) {
      return 55;
   }

   public boolean getScrollableTracksViewportWidth() {
      return false;
   }

   public boolean getScrollableTracksViewportHeight() {
      return false;
   }
}
