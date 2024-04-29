package BCK.ANN;

import java.io.Serializable;

public class BCKConnection implements Serializable {
   public int post;
   public int pre;
   public double weight;
   public int delay;

   public BCKConnection(int var1, int var2, double var3, int var5) {
      this.post = var2;
      this.pre = var1;
      this.weight = var3;
      this.delay = var5;
   }
}
