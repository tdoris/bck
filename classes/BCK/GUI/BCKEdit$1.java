package BCK.GUI;

import com.sun.java.swing.JCheckBoxMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKEdit$1 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      JCheckBoxMenuItem var2 = (JCheckBoxMenuItem)var1.getSource();
      if (var2.isSelected()) {
         this.this$0.editor.setShowLines(true);
      } else {
         this.this$0.editor.setShowLines(false);
      }
   }
}
