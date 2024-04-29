package BCK.GUI;

import com.sun.java.swing.JMenuItem;
import com.sun.java.swing.JPopupMenu;
import com.sun.java.swing.JSeparator;

public class BCKPopup extends JPopupMenu {
   JMenuItem edit = new JMenuItem("Edit...");
   JMenuItem train = new JMenuItem("Train...");
   JMenuItem cut = new JMenuItem("cut");
   JMenuItem classify = new JMenuItem("Classify");
   JMenuItem map = new JMenuItem("2-d map");
   JMenuItem connect = new JMenuItem("Connect");

   public BCKPopup() {
      this.add(this.edit);
      this.add(this.train);
      this.add(this.classify);
      this.add(this.map);
      this.add(new JSeparator());
      this.add(this.connect);
      this.add(new JSeparator());
      this.add(this.cut);
   }
}
