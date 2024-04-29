package BCK.GUI;

import com.sun.java.swing.JDialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class BCKDialog extends JDialog implements WindowListener {
   public BCKDialog() {
      this.addWindowListener(this);
      this.setModal(true);
   }

   public BCKDialog(Frame var1) {
      super(var1);
      this.addWindowListener(this);
      this.setModal(true);
   }

   public BCKDialog(Frame var1, boolean var2) {
      super(var1, var2);
      this.addWindowListener(this);
      this.setModal(true);
   }

   public BCKDialog(Frame var1, String var2) {
      super(var1, var2);
      this.addWindowListener(this);
      this.setModal(true);
   }

   public BCKDialog(Frame var1, String var2, boolean var3) {
      super(var1, var2, var3);
      this.addWindowListener(this);
      this.setModal(true);
   }

   public void centerDialog() {
      Dimension var1 = this.getToolkit().getScreenSize();
      Dimension var2 = this.getSize();
      var1.height /= 2;
      var1.width /= 2;
      var2.height /= 2;
      var2.width /= 2;
      int var3 = var1.height - var2.height;
      int var4 = var1.width - var2.width;
      this.setLocation(var4, var3);
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowClosed(WindowEvent var1) {
      bckframe.top.show();
   }

   public void windowClosing(WindowEvent var1) {
      bckframe.top.show();
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowOpened(WindowEvent var1) {
   }
}
