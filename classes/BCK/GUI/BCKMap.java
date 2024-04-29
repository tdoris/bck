package BCK.GUI;

import BCK.ANN.BCKNeuralNetwork;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;

public class BCKMap extends BCKDialog {
   bckNumberBox xNeuron;
   bckNumberBox yNeuron;
   bckNumberBox zNeuron;
   double xrangeValue;
   double yrangeValue;
   int xNeuronNumber;
   int yNeuronNumber;
   int zNeuronNumber;
   BCKPlot plotPane;
   JTextField xrange;
   JTextField yrange;
   BCKNeuralNetwork Net;

   public BCKMap(BCKNeuralNetwork var1) {
      super(bckframe.top, "2-d Map", true);
      this.Net = var1;
      this.xrangeValue = 1.0;
      this.yrangeValue = 1.0;
      this.makePane();
      this.pack();
      this.centerDialog();
   }

   public void makePane() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = this.createNeuronSelection();
      JPanel var3 = this.createGraph();
      var1.add(var2);
      var1.add(var3);
      JButton var4 = new JButton("Done");
      var4.addActionListener(new BCKMap$1(this));
      JPanel var5 = new JPanel();
      var5.add(var4);
      var1.add(var5);
      this.getContentPane().add(var1);
   }

   public JPanel createGraph() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = new JPanel();
      var2.setLayout(new FlowLayout());
      JButton var3 = new JButton("Draw");
      var2.add(var3);
      var3.addActionListener(new BCKMap$2(this));
      JButton var4 = new JButton("Zoom In");
      var2.add(var4);
      var4.addActionListener(new BCKMap$3(this));
      JButton var5 = new JButton("Zoom Out");
      var2.add(var5);
      var5.addActionListener(new BCKMap$4(this));
      var1.add(var2);
      JPanel var6 = new JPanel();
      var6.setLayout(new ColumnLayout());
      JPanel var7 = new JPanel();
      var7.setLayout(new FlowLayout());
      var7.add(new JLabel("X range :"));
      this.xrange = new JTextField(15);
      var7.add(this.xrange);
      var6.add(var7);
      JPanel var8 = new JPanel();
      var8.setLayout(new FlowLayout());
      var8.add(new JLabel("Y range :"));
      this.yrange = new JTextField(15);
      var8.add(this.yrange);
      var6.add(var8);
      this.setRangesText();
      var1.add(var6);
      this.plotPane = new BCKPlot(bckframe.top, 200, 200);
      this.plotPane.setBackground(Color.white);
      var1.add(this.plotPane);
      return var1;
   }

   public JPanel createNeuronSelection() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = new JPanel();
      var2.setLayout(new FlowLayout());
      var2.add(new JLabel("Input X axis node : "));
      this.xNeuron = new bckNumberBox();
      var2.add(this.xNeuron);
      var1.add(var2);
      JPanel var3 = new JPanel();
      var3.setLayout(new FlowLayout());
      var3.add(new JLabel("Input Y axis node : "));
      this.yNeuron = new bckNumberBox();
      var3.add(this.yNeuron);
      var1.add(var3);
      JPanel var4 = new JPanel();
      var4.setLayout(new FlowLayout());
      var4.add(new JLabel("Output Z axis node :"));
      this.zNeuron = new bckNumberBox();
      var4.add(this.zNeuron);
      var1.add(var4);
      return var1;
   }

   public void CancelPressed() {
      this.setVisible(false);
   }

   public void setRangesText() {
      this.yrange.setText("-" + this.yrangeValue + " to +" + this.yrangeValue);
      this.xrange.setText("-" + this.xrangeValue + " to +" + this.xrangeValue);
   }

   public boolean setNeuronNumbers() {
      this.xNeuronNumber = this.xNeuron.getInteger();
      this.yNeuronNumber = this.yNeuron.getInteger();
      this.zNeuronNumber = this.zNeuron.getInteger();
      if (this.zNeuronNumber < 0 || this.zNeuronNumber >= this.Net.getNumberOfNeurons()) {
         JOptionPane.showMessageDialog(null, "Please enter a valid neuron number for Z neuron", "Alert", 0);
         return false;
      } else if (this.yNeuronNumber < 0 || this.yNeuronNumber >= this.Net.getNumberOfNeurons()) {
         JOptionPane.showMessageDialog(null, "Please enter a valid neuron number for Y neuron", "Alert", 0);
         return false;
      } else if (this.xNeuronNumber >= 0 && this.xNeuronNumber < this.Net.getNumberOfNeurons()) {
         return true;
      } else {
         JOptionPane.showMessageDialog(null, "Please enter a valid neuron number for X neuron", "Alert", 0);
         return false;
      }
   }

   public void drawPressed() {
      Color var1 = Color.white;
      if (this.setNeuronNumbers()) {
         for (byte var8 = 0; var8 < this.plotPane.width; var8 += 2) {
            for (byte var9 = 0; var9 < this.plotPane.height; var9 += 2) {
               double var2 = 2.0 * this.xrangeValue * (((double)var8 - (double)this.plotPane.width / 2.0) / (double)this.plotPane.width);
               this.Net.setOutput(this.xNeuronNumber, var2);
               double var4 = 2.0 * this.yrangeValue * (((double)var9 - (double)this.plotPane.height / 2.0) / (double)this.plotPane.height);
               this.Net.setOutput(this.yNeuronNumber, var4);
               this.forwardPass();
               double var6 = this.Net.getState(this.zNeuronNumber);
               var6 = 1.0 / (1.0 + Math.exp(-var6));
               var1 = new Color((float)var6, (float)var6, (float)var6);
               this.plotPane.new2by2(new Point(var8, var9), var1);
            }
         }

         this.plotPane.crossHair();
      }
   }

   public void forwardPass() {
      try {
         int var1 = Math.max(this.yNeuronNumber, this.xNeuronNumber) + 1;

         for (int var2 = var1; var2 < this.Net.getNumberOfNeurons(); var2++) {
            this.Net.calcState(var2);
         }
      } catch (Exception var3) {
         JOptionPane.showMessageDialog(bckframe.top, "Error " + var3.toString(), "Alert", 0);
      }
   }

   public void setAllZero() {
      int var1 = this.Net.getNumberOfNeurons();

      for (int var2 = 0; var2 < var1; var2++) {
         this.Net.setOutput(var2, 0.0);
      }
   }

   public void zoomInPressed() {
      this.xrangeValue *= 0.5;
      this.yrangeValue *= 0.5;
      this.setRangesText();
   }

   public void zoomOutPressed() {
      this.xrangeValue *= 2.0;
      this.yrangeValue *= 2.0;
      this.setRangesText();
   }
}
