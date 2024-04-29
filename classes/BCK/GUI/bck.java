package BCK.GUI;

import com.sun.java.swing.UIManager;

public class bck {
   public bck() {
      bckframe var1 = new bckframe();
      var1.setVisible(true);
   }

   public static void main(String[] var0) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var1) {
      }

      new bck();
   }
}
