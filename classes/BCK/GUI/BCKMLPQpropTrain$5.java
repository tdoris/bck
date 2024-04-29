package BCK.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKMLPQpropTrain$5 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      this.this$0.trainForEpochsPressed();
      this.this$0.Net.notifyObservers();
   }
}
