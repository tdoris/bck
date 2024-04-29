package BCK.GUI;

import com.sun.java.swing.ImageIcon;
import com.sun.java.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class BCKImageControl extends JButton implements MouseMotionListener {
   int maxHeight;
   boolean selected = false;
   String pictureFile;

   public static ImageIcon loadImageIcon(String var0, String var1) {
      return new ImageIcon(var0, var1);
   }

   public BCKImageControl(ImageIcon var1) {
      super(var1);
      this.setSize(new Dimension(75, 75));
      this.setForeground(Color.white);
      this.addMouseMotionListener(this);
      this.enableEvents(16L);
      this.setVerticalTextPosition(3);
      this.setHorizontalTextPosition(0);
   }

   public BCKImageControl(String var1, ImageIcon var2) {
      super(var1, var2);
      this.setSize(new Dimension(75, 95));
      this.setForeground(Color.white);
      this.addMouseMotionListener(this);
      this.enableEvents(16L);
      this.setVerticalTextPosition(3);
      this.setHorizontalTextPosition(0);
   }

   public Point getCenter() {
      return new Point(this.getLocation().x + this.getSize().width / 2, this.getLocation().y + this.getSize().height / 2);
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean var1) {
      this.selected = var1;
   }

   public void setImage(String var1) {
      this.setIcon(loadImageIcon(var1, var1));
      this.repaint();
   }

   public void mouseDragged(MouseEvent var1) {
      if (var1.getY() + this.getLocation().y - this.getSize().width / 2 > this.maxHeight) {
         this.setLocation(var1.getX() + this.getLocation().x - this.getSize().width / 2, var1.getY() + this.getLocation().y - this.getSize().height / 2);
      } else {
         this.setLocation(var1.getX() + this.getLocation().x - this.getSize().width / 2, this.maxHeight);
      }

      bckframe.top.repaint();
   }

   public void mouseMoved(MouseEvent var1) {
   }
}
