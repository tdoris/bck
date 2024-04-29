package BCK.GUI;

import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;

class BCKMLPQpropTrain extends BCKDialog {
   int epochsSinceRestart;
   JTextField epochCount;
   JTextField trainfilename = new JTextField(25);
   JTextField testfilename = new JTextField(25);
   JButton OK = new JButton("OK");
   bckNumberBox trainForEpochs;
   bckRealNumberBox trainUntilGE;
   bckRealNumberBox mu;
   bckRealNumberBox epsilon;
   bckNumberBox stopAfter;
   Graph graphPane;
   BCKMLPQpropDrawable Net;
   JTextField globalError;
   JTextField testScore;
   JTextField max;

   public BCKMLPQpropTrain(BCKMLPQpropDrawable var1) {
      super(bckframe.top, "Train MLP Using QuickProp", true);
      this.trainfilename.setEditable(false);
      this.testfilename.setEditable(false);
      this.Net = var1;
      JPanel var2 = new JPanel();
      var2.setLayout(new ColumnLayout());
      JPanel var3 = this.createTrainFilePanel();
      JPanel var4 = this.createTestFilePanel();
      JPanel var5 = this.createParametersPanel();
      JPanel var6 = this.createTrainForEpochs();
      JPanel var7 = this.createUntilError();
      JPanel var8 = this.createRestart();
      JPanel var9 = this.createTestPanel();
      JPanel var10 = this.createGraph();
      JPanel var11 = new JPanel();
      var11.setLayout(new FlowLayout());
      this.OK.addActionListener(new BCKMLPQpropTrain$1(this));
      JButton var12 = new JButton("Cancel");
      var11.add(this.OK);
      var11.add(var12);
      var12.addActionListener(new BCKMLPQpropTrain$2(this));
      var2.add(var3);
      var2.add(var4);
      var2.add(var5);
      var2.add(var6);
      var2.add(var7);
      var2.add(var8);
      var2.add(var9);
      var2.add(var10);
      var2.add(var11);
      this.getContentPane().add(var2);
      this.pack();
      this.centerDialog();
      this.globalError.setEditable(false);
      this.testScore.setEditable(false);
      this.max.setEditable(false);
   }

   public JPanel createTrainFilePanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JLabel var2 = new JLabel("Trianing File");
      JButton var3 = new JButton("Browse");
      var3.addActionListener(new BCKMLPQpropTrain$3(this));
      var1.add(var2);
      var1.add(this.trainfilename);
      var1.add(var3);
      return var1;
   }

   public JPanel createTestFilePanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JLabel var2 = new JLabel("Testing File ");
      JButton var3 = new JButton("Browse");
      var3.addActionListener(new BCKMLPQpropTrain$4(this));
      var1.add(var2);
      var1.add(this.testfilename);
      var1.add(var3);
      return var1;
   }

   public JPanel createParametersPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(new JLabel("Mu :"));
      this.mu = new bckRealNumberBox();
      var1.add(this.mu);
      var1.add(new JLabel("Epsilon :"));
      this.epsilon = new bckRealNumberBox();
      var1.add(this.epsilon);
      this.mu.setText(".5");
      this.epsilon.setText("1.1");
      return var1;
   }

   public JPanel createTrainForEpochs() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JLabel var2 = new JLabel(" Train For :                        ");
      this.trainForEpochs = new bckNumberBox();
      JLabel var3 = new JLabel(" Epochs ");
      JButton var4 = new JButton("Train");
      var4.addActionListener(new BCKMLPQpropTrain$5(this));
      var1.add(var2);
      var1.add(this.trainForEpochs);
      var1.add(var3);
      var1.add(var4);
      return var1;
   }

   public JPanel createUntilError() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = new JPanel();
      JPanel var3 = new JPanel();
      var2.setLayout(new FlowLayout());
      var2.add(new JLabel("Train until Global Error < "));
      this.trainUntilGE = new bckRealNumberBox();
      var2.add(this.trainUntilGE);
      JButton var4 = new JButton("Train");
      var2.add(var4);
      var4.addActionListener(new BCKMLPQpropTrain$6(this));
      var3.setLayout(new FlowLayout());
      var3.add(new JLabel("Stop After                        "));
      this.stopAfter = new bckNumberBox();
      var3.add(this.stopAfter);
      var3.add(new JLabel(" Epochs"));
      var1.add(var2);
      var1.add(var3);
      return var1;
   }

   public JPanel createRestart() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JButton var2 = new JButton("Restart");
      var1.add(var2);
      var2.addActionListener(new BCKMLPQpropTrain$7(this));
      var1.add(new JLabel("Epoch Count :"));
      this.epochCount = new JTextField(10);
      this.epochCount.setEditable(false);
      var1.add(this.epochCount);
      return var1;
   }

   public JPanel createTestPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JButton var2 = new JButton("Test");
      var1.add(var2);
      var2.addActionListener(new BCKMLPQpropTrain$8(this));
      var1.add(new JLabel("Test Score % : "));
      this.testScore = new JTextField(10);
      var1.add(this.testScore);
      return var1;
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
      var3.addActionListener(new BCKMLPQpropTrain$9(this));
      var4.addActionListener(new BCKMLPQpropTrain$10(this));
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

   public void OKPressed() {
      this.setVisible(false);
   }

   public void CancelPressed() {
      this.setVisible(false);
   }

   public void trainForEpochsPressed() {
      int var1 = this.trainForEpochs.getInteger();
      if (var1 < 1) {
         JOptionPane.showMessageDialog(null, "Illegal Number of Epochs:  " + var1, "Alert", 0);
      } else {
         this.Net.setEpsilon(this.epsilon.getRealNumber());
         this.Net.setMu(this.mu.getRealNumber());

         for (int var5 = 0; var5 < var1; var5++) {
            try {
               this.epochsSinceRestart++;
               this.epochCount.setText(String.valueOf(this.epochsSinceRestart));
               this.Net.train(1);
            } catch (Exception var7) {
               JOptionPane.showMessageDialog(null, var7.toString(), "Alert", 0);
               return;
            }

            double var2 = this.Net.getGlobalError();
            int var4 = (int)Math.ceil(var2);
            this.graphPane.newPoint(var2);
            this.globalError.setText(String.valueOf(var2));
         }
      }
   }

   public void trainUntilPressed() {
      double var1 = this.trainUntilGE.getRealNumber();
      int var3 = this.stopAfter.getInteger();
      if (var3 < 1) {
         JOptionPane.showMessageDialog(bckframe.top, "Illegal Number of Epochs in Stop After Field:  " + var3, "Alert", 0);
      } else {
         this.Net.setEpsilon(this.epsilon.getRealNumber());
         this.Net.setMu(this.mu.getRealNumber());

         for (int var7 = 0; var7 < var3; var7++) {
            try {
               this.epochsSinceRestart++;
               this.epochCount.setText(String.valueOf(this.epochsSinceRestart));
               this.Net.train(1);
            } catch (Exception var9) {
               JOptionPane.showMessageDialog(bckframe.top, var9.toString(), "Alert", 0);
               return;
            }

            double var4 = this.Net.getGlobalError();
            int var6 = (int)Math.ceil(var4);
            this.graphPane.newPoint(var4);
            this.globalError.setText(String.valueOf(var4));
            if (var4 < var1) {
               JOptionPane.showMessageDialog(bckframe.top, "Training Complete after " + var7 + " epochs, global Error reached : " + var4, "Alert", 0);
               return;
            }
         }

         JOptionPane.showMessageDialog(bckframe.top, "Training Complete, Target Global Error was NOT ACHIEVED", "Alert", 0);
      }
   }

   public void restartPressed() {
      this.Net.randomiseWeights();
   }

   public void testPressed() {
      double var1 = 0.0;

      try {
         var1 = this.Net.test();
      } catch (Exception var4) {
         JOptionPane.showMessageDialog(bckframe.top, var4.toString(), "Alert", 0);
         return;
      }

      this.testScore.setText(String.valueOf(var1));
   }

   public void browseTrainPressed() {
      FileDialog var1 = new FileDialog(bckframe.top, "Load Training File", 0);
      var1.show();
      String var2 = var1.getFile();
      if (var2 != null) {
         this.trainfilename.setText(var1.getDirectory() + var2);

         try {
            this.Net.setTrainingFile(var1.getDirectory() + var2);
         } catch (Exception var4) {
            JOptionPane.showMessageDialog(null, "Error Reading file \n" + var4, "Alert", 0);
         }
      }
   }

   public void browseTestPressed() {
      FileDialog var1 = new FileDialog(bckframe.top, "Load Training File", 0);
      var1.show();
      String var2 = var1.getFile();
      if (var2 != null) {
         this.testfilename.setText(var1.getDirectory() + var2);

         try {
            this.Net.setTestingFile(var1.getDirectory() + var2);
         } catch (Exception var4) {
            JOptionPane.showMessageDialog(null, "Error Reading file \n" + var4, "Alert", 0);
         }
      }
   }
}
