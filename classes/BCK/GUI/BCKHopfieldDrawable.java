package BCK.GUI;

import BCK.ANN.BCKHopfield;
import com.sun.java.swing.JLayeredPane;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;

public class BCKHopfieldDrawable extends BCKHopfield implements Drawable, MouseListener, ActionListener {
   private transient BCKHopfieldTrain train;
   private transient BCKPopup popup;
   private transient BCKImageControl image;
   private transient BCKEdit editinternal;

   public BCKHopfieldDrawable() {
      this.setImage();
   }

   public BCKHopfieldDrawable(int var1) {
      super(var1);
      this.setImage();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.setImage();
   }

   private void setImage() {
      this.image = new BCKImageControl(this.getName(), BCKImageControl.loadImageIcon(bckframe.home + "images/HOPFIELD.jpg", "hello"));
      this.image.addMouseListener(this);
      this.popup = new BCKPopup();
      this.popup.edit.addActionListener(this);
      this.popup.train.addActionListener(this);
      this.popup.classify.addActionListener(this);
      this.popup.map.setEnabled(false);
      this.popup.cut.addActionListener(this);
      this.popup.connect.addActionListener(this);
      this.train = new BCKHopfieldTrain(this);
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
         this.image.setImage(bckframe.home + "images/HOPFIELDSELECTED.jpg");
      } else {
         this.image.setImage(bckframe.home + "images/HOPFIELD.jpg");
      }
   }

   public void setVisible(boolean var1) {
      this.image.setVisible(var1);
   }

   public BCKImageControl getImage() {
      return this.image;
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.popup.edit) {
         this.editPressed();
      }

      if (var1.getSource() == this.popup.train) {
         this.trainPressed();
      }

      if (var1.getSource() == this.popup.cut) {
         this.cutPressed();
      }

      if (var1.getSource() == this.popup.classify) {
         this.classifyPressed();
      }

      if (var1.getSource() == this.popup.connect) {
         BCKConnectNetworks var2 = new BCKConnectNetworks(this);
         var2.setVisible(true);
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
      BCKHopfieldClassify var1 = new BCKHopfieldClassify(this);
      var1.setVisible(true);
   }

   public void cutPressed() {
      bckframe.top.saveNetwork(this, "CLIPBOARD.NET");
      bckframe.top.removeNetwork(this);
   }
}
