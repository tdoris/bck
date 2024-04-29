package BCK.ANN;

import java.io.Serializable;

public class BCKRBFDDA extends BCKRBF implements Serializable {
   protected BCKRBFDDA() {
   }

   public BCKRBFDDA(int var1, int var2) throws Exception {
      super(var1, var2);
   }

   public int getWinningNeuron() {
      double var1 = -1.0;
      int var5 = 0;
      int var6 = 0;

      for (int var7 = super.numInputs + super.numHidden; var6 < super.numOutputs; var6++) {
         double var3 = this.getState(var7 + var6);
         if (var3 > var1) {
            var5 = var7 + var6;
            var1 = var3;
         }
      }

      return var5;
   }

   public void forwardPass() {
      super.forwardPass();
      this.normaliseOutputNeurons();
   }

   public void normaliseOutputNeurons() {
      int var1 = 0;
      int var3 = 0;
      var1 = this.getWinningNeuron();

      for (int var4 = super.numInputs + super.numHidden; var3 < super.numOutputs; var3++) {
         int var2 = var4 + var3;
         if (var2 == var1) {
            this.setOutput(var2, 1.0);
         } else {
            this.setOutput(var2, 0.0);
         }
      }
   }

   public int getWinningClass() {
      return this.getWinningNeuron() - super.numInputs - super.numHidden;
   }

   public void train(int var1) throws Exception {
      double var2 = 0.4;
      double var4 = 0.2;
      double var6 = -1.66;
      double[] var8 = new double[super.numOutputs + super.numInputs];
      double[] var9 = new double[super.numInputs];
      double var10 = 0.0;
      double var12 = 0.0;
      int var16 = 0;
      int var17 = -1;
      int var21 = super.trainData.getNumberOfRecords();
      int var22 = 0;
      if (super.trainData == null) {
         throw new Exception("You must specify a training file before attempting to train the network.");
      } else if (super.trainData.getRecordSize() != var8.length) {
         throw new Exception(
            "ERROR - Training file has record of length : "
               + super.trainData.getRecordSize()
               + " the current network requires records of length: "
               + var8.length
         );
      } else {
         for (int var24 = 0; var24 < var1; var24++) {
            super.globalError = 0.0;

            for (int var25 = 0; var25 < var21; var25++) {
               var17 = -1;
               double[] var20 = new double[super.numHidden];

               for (int var26 = super.numInputs + super.numHidden; var26 < super.numInputs + super.numHidden + super.numOutputs; var26++) {
                  int[] var19 = ((BCKNeuron)super.neurons.elementAt(var26)).getInputArrayIds();

                  for (int var27 = 0; var27 < var19.length; var27++) {
                     var20[var19[var27] - super.numInputs] = this.getWeight(var19[var27], var26);
                  }
               }

               var8 = super.trainData.getNextRecord();

               for (int var38 = 0; var38 < super.numInputs; var38++) {
                  var9[var38] = var8[var38];
               }

               for (int var28 = super.numInputs; var28 < super.numOutputs + super.numInputs; var28++) {
                  if (var8[var28] == 1.0) {
                     int var29 = var28 - super.numInputs;
                     var16 = super.numInputs + super.numHidden + var29;
                  }
               }

               BCKNeuron var14 = (BCKNeuron)super.neurons.elementAt(var16);
               int[] var18 = var14.getInputArrayIds();
               boolean[] var23 = new boolean[super.numHidden];

               for (int var39 = 0; var39 < var23.length; var39++) {
                  var23[var39] = true;
               }

               for (int var30 = 0; var30 < var18.length; var30++) {
                  var23[var18[var30] - super.numInputs] = false;
               }

               this.setInput(var9);
               this.forwardPass();

               for (int var31 = 0; var31 < var18.length; var31++) {
                  var22 = var18[var31];
                  if (((BCKRadialNeuron)super.neurons.elementAt(var22)).getState(0) > var2) {
                     var17 = var22;
                     var14.deltaWeight(var22, 0, 1.0);
                  }
               }

               for (int var32 = super.numInputs; var32 < super.numInputs + super.numHidden; var32++) {
                  if (var23[var32 - super.numInputs] && var4 < ((BCKNeuron)super.neurons.elementAt(var32)).getState(0)) {
                     BCKRadialNeuron var15 = (BCKRadialNeuron)super.neurons.elementAt(var32);
                     var10 = var15.getActivation();
                     var12 = Math.sqrt(Math.abs(-var10 / (Math.log(var20[var32 - super.numInputs]) - var6)));
                     var15.setStdev(var12);
                  }
               }

               if (var17 == -1) {
                  this.addHiddenNode(var9, var16);
               }
            }
         }
      }
   }
}
