package BCK.ANN;

import java.io.Serializable;

public class BCKMLP extends BCKNeuralNetwork implements Serializable {
   protected int numNeurons;
   protected transient double globalError;
   protected transient double[][] lastWeightChange;
   protected int[] numNeuronsInLayer;
   protected BCKNeuron bias;

   protected BCKMLP() {
   }

   public BCKMLP(int[] var1) {
      super(0, "Sigmoid");
      this.numNeurons = 0;
      this.numNeuronsInLayer = new int[var1.length];
      super.numOutputs = var1[var1.length - 1];
      super.numInputs = var1[0];

      for (int var2 = 0; var2 < var1.length; var2++) {
         this.numNeuronsInLayer[var2] = var1[var2];
         this.numNeurons = this.numNeurons + var1[var2];
      }

      this.lastWeightChange = new double[this.numNeurons][this.numNeurons];
      int var5 = 0;
      int var6 = super.numInputs;
      int var7 = 0;
      double var8 = 0.0;

      for (int var10 = 0; var10 < this.numNeurons; var10++) {
         this.addNeuron(new BCKSigmoidNeuron());
      }

      this.bias = new BCKNeuron();
      this.bias.setOutput(1.0);
      this.bias.id = -1;

      for (int var11 = 1; var11 < this.numNeuronsInLayer.length; var11++) {
         var7 = this.numNeuronsInLayer[var11 - 1];
         var8 = 6.8 / (double)var7;

         for (int var12 = 0; var12 < this.numNeuronsInLayer[var11]; var12++) {
            try {
               this.connectExternal(this.bias, var6 + var12, 0.5, 0);
            } catch (Exception var16) {
               System.out.println("Error connecting to bias neuron" + var16.toString());
            }

            for (int var13 = 0; var13 < this.numNeuronsInLayer[var11 - 1]; var13++) {
               double var3 = var8 * (0.5 - Math.random());

               try {
                  this.connectInternal(var5 + var13, var6 + var12, var3, 0);
               } catch (Exception var15) {
                  System.out.println("Error in MLP constructor while connecting neurons: " + var15.toString());
               }
            }
         }

         var5 += this.numNeuronsInLayer[var11 - 1];
         var6 += this.numNeuronsInLayer[var11];
      }
   }

   public BCKNeuron getBias() {
      return this.bias;
   }

   public void forwardPass() {
      try {
         for (int var1 = super.numInputs; var1 < this.getNumberOfNeurons(); var1++) {
            this.calcState(var1);
         }
      } catch (Exception var2) {
         System.out.println(var2.toString());
      }
   }

   public void setInput(double[] var1) {
      for (int var2 = 0; var2 < var1.length; var2++) {
         this.setOutput(var2, var1[var2]);
      }
   }

   public double[] getOutputs() {
      double[] var1 = new double[super.numOutputs];
      int var2 = 0;

      for (int var3 = this.numNeurons - super.numOutputs; var2 < super.numOutputs; var2++) {
         var1[var2] = this.getState(var3 + var2);
      }

      return var1;
   }

   public double getGlobalError() {
      return this.globalError;
   }

   public void randomiseWeights() {
      for (int var2 = 0; var2 < super.neurons.size(); var2++) {
         BCKNeuron var3 = (BCKNeuron)super.neurons.elementAt(var2);
         BCKSynapse[] var1 = var3.getSynapseArray();

         for (int var4 = 0; var4 < var1.length; var4++) {
            var1[var4].weight = 3.0 * (0.5 - Math.random());
         }
      }

      for (int var5 = 0; var5 < this.lastWeightChange.length; var5++) {
         for (int var6 = 0; var6 < this.lastWeightChange[var5].length; var6++) {
            this.lastWeightChange[var5][var6] = 0.0;
         }
      }
   }

   public int getNumberOfLayers() {
      return this.numNeuronsInLayer.length;
   }

   public int getNumberOfNeuronsInLayer(int var1) {
      return this.numNeuronsInLayer[var1];
   }
}
