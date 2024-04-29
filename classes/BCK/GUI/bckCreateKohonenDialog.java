package BCK.GUI;

import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

public class bckCreateKohonenDialog extends BCKDialog {
   BCKKohonenDrawable Net;
   JPanel panel1 = new JPanel();
   JButton btnCancel = new JButton();
   private bckNumberBox inField;
   private bckNumberBox outField;

   public bckCreateKohonenDialog(Frame var1) {
      super(var1, "Create a Kohonen Neural Network", true);
      this.setSize(300, 80);
      JPanel var2 = new JPanel();
      var2.setLayout(new BorderLayout());
      JPanel var3 = this.buildInputPanel();
      JPanel var4 = this.buildOutputPanel();
      JPanel var5 = new JPanel();
      JButton var6 = new JButton("OK");
      var6.addActionListener(new bckCreateKohonenDialog$1(this));
      var5.add(var6);
      this.getRootPane().setDefaultButton(var6);
      var6.setToolTipText("This is the OK button");
      var5.setLayout(new FlowLayout(2));
      JButton var7 = new JButton("Cancel");
      var7.addActionListener(new bckCreateKohonenDialog$2(this));
      var5.add(var7);
      var2.add(var3, "North");
      var2.add(var4, "Center");
      var2.add(var5, "South");
      this.getContentPane().add(var2);
      this.pack();
      this.centerDialog();
      this.setResizable(false);
   }

   public JPanel buildInputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(2));
      JLabel var2 = new JLabel("Inputs:            ", 4);
      this.inField = new bckNumberBox();
      this.inField.requestFocus();
      var1.add(var2);
      var1.add(this.inField);
      return var1;
   }

   public JPanel buildOutputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(2));
      JLabel var2 = new JLabel("Outputs:            ", 4);
      this.outField = new bckNumberBox();
      var1.add(var2);
      var1.add(this.outField);
      return var1;
   }

   public void CancelPressed() {
      this.Net = null;
      this.setVisible(false);
   }

   public void OKPressed() {
      int var1 = this.inField.getInteger();
      int var2 = this.outField.getInteger();
      if (var1 < 1) {
         JOptionPane.showMessageDialog(null, "You must enter a number of inputs >0", "Alert", 0);
      } else if (var2 < 1) {
         JOptionPane.showMessageDialog(null, "You must enter a number of outputs >0", "Alert", 0);
      } else {
         try {
            this.Net = new BCKKohonenDrawable(var1, var2);
         } catch (Exception var4) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + var4.toString(), "Alert", 0);
            return;
         }

         this.setVisible(false);
      }
   }

   public BCKKohonenDrawable getNet() {
      return this.Net;
   }
}
