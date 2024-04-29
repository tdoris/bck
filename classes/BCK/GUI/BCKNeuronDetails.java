package BCK.GUI;

import BCK.ANN.BCKMLP;
import BCK.ANN.BCKNeuralNetwork;
import BCK.ANN.BCKNeuron;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTable;
import com.sun.java.swing.JTextField;
import java.awt.FlowLayout;

public class BCKNeuronDetails extends BCKDialog {
   JTextField NeuronName = new JTextField(25);
   JButton OK = new JButton("  OK  ");
   BCKNeuralNetwork Net;
   int neuronId;
   JPanel table;
   JPanel addremove;
   JPanel buttons;
   JPanel container;
   BCKPlot weightPlot = new BCKPlot(bckframe.top, 200, 200);
   bckNumberBox matrixWidth = new bckNumberBox();

   public BCKNeuronDetails(int var1, BCKNeuralNetwork var2) {
      super(bckframe.top, "Details of Neuron " + var1, true);
      this.neuronId = var1;
      this.Net = var2;
      this.container = new JPanel();
      this.container.setLayout(new ColumnLayout());
      JPanel var3 = this.createNamePanel();
      this.createInputsPanel();
      this.addremove = new JPanel();
      JButton var4 = new JButton("Remove");
      var4.addActionListener(new BCKNeuronDetails$1(this));
      this.addremove.add(var4);
      this.buttons = new JPanel();
      this.buttons.setLayout(new FlowLayout());
      this.OK.addActionListener(new BCKNeuronDetails$2(this));
      JButton var5 = new JButton("Cancel");
      this.buttons.add(this.OK);
      this.buttons.add(var5);
      var5.addActionListener(new BCKNeuronDetails$3(this));
      JPanel var6 = this.createPlotPanel();
      this.container.add(var3);
      this.container.add(var6);
      this.container.add(this.table);
      this.container.add(this.addremove);
      this.container.add(this.buttons);
      this.getContentPane().add(this.container);
      this.pack();
      this.centerDialog();
   }

   public JPanel createPlotPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = new JPanel();
      var2.add(new JLabel("Width of Matrix:"));
      var2.add(this.matrixWidth);
      JButton var3 = new JButton("Draw");
      var3.addActionListener(new BCKNeuronDetails$4(this));
      var2.add(var3);
      var1.add(var2);
      var1.add(this.weightPlot);
      return var1;
   }

   public JPanel createNamePanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = new JPanel();
      var2.setLayout(new FlowLayout());
      var2.add(new JLabel("Name : "));
      this.NeuronName.setText(this.Net.getNeuronName(this.neuronId));
      var2.add(this.NeuronName);
      JPanel var3 = new JPanel();
      var3.add(new JLabel("Type : "));
      JTextField var4 = new JTextField(20);
      var4.setText(this.Net.getNeuron(this.neuronId).getType());
      var4.setEditable(false);
      var3.add(var4);
      var1.add(var2);
      var1.add(var3);
      var1.add(new JLabel("Connections Details : "));
      return var1;
   }

   public JPanel createInputsPanel() {
      this.table = new BCKNeuronTablePanel(this.neuronId, this.Net);
      return this.table;
   }

   public void removePressed() {
      BCKNeuron var1 = this.Net.getNeuron(this.neuronId);
      var1.setName(this.NeuronName.getText());
      JTable var2 = ((BCKNeuronTablePanel)this.table).tableView;
      String var3 = "";
      int var10 = var2.getRowCount();
      BCKNeuron var11 = this.Net.getNeuron(this.neuronId);
      int var12 = var2.getSelectedRow();
      var11.removeInputs();

      for (int var13 = 0; var13 < var10; var13++) {
         var2.editCellAt(0, 0);
         var3 = (String)var2.getValueAt(var13, 0);

         Integer var4;
         try {
            var4 = Integer.valueOf(var3);
         } catch (NumberFormatException var18) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var13 + " column 0 " + var18.toString(), "Alert", 0);
            return;
         }

         int var9 = var4;
         var3 = (String)var2.getValueAt(var13, 1);

         Double var5;
         try {
            var5 = Double.valueOf(var3);
         } catch (NumberFormatException var17) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var13 + " column 1 " + var17.toString(), "Alert", 0);
            return;
         }

         double var6 = var5;
         var3 = (String)var2.getValueAt(var13, 2);

         try {
            var4 = Integer.valueOf(var3);
         } catch (NumberFormatException var16) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var13 + " column 2 " + var16.toString(), "Alert", 0);
            return;
         }

         int var8 = var4;
         if (var13 != var12) {
            try {
               if (var9 == -1) {
                  BCKNeuron var14 = ((BCKMLP)this.Net).getBias();
                  this.Net.connectExternal(var14, this.neuronId, var6, var8);
               } else {
                  this.Net.connectInternal(var9, this.neuronId, var6, var8);
               }
            } catch (Exception var15) {
               JOptionPane.showMessageDialog(null, "Error at row " + var13 + " : " + var15.toString(), "Alert", 0);
               return;
            }
         }
      }

      this.Net.notifyObservers();
      this.container.remove(this.table);
      this.container.remove(this.addremove);
      this.container.remove(this.buttons);
      this.createInputsPanel();
      this.container.add(this.table);
      this.container.add(this.addremove);
      this.container.add(this.buttons);
      this.pack();
      this.repaint();
   }

   public void OKPressed() {
      BCKNeuron var1 = this.Net.getNeuron(this.neuronId);
      var1.setName(this.NeuronName.getText());
      JTable var2 = ((BCKNeuronTablePanel)this.table).tableView;
      String var3 = "";
      int var10 = var2.getRowCount();
      BCKNeuron var11 = this.Net.getNeuron(this.neuronId);
      var11.removeInputs();

      for (int var12 = 0; var12 < var10; var12++) {
         var2.editCellAt(0, 0);
         var3 = (String)var2.getValueAt(var12, 0);

         Integer var4;
         try {
            var4 = Integer.valueOf(var3);
         } catch (NumberFormatException var17) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var12 + " column 0 " + var17.toString(), "Alert", 0);
            return;
         }

         int var9 = var4;
         var3 = (String)var2.getValueAt(var12, 1);

         Double var5;
         try {
            var5 = Double.valueOf(var3);
         } catch (NumberFormatException var16) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var12 + " column 1 " + var16.toString(), "Alert", 0);
            return;
         }

         double var6 = var5;
         var3 = (String)var2.getValueAt(var12, 2);

         try {
            var4 = Integer.valueOf(var3);
         } catch (NumberFormatException var15) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var12 + " column 2 " + var15.toString(), "Alert", 0);
            return;
         }

         int var8 = var4;

         try {
            if (var9 == -1) {
               BCKNeuron var13 = ((BCKMLP)this.Net).getBias();
               this.Net.connectExternal(var13, this.neuronId, var6, var8);
            } else {
               this.Net.connectInternal(var9, this.neuronId, var6, var8);
            }
         } catch (Exception var14) {
            JOptionPane.showMessageDialog(null, "Error at row " + var12 + " : " + var14.toString(), "Alert", 0);
            return;
         }
      }

      this.setVisible(false);
   }

   public void CancelPressed() {
      this.setVisible(false);
   }
}
