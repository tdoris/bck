package BCK.GUI;

import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.FileDialog;
import java.awt.FlowLayout;

public class BCKRBFTrain extends BCKDialog {
   int epochsSinceRestart;
   JTextField epochCount;
   JTextField trainfilename = new JTextField(25);
   JTextField testfilename = new JTextField(25);
   JButton OK = new JButton("OK");
   bckNumberBox trainForEpochs;
   BCKRBFDrawable Net;
   JTextField globalError;
   JTextField testScore;
   JTextField max;
   BCKProgressPanel progress;
   BCKRBFTrain.TrainThread trainThread;
   Object lock = new Object();
   boolean shouldStop = false;

   public BCKRBFTrain(BCKRBFDrawable var1) {
      super(bckframe.top, "RBF Dynamic Decay Adjustment", true);
      this.Net = var1;
      JPanel var2 = new JPanel();
      var2.setLayout(new ColumnLayout());
      this.trainfilename.setEditable(false);
      this.testfilename.setEditable(false);
      JPanel var3 = this.createTrainFilePanel();
      JPanel var4 = this.createTestFilePanel();
      JPanel var5 = this.createTrainForEpochs();
      JPanel var6 = this.createRestart();
      JPanel var7 = this.createTestPanel();
      this.progress = new BCKProgressPanel();
      JPanel var8 = new JPanel();
      var8.setLayout(new FlowLayout());
      this.OK.addActionListener(new BCKRBFTrain$1(this));
      JButton var9 = new JButton("Cancel");
      var8.add(this.OK);
      var8.add(var9);
      var9.addActionListener(new BCKRBFTrain$2(this));
      var2.add(var3);
      var2.add(var4);
      var2.add(var5);
      var2.add(this.progress);
      var2.add(var6);
      var2.add(var7);
      var2.add(var8);
      this.getContentPane().add(var2);
      this.pack();
      this.centerDialog();
      this.testScore.setEditable(false);
   }

   public JPanel createTrainFilePanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JLabel var2 = new JLabel("Training File");
      JButton var3 = new JButton("Browse");
      var3.addActionListener(new BCKRBFTrain$3(this));
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
      var3.addActionListener(new BCKRBFTrain$4(this));
      var1.add(var2);
      var1.add(this.testfilename);
      var1.add(var3);
      return var1;
   }

   public JPanel createTrainForEpochs() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      JLabel var2 = new JLabel(" Train For :                        ");
      this.trainForEpochs = new bckNumberBox();
      JLabel var3 = new JLabel(" Epochs ");
      JButton var4 = new JButton("Train");
      var4.addActionListener(new BCKRBFTrain$5(this));
      JButton var5 = new JButton("Stop");
      var5.addActionListener(new BCKRBFTrain$6(this));
      var1.add(var2);
      var1.add(this.trainForEpochs);
      var1.add(var3);
      var1.add(var4);
      var1.add(var5);
      return var1;
   }

   public JPanel createRestart() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
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
      var2.addActionListener(new BCKRBFTrain$7(this));
      var1.add(new JLabel("Test Score % : "));
      this.testScore = new JTextField(10);
      var1.add(this.testScore);
      return var1;
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
         this.trainThread = new BCKRBFTrain.TrainThread();
         this.trainThread.start();
      }
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

   class TrainThread extends Thread {
      int currentEpoch;
      int maxEpoch;

      // $VF: Could not inline inconsistent finally blocks
      // $VF: Could not create synchronized statement, marking monitor enters and exits
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      public void run() {
         int var1 = BCKRBFTrain.this.trainForEpochs.getInteger();
         if (var1 < 1) {
            JOptionPane.showMessageDialog(null, "Illegal Number of Epochs:  " + var1, "Alert", 0);
         } else {
            BCKRBFTrain.this.progress.setMaximum(var1);
            BCKRBFTrain.this.progress.setValue(0);

            for (int var2 = 0; var2 < var1; var2++) {
               try {
                  BCKRBFTrain.this.epochsSinceRestart++;
                  BCKRBFTrain.this.epochCount.setText(String.valueOf(BCKRBFTrain.this.epochsSinceRestart));
                  BCKRBFTrain.this.Net.train(1);
                  BCKRBFTrain.this.Net.notifyObservers();
                  BCKRBFTrain.this.progress.setValue(var2 + 1);
               } catch (Exception var8) {
                  JOptionPane.showMessageDialog(null, var8.toString(), "Alert", 0);
                  return;
               }

               Object var3 = BCKRBFTrain.this.lock;
               synchronized (var3){} // $VF: monitorenter 

               label65: {
                  try {
                     if (!BCKRBFTrain.this.shouldStop) {
                        try {
                           BCKRBFTrain.this.lock.wait(1L);
                        } catch (InterruptedException var9) {
                        }
                        break label65;
                     }
                  } catch (Throwable var10) {
                     // $VF: monitorexit
                     throw var10;
                  }

                  // $VF: monitorexit
                  break;
               }

               // $VF: monitorexit
            }

            BCKRBFTrain.this.trainThread = null;
         }
      }
   }
}
