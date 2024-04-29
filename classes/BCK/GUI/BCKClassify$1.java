package BCK.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKClassify$1 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      if (this.this$0.inputOnlyCheck.isSelected()) {
         this.this$0.inputOnly = true;
      } else {
         this.this$0.inputOnly = false;
      }
   }
}
