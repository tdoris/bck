package BCK.GUI;

import com.sun.java.swing.JProgressBar;
import java.awt.Dimension;

final class BCKProgressPanel$1 extends JProgressBar {
   public Dimension getPreferredSize() {
      return new Dimension(300, super.getPreferredSize().height);
   }
}
