package BCK.ANN;

import java.io.Serializable;

public class BCKSynapse implements Serializable {
   public BCKNeuron output;
   public int delay;
   public double weight;

   public BCKSynapse() {
      this.output = null;
      this.weight = 0.0;
      this.delay = 0;
   }

   public BCKSynapse(BCKNeuron var1, double var2, int var4) {
      this.output = var1;
      this.weight = var2;
      this.delay = var4;
   }

   public BCKSynapse(BCKSynapse var1) {
      this.output = var1.output;
      this.weight = var1.weight;
      this.delay = var1.delay;
   }

   public void deltaWeight(double var1) {
      this.weight += var1;
   }

   public double getWeight() {
      return this.weight;
   }

   public double getActivation() throws Exception {
      return this.output.getState(this.delay) * this.weight;
   }

   public BCKNeuron getNeuron() {
      return this.output;
   }

   public int getDelay() {
      return this.delay;
   }
}
