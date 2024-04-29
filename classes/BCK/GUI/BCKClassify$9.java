package BCK.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKClassify$9 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      if (this.this$0.printClassNames.isSelected()) {
         this.this$0.printClass = true;
      } else {
         this.this$0.printClass = false;
      }
   }
}
