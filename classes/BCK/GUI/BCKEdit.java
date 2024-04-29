package BCK.GUI;

import BCK.ANN.BCKNeuralNetwork;
import com.sun.java.swing.JCheckBoxMenuItem;
import com.sun.java.swing.JInternalFrame;
import com.sun.java.swing.JMenu;
import com.sun.java.swing.JMenuBar;
import com.sun.java.swing.JScrollPane;
import com.sun.java.swing.JViewport;
import com.sun.java.swing.event.InternalFrameEvent;
import com.sun.java.swing.event.InternalFrameListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class BCKEdit extends JInternalFrame implements InternalFrameListener {
   JScrollPane spane;
   BCKNetworkEditor editor;
   JMenuBar editMenuBar = new JMenuBar();
   JMenu Options = new JMenu("Options");
   JCheckBoxMenuItem ShowLines = new JCheckBoxMenuItem("Show Connections");

   public BCKEdit(BCKNeuralNetwork var1) {
      super("Edit Neural Network " + var1.idNumber, true, true, true, false);
      this.getContentPane().setLayout(new BorderLayout());
      this.createMenu();
      this.editMenuBar.add(this.Options);
      this.setMenuBar(this.editMenuBar);
      this.setBackground(Color.white);
      this.editor = new BCKNetworkEditor(var1);
      this.editor.setSize(800, 800);
      this.spane = new JScrollPane();
      JViewport var2 = this.spane.getViewport();
      var2.add(this.editor);
      this.setContentPane(this.spane);
      this.setPreferredSize(new Dimension(300, 300));
      this.show();
      this.setOpaque(true);
      this.addInternalFrameListener(this);
      this.editor.drawNeurons();
   }

   public void createMenu() {
      this.ShowLines.setSelected(false);
      this.Options.add(this.ShowLines);
      this.ShowLines.addActionListener(new BCKEdit$1(this));
   }

   public void internalFrameActivated(InternalFrameEvent var1) {
      this.spane.validate();
      this.repaint();
   }

   public void internalFrameClosed(InternalFrameEvent var1) {
      this.setVisible(false);
      bckframe.top.bckInternal.lc.remove(this);
   }

   public void internalFrameClosing(InternalFrameEvent var1) {
   }

   public void internalFrameDeactivated(InternalFrameEvent var1) {
   }

   public void internalFrameDeiconified(InternalFrameEvent var1) {
   }

   public void internalFrameIconified(InternalFrameEvent var1) {
   }

   public void internalFrameOpened(InternalFrameEvent var1) {
   }
}
