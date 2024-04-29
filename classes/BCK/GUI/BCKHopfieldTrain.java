package BCK.GUI;

import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.FileDialog;
import java.awt.FlowLayout;

class BCKHopfieldTrain extends BCKDialog {
   JTextField filename = new JTextField(25);
   JButton OK = new JButton("Train");
   BCKHopfieldDrawable Net;

   public BCKHopfieldTrain(BCKHopfieldDrawable var1) {
      super(bckframe.top, "Train Hopfield", true);
      this.Net = var1;
      this.filename.setEditable(false);
      JPanel var2 = new JPanel();
      var2.setLayout(new ColumnLayout());
      JPanel var3 = this.createTrainFilePanel();
      JPanel var4 = new JPanel();
      var4.setLayout(new FlowLayout());
      this.OK.addActionListener(new BCKHopfieldTrain$1(this));
      JButton var5 = new JButton("Cancel");
      var4.add(this.OK);
      var4.add(var5);
      var5.addActionListener(new BCKHopfieldTrain$2(this));
      var2.add(var3);
      var2.add(var4);
      this.getContentPane().add(var2);
      this.pack();
      this.centerDialog();
   }

   public JPanel createTrainFilePanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JLabel var2 = new JLabel("Trianing File");
      JButton var3 = new JButton("Browse");
      var3.addActionListener(new BCKHopfieldTrain$3(this));
      var1.add(var2);
      var1.add(this.filename);
      var1.add(var3);
      return var1;
   }

   public void OKPressed() {
      try {
         this.Net.setTrainingFile(this.filename.getText());
         this.Net.train();
         this.setVisible(false);
      } catch (Exception var2) {
         JOptionPane.showMessageDialog(null, var2.toString(), "Alert", 0);
      }
   }

   public void CancelPressed() {
      this.setVisible(false);
   }

   public void browsePressed() {
      FileDialog var1 = new FileDialog(bckframe.top, "Load Training File", 0);
      var1.show();
      String var2 = var1.getFile();
      if (var2 != null) {
         this.filename.setText(var1.getDirectory() + var2);
      }
   }
}
