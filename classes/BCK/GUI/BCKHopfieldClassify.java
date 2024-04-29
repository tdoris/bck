package BCK.GUI;

import BCK.ANN.BCKFilter;
import BCK.ANN.BCKNeuralNetwork;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Graphics;

public class BCKHopfieldClassify extends BCKDialog {
   JTextField filename;
   BCKFilter input;
   BCKNeuralNetwork Net;
   BCKPlot plotPane = new BCKPlot(bckframe.top, 200, 200);
   BCKPlot resultPlot = new BCKPlot(bckframe.top, 200, 200);
   bckNumberBox numOfRecords;
   bckNumberBox recordNumber;
   bckNumberBox widthNumberBox = new bckNumberBox();
   double maxFieldValue;
   int currentRecordNumber;
   JPanel table;
   String winnerName;
   String fileNameString = "";
   int numberOfRecords;

   public BCKHopfieldClassify(BCKNeuralNetwork var1) {
      super(bckframe.top, "Classification", true);
      this.Net = var1;
      this.makePane();
      this.pack();
      this.centerDialog();
   }

   public void makePane() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = new JPanel();
      var2.setLayout(new FlowLayout());
      var2.add(new JLabel("Input File"));
      this.filename = new JTextField(25);
      this.filename.setText(this.fileNameString);
      var2.add(this.filename);
      JButton var3 = new JButton("Browse");
      var3.addActionListener(new BCKHopfieldClassify$1(this));
      var2.add(var3);
      JPanel var4 = new JPanel();
      JLabel var5 = new JLabel("Number of Records is : ");
      this.numOfRecords = new bckNumberBox();
      this.numOfRecords.setText(String.valueOf(this.numberOfRecords));
      this.numOfRecords.setEditable(false);
      var4.add(var5);
      var4.add(this.numOfRecords);
      JPanel var6 = new JPanel();
      var6.setLayout(new FlowLayout());
      var6.add(new JLabel("Record Number :"));
      this.recordNumber = new bckNumberBox();
      this.recordNumber.setText(String.valueOf(this.currentRecordNumber));
      var6.add(this.recordNumber);
      JButton var7 = new JButton("Load");
      var6.add(var7);
      var7.addActionListener(new BCKHopfieldClassify$2(this));
      JButton var8 = new JButton("Next");
      var6.add(var8);
      var8.addActionListener(new BCKHopfieldClassify$3(this));
      JPanel var9 = new JPanel();
      var9.add(this.plotPane);
      var9.add(this.resultPlot);
      JPanel var10 = new JPanel();
      var10.setLayout(new FlowLayout());
      JButton var11 = new JButton("Classify");
      var10.add(var11);
      var11.addActionListener(new BCKHopfieldClassify$4(this));
      JPanel var12 = new JPanel();
      this.table = this.createTable();
      var12.add(this.table);
      JPanel var13 = new JPanel();
      var13.setLayout(new FlowLayout());
      JButton var14 = new JButton("OK");
      JButton var15 = new JButton("Cancel");
      var13.add(var14);
      var13.add(var15);
      var15.addActionListener(new BCKHopfieldClassify$5(this));
      JPanel var16 = new JPanel();
      var16.add(new JLabel("Width :"));
      var16.add(this.widthNumberBox);
      var1.add(var2);
      var1.add(var4);
      var1.add(var6);
      var1.add(var16);
      var1.add(var9);
      var1.add(var10);
      var1.add(var13);
      this.getContentPane().removeAll();
      this.getContentPane().add(var1);
   }

   public JPanel createTable() {
      return new BCKOutputTable(this.Net);
   }

   public void paint(Graphics var1) {
      super.paint(var1);
      if (this.input != null) {
         int var2 = this.widthNumberBox.getInteger();
         if (this.numOfRecords.getInteger() <= this.recordNumber.getInteger() || this.numOfRecords.getInteger() < 0) {
            return;
         }

         if (var2 > 0) {
            this.loadPressed();
         }
      }
   }

   public void CancelPressed() {
      this.setVisible(false);
   }

   public void loadPressed() {
      if (this.numOfRecords.getInteger() > this.recordNumber.getInteger() && this.numOfRecords.getInteger() >= 0) {
         this.displayRecord(this.recordNumber.getInteger());
         this.currentRecordNumber = this.recordNumber.getInteger();
      } else {
         JOptionPane.showMessageDialog(null, "The Record Number You Choose must be less than " + this.numOfRecords.getInteger(), "Alert", 0);
      }
   }

   public void nextPressed() {
      this.setRecordNumber();
      int var1 = this.recordNumber.getInteger();
      if (var1 < 0) {
         JOptionPane.showMessageDialog(null, "Enter a valid record number first." + this.numOfRecords.getInteger(), "Alert", 0);
      } else {
         if (++var1 >= this.numOfRecords.getInteger()) {
            var1 = 0;
         }

         this.recordNumber.setText(String.valueOf(var1));
         this.loadPressed();
      }
   }

   public void browsePressed() {
      FileDialog var1 = new FileDialog(bckframe.top, "Set Input File", 0);
      var1.show();
      String var2 = var1.getFile();
      if (var2 != null) {
         try {
            this.input = new BCKFilter(var1.getDirectory() + var2, this.Net.getNumInputs());
         } catch (Exception var4) {
            JOptionPane.showMessageDialog(
               null,
               "Error in file Format \n Please check that the architecture you have specified \n matches the format of the data you are trying to classify.\n"
                  + var4.toString(),
               "Alert",
               0
            );
            return;
         }

         this.filename.setText(var1.getDirectory() + var2);
         this.numOfRecords.setText(new Integer(this.input.getNumberOfRecords()).toString());
         this.numberOfRecords = this.numOfRecords.getInteger();
         this.fileNameString = this.filename.getText();
      }
   }

   private void setRecordNumber() {
      this.currentRecordNumber = this.recordNumber.getInteger();
   }

   public void classifyPressed() {
      Object var1 = null;

      try {
         double[] var2 = this.input.getRecordAt(this.currentRecordNumber);
         var1 = this.Net.classify(var2);
      } catch (Exception var3) {
         JOptionPane.showMessageDialog(null, "Error : " + var3.toString(), "Alert", 0);
         return;
      }

      this.drawOutput((double[])var1);
   }

   public void drawOutput(double[] var1) {
      int var2 = this.Net.getNumOutputs();
      int[] var3 = new int[var2];
      int var4 = this.widthNumberBox.getInteger();
      byte var5 = 4;
      if (var4 <= 0) {
         JOptionPane.showMessageDialog(null, "Please Enter the correct width", "Alert", 0);
      } else {
         int var6 = this.Net.getNumOutputs() / var4;

         for (int var7 = 0; var7 < var2; var7++) {
            if (var1[var7] == -1.0) {
               var3[var7] = 0;
            } else {
               var3[var7] = 1;
            }
         }

         if (var6 > this.plotPane.height || var4 > this.plotPane.width) {
            this.plotPane.setPreferredSize(new Dimension(var4 * var5, var6 * var5));
            this.pack();
         }

         this.resultPlot.drawPicture(var3, var4, 1, var5);
      }
   }

   public void displayRecord(int var1) {
      double[] var2 = this.input.getRecordAt(var1);
      int var3 = this.Net.getNumInputs();
      int[] var4 = new int[var3];
      int var5 = this.widthNumberBox.getInteger();
      byte var6 = 4;
      if (var5 <= 0) {
         JOptionPane.showMessageDialog(null, "Please Enter the correct width", "Alert", 0);
      } else {
         int var7 = this.Net.getNumInputs() / var5;
         double var8 = 0.0;

         for (int var10 = 0; var10 < var3; var10++) {
            var8 = Math.max(var2[var10], var8);
            var4[var10] = (int)var2[var10];
         }

         if (var8 == 0.0) {
            var8 = 1.0;
         }

         int var11 = (int)var8;
         if (var7 > this.plotPane.height || var5 > this.plotPane.width) {
            this.plotPane.setPreferredSize(new Dimension(var5 * var6, var7 * var6));
            this.pack();
         }

         this.plotPane.drawPicture(var4, var5, var11, var6);
      }
   }
}
