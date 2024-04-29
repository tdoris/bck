package BCK.GUI;

import BCK.ANN.BCKRBFDDA;
import com.sun.java.swing.JLayeredPane;
import com.sun.java.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class BCKRBFDrawable extends BCKRBFDDA implements Drawable, MouseListener, ActionListener, Serializable {
   private transient BCKRBFTrain train;
   private transient BCKPopup popup;
   private transient BCKImageControl image;
   private transient BCKEdit editinternal;

   public BCKRBFDrawable() {
      this.setImage();
   }

   public BCKRBFDrawable(int var1, int var2) throws Exception {
      super(var1, var2);
      this.setImage();
   }

   public BCKImageControl getImage() {
      return this.image;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.setImage();
   }

   private void setImage() {
      this.image = new BCKImageControl(this.getName(), BCKImageControl.loadImageIcon(bckframe.home + "images/RBF.jpg", "hello"));
      this.image.addMouseListener(this);
      this.popup = new BCKPopup();
      this.popup.edit.addActionListener(this);
      this.popup.train.addActionListener(this);
      this.popup.classify.addActionListener(this);
      this.popup.cut.addActionListener(this);
      this.popup.map.addActionListener(this);
      this.popup.connect.addActionListener(this);
      this.train = new BCKRBFTrain(this);
      this.train.OK.addActionListener(this);
      this.setText("No Name");
   }

   public void setText(String var1) {
      this.setName(var1);
      this.image.setText(super.idNumber + " " + var1);
      this.image.setToolTipText(super.idNumber + " " + var1);
   }

   public BCKImageControl Draw() {
      return this.image;
   }

   public boolean isSelected() {
      return this.image.isSelected();
   }

   public void setSelected(boolean var1) {
      this.image.setSelected(var1);
      if (var1) {
         this.image.setImage(bckframe.home + "images/RBFSELECTED.jpg");
      } else {
         this.image.setImage(bckframe.home + "images/RBF.jpg");
      }
   }

   public void setVisible(boolean var1) {
      this.image.setVisible(var1);
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.popup.edit) {
         this.editPressed();
      }

      if (var1.getSource() == this.popup.train) {
         this.trainPressed();
      }

      if (var1.getSource() == this.popup.classify) {
         this.classifyPressed();
      }

      if (var1.getSource() == this.popup.cut) {
         this.cutPressed();
      }

      if (var1.getSource() == this.popup.map) {
         BCKMap var2 = new BCKMap(this);
         var2.setVisible(true);
      }

      if (var1.getSource() == this.popup.connect) {
         BCKConnectNetworks var4 = new BCKConnectNetworks(this);
         var4.setVisible(true);
      }

      if (var1.getSource() == this.train.OK) {
         try {
            this.train.setVisible(false);
         } catch (Exception var3) {
            JOptionPane.showMessageDialog(null, var3.toString(), "Alert", 0);
         }
      }
   }

   public void mouseClicked(MouseEvent var1) {
      if (var1.isAltDown() && var1.getSource() == this.image) {
         this.popup.show(this.image, var1.getX(), var1.getY());
      }

      if (var1.getClickCount() == 2) {
         BCKSetNetName var2 = new BCKSetNetName(this.getName());
         var2.setVisible(true);
         if (var2.isOk()) {
            this.setText(var2.getName());
         }
      }
   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }

   public void mousePressed(MouseEvent var1) {
   }

   public void mouseReleased(MouseEvent var1) {
   }

   public void trainPressed() {
      this.train.show();
   }

   public void editPressed() {
      this.editinternal = new BCKEdit(this);
      this.editinternal.setPreferredSize(new Dimension(300, 200));
      this.editinternal.setLocation(10, 10);
      bckframe.top.bckInternal.lc.add(this.editinternal, JLayeredPane.PALETTE_LAYER);
   }

   public void classifyPressed() {
      BCKClassify var1 = new BCKClassify(this);
      var1.setVisible(true);
   }

   public void cutPressed() {
      bckframe.top.saveNetwork(this, "CLIPBOARD.NET");
      bckframe.top.removeNetwork(this);
   }
}
