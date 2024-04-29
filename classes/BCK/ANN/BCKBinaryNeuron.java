package BCK.ANN;

import java.io.Serializable;

public class BCKBinaryNeuron extends BCKNeuron implements Serializable {
   public BCKBinaryNeuron() {
      super.type = "Binary";
   }

   protected double transfer() {
      this.timeAdvance();
      if (super.activation > 0.0) {
         super.StateHistory[super.tick] = 1.0;
      } else {
         super.StateHistory[super.tick] = 0.0;
      }

      return super.StateHistory[super.tick];
   }
}
