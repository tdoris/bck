package BCK.ANN;

import java.io.Serializable;

public class BCKSigmoidNeuron extends BCKNeuron implements Serializable {
   public BCKSigmoidNeuron() {
      super.type = "Sigmoid";
   }

   public double fprime() {
      return super.StateHistory[super.tick] * (1.0 - super.StateHistory[super.tick]);
   }

   protected double transfer() {
      this.timeAdvance();
      super.StateHistory[super.tick] = 1.0 / (1.0 + Math.exp(-(super.activation - 0.5)));
      return super.StateHistory[super.tick];
   }
}
