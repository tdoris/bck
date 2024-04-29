package BCK.ANN;

public class BCKKohonen extends BCKNeuralNetwork {
   public int side;
   protected double globalError;
   double currentEta = 0.9;
   double startEta = 0.9;
   double etaShrink = 0.1;
   int startNeighbourhood;

   protected BCKKohonen() {
   }

   public BCKKohonen(int var1, int var2) throws Exception {
      super.numInputs = var1;
      super.numOutputs = var2;
      this.side = (int)Math.sqrt((double)var2);
      if (Math.sqrt((double)var2) != (double)this.side) {
         throw new Exception("You must have a square number of output neurons in Kohonen");
      } else {
         this.startNeighbourhood = (int)Math.ceil(0.5 * (double)this.side);

         for (int var5 = 0; var5 < var1; var5++) {
            this.addNeuron(new BCKLinearNeuron());
         }

         for (int var6 = 0; var6 < var2; var6++) {
            this.addNeuron(new BCKRadialNeuron(1.0));
         }

         for (int var7 = var1; var7 < var1 + var2; var7++) {
            for (int var8 = 0; var8 < var1; var8++) {
               double var3 = 2.0 - 4.0 * Math.random();
               this.connectInternal(var8, var7, var3, 0);
            }
         }
      }
   }

   public void setEta(double var1) {
      this.startEta = var1;
   }

   public double getEta() {
      return this.startEta;
   }

   public void setNeighbourhood(int var1) {
      this.startNeighbourhood = var1;
   }

   public int getNeighbourhood() {
      return this.startNeighbourhood;
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

   public void train(int var1) throws Exception {
      this.currentEta = this.startEta;
      int var2 = 0;
      short var3 = 1000;
      byte var4 = 10;
      int var5 = 1;
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;
      int var10 = this.startNeighbourhood;
      if (super.trainData == null) {
         throw new Exception("You must specify a training file before attempting to train the network.");
      } else {
         int var16 = super.trainData.getNumberOfRecords();

         for (int var17 = 0; var17 < var1; var17++) {
            this.globalError = 0.0;

            for (int var18 = 0; var18 < var16; var18++) {
               double[] var9 = super.trainData.getNextRecordRandomly();
               this.classify(var9);
               var8 = this.getWinningNeuron() - super.numInputs;
               double[] var15 = this.getNeuron(this.getWinningNeuron()).getWeights();

               for (int var19 = 0; var19 < super.numInputs; var19++) {
                  double var20 = var15[var19] - var9[var19];
                  this.globalError = this.globalError + Math.sqrt(var20 * var20) / (double)var16 * (double)super.numInputs;
               }

               var6 = var8 % this.side;
               var7 = var8 / this.side;
               int var11 = Math.max(0, var6 - var10);
               int var12 = Math.min(this.side - 1, var6 + var10);
               int var13 = Math.max(0, var7 - var10);
               int var14 = Math.min(this.side - 1, var7 + var10);

               for (int var33 = 0; var33 < var9.length; var33++) {
                  BCKNeuron var21 = this.getNeuron(this.getWinningNeuron());
                  var15 = var21.getWeights();
                  double var22 = this.currentEta * (var9[var33] - var15[var33]);
                  var21.deltaWeight(var33, 0, var22);
               }

               for (int var34 = var13; var34 <= var14; var34++) {
                  for (int var35 = var11; var35 <= var12; var35++) {
                     int var23 = super.numInputs + this.side * var34 + var35;
                     BCKNeuron var24 = this.getNeuron(var23);
                     var15 = var24.getWeights();

                     for (int var25 = 0; var25 < var9.length; var25++) {
                        double var26 = this.currentEta * (var9[var25] - var15[var25]);
                        var24.deltaWeight(var25, 0, var26);
                     }
                  }
               }
            }

            var2++;
            var10 = this.startNeighbourhood * (1 - var2 / var3);
            this.currentEta = this.startEta * (double)(1 - var2 / var3);
            if (var2 > var3) {
               var2 = 0;
               var3 *= var4;
               var10 = 0;
               this.startEta = this.startEta * this.etaShrink;
               this.currentEta = this.startEta;
               var5++;
            }
         }
      }
   }

   public void normaliseWeights(int var1) throws Exception {
      double var2 = 0.0;
      double var4 = 0.0;
      BCKNeuron var6 = this.getNeuron(var1);

      for (int var7 = 0; var7 < super.numInputs; var7++) {
         var4 = this.getWeight(var7, var1);
         var2 += var4 * var4;
      }

      var2 = Math.sqrt(var2);

      for (int var8 = 0; var8 < super.numInputs; var8++) {
         var6.setWeight(var8, 0, this.getWeight(var8, var1) / var2);
      }
   }

   public int getWinningNeuron() {
      double var1 = -1.0;
      int var5 = 0;
      int var6 = 0;

      for (int var7 = super.numInputs; var6 < super.numOutputs; var6++) {
         double var3 = this.getState(var7 + var6);
         if (var3 > var1) {
            var5 = var7 + var6;
            var1 = var3;
         }
      }

      return var5;
   }

   public void normaliseOutputNeurons() {
      int var1 = 0;
      int var3 = 0;
      var1 = this.getWinningNeuron();

      for (int var4 = super.numInputs; var3 < super.numOutputs; var3++) {
         int var2 = var4 + var3;
         if (var2 == var1) {
            this.setOutput(var2, 1.0);
         } else {
            this.setOutput(var2, 0.0);
         }
      }
   }

   public int getWinningClass() {
      return this.getWinningNeuron() - super.numInputs;
   }

   public double getGlobalError() {
      return this.globalError;
   }
}
