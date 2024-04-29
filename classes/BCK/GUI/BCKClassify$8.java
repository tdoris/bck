package BCK.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKClassify$8 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      if (this.this$0.printInput.isSelected()) {
         this.this$0.printInputRecords = true;
      } else {
         this.this$0.printInputRecords = false;
      }
   }
}
