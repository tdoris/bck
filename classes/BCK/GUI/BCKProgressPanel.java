package BCK.GUI;

import com.sun.java.swing.JPanel;
import com.sun.java.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Insets;

public class BCKProgressPanel extends JPanel {
   JProgressBar progressBar;

   public BCKProgressPanel() {
      this.setLayout(new BorderLayout());
      JPanel var1 = new JPanel();
      this.add(var1, "South");
      this.progressBar = new BCKProgressPanel$1();
      this.progressBar.getAccessibleContext().setAccessibleName("Text loading progress");
      var1.add(this.progressBar);
      this.progressBar.setValue(0);
      this.progressBar.setMinimum(0);
   }

   public Insets getInsets() {
      return new Insets(10, 10, 10, 10);
   }

   public void setMaximum(int var1) {
      this.progressBar.setMaximum(var1);
   }

   public void setValue(int var1) {
      this.progressBar.setValue(var1);
   }
}
