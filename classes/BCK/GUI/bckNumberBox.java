package BCK.GUI;

import com.sun.java.swing.JTextField;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class bckNumberBox extends JTextField implements KeyListener {
   public bckNumberBox() {
      super(10);
      this.setBackground(Color.white);
      this.setToolTipText("Enter an Integer here");
      this.addKeyListener(this);
   }

   public void keyPressed(KeyEvent var1) {
      if (!Character.isDigit(var1.getKeyChar()) && !Character.isISOControl(var1.getKeyChar())) {
         var1.consume();
      }
   }

   public void keyReleased(KeyEvent var1) {
   }

   public void keyTyped(KeyEvent var1) {
      if (!Character.isDigit(var1.getKeyChar()) && !Character.isISOControl(var1.getKeyChar())) {
         var1.consume();
      }
   }

   public boolean isInteger() {
      String var2 = this.getText();
      if (var2 == null) {
         return false;
      } else {
         try {
            Integer var1 = Integer.valueOf(var2);
            return true;
         } catch (NumberFormatException var3) {
            return false;
         }
      }
   }

   public int getInteger() {
      if (this.isInteger()) {
         String var2 = this.getText();
         Integer var1 = Integer.valueOf(var2);
         return var1;
      } else {
         return -1;
      }
   }
}
