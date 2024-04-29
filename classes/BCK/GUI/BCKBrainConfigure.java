package BCK.GUI;

import BCK.ANN.BCKLinearNeuron;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class BCKBrainConfigure extends BCKDialog {
   JPanel panel1 = new JPanel();
   JButton btnCancel = new JButton();
   private bckNumberBox inField;
   private bckNumberBox outField;

   public BCKBrainConfigure() {
      super(bckframe.top, "Configuration", true);
      this.setSize(300, 80);
      JPanel var1 = new JPanel();
      var1.setLayout(new BorderLayout());
      JPanel var2 = this.buildInputPanel();
      JPanel var3 = this.buildOutputPanel();
      JPanel var4 = new JPanel();
      JButton var5 = new JButton("OK");
      var5.addActionListener(new BCKBrainConfigure$1(this));
      var4.add(var5);
      this.getRootPane().setDefaultButton(var5);
      var5.setToolTipText("This is the OK button");
      var4.setLayout(new FlowLayout(2));
      JButton var6 = new JButton("Cancel");
      var6.addActionListener(new BCKBrainConfigure$2(this));
      var4.add(var6);
      var1.add(var2, "North");
      var1.add(var3, "Center");
      var1.add(var4, "South");
      this.getContentPane().add(var1);
      this.pack();
      this.centerDialog();
      this.setResizable(false);
   }

   public JPanel buildInputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(2));
      JLabel var2 = new JLabel("Inputs:              ", 4);
      this.inField = new bckNumberBox();
      this.inField.requestFocus();
      var1.add(var2);
      var1.add(this.inField);
      return var1;
   }

   public JPanel buildOutputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(2));
      JLabel var2 = new JLabel("Outputs:             ", 4);
      this.outField = new bckNumberBox();
      var1.add(var2);
      var1.add(this.outField);
      return var1;
   }

   public void CancelPressed() {
      this.setVisible(false);
   }

   public void OKPressed() {
      int var1 = this.inField.getInteger();
      int var2 = this.outField.getInteger();
      if (var1 < 0) {
         JOptionPane.showMessageDialog(null, "You must enter a positive number of inputs", "Alert", 0);
      } else if (var2 < 0) {
         JOptionPane.showMessageDialog(null, "You must enter a positive number of outputs", "Alert", 0);
      } else {
         bckframe.top.theBrain.setNumInputs(var1);
         bckframe.top.theBrain.setNumOutputs(var2);

         for (int var3 = 0; var3 < var1 + var2; var3++) {
            bckframe.top.theBrain.addNeuron(new BCKLinearNeuron());
         }

         this.setVisible(false);
      }
   }
}
