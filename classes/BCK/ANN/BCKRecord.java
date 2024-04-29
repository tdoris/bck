package BCK.ANN;

import java.io.Serializable;

public class BCKRecord implements Serializable {
   double[] data;
   int size;

   protected BCKRecord() {
   }

   public BCKRecord(BCKRecord var1) {
      this.size = var1.size;
      this.data = new double[var1.size];

      for (int var2 = 0; var2 < this.size; var2++) {
         this.data[var2] = var1.data[var2];
      }
   }

   public BCKRecord(int var1) {
      this.size = var1;
      this.data = new double[var1];
   }

   public double[] getData() {
      return this.data;
   }
}
