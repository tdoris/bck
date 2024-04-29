package BCK.GUI;

import com.sun.java.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKImageConversion$6 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      String var2 = "";
      int var3 = this.this$0.size.getInteger();
      int var4 = this.this$0.classId.getInteger();
      if (var3 < 1 || var4 < 1) {
         JOptionPane.showMessageDialog(bckframe.top, "You must specify valid size and class parameters", "Alert", 0);
      }

      for (int var5 = 0; var5 < var3; var5++) {
         if (var5 + 1 == var4) {
            var2 = var2 + "1 ";
         } else {
            var2 = var2 + "0 ";
         }

         BCKImageConversion.access$0(this.this$0).setText(var2);
      }
   }
}
