package BCK.GUI;

import com.sun.java.swing.ImageIcon;
import com.sun.java.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class BCKPicture extends JPanel {
   private ImageIcon image;

   public BCKPicture(String var1) {
      this.setImage(var1);
      this.setPreferredSize(new Dimension(100, 100));
      this.setVisible(true);
   }

   public BCKPicture() {
      this.setPreferredSize(new Dimension(100, 100));
      this.setVisible(true);
   }

   public void paintComponent(Graphics var1) {
      int var2 = this.getPreferredSize().width;
      int var3 = this.getPreferredSize().height;
      if (this.image != null) {
         var1.drawImage(this.getImage(), 0, 0, var2, var3, this);
      } else {
         var1.fillRect(0, 0, var2, var3);
      }
   }

   public void setImage(String var1) {
      if (var1 == null) {
         this.image = null;
      } else {
         this.image = new ImageIcon(var1);
      }

      this.repaint();
   }

   public Image getImage() {
      return this.image.getImage();
   }
}
