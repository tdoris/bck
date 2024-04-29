package BCK.ANN;

import java.io.Serializable;

public class BCKBipolarNeuron extends BCKNeuron implements Serializable {
   public BCKBipolarNeuron() {
      super.type = "Bipolar";
   }

   protected double transfer() {
      this.timeAdvance();
      if (super.activation > 0.0) {
         super.StateHistory[super.tick] = 1.0;
      } else {
         super.StateHistory[super.tick] = -1.0;
      }

      return super.StateHistory[super.tick];
   }
}
