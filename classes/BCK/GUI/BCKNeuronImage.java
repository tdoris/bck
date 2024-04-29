package BCK.GUI;

import BCK.ANN.BCKNeuralNetwork;
import BCK.ANN.BCKNeuron;
import com.sun.java.swing.ImageIcon;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BCKNeuronImage extends BCKImageControl implements MouseListener, ActionListener {
   BCKNetworkEditor parentObject;
   int id;
   BCKNeuron neuron;
   BCKNeuralNetwork Net;
   BCKNeuronPopup popup;
   BCKConnectNeurons connect;
   BCKNeuronDetails details;
   static ImageIcon NeuronImageIcon = BCKImageControl.loadImageIcon(bckframe.home + "images/NEURON.jpg", "This is a Neuron");

   public BCKNeuronImage(int var1, BCKNetworkEditor var2, BCKNeuron var3, BCKNeuralNetwork var4) {
      super(NeuronImageIcon);
      this.parentObject = var2;
      this.id = var1;
      this.neuron = var3;
      this.Net = var4;
      this.addMouseListener(this);
      this.popup = new BCKNeuronPopup();
      this.popup.connect.addActionListener(this);
      this.popup.details.addActionListener(this);
      this.setLayout(new FlowLayout());
      JLabel var5 = new JLabel(new Integer(this.id).toString());
      var5.setOpaque(false);
      this.add(var5);
      this.setSize(50, 50);
   }

   public Point getCenter() {
      return new Point(this.getLocation().x + this.getSize().width / 2, this.getLocation().y + this.getSize().height / 2);
   }

   public void mouseDragged(MouseEvent var1) {
      if (var1.getY() + this.getLocation().y - this.getSize().width / 2 > super.maxHeight) {
         this.setLocation(var1.getX() + this.getLocation().x - this.getSize().width / 2, var1.getY() + this.getLocation().y - this.getSize().height / 2);
      } else {
         this.setLocation(var1.getX() + this.getLocation().x - this.getSize().width / 2, super.maxHeight);
      }

      this.parentObject.drawConnections();
      this.parentObject.repaint();
   }

   public void mouseMoved(MouseEvent var1) {
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.popup.details) {
         this.details = new BCKNeuronDetails(this.id, this.Net);
         this.details.setVisible(true);
      }

      if (var1.getSource() == this.popup.connect) {
         this.connect = new BCKConnectNeurons(bckframe.top, this.id, this.Net);
         this.connect.setVisible(true);

         try {
            this.Net.connectInternal(this.connect.preNeuron, this.connect.postNeuron, this.connect.weight, this.connect.delay);
         } catch (Exception var3) {
            JOptionPane.showMessageDialog(null, var3.toString(), "Alert", 0);
         }
      }

      this.parentObject.drawConnections();
      this.parentObject.repaint();
   }

   public void mouseClicked(MouseEvent var1) {
      if (var1.isAltDown() && var1.getSource() == this) {
         this.popup.show(this, var1.getX(), var1.getY());
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
}
