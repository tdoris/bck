package BCK.ANN;

import java.io.Serializable;

public class BCKHopfield extends BCKNeuralNetwork implements Serializable {
   protected int numNeurons;

   protected BCKHopfield() {
   }

   public BCKHopfield(int var1) {
      super(var1, "Bipolar");
      this.numNeurons = var1;
      super.numInputs = var1;
      super.numOutputs = var1;
   }

   public void setTrainingFile(String var1) throws Exception {
      super.trainData = new BCKFilter(var1, super.numInputs);
   }

   public void setTestingFile(String var1) throws Exception {
      super.testData = new BCKFilter(var1, super.numInputs);
   }

   public void setInputFile(String var1) throws Exception {
      super.inputData = new BCKFilter(var1, super.numInputs);
   }

   public void train() throws Exception {
      int var1 = super.trainData.getNumberOfRecords();
      double[][] var2 = new double[var1][this.numNeurons];

      for (int var3 = 0; var3 < var1; var3++) {
         var2[var3] = super.trainData.getNextRecordBipolar();
      }

      for (int var6 = 0; var6 < this.numNeurons; var6++) {
         for (int var7 = 0; var7 < this.numNeurons; var7++) {
            if (var6 != var7) {
               double var4 = 0.0;

               for (int var8 = 0; var8 < var1; var8++) {
                  var4 += var2[var8][var6] * var2[var8][var7];
               }

               this.connectInternal(var7, var6, var4, 0);
            }
         }
      }
   }

   public double[] classify(double[] var1) throws Exception {
      if (var1.length != this.numNeurons) {
         throw new Exception(
            "Number of fields in pattern : " + var1.length + "\n does not match the number of neurons :" + this.numNeurons + "\n in the Hopfield network"
         );
      } else {
         this.categorise(var1, 10 * this.numNeurons);
         return this.getOutputs();
      }
   }

   public void categorise(double[] var1, int var2) throws Exception {
      double[] var3 = new double[this.numNeurons];
      boolean var4 = false;
      int[] var5 = new int[this.numNeurons];
      int var6 = 0;

      while (var6 < this.numNeurons) {
         var5[var6] = var6++;
      }

      for (int var7 = 0; var7 < this.numNeurons; var7++) {
         this.setOutput(var7, var1[var7]);
         var3[var7] = var1[var7];
      }

      int var8 = 0;

      while (!var4 && var8++ < var2) {
         for (int var9 = 0; var9 < this.numNeurons; var9++) {
            if (Math.random() < 0.5) {
               int var10 = (int)Math.floor(Math.random() * (double)this.numNeurons);
               int var11 = var5[var9];
               var5[var9] = var5[var10];
               var5[var10] = var11;
            }
         }

         for (int var12 = 0; var12 < this.numNeurons; var12++) {
            this.calcState(var5[var12]);
         }

         var4 = true;

         for (int var13 = 0; var13 < this.numNeurons; var13++) {
            if (this.getState(var13) != var3[var13]) {
               var4 = false;
               var3[var13] = this.getState(var13);
            }
         }
      }
   }

   public int getBinaryState(int var1) {
      return this.getState(var1) > 0.5 ? 1 : 0;
   }
}
