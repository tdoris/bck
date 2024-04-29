package BCK.GUI;

import com.sun.java.swing.BorderFactory;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JCheckBox;
import com.sun.java.swing.JDesktopPane;
import com.sun.java.swing.JInternalFrame;
import com.sun.java.swing.JLayeredPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

public class InternalWindowPanel extends JPanel implements ActionListener {
   JCheckBox closeBox;
   JCheckBox maxBox;
   JCheckBox iconBox;
   JCheckBox resizeBox;
   JTextField titleField;
   JTextField layerField;
   JButton closeAllButton;
   JButton makeButton;
   JLayeredPane lc;
   int makeCount;
   JInternalFrame maker;

   public InternalWindowPanel() {
      this.setLayout(new BorderLayout());
      this.lc = new JDesktopPane();
      this.lc.setOpaque(false);
      this.maker = this.createMakerFrame();
      this.lc.add(this.maker, JLayeredPane.PALETTE_LAYER);
      this.add("Center", this.lc);
      this.maker.setVisible(false);
   }

   public JInternalFrame createMakerFrame() {
      JInternalFrame var1 = new JInternalFrame("Frame Creator");
      Container var3 = var1.getContentPane();
      var3.setLayout(new GridLayout(0, 1));
      JPanel var2 = new JPanel();
      var2.setLayout(new GridLayout(2, 2));
      this.closeBox = new JCheckBox("is Closable ");
      this.closeBox.setSelected(true);
      var2.add(this.closeBox);
      this.maxBox = new JCheckBox("is Maxable  ");
      this.maxBox.setSelected(true);
      var2.add(this.maxBox);
      this.iconBox = new JCheckBox("is Iconifiable ");
      this.iconBox.setSelected(true);
      var2.add(this.iconBox);
      this.resizeBox = new JCheckBox("is Resizable");
      this.resizeBox.setSelected(true);
      var2.add(this.resizeBox);
      var3.add(var2);
      var2 = new JPanel();
      var2.setBorder(BorderFactory.createTitledBorder("Title"));
      var2.setLayout(new BorderLayout());
      this.titleField = new JTextField();
      this.titleField.setText("");
      this.titleField.setMinimumSize(new Dimension(50, 25));
      this.titleField.setEditable(true);
      this.titleField.getAccessibleContext().setAccessibleName("Title for created frame");
      var2.add(this.titleField, "Center");
      var3.add(var2);
      var2 = new JPanel();
      var2.setBorder(BorderFactory.createTitledBorder("Layer"));
      var2.setLayout(new BorderLayout());
      this.layerField = new JTextField();
      this.layerField.setMinimumSize(new Dimension(50, 25));
      this.layerField.setEditable(true);
      this.layerField.setText("5");
      this.layerField.getAccessibleContext().setAccessibleName("Layer for created frame");
      this.layerField
         .getAccessibleContext()
         .setAccessibleDescription(
            "This must be an Integer value, which determines which layer in the stacking order to place the newly created Internal Frame"
         );
      var2.add(this.layerField, "Center");
      var3.add(var2);
      var2 = new JPanel();
      var2.setLayout(new GridLayout(1, 2));
      this.closeAllButton = new JButton("Clear");
      this.closeAllButton.addActionListener(this);
      var2.add(this.closeAllButton);
      this.makeButton = new JButton("Make");
      this.makeButton.addActionListener(this);
      var2.add(this.makeButton);
      var3.add(var2);
      var1.setBounds(360, 10, 270, 250);
      var1.setResizable(true);
      return var1;
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.closeAllButton) {
         this.lc.removeAll();
         this.lc.add(this.maker);
         this.lc.repaint();
         this.makeCount = 0;
      } else if (var1.getSource() == this.makeButton) {
         JInternalFrame var2 = new JInternalFrame();
         var2.setClosable(this.closeBox.isSelected());
         var2.setMaximizable(this.maxBox.isSelected());
         var2.setIconifiable(this.iconBox.isSelected());
         String var4 = this.titleField.getText();
         if (var4.equals("")) {
            var2.setTitle("Internal Frame " + (this.makeCount + 1));
         } else {
            var2.setTitle(var4);
         }

         var2.setResizable(this.resizeBox.isSelected());

         int var3;
         try {
            var3 = Integer.parseInt(this.layerField.getText());
         } catch (NumberFormatException var6) {
            var3 = 0;
         }

         this.makeCount++;
         var2.setBounds(20 * (this.makeCount % 10), 20 * (this.makeCount % 10), 225, 150);
         var2.setContentPane(new MyScrollPane(var3, this.makeCount));
         this.lc.add(var2, new Integer(var3));

         try {
            var2.setSelected(true);
         } catch (PropertyVetoException var5) {
         }
      }
   }
}
