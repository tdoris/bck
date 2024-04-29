package BCK.GUI;

import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.FlowLayout;

public class BCKSetNetName extends BCKDialog {
   private JTextField n;
   private boolean OK = false;
   private String curName;

   public BCKSetNetName(String var1) {
      super(bckframe.top, "Set Network Name", true);
      this.curName = var1;
      this.getContentPane().setLayout(new ColumnLayout());
      this.getContentPane().add(this.createNamePanel());
      this.getContentPane().add(this.createButtonPanel());
      this.repaint();
      this.pack();
      this.centerDialog();
   }

   private JPanel createNamePanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JLabel var2 = new JLabel("Enter name :");
      this.n = new JTextField(15);
      this.n.setText(this.curName);
      var1.add(var2);
      var1.add(this.n);
      return var1;
   }

   private JPanel createButtonPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JButton var2 = new JButton("OK");
      var2.addActionListener(new BCKSetNetName$1(this));
      JButton var3 = new JButton("Cancel");
      var3.addActionListener(new BCKSetNetName$2(this));
      var1.add(var2);
      var1.add(var3);
      return var1;
   }

   public String getName() {
      return this.n.getText();
   }

   public boolean isOk() {
      return this.OK;
   }
}
