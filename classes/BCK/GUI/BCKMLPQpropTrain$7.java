package BCK.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKMLPQpropTrain$7 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      this.this$0.restartPressed();
      this.this$0.epochsSinceRestart = 0;
      this.this$0.epochCount.setText(String.valueOf(this.this$0.epochsSinceRestart));
      this.this$0.Net.notifyObservers();
   }
}
