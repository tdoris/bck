package BCK.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class bckframe$10 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      this.this$0.CreateKohonen.setVisible(true);
      BCKKohonenDrawable var2 = this.this$0.CreateKohonen.getNet();
      if (var2 != null) {
         this.this$0.addANet(var2);
      }
   }
}
