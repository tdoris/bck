package BCK.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class bckframe$8 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      this.this$0.CreateHopfield.setVisible(true);
      BCKHopfieldDrawable var2 = this.this$0.CreateHopfield.getNet();
      if (var2 != null) {
         this.this$0.addANet(var2);
      }
   }
}
