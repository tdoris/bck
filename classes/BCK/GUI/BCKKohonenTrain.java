package BCK.GUI;

import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;

class BCKKohonenTrain extends BCKDialog {
   JTextField max;
   BCKKohonenDrawable Net;
   JTextField filename = new JTextField(25);
   JButton train;
   JButton stop;
   bckNumberBox numEpochs;
   JTextField globalError;
   bckRealNumberBox eta = new bckRealNumberBox();
   bckNumberBox neighbourhood = new bckNumberBox();
   Graph graphPane;

   public BCKKohonenTrain(BCKKohonenDrawable var1) {
      super(bckframe.top);
      this.filename.setEditable(false);
      this.Net = var1;
      this.setTitle("Train Kohonen");
      JPanel var2 = new JPanel();
      var2.setLayout(new ColumnLayout());
      JPanel var3 = this.createTrainFilePanel();
      JPanel var4 = this.createParametersPanel();
      JPanel var5 = this.createButtonsPanel();
      JPanel var6 = this.createTrainPanel();
      JPanel var7 = this.createGraph();
      var2.add(var3);
      var2.add(var4);
      var2.add(var6);
      var2.add(var7);
      var2.add(var5);
      this.getContentPane().add(var2);
      this.pack();
      this.centerDialog();
   }

   public JPanel createGraph() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      var1.add(new JLabel("     Graph of Global Error"));
      JPanel var2 = new JPanel();
      var2.setLayout(new FlowLayout());
      JButton var3 = new JButton("Zoom In");
      JButton var4 = new JButton("Zoom Out");
      var2.add(new JLabel("Max : "));
      this.max = new JTextField(10);
      var2.add(this.max);
      var2.add(var3);
      var2.add(var4);
      var1.add(var2);
      var3.addActionListener(new BCKKohonenTrain$1(this));
      var4.addActionListener(new BCKKohonenTrain$2(this));
      this.graphPane = new Graph(bckframe.top, 400, 100);
      this.graphPane.setBackground(Color.white);
      this.graphPane.setSize(400, 100);
      var1.add(this.graphPane);
      JPanel var5 = new JPanel();
      var5.setLayout(new FlowLayout());
      var5.add(new JLabel("Global Error :"));
      this.globalError = new JTextField(20);
      var5.add(this.globalError);
      var1.add(var5);
      return var1;
   }

   public void zoomInPressed() {
      this.graphPane.zoomIn();
      double var1 = this.graphPane.getScale();
      double var3 = (double)this.graphPane.getHeight() / var1;
      this.max.setText(String.valueOf(var3));
   }

   public void zoomOutPressed() {
      this.graphPane.zoomOut();
      double var1 = this.graphPane.getScale();
      double var3 = (double)this.graphPane.getHeight() / var1;
      this.max.setText(String.valueOf(var3));
   }

   public JPanel createParametersPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(new JLabel("Learning Rate :"));
      var1.add(this.eta);
      this.eta.setText("0.9");
      var1.add(new JLabel("Neighbourhood :"));
      this.neighbourhood.setText(String.valueOf(this.Net.getNeighbourhood()));
      var1.add(this.neighbourhood);
      return var1;
   }

   public JPanel createTrainFilePanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JLabel var2 = new JLabel("Trianing File");
      JButton var3 = new JButton("Browse");
      var3.addActionListener(new BCKKohonenTrain$3(this));
      var1.add(var2);
      var1.add(this.filename);
      var1.add(var3);
      return var1;
   }

   public JPanel createButtonsPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JButton var2 = new JButton("Cancel");
      JButton var3 = new JButton("OK");
      var1.add(var3);
      var3.addActionListener(new BCKKohonenTrain$4(this));
      var1.add(var2);
      var2.addActionListener(new BCKKohonenTrain$5(this));
      return var1;
   }

   public JPanel createTrainPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(new JLabel("Epochs :"));
      this.numEpochs = new bckNumberBox();
      var1.add(this.numEpochs);
      this.train = new JButton("Train");
      this.train.addActionListener(new BCKKohonenTrain$6(this));
      var1.add(this.train);
      return var1;
   }

   public void trainPressed() {
      int var1 = this.numEpochs.getInteger();
      double var2 = 0.0;
      if (var1 < 1) {
         JOptionPane.showMessageDialog(null, "Illegal Number of Epochs:  " + var1, "Alert", 0);
      } else {
         double var4 = this.eta.getRealNumber();
         if (!Double.isNaN(var4) && !(var4 < 0.0)) {
            int var6 = this.neighbourhood.getInteger();
            if (var6 >= 0 && var6 <= (int)Math.ceil(0.5 * (double)this.Net.side)) {
               this.Net.setEta(var4);
               this.Net.setNeighbourhood(var6);

               try {
                  for (int var7 = 0; var7 < var1; var7++) {
                     this.Net.train(1);
                     var2 = this.Net.getGlobalError();
                     this.globalError.setText(String.valueOf(this.Net.getGlobalError()));
                     this.graphPane.newPoint(var2);
                  }
               } catch (Exception var8) {
                  JOptionPane.showMessageDialog(null, var8.toString(), "Alert", 0);
               }
            } else {
               JOptionPane.showMessageDialog(null, "Illegal Neighbourhood:  " + this.neighbourhood, "Alert", 0);
            }
         } else {
            JOptionPane.showMessageDialog(null, "Illegal Value for Learning rate:  " + var4, "Alert", 0);
         }
      }
   }

   public void CancelPressed() {
      this.setVisible(false);
   }

   public void OKPressed() {
      this.setVisible(false);
   }

   public void browsePressed() {
      FileDialog var1 = new FileDialog(bckframe.top, "Load Training File", 0);
      var1.show();
      String var2 = var1.getFile();
      if (var2 != null) {
         this.filename.setText(var1.getDirectory() + var2);

         try {
            this.Net.setTrainingFile(var1.getDirectory() + var2);
         } catch (Exception var4) {
            JOptionPane.showMessageDialog(null, "Error Setting Training File: " + var4, "Alert", 0);
         }
      }
   }
}
