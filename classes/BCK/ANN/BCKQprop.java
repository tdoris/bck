package BCK.ANN;

public class BCKQprop extends BCKMLP {
   double startMu;
   double startEpsilon;

   protected BCKQprop() {
   }

   public BCKQprop(int[] var1) {
      super(var1);
      this.startMu = 0.9;
      this.startEpsilon = 1.1;
   }

   public void setEpsilon(double var1) {
      this.startEpsilon = var1;
   }

   public void setMu(double var1) {
      this.startMu = var1;
   }

   public void train(int var1) throws Exception {
      double[] var2 = new double[super.numOutputs + super.numInputs];
      double[] var3 = new double[super.numInputs];
      double[] var4 = new double[super.numNeurons];
      double[] var5 = new double[super.numOutputs];
      double[] var6 = new double[super.numNeurons];
      double var11 = this.startMu;
      double var13 = this.startEpsilon;
      double var15 = var11 / (var11 + 1.0);
      double[][] var17 = new double[super.numNeurons][super.numNeurons];
      double[][] var18 = new double[super.numNeurons][super.numNeurons];
      if (super.trainData == null) {
         throw new Exception("You must specify a training file before attempting to train the network.");
      } else {
         int var20 = super.trainData.getNumberOfRecords();

         for (int var21 = 0; var21 < var1; var21++) {
            super.globalError = 0.0;

            for (int var22 = 0; var22 < var20; var22++) {
               var2 = super.trainData.getNextRecordRandomly();

               for (int var23 = 0; var23 < super.numInputs; var23++) {
                  var3[var23] = var2[var23];
               }

               for (int var24 = super.numInputs; var24 < super.numOutputs + super.numInputs; var24++) {
                  var5[var24 - super.numInputs] = var2[var24];
               }

               this.setInput(var3);
               this.forwardPass();
               int var25 = 0;
               double var27 = 0.0;
               double var29 = 0.0;
               double var31 = 0.0;

               for (int var33 = super.numNeurons - 1; var33 > super.numInputs - 1; var33--) {
                  boolean var26 = super.numNeurons - var33 <= super.numOutputs;
                  double var9 = this.getState(var33);
                  var31 = this.getPrime(var33);
                  if (var26) {
                     var25 = super.numNeurons - var33 - 1;
                     var4[var33] = var5[var25] - var9;
                     super.globalError = super.globalError + var4[var33] * var4[var33];
                     var6[var33] = var4[var33] * var31;
                  } else {
                     var27 = 0.0;

                     for (int var34 = super.numNeurons - 1; var34 > var33; var34--) {
                        var27 += this.getWeight(var33, var34) * var6[var34];
                     }

                     var6[var33] = var31 * var27;
                  }

                  BCKNeuron[] var19 = this.getNeuron(var33).getInputArray();

                  for (int var48 = 0; var48 < var19.length; var48++) {
                     var29 = var19[var48].getState(0);
                     var17[var33][var48] = var17[var33][var48] - var6[var33] * var29;
                  }
               }
            }

            for (int var37 = super.numNeurons - 1; var37 > super.numInputs - 1; var37--) {
               BCKNeuron[] var36 = this.getNeuron(var37).getInputArray();

               for (int var38 = 0; var38 < var36.length; var38++) {
                  double var40 = this.getWeight(var38, var37);
                  double var42 = super.lastWeightChange[var37][var38];
                  double var44 = var17[var37][var38];
                  double var46 = var18[var37][var38];
                  double var47 = -1.0E-4;
                  double var7 = this.quickprop(var40, var42, var44, var46, var13, var47, var11, var15);
                  super.lastWeightChange[var37][var38] = var7;
                  this.deltaWeight(var36[var38], var37, 0, var7);
                  var18[var37][var38] = var17[var37][var38];
                  var17[var37][var38] = 0.0;
               }
            }
         }
      }
   }

   private double quickprop(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      double var25 = 0.0;
      double var21 = var5 + var11 * var1;
      if (var3 < 0.0) {
         if (var21 > 0.0) {
            var25 -= var9 * var21;
         }

         if (var21 >= var15 * var7) {
            var25 += var13 * var3;
         } else {
            var25 += var3 * var21 / (var7 - var21);
         }
      } else if (var3 > 0.0) {
         if (var21 < 0.0) {
            var25 -= var9 * var21;
         }

         if (var21 <= var15 * var7) {
            var25 += var13 * var3;
         } else {
            var25 += var3 * var21 / (var7 - var21);
         }
      } else {
         var25 -= var9 * var21;
      }

      return var25;
   }
}
