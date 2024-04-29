package BCK.GUI;

import com.sun.java.swing.JMenuItem;
import com.sun.java.swing.JPopupMenu;

public class BCKNeuronPopup extends JPopupMenu {
   JMenuItem connect = new JMenuItem("Connect to");
   JMenuItem details = new JMenuItem("Show Details");

   public BCKNeuronPopup() {
      this.add(this.connect);
      this.add(this.details);
   }
}
