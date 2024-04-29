package BCK.ANN;

import java.io.Serializable;

public class BCKLinearNeuron extends BCKNeuron implements Serializable {
   public BCKLinearNeuron() {
      super.type = "Linear";
   }

   protected double transfer() {
      this.timeAdvance();
      super.StateHistory[super.tick] = super.activation;
      return super.StateHistory[super.tick];
   }
}
