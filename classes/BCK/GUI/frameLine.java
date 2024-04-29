package BCK.GUI;

import java.io.Serializable;

class frameLine implements Serializable {
   int x1;
   int y1;
   int x2;
   int y2;

   public frameLine(int var1, int var2, int var3, int var4) {
      this.x1 = var1;
      this.y1 = var2;
      this.x2 = var3;
      this.y2 = var4;
   }
}
