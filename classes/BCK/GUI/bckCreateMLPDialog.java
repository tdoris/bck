package BCK.GUI;

import BCK.ANN.BCKNeuralNetwork;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JComboBox;
import com.sun.java.swing.JDialog;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

public class bckCreateMLPDialog extends JDialog {
   JPanel panel1 = new JPanel();
   BCKNeuralNetwork Net;
   public JButton btnOK;
   JButton btnCancel = new JButton();
   private JComboBox algorithms;
   private JPanel algorithm;
   private JPanel hiddens;
   private bckNumberBox inField;
   private bckNumberBox outField;
   private bckNumberBox hidField;
   private bckNumberBox hiddenLayer1Neurons;
   private bckNumberBox[] hiddenLayerNeurons;

   public bckCreateMLPDialog(Frame var1) {
      super(var1, "Create an MLP Neural Network", true);
      JPanel var2 = new JPanel();
      var2.setLayout(new ColumnLayout());
      this.algorithm = this.buildAlgorithmPanel();
      JPanel var3 = this.buildInputPanel();
      JPanel var4 = new JPanel();
      var4.setLayout(new ColumnLayout());
      this.hiddens = new JPanel();
      this.hiddens.setLayout(new ColumnLayout());
      JPanel var5 = new JPanel();
      var5.setLayout(new FlowLayout());
      JLabel var6 = new JLabel("Neurons in hidden layer 1", 4);
      this.hiddenLayer1Neurons = new bckNumberBox();
      var5.add(var6);
      var5.add(this.hiddenLayer1Neurons);
      var4.add(var5);
      JPanel var7 = new JPanel();
      var7.setLayout(new FlowLayout(2));
      this.btnOK = new JButton("OK");
      var7.add(this.btnOK);
      this.btnOK.addActionListener(new bckCreateMLPDialog$1(this));
      this.getRootPane().setDefaultButton(this.btnOK);
      this.btnOK.setToolTipText("This is the OK button");
      JButton var8 = new JButton("Cancel");
      var8.addActionListener(new bckCreateMLPDialog$2(this));
      var7.add(var8);
      var2.add(this.algorithm);
      var2.add(var3);
      var2.add(var4);
      JScrollPane var9 = new JScrollPane(this.hiddens);
      var9.setPreferredSize(new Dimension(400, 200));
      var2.add(var9);
      var2.add(var7);
      this.getContentPane().add(var2);
      this.pack();
      this.centerDialog();
      this.setSize(400, 200);
      this.setResizable(false);
      this.pack();
   }

   public JPanel buildAlgorithmPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JLabel var2 = new JLabel("Algorithm:", 4);
      this.algorithms = new JComboBox();
      this.algorithms.addItem("Quickprop");
      this.algorithms.addItem("Backprop");
      this.algorithms.setPreferredSize(new Dimension(120, 20));
      var1.add(var2);
      var1.add(this.algorithms);
      return var1;
   }

   public JPanel buildInputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new GridLayout(3, 2, 5, 5));
      JLabel var2 = new JLabel("Number of Input Neurons:", 4);
      this.inField = new bckNumberBox();
      this.inField.requestFocus();
      var1.add(var2);
      var1.add(this.inField);
      JLabel var3 = new JLabel("Number of Output Neurons:", 4);
      this.outField = new bckNumberBox();
      var1.add(var3);
      var1.add(this.outField);
      JLabel var4 = new JLabel("Number of Hidden Layers:", 4);
      this.hidField = new bckNumberBox();
      this.hidField.addFocusListener(new bckCreateMLPDialog$3(this));
      var1.add(var4);
      var1.add(this.hidField);
      return var1;
   }

   public void CancelPressed() {
      this.Net = null;
      this.setVisible(false);
   }

   public void OKPressed() {
      int var1 = this.inField.getInteger();
      if (var1 < 1) {
         JOptionPane.showMessageDialog(null, "You must enter a number of inputs >0", "Alert", 0);
      } else {
         int var2 = this.outField.getInteger();
         if (var2 < 1) {
            JOptionPane.showMessageDialog(null, "You must enter a number of outputs >0", "Alert", 0);
         } else {
            int var3 = this.hidField.getInteger();
            if (var3 < 1) {
               JOptionPane.showMessageDialog(null, "You must enter a number of hiden layers >0", "Alert", 0);
            } else {
               int var4 = this.hiddenLayer1Neurons.getInteger();
               if (var4 < 1) {
                  JOptionPane.showMessageDialog(null, "You must enter a number of neurons >0 in hidden layer 1", "Alert", 0);
               } else {
                  for (int var5 = 0; var5 < this.hiddenLayerNeurons.length; var5++) {
                     int var6 = this.hiddenLayerNeurons[var5].getInteger();
                     int var7 = var5 + 2;
                     if (var6 < 1) {
                        JOptionPane.showMessageDialog(null, "You must enter a number of neurons >0 in hidden layer " + var7, "Alert", 0);
                        return;
                     }
                  }

                  int[] var9 = new int[3 + this.hiddenLayerNeurons.length];
                  var9[0] = var1;
                  var9[1] = var4;

                  for (int var10 = 0; var10 < this.hiddenLayerNeurons.length; var10++) {
                     var9[2 + var10] = this.hiddenLayerNeurons[var10].getInteger();
                  }

                  var9[2 + this.hiddenLayerNeurons.length] = var2;
                  int var8 = this.algorithms.getSelectedIndex();
                  if (var8 == 0) {
                     this.Net = new BCKMLPQpropDrawable(var9);
                  } else {
                     if (var8 != 1) {
                        return;
                     }

                     this.Net = new BCKMLPBpropDrawable(var9);
                  }

                  this.setVisible(false);
               }
            }
         }
      }
   }

   public void CreateHiddenArray() {
      int var1 = this.hidField.getInteger();
      if (var1 < 1) {
         JOptionPane.showMessageDialog(bckframe.top, "You must enter an integer>=1", "Alert", 0);
      } else {
         this.hiddenLayerNeurons = new bckNumberBox[var1 - 1];
         var1--;
         this.hiddens.removeAll();

         for (int var2 = 0; var2 < var1; var2++) {
            int var3 = var2 + 2;
            JPanel var4 = new JPanel();
            var4.setLayout(new FlowLayout());
            JLabel var5 = new JLabel("Neurons in hidden layer " + var3, 4);
            this.hiddenLayerNeurons[var2] = new bckNumberBox();
            var4.add(var5);
            var4.add(this.hiddenLayerNeurons[var2]);
            this.hiddens.add(var4);
         }

         this.pack();
         this.repaint();
      }
   }

   public BCKNeuralNetwork getNet() {
      return this.Net;
   }

   protected void centerDialog() {
      Dimension var1 = this.getToolkit().getScreenSize();
      Dimension var2 = this.getSize();
      var1.height /= 2;
      var1.width /= 2;
      var2.height /= 2;
      var2.width /= 2;
      int var3 = var1.height - var2.height;
      int var4 = var1.width - var2.width;
      this.setLocation(var4, var3);
   }
}
