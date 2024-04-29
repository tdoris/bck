package BCK.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKConnectNetworks$4 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      this.this$0.connectAll();
      this.this$0.makePane();
      this.this$0.pack();
      this.this$0.repaint();
   }
}
