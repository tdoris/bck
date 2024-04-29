package BCK.GUI;

import BCK.ANN.BCKNeuralNetwork;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

public class BCKConnectNeurons extends BCKDialog {
   JPanel panel1 = new JPanel();
   JButton btnCancel = new JButton();
   private bckNumberBox neuronidBox;
   private bckRealNumberBox weightBox;
   private bckNumberBox delayBox;
   private BCKNeuralNetwork Net;
   int postNeuron;
   int preNeuron;
   double weight;
   int delay;

   public BCKConnectNeurons(Frame var1, int var2, BCKNeuralNetwork var3) {
      super(var1, "Connect " + var2, true);
      this.Net = var3;
      this.postNeuron = var2;
      this.setSize(300, 80);
      JPanel var4 = new JPanel();
      var4.setLayout(new BorderLayout());
      JPanel var5 = this.buildInputPanel();
      JPanel var6 = new JPanel();
      JButton var7 = new JButton("OK");
      var7.addActionListener(new BCKConnectNeurons$1(this));
      var6.add(var7);
      this.getRootPane().setDefaultButton(var7);
      var7.setToolTipText("This is the OK button");
      var6.setLayout(new FlowLayout(2));
      JButton var8 = new JButton("Cancel");
      var8.addActionListener(new BCKConnectNeurons$2(this));
      var6.add(var8);
      var4.add(var5, "Center");
      var4.add(var6, "South");
      this.getContentPane().add(var4);
      this.pack();
      this.centerDialog();
      this.setResizable(false);
   }

   public JPanel buildInputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      this.neuronidBox = new bckNumberBox();
      this.weightBox = new bckRealNumberBox();
      this.delayBox = new bckNumberBox();
      JPanel var2 = new JPanel();
      var2.setLayout(new FlowLayout());
      var2.add(new JLabel("Neuron : "));
      var2.add(this.neuronidBox);
      JPanel var3 = new JPanel();
      var3.setLayout(new FlowLayout());
      var3.add(new JLabel("Weight : "));
      var3.add(this.weightBox);
      JPanel var4 = new JPanel();
      var4.setLayout(new FlowLayout());
      var4.add(new JLabel("Delay : "));
      var4.add(this.delayBox);
      var1.add(var2);
      var1.add(var3);
      var1.add(var4);
      return var1;
   }

   public void CancelPressed() {
      this.setVisible(false);
   }

   public void OKPressed() {
      if (this.neuronidBox.getInteger() < 0 || this.neuronidBox.getInteger() > this.Net.getNumberOfNeurons()) {
         JOptionPane.showMessageDialog(null, "You must enter a valid of neuron Id number.", "Alert", 0);
      } else if (Double.isNaN(this.weightBox.getRealNumber())) {
         JOptionPane.showMessageDialog(null, "You must enter a valid weight.", "Alert", 0);
      } else if (this.delayBox.getInteger() >= 0 && this.delayBox.getInteger() <= 9) {
         this.preNeuron = this.neuronidBox.getInteger();
         this.weight = this.weightBox.getRealNumber();
         this.delay = this.delayBox.getInteger();
         this.setVisible(false);
      } else {
         JOptionPane.showMessageDialog(null, "You must enter a valid delay (0 to 9).", "Alert", 0);
      }
   }
}
