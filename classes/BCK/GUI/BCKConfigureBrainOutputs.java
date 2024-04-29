package BCK.GUI;

import BCK.ANN.BCKNeuralNetwork;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTable;
import java.awt.FlowLayout;

public class BCKConfigureBrainOutputs extends BCKDialog {
   BCKNeuralNetwork Net;
   protected int targetNet;
   protected int sourceNet;
   protected int targetNeuron;
   protected int sourceNeuron;
   protected double weight = 1.0;
   protected double delay;
   protected JPanel tablePanel;
   protected bckNumberBox sourceNetworkNumber;

   public BCKConfigureBrainOutputs(BCKNeuralNetwork var1) {
      super(bckframe.top, "Configure Brain Outputs", true);
      this.Net = var1;
      this.targetNet = var1.getId();
      this.makePane();
      this.pack();
      this.centerDialog();
   }

   public void makePane() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = this.createNetIdsPanel();
      var1.add(var2);
      this.tablePanel = this.createTable();
      this.tablePanel.setLayout(new ColumnLayout());
      var1.add(this.tablePanel);
      JPanel var3 = new JPanel();
      var3.setLayout(new FlowLayout());
      JButton var4 = new JButton("Apply");
      var4.addActionListener(new BCKConfigureBrainOutputs$1(this));
      var3.add(var4);
      JButton var5 = new JButton("Done");
      var5.addActionListener(new BCKConfigureBrainOutputs$2(this));
      var3.add(var5);
      var1.add(var3);
      this.getContentPane().removeAll();
      this.getContentPane().add(var1);
   }

   public JPanel createNetIdsPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      var1.add(new JLabel("Network ID :" + this.targetNet));
      JPanel var2 = new JPanel();
      var2.setLayout(new FlowLayout());
      var2.add(new JLabel("Source Network Id number:"));
      this.sourceNetworkNumber = new bckNumberBox();
      this.sourceNetworkNumber.setText(String.valueOf(this.sourceNet));
      var2.add(this.sourceNetworkNumber);
      JButton var3 = new JButton("Load");
      var2.add(var3);
      var3.addActionListener(new BCKConfigureBrainOutputs$3(this));
      var1.add(var2);
      return var1;
   }

   public JPanel createTable() {
      return new BCKNetConnectTable(this.Net, this.sourceNet);
   }

   public void applyPressed() {
      JTable var1 = ((BCKNetConnectTable)this.tablePanel).tableView;
      String var2 = (String)var1.getValueAt(0, 0);
      int var10 = var1.getRowCount();

      for (int var11 = 0; var11 < var10; var11++) {
         var1.editCellAt(0, 0);
         var2 = (String)var1.getValueAt(var11, 1);

         Integer var3;
         try {
            var3 = Integer.valueOf(var2);
         } catch (NumberFormatException var16) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var11 + " column 1 " + var16.toString(), "Alert", 0);
            return;
         }

         int var5 = var3;
         var2 = (String)var1.getValueAt(var11, 2);

         try {
            var3 = Integer.valueOf(var2);
         } catch (NumberFormatException var15) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var11 + " column 2 " + var15.toString(), "Alert", 0);
            return;
         }

         int var6 = var3;
         var2 = (String)var1.getValueAt(var11, 3);

         Double var4;
         try {
            var4 = Double.valueOf(var2);
         } catch (NumberFormatException var14) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var11 + " column 3 " + var14.toString(), "Alert", 0);
            return;
         }

         double var7 = var4;
         var2 = (String)var1.getValueAt(var11, 4);

         try {
            var3 = Integer.valueOf(var2);
         } catch (NumberFormatException var13) {
            JOptionPane.showMessageDialog(null, "Illegal value at row " + var11 + " column 4 " + var13.toString(), "Alert", 0);
            return;
         }

         int var9 = var3;
         if (var6 >= 0) {
            bckframe.top.theBrain.setProxyInput(this.sourceNet, var5, this.targetNet, var6, var7, var9);
         }
      }

      bckframe.top.drawConnections();
      bckframe.top.repaint();
   }

   public void donePressed() {
      this.setVisible(false);
   }

   public void CancelPressed() {
      this.setVisible(false);
   }
}
