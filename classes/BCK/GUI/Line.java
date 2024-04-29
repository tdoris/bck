package BCK.GUI;

import java.awt.Color;

class Line {
   int x1;
   int y1;
   int x2;
   int y2;
   public Color color;
   int thickness;

   public Line(int var1, int var2, int var3, int var4, Color var5, int var6) {
      this.x1 = var1;
      this.y1 = var2;
      this.x2 = var3;
      this.y2 = var4;
      this.color = var5;
      this.thickness = var6;
   }
}
