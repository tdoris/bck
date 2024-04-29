package BCK.GUI;

import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

public class bckCreateHopfieldDialog extends BCKDialog {
   BCKHopfieldDrawable Net;
   JPanel panel1 = new JPanel();
   JButton btnCancel = new JButton();
   private bckNumberBox inField;

   public bckCreateHopfieldDialog(Frame var1) {
      super(var1, "Create a Hopfield Neural Network", true);
      this.setSize(300, 80);
      JPanel var2 = new JPanel();
      var2.setLayout(new BorderLayout());
      JPanel var3 = this.buildInputPanel();
      JPanel var4 = new JPanel();
      JButton var5 = new JButton("OK");
      var5.addActionListener(new bckCreateHopfieldDialog$1(this));
      var4.add(var5);
      this.getRootPane().setDefaultButton(var5);
      var5.setToolTipText("This is the OK button");
      var4.setLayout(new FlowLayout(2));
      JButton var6 = new JButton("Cancel");
      var6.addActionListener(new bckCreateHopfieldDialog$2(this));
      var4.add(var6);
      var2.add(var3, "Center");
      var2.add(var4, "South");
      this.getContentPane().add(var2);
      this.pack();
      this.centerDialog();
      this.setResizable(false);
   }

   public JPanel buildInputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout(2));
      JLabel var2 = new JLabel("Number of Neurons:            ", 4);
      this.inField = new bckNumberBox();
      this.inField.requestFocus();
      var1.add(var2);
      var1.add(this.inField);
      return var1;
   }

   public void CancelPressed() {
      this.Net = null;
      this.setVisible(false);
   }

   public void OKPressed() {
      int var1 = this.inField.getInteger();
      if (var1 < 1) {
         JOptionPane.showMessageDialog(null, "You must enter a number of neurons >0", "Alert", 0);
      } else {
         this.Net = new BCKHopfieldDrawable(var1);
         this.setVisible(false);
      }
   }

   public BCKHopfieldDrawable getNet() {
      return this.Net;
   }
}
