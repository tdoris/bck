package BCK.ANN;

import java.io.Serializable;

public class BCKNetConnection implements Serializable {
   public int sourceNet;
   public int sourceNeuron;
   public int targetNet;
   public int targetNeuron;
   public double weight;
   public int delay;

   protected BCKNetConnection() {
   }

   protected BCKNetConnection(int var1, int var2, int var3, int var4, double var5, int var7) {
      this.sourceNet = var1;
      this.sourceNeuron = var2;
      this.targetNet = var3;
      this.targetNeuron = var4;
      this.weight = var5;
      this.delay = var7;
   }

   public int getSourceNet() {
      return this.sourceNet;
   }

   public int getTargetNet() {
      return this.targetNet;
   }

   public int getSourceNeuron() {
      return this.sourceNeuron;
   }

   public int getTargetNeuron() {
      return this.targetNeuron;
   }
}
