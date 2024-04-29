package BCK.ANN;

import java.io.Serializable;

public class BCKRadialNeuron extends BCKNeuron implements Serializable {
   protected double stdev;

   public BCKRadialNeuron() {
      super.type = "Radial";
      this.stdev = 1000.0;
   }

   public BCKRadialNeuron(double var1) {
      super.type = "Radial";
      this.stdev = var1;
   }

   public double fprime() {
      return -Math.exp(-super.activation);
   }

   public double calcActivation() throws Exception {
      super.activation = 0.0;

      for (int var4 = 0; var4 < super.inputs.size(); var4++) {
         BCKSynapse var3 = (BCKSynapse)super.inputs.elementAt(var4);
         double var1 = var3.output.getState(var3.delay) - var3.weight;
         super.activation += var1 * var1;
      }

      return super.activation;
   }

   protected double transfer() {
      this.timeAdvance();
      double var1 = 1.0 / (this.stdev * this.stdev);
      super.StateHistory[super.tick] = Math.exp(-(var1 * super.activation));
      return super.StateHistory[super.tick];
   }

   public void setStdev(double var1) {
      this.stdev = var1;
   }

   public double getStdev() {
      return this.stdev;
   }
}
