package BCK.GUI;

import com.sun.java.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKConfigureBrainOutputs$3 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      this.this$0.sourceNet = this.this$0.sourceNetworkNumber.getInteger();
      if (this.this$0.sourceNet >= 0 && this.this$0.sourceNet < bckframe.top.theBrain.size()) {
         this.this$0.makePane();
         this.this$0.pack();
         this.this$0.repaint();
      } else {
         this.this$0.sourceNet = 0;
         JOptionPane.showMessageDialog(null, "Illegal value for source net", "Alert", 0);
      }
   }
}
