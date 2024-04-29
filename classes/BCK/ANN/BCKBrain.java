package BCK.ANN;

import java.io.Serializable;
import java.util.Vector;

public class BCKBrain extends BCKRBFDDA implements Serializable {
   protected Vector Nets = new Vector(0);
   public Vector proxyInputs = new Vector(0);

   public BCKBrain() {
      super.idNumber = 0;
      this.addNetwork(this);
   }

   public void setNumInputs(int var1) {
      super.numInputs = var1;
   }

   public void setNumOutputs(int var1) {
      super.numOutputs = var1;
   }

   public void addNetwork(BCKNeuralNetwork var1) {
      var1.setId(this.Nets.size());
      this.Nets.addElement(var1);
      this.proxyInputs.addElement(new Vector(0));
   }

   public BCKNeuralNetwork getNet(int var1) {
      return (BCKNeuralNetwork)this.Nets.elementAt(var1);
   }

   public void forwardPass() {
      for (int var1 = 1; var1 < this.Nets.size(); var1++) {
         this.setInputsForNet(var1);
         ((BCKNeuralNetwork)this.Nets.elementAt(var1)).forwardPass();
      }

      super.forwardPass();
      this.setInputsForNet(0);
   }

   public void setProxyInput(int var1, int var2, int var3, int var4, double var5, int var7) {
      Vector var8 = (Vector)this.proxyInputs.elementAt(var3);
      var8.addElement(new BCKNetConnection(var1, var2, var3, var4, var5, var7));
   }

   private void setInputsForNet(int var1) {
      Vector var2 = (Vector)this.proxyInputs.elementAt(var1);
      BCKNeuralNetwork var3 = (BCKNeuralNetwork)this.Nets.elementAt(var1);
      var3.getNumInputs();

      for (int var7 = 0; var7 < var2.size(); var7++) {
         BCKNetConnection var4 = (BCKNetConnection)var2.elementAt(var7);
         double var5;
         if (var4.sourceNet == 0) {
            var5 = this.getState(var4.sourceNeuron);
         } else {
            var5 = this.getState(var4.sourceNet, var4.sourceNeuron, var4.delay);
         }

         this.setOutput(var4.targetNet, var4.targetNeuron, var5 * var4.weight);
      }
   }

   public double[] getNextInputRecord(BCKFilter var1) throws Exception {
      if (var1 == null) {
         throw new Exception("Cannot get next input record until input file has been set");
      } else {
         return var1.getNextRecord();
      }
   }

   public double getState(int var1, int var2, int var3) {
      BCKNeuralNetwork var4 = (BCKNeuralNetwork)this.Nets.elementAt(var1);
      return var4.getState(var2, var3);
   }

   public void setOutput(int var1, int var2, double var3) {
      BCKNeuralNetwork var5 = (BCKNeuralNetwork)this.Nets.elementAt(var1);
      var5.setOutput(var2, var3);
   }

   public int size() {
      return this.Nets.size();
   }

   public BCKNeuralNetwork elementAt(int var1) {
      return (BCKNeuralNetwork)this.Nets.elementAt(var1);
   }

   public void removeElement(BCKNeuralNetwork var1) {
      this.Nets.removeElement(var1);
      int var3 = var1.idNumber;
      this.proxyInputs.removeElementAt(var3);

      for (int var4 = 0; var4 < this.proxyInputs.size(); var4++) {
         ((BCKNeuralNetwork)this.Nets.elementAt(var4)).idNumber = var4;
         Vector var5 = (Vector)this.proxyInputs.elementAt(var4);

         for (int var6 = 0; var6 < var5.size(); var6++) {
            BCKNetConnection var2 = (BCKNetConnection)var5.elementAt(var6);
            if (var2.sourceNet != var3 && var2.targetNet != var3) {
               if (var2.sourceNet > var3) {
                  var2.sourceNet--;
               }

               if (var2.targetNet > var3) {
                  var2.targetNet--;
               }
            } else {
               var5.removeElement(var2);
            }
         }
      }
   }
}
