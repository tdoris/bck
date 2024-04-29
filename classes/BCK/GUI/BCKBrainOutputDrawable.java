package BCK.GUI;

import BCK.ANN.BCKRBF;
import com.sun.java.swing.JLayeredPane;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.Vector;

public class BCKBrainOutputDrawable extends BCKRBF implements Serializable, Drawable {
   private transient BCKImageControl image;
   Vector locations = new Vector(0);

   public BCKBrainOutputDrawable() {
      this.setImage();
   }

   private void setImage() {
      this.image = new BCKImageControl(BCKImageControl.loadImageIcon(bckframe.home + "images/pensmall.gif", "hello"));
      this.image.setToolTipText("Brain Outputs");
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
