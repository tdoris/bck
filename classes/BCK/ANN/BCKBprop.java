package BCK.ANN;

import java.io.Serializable;

public class BCKBprop extends BCKMLP implements Serializable {
   private double LearningRate = 0.9;
   private double Momentum = 0.7;

   protected BCKBprop() {
   }

   public BCKBprop(int[] var1) {
      super(var1);
   }

   public void setLearningRate(double var1) {
      this.LearningRate = var1;
   }

   public void setMomentum(double var1) {
      this.Momentum = var1;
   }

   public void train(int var1) throws Exception {
      double[] var2 = new double[super.numOutputs + super.numInputs];
      double[] var3 = new double[super.numInputs];
      double[] var4 = new double[super.numNeurons];
      double[] var5 = new double[super.numOutputs];
      double[] var6 = new double[super.numNeurons];
      double var7 = 0.0;
      double var9 = 0.0;
      if (super.trainData == null) {
         throw new Exception("You must specify a training file before attempting to train the network.");
      } else {
         int var12 = super.trainData.getNumberOfRecords();

         for (int var13 = 0; var13 < var1; var13++) {
            super.globalError = 0.0;

            for (int var14 = 0; var14 < var12; var14++) {
               var2 = super.trainData.getNextRecord();

               for (int var15 = 0; var15 < super.numInputs; var15++) {
                  var3[var15] = var2[var15];
               }

               for (int var16 = super.numInputs; var16 < super.numOutputs + super.numInputs; var16++) {
                  var5[var16 - super.numInputs] = var2[var16];
               }

               this.setInput(var3);
               this.forwardPass();
               int var17 = 0;
               double var19 = 0.0;
               double var21 = 0.0;
               double var23 = 0.0;

               for (int var25 = super.numNeurons - 1; var25 > super.numInputs - 1; var25--) {
                  boolean var18 = super.numNeurons - var25 <= super.numOutputs;
                  var9 = this.getState(var25);
                  var23 = this.getPrime(var25);
                  if (var18) {
                     var17 = super.numNeurons - var25 - 1;
                     var4[var25] = var5[var17] - var9;
                     super.globalError = super.globalError + var4[var25] * var4[var25];
                     var6[var25] = var4[var25] * var23;
                  } else {
                     var19 = 0.0;

                     for (int var26 = super.numNeurons - 1; var26 > var25; var26--) {
                        var19 += (this.getWeight(var25, var26) + super.lastWeightChange[var26][var25]) * var6[var26];
                     }

                     var6[var25] = var23 * var19;
                  }

                  BCKNeuron[] var11 = this.getInputArray(var25);

                  for (int var36 = 0; var36 < var11.length; var36++) {
                     try {
                        var21 = var11[var36].getState(0);
                     } catch (Exception var29) {
                        System.out.println("Error getting output of " + var36 + "th input to Neuron number " + var25 + " " + var29.toString());
                     }

                     var7 = this.Momentum * super.lastWeightChange[var25][var36] + this.LearningRate * var6[var25] * var21;
                     super.lastWeightChange[var25][var36] = var7;

                     try {
                        this.deltaWeight(var11[var36], var25, 0, var7);
                     } catch (Exception var28) {
                        System.out.println("Error while updating weights " + var28.toString());
                     }
                  }
               }
            }
         }
      }
   }
}
