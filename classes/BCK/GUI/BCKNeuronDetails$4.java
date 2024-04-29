package BCK.GUI;

import BCK.ANN.BCKNeuron;
import com.sun.java.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class BCKNeuronDetails$4 implements ActionListener {
   public void actionPerformed(ActionEvent var1) {
      BCKNeuron var2 = this.this$0.Net.getNeuron(this.this$0.neuronId);
      int var3 = this.this$0.matrixWidth.getInteger();
      if (var3 < 1) {
         JOptionPane.showMessageDialog(null, "Illegal value for width", "Alert", 0);
      } else {
         double[] var4 = var2.getWeights();
         double var5 = 1000.0;

         for (int var7 = 0; var7 < var4.length; var7++) {
            if (var5 > var4[var7]) {
               var5 = var4[var7];
            }
         }

         if (var5 < 0.0) {
            var5 = -var5;

            for (int var8 = 0; var8 < var4.length; var8++) {
               var4[var8] += var5;
            }
         }

         this.this$0.weightPlot.drawPicture(var4, var3, 5);
      }
   }
}
