package BCK.GUI;

import com.sun.java.swing.JTextField;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class bckRealNumberBox extends JTextField implements KeyListener {
   public bckRealNumberBox() {
      super(10);
      this.setBackground(Color.white);
      this.setToolTipText("Enter a Number here");
      this.addKeyListener(this);
   }

   public void keyPressed(KeyEvent var1) {
      char var2 = var1.getKeyChar();
      if (!Character.isDigit(var2) && !Character.isISOControl(var2) && var2 != '.' && var2 != '-') {
         var1.consume();
      }
   }

   public void keyReleased(KeyEvent var1) {
   }

   public void keyTyped(KeyEvent var1) {
      char var2 = var1.getKeyChar();
      if (!Character.isDigit(var2) && !Character.isISOControl(var2) && var2 != '.' && var2 != '-') {
         var1.consume();
      }
   }

   public boolean isRealNumber() {
      String var2 = this.getText();
      if (var2 == null) {
         return false;
      } else {
         try {
            Double var1 = Double.valueOf(var2);
            return true;
         } catch (NumberFormatException var3) {
            return false;
         }
      }
   }

   public double getRealNumber() {
      if (this.isRealNumber()) {
         String var2 = this.getText();
         Double var1 = Double.valueOf(var2);
         return var1;
      } else {
         return Double.NaN;
      }
   }
}
