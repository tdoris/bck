package BCK.GUI;

import BCK.ANN.BCKNeuralNetwork;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class bckframe$9 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      this.this$0.CreateMLP.setVisible(true);
      BCKNeuralNetwork var2 = this.this$0.CreateMLP.getNet();
      if (var2 instanceof BCKMLPQpropDrawable) {
         BCKMLPQpropDrawable var4 = (BCKMLPQpropDrawable)var2;
         this.this$0.addANet(var4);
      } else if (var2 instanceof BCKMLPBpropDrawable) {
         BCKMLPBpropDrawable var3 = (BCKMLPBpropDrawable)var2;
         this.this$0.addANet(var3);
      }
   }
}
