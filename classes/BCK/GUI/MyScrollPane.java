package BCK.GUI;

import com.sun.java.swing.ImageIcon;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

class MyScrollPane extends JScrollPane {
   static ImageIcon[] icon = new ImageIcon[5];

   public MyScrollPane(int var1, int var2) {
      JPanel var3 = new JPanel();
      var3.setOpaque(false);
      var3.setLayout(new BorderLayout());
      JLabel var4 = new JLabel("Layer " + var1);
      var4.setOpaque(false);
      var3.add(new JLabel(icon[var2 % 5]), "Center");
      var3.add(var4, "North");
      this.getViewport().add(var3);
   }

   public Dimension getMinimumSize() {
      return new Dimension(25, 25);
   }

   public boolean isOpaque() {
      return true;
   }
}
