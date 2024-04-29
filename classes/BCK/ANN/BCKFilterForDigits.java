package BCK.ANN;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

public class BCKFilterForDigits extends BCKFilter implements Serializable {
   int inputs;
   int outputs;

   public BCKFilterForDigits(String var1, int var2, int var3) throws Exception {
      super.inputNet = null;
      super.inputIsANN = false;
      super.RecordSize = var2 + var3;
      super.tokens = new Hashtable();
      this.inputs = var2;
      this.outputs = var3;
      super.data = new Vector(0);
      super.index = 0;

      FileReader var4;
      try {
         var4 = new FileReader(var1);
      } catch (FileNotFoundException var6) {
         throw new Exception("Could not find file" + var1);
      }

      this.ReadFile(var1, super.RecordSize);
      var4.close();
      super.numRecords = super.data.size();
      super.randomMapping = new int[super.numRecords];
      int var5 = 0;

      while (var5 < super.randomMapping.length) {
         super.randomMapping[var5] = var5++;
      }

      super.randomIndex = 0;
   }

   private void ReadFile(String var1, int var2) throws Exception {
      boolean var3 = false;
      super.RecordSize = -1;
      int var4 = this.inputs + 1;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      super.RecordSize = var2;
      FileReader var11 = new FileReader(var1);
      BCKStreamTokenizer var12 = new BCKStreamTokenizer(var11);
      var12.eolIsSignificant(false);
      var12.parseNumbers();

      for (BCKRecord var13 = new BCKRecord(super.RecordSize); !var3; var6++) {
         for (int var14 = 0; var14 < var4; var14++) {
            var7 = var12.nextToken();
            switch (var7) {
               case -2:
                  double var8 = var12.nval;
                  if (var14 < var4 - 1) {
                     var13.data[var14] = var8;
                  } else {
                     double[] var10 = this.intToVector((int)var8);

                     for (int var15 = 0; var15 < var10.length; var15++) {
                        var13.data[var14 + var15] = var10[var15];
                     }
                  }
                  break;
               case -1:
                  if (var14 != 0) {
                     throw new IOException("Unexpected EOF");
                  } else {
                     super.numRecords = var6;
                     var11.close();
                     return;
                  }
            }

            var5++;
         }

         super.data.addElement(var13);
         var13 = new BCKRecord(super.RecordSize);
      }
   }

   public double[] intToVector(int var1) {
      double[] var2 = new double[10];

      for (int var3 = 0; var3 < 10; var3++) {
         if (var1 == var3) {
            var2[var3] = 0.0;
         } else {
            var2[var3] = 1.0;
         }
      }

      return var2;
   }

   public double[] getInputVectorAt(int var1) {
      double[] var2 = this.getRecordAt(var1);
      double[] var3 = new double[this.inputs];

      for (int var4 = 0; var4 < this.inputs; var4++) {
         var3[var4] = var2[var4];
      }

      return var3;
   }

   public double[] getOutputVectorAt(int var1) {
      double[] var2 = this.getRecordAt(var1);
      double[] var3 = new double[this.outputs];

      for (int var4 = this.inputs; var4 < this.outputs + this.inputs; var4++) {
         var3[var4 - this.inputs] = var2[var4];
      }

      return var3;
   }
}
