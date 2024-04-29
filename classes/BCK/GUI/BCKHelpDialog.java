package BCK.GUI;

import com.sun.java.swing.JLabel;
import com.sun.java.swing.JPanel;

public class BCKHelpDialog extends BCKDialog {
   public BCKHelpDialog() {
      super(bckframe.top, "HELP", true);
      this.setSize(400, 200);
      this.centerDialog();
      JPanel var1 = this.createHelpPanel();
      this.getContentPane().add(var1);
   }

   public JPanel createHelpPanel() {
      JPanel var1 = new JPanel();
      var1.add(new JLabel("The user manual is located at : " + bckframe.home + "Help"));
      return var1;
   }
}
