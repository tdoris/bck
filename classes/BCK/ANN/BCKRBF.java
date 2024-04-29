package BCK.ANN;

import java.io.Serializable;

public class BCKRBF extends BCKNeuralNetwork implements Serializable {
   protected int numNeurons;
   protected int numHidden;
   protected double globalError;

   protected BCKRBF() {
   }

   public BCKRBF(int var1, int var2) throws Exception {
      this.numNeurons = var1 + var2;
      super.numOutputs = var2;
      super.numInputs = var1;
      this.numHidden = 0;

      for (int var3 = 0; var3 < super.numInputs; var3++) {
         this.addNeuron(new BCKLinearNeuron());
      }

      for (int var4 = 0; var4 < super.numOutputs; var4++) {
         this.addNeuron(new BCKLinearNeuron());
      }
   }

   public void addHiddenNode(double[] var1, int var2) throws Exception {
      if (var1.length != super.numInputs) {
         throw new Exception("Tried to add a hidden node in RBF with incorrect number of input weights specified");
      } else {
         BCKRadialNeuron var3 = new BCKRadialNeuron();
         this.insertNeuron(var3, super.numInputs + this.numHidden);

         for (int var4 = 0; var4 < super.numInputs; var4++) {
            this.connectInternal(var4, var3.id, var1[var4], 0);
         }

         this.connectInternal(var3.id, var2 + 1, 1.0, 0);
         this.numNeurons++;
         this.numHidden++;
      }
   }

   public void setInput(double[] var1) {
      for (int var2 = 0; var2 < super.numInputs; var2++) {
         this.setOutput(var2, var1[var2]);
      }
   }

   public double[] getOutputs() {
      double[] var1 = new double[super.numOutputs];
      int var2 = 0;

      for (int var3 = super.numInputs + this.numHidden; var2 < super.numOutputs; var2++) {
         var1[var2] = this.getState(var3 + var2);
      }

      return var1;
   }

   public int getNumHidden() {
      return this.numHidden;
   }

   public double getGlobalError() {
      return this.globalError;
   }
}
