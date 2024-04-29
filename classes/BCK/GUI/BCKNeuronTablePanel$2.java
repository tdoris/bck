package BCK.GUI;

import com.sun.java.swing.table.DefaultTableCellRenderer;
import java.awt.Color;

final class BCKNeuronTablePanel$2 extends DefaultTableCellRenderer {
   public void setValue(Object var1) {
      if (var1 instanceof Color) {
         Color var2 = (Color)var1;
         this.setForeground(var2);
         this.setText(var2.getRed() + ", " + var2.getGreen() + ", " + var2.getBlue());
      }
   }
}
