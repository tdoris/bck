package BCK.GUI;

import BCK.ANN.BCKBrain;
import com.sun.java.swing.JLayeredPane;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

public class BCKBrainDrawable extends BCKBrain implements Serializable, Drawable {
   private transient BCKImageControl image;
   Vector locations = new Vector(0);

   public BCKBrainDrawable() {
      this.setImage();
   }

   private void setImage() {
      this.image = new BCKImageControl(BCKImageControl.loadImageIcon(bckframe.home + "images/hal.jpg", "hello"));
      this.image.setToolTipText("Brain Inputs");
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.setImage();

      for (int var2 = 0; var2 < this.size(); var2++) {
         Point var3 = (Point)this.locations.elementAt(var2);
         ((Drawable)this.getNet(var2)).getImage().setLocation(var3.x, var3.y);
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      for (int var2 = 0; var2 < this.size(); var2++) {
         Point var3 = ((Drawable)this.getNet(var2)).getImage().getLocation();
         this.locations.addElement(var3);
      }

      var1.defaultWriteObject();
   }

   public BCKImageControl Draw() {
      return this.image;
   }

   public boolean isSelected() {
      return false;
   }

   public void setSelected(boolean var1) {
   }

   public void setVisible(boolean var1) {
   }

   public BCKImageControl getImage() {
      return this.image;
   }

   public void trainPressed() {
   }

   public void editPressed() {
      BCKEdit var1 = new BCKEdit(this);
      var1.setPreferredSize(new Dimension(300, 200));
      var1.setLocation(10, 10);
      bckframe.top.bckInternal.lc.add(var1, JLayeredPane.PALETTE_LAYER);
   }

   public void classifyPressed() {
      BCKClassify var1 = new BCKClassify(this);
      var1.setVisible(true);
   }

   public void cutPressed() {
   }
}
