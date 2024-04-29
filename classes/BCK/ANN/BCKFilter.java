package BCK.ANN;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BCKFilter {
   protected int RecordSize;
   protected Vector data;
   protected String input_file;
   protected BCKNeuralNetwork inputNet;
   protected boolean inputIsANN;
   protected int index;
   protected int numRecords;
   protected Hashtable tokens;
   protected int randomIndex;
   protected int[] randomMapping;

   protected BCKFilter() {
      this.index = 0;
   }

   public BCKFilter(String var1, int var2) throws Exception {
      this.inputNet = null;
      this.inputIsANN = false;
      this.RecordSize = var2;
      this.tokens = new Hashtable();
      this.data = new Vector(0);
      this.index = 0;

      FileReader var3;
      try {
         var3 = new FileReader(var1);
      } catch (FileNotFoundException var5) {
         throw new Exception("Could not find file" + var1);
      }

      this.ReadFile(var1, this.RecordSize);
      var3.close();
      this.numRecords = this.data.size();
      this.randomMapping = new int[this.numRecords];
      int var4 = 0;

      while (var4 < this.randomMapping.length) {
         this.randomMapping[var4] = var4++;
      }

      this.randomIndex = 0;
   }

   private void ReadFile(String var1, int var2) throws Exception {
      boolean var3 = false;
      this.RecordSize = -1;
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      this.RecordSize = var2;
      FileReader var14 = new FileReader(var1);
      BCKStreamTokenizer var15 = new BCKStreamTokenizer(var14);
      var15.eolIsSignificant(false);
      var15.parseNumbers();
      BCKRecord var16 = new BCKRecord(this.RecordSize);
      var6 = var15.nextToken();
      String var17 = "";
      if (var6 == -2) {
         var15.pushBack();
      } else {
         var17 = var15.sval.toString();
         if (!var17.equals("TOKENDEFS")) {
            throw new Exception("The first field in the file must be either a numeric value or the 'TOKENDEFS' keyword");
         }
      }

      while (var17.equals("TOKENDEFS")) {
         String var10 = var15.getWord();
         if (var10.equals("STARTDATA")) {
            break;
         }

         var15.eatChar('=');
         double var8 = var15.getNumber();
         var7++;
         this.tokens.put(var10, new Double(var8));
      }

      while (!var3) {
         for (int var18 = 0; var18 < this.RecordSize; var18++) {
            var6 = var15.nextToken();
            switch (var6) {
               case -3:
                  Double var13 = (Double)this.tokens.get(var15.sval.toString());
                  if (var13 == null) {
                     throw new IOException("Undefined token : '" + var15.sval.toString() + "' at line : " + var7);
                  }

                  double var21 = var13;
                  var16.data[var18] = var21;
                  break;
               case -2:
                  double var11 = var15.nval;
                  var16.data[var18] = var11;
                  break;
               case -1:
                  if (var18 != 0) {
                     throw new IOException("Unexpected EOF");
                  }

                  this.numRecords = var5;
                  var14.close();
                  return;
            }

            var4++;
         }

         this.data.addElement(var16);
         var16 = new BCKRecord(this.RecordSize);
         var5++;
      }
   }

   public BCKFilter(String var1) throws Exception {
      this.inputNet = null;
      this.inputIsANN = false;
      this.tokens = new Hashtable();
      this.data = new Vector(0);
      this.index = 0;

      FileReader var2;
      try {
         var2 = new FileReader(var1);
      } catch (FileNotFoundException var4) {
         throw new Exception("Could not find file" + var1);
      }

      this.ReadFile(var1);
      var2.close();
      this.numRecords = this.data.size();
      this.randomMapping = new int[this.numRecords];
      int var3 = 0;

      while (var3 < this.randomMapping.length) {
         this.randomMapping[var3] = var3++;
      }

      this.randomIndex = 0;
   }

   private void ReadFile(String var1) throws Exception {
      boolean var2 = false;
      this.RecordSize = -1;
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      FileReader var13 = new FileReader(var1);
      BCKStreamTokenizer var14 = new BCKStreamTokenizer(var13);
      var14.eolIsSignificant(false);
      var14.parseNumbers();

      try {
         var14.eatString("FIELDS");
      } catch (IOException var20) {
         throw new IOException("The first line of the file should specify the number of fields per record, 'FIELDS = N' 'FIELDS' not found" + var20.toString());
      }

      try {
         var14.eatChar('=');
      } catch (IOException var19) {
         throw new IOException("The first line of the file should specify the number of fields per record, 'FIELDS = N' '=' not found");
      }

      try {
         this.RecordSize = (int)var14.getNumber();
      } catch (IOException var18) {
         throw new IOException("The first line of the file should specify the number of fields per record, 'FIELDS = N' number 'N' not found");
      }

      var6 = 2;
      String var15 = var14.getWord();

      while (var15.equals("TOKENDEFS")) {
         String var9 = var14.getWord();
         if (var9.equals("STARTDATA")) {
            break;
         }

         var14.eatChar('=');
         double var7 = var14.getNumber();
         var6++;
         this.tokens.put(var9, new Double(var7));
      }

      for (BCKRecord var16 = new BCKRecord(this.RecordSize); !var2; var4++) {
         for (int var17 = 0; var17 < this.RecordSize; var17++) {
            var5 = var14.nextToken();
            switch (var5) {
               case -3:
                  Double var12 = (Double)this.tokens.get(var14.sval.toString());
                  if (var12 == null) {
                     throw new IOException("Undefined token : '" + var14.sval.toString() + "' at line : " + var6);
                  }

                  double var23 = var12;
                  var16.data[var17] = var23;
                  break;
               case -2:
                  double var10 = var14.nval;
                  var16.data[var17] = var10;
                  break;
               case -1:
                  if (var17 != 0) {
                     throw new IOException("Unexpected EOF");
                  }

                  this.numRecords = var4;
                  var13.close();
                  return;
            }

            var3++;
         }

         this.data.addElement(var16);
         var16 = new BCKRecord(this.RecordSize);
      }
   }

   public double[] getNextRecord() {
      if (this.data.size() <= this.index) {
         this.index = 0;
      }

      return ((BCKRecord)this.data.elementAt(this.index++)).getData();
   }

   public double[] getNextRecordBipolar() {
      double[] var1 = new double[this.RecordSize];
      double[] var2 = this.getNextRecord();

      for (int var3 = 0; var3 < this.RecordSize; var3++) {
         if (var2[var3] < 1.0) {
            var1[var3] = -1.0;
         } else {
            var1[var3] = 1.0;
         }
      }

      return var1;
   }

   public double[] getNextRecordNormalised() {
      double[] var1 = this.getNextRecord();
      double var2 = 0.0;

      for (int var4 = 0; var4 < var1.length; var4++) {
         var2 += var1[var4] * var1[var4];
      }

      if (var2 == 0.0) {
         return var1;
      } else {
         var2 = Math.sqrt(var2);

         for (int var5 = 0; var5 < var1.length; var5++) {
            var1[var5] /= var2;
         }

         return var1;
      }
   }

   public double[] getNextRecordRandomly() {
      int var1 = this.randomMapping[this.randomIndex];
      this.randomIndex++;
      if (this.randomIndex == this.randomMapping.length) {
         this.randomIndex = 0;
         this.scrambleRandomMapping();
      }

      return ((BCKRecord)this.data.elementAt(var1)).getData();
   }

   public double[] getRecordAt(int var1) {
      return ((BCKRecord)this.data.elementAt(var1)).getData();
   }

   public int getNumberOfRecords() {
      return this.data == null ? 0 : this.numRecords;
   }

   public int getRecordSize() {
      return this.RecordSize;
   }

   public String getFileName() {
      return this.input_file;
   }

   public void scrambleRandomMapping() {
      int var1 = (int)Math.floor(Math.random() * (double)this.data.size());
      if (var1 == this.data.size()) {
         var1--;
      }

      if (var1 < 0) {
         var1 = 0;
      }

      int var2 = this.randomMapping[0];
      this.randomMapping[0] = this.randomMapping[var1];
      this.randomMapping[var1] = var2;
   }
}
