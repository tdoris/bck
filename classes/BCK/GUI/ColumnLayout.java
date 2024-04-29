package BCK.GUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

public class ColumnLayout implements LayoutManager, Serializable {
   int xInset = 5;
   int yInset = 5;
   int yGap = 2;

   public void addLayoutComponent(String var1, Component var2) {
   }

   public void layoutContainer(Container var1) {
      Insets var2 = var1.getInsets();
      int var3 = this.yInset + var2.top;
      Component[] var4 = var1.getComponents();
      Dimension var5 = null;

      for (int var6 = 0; var6 < var4.length; var6++) {
         var5 = var4[var6].getPreferredSize();
         var4[var6].setSize(var5.width, var5.height);
         var4[var6].setLocation(this.xInset + var2.left, var3);
         var3 += var5.height + this.yGap;
      }
   }

   public Dimension minimumLayoutSize(Container var1) {
      Insets var2 = var1.getInsets();
      int var3 = this.yInset + var2.top;
      int var4 = var2.left + var2.right;
      Component[] var5 = var1.getComponents();
      Dimension var6 = null;

      for (int var7 = 0; var7 < var5.length; var7++) {
         var6 = var5[var7].getPreferredSize();
         var3 += var6.height + this.yGap;
         var4 = Math.max(var4, var6.width + var2.left + var2.right + this.xInset * 2);
      }

      var3 += var2.bottom;
      return new Dimension(var4, var3);
   }

   public Dimension preferredLayoutSize(Container var1) {
      return this.minimumLayoutSize(var1);
   }

   public void removeLayoutComponent(Component var1) {
   }
}
