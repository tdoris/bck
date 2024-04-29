package BCK.GUI;

import com.sun.java.swing.UIManager;

public class bckgui {
   public bckgui() {
      bckframe var1 = new bckframe();
      var1.setVisible(true);
   }

   public static void main(String[] var0) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var1) {
      }

      new bckgui();
   }
}
