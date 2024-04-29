package BCK.ANN;

import java.io.Serializable;
import java.util.Vector;

public class BCKNeuron implements Serializable {
   public int id;
   protected String type;
   protected String Name;
   protected int MemorySize;
   protected double[] StateHistory;
   protected Vector inputs = new Vector(0);
   protected int tick;
   protected double activation;

   public BCKNeuron() {
      this.MemorySize = 10;
      this.StateHistory = new double[this.MemorySize];
      this.tick = 0;
      this.activation = 0.0;
      this.type = "Neuron";
      this.Name = "No Name";
   }

   public void removeInputs() {
      this.inputs = new Vector(0);
   }

   public void connect(BCKNeuron var1, double var2, int var4) throws Exception {
      if (var1 == null) {
         throw new Exception("Attempt to connect to null neuron in connect in BCKNeuron");
      } else if (var4 < 0) {
         throw new Exception("Attempt to connect to neuron with negative delay in connect in BCKNeuron");
      } else {
         this.inputs.addElement(new BCKSynapse(var1, var2, var4));
      }
   }

   public void deltaWeight(BCKNeuron var1, int var2, double var3) throws Exception {
      for (int var6 = 0; var6 < this.inputs.size(); var6++) {
         BCKSynapse var5 = (BCKSynapse)this.inputs.elementAt(var6);
         if (var1 == var5.output && var2 == var5.delay) {
            var5.weight += var3;
            return;
         }
      }

      throw new Exception("Attempt to alter the weight of a non-existent synapse in deltaWeight in BCKNeuron");
   }

   public void deltaWeight(int var1, int var2, double var3) throws Exception {
      for (int var6 = 0; var6 < this.inputs.size(); var6++) {
         BCKSynapse var5 = (BCKSynapse)this.inputs.elementAt(var6);
         if (var1 == var5.output.id && var2 == var5.delay) {
            var5.weight += var3;
            return;
         }
      }

      throw new Exception("Attempt to alter the weight of a non-existent synapse in deltaWeight in BCKNeuron");
   }

   public void setWeight(int var1, int var2, double var3) throws Exception {
      for (int var6 = 0; var6 < this.inputs.size(); var6++) {
         BCKSynapse var5 = (BCKSynapse)this.inputs.elementAt(var6);
         if (var1 == var5.output.id && var2 == var5.delay) {
            var5.weight = var3;
            return;
         }
      }

      throw new Exception("Attempt to alter the weight of a non-existent synapse in setWeight in BCKNeuron");
   }

   protected double calcActivation() throws Exception {
      this.activation = 0.0;

      for (int var2 = 0; var2 < this.inputs.size(); var2++) {
         BCKSynapse var1 = (BCKSynapse)this.inputs.elementAt(var2);
         this.activation = this.activation + var1.output.getState(var1.delay) * var1.weight;
      }

      return this.activation;
   }

   public double fprime() {
      return this.StateHistory[this.tick] * (1.0 - this.StateHistory[this.tick]);
   }

   protected double transfer() {
      this.timeAdvance();
      this.StateHistory[this.tick] = 1.0 / (1.0 + Math.exp(-(this.activation - 0.5)));
      return this.StateHistory[this.tick];
   }

   protected void timeAdvance() {
      this.tick = (this.tick + 1) % this.MemorySize;
   }

   public double calcState() {
      try {
         this.calcActivation();
      } catch (Exception var2) {
         System.out.println(var2.toString());
      }

      return this.transfer();
   }

   public double[] getWeights() {
      double[] var1 = new double[this.inputs.size()];

      for (int var2 = 0; var2 < this.inputs.size(); var2++) {
         var1[var2] = ((BCKSynapse)this.inputs.elementAt(var2)).weight;
      }

      return var1;
   }

   public BCKNeuron[] getInputArray() {
      BCKNeuron[] var1 = new BCKNeuron[this.inputs.size()];

      for (int var3 = 0; var3 < this.inputs.size(); var3++) {
         BCKSynapse var2 = (BCKSynapse)this.inputs.elementAt(var3);
         var1[var3] = var2.output;
      }

      return var1;
   }

   public BCKSynapse[] getSynapseArray() {
      BCKSynapse[] var1 = new BCKSynapse[this.inputs.size()];

      for (int var2 = 0; var2 < this.inputs.size(); var2++) {
         var1[var2] = (BCKSynapse)this.inputs.elementAt(var2);
      }

      return var1;
   }

   public int[] getInputArrayIds() {
      int[] var1 = new int[this.inputs.size()];

      for (int var3 = 0; var3 < this.inputs.size(); var3++) {
         BCKSynapse var2 = (BCKSynapse)this.inputs.elementAt(var3);
         var1[var3] = var2.output.id;
      }

      return var1;
   }

   public double getActivation() {
      return this.activation;
   }

   public String getType() {
      return this.type;
   }

   public void setOutput(double var1) {
      this.timeAdvance();
      this.StateHistory[this.tick] = var1;
   }

   public void setName(String var1) {
      this.Name = var1;
   }

   public String getName() {
      return this.Name;
   }

   public double getState(int var1) {
      if (var1 >= this.MemorySize || var1 < 0) {
         return 0.0;
      } else {
         return this.tick >= var1 ? this.StateHistory[this.tick - var1] : this.StateHistory[this.MemorySize - (var1 - this.tick)];
      }
   }

   public boolean isConnectedTo(BCKNeuron var1) {
      for (int var3 = 0; var3 < this.inputs.size(); var3++) {
         BCKSynapse var2 = (BCKSynapse)this.inputs.elementAt(var3);
         if (var2.output == var1) {
            return true;
         }
      }

      return false;
   }

   public boolean isConnectedTo(BCKNeuron var1, int var2) {
      for (int var4 = 0; var4 < this.inputs.size(); var4++) {
         BCKSynapse var3 = (BCKSynapse)this.inputs.elementAt(var4);
         if (var3.output == var1 && var3.delay == var2) {
            return true;
         }
      }

      return false;
   }

   public BCKSynapse getSynapse(BCKNeuron var1) {
      for (int var3 = 0; var3 < this.inputs.size(); var3++) {
         BCKSynapse var2 = (BCKSynapse)this.inputs.elementAt(var3);
         if (var2.output == var1) {
            return var2;
         }
      }

      return null;
   }
}
