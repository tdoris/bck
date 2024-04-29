package BCK.GUI;

import BCK.ANN.BCKFilter;
import BCK.ANN.BCKNeuralNetwork;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JCheckBox;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.FileWriter;
import java.io.PrintWriter;

public class BCKClassify extends BCKDialog {
   JTextField filename;
   JTextField outputFileName = new JTextField(25);
   JTextField className;
   BCKFilter input;
   BCKNeuralNetwork Net;
   BCKPlot plotPane = new BCKPlot(bckframe.top, 200, 200);
   bckNumberBox numOfRecords;
   bckNumberBox recordNumber;
   bckNumberBox widthNumberBox = new bckNumberBox();
   double maxFieldValue;
   int currentRecordNumber;
   JPanel table;
   boolean printClass = false;
   boolean printInputRecords = false;
   boolean inputOnly;
   String winnerName;
   String fileNameString = "";
   int numberOfRecords;
   JCheckBox printClassNames;
   JCheckBox printInput;
   JCheckBox inputOnlyCheck;

   public BCKClassify(BCKNeuralNetwork var1) {
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
      this.inputOnlyCheck = new JCheckBox("File Contains Input Fields Only");
      this.inputOnlyCheck.addActionListener(new BCKClassify$1(this));
      var1.add(this.inputOnlyCheck);
      var2.add(new JLabel("Input File"));
      this.filename = new JTextField(25);
      this.filename.setText(this.fileNameString);
      var2.add(this.filename);
      JButton var3 = new JButton("Browse");
      var3.addActionListener(new BCKClassify$2(this));
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
      var7.addActionListener(new BCKClassify$3(this));
      JButton var8 = new JButton("Next");
      var6.add(var8);
      var8.addActionListener(new BCKClassify$4(this));
      JPanel var9 = new JPanel();
      var9.add(this.plotPane);
      JPanel var10 = new JPanel();
      var10.setLayout(new FlowLayout());
      JButton var11 = new JButton("Classify");
      var10.add(var11);
      var11.addActionListener(new BCKClassify$5(this));
      var10.add(new JLabel("Class :"));
      this.className = new JTextField(20);
      this.className.setText(this.winnerName);
      var10.add(this.className);
      JPanel var12 = this.createBatchPanel();
      JPanel var13 = new JPanel();
      this.table = this.createTable();
      var13.add(this.table);
      JPanel var14 = new JPanel();
      var14.setLayout(new FlowLayout());
      JButton var15 = new JButton("OK");
      JButton var16 = new JButton("Cancel");
      var14.add(var15);
      var14.add(var16);
      var16.addActionListener(new BCKClassify$6(this));
      JPanel var17 = new JPanel();
      var17.add(new JLabel("Width :"));
      var17.add(this.widthNumberBox);
      var1.add(var2);
      var1.add(var4);
      var1.add(var6);
      var1.add(var17);
      var1.add(var9);
      var1.add(var10);
      var1.add(var12);
      var1.add(var13);
      var1.add(var14);
      this.getContentPane().removeAll();
      this.getContentPane().add(var1);
   }

   public JPanel createBatchPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = new JPanel();
      var2.add(new JLabel("Output File"));
      var2.add(this.outputFileName);
      JButton var3 = new JButton("Browse");
      var3.addActionListener(new BCKClassify$7(this));
      var2.add(var3);
      var1.add(var2);
      this.printInput = new JCheckBox("Print Input Records");
      this.printInput.addActionListener(new BCKClassify$8(this));
      this.printClassNames = new JCheckBox("Print Class Name");
      this.printClassNames.addActionListener(new BCKClassify$9(this));
      JButton var4 = new JButton("Process File");
      var4.addActionListener(new BCKClassify$10(this));
      var1.add(this.printInput);
      var1.add(this.printClassNames);
      var1.add(var4);
      return var1;
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
         if (this.inputOnly) {
            try {
               this.input = new BCKFilter(var1.getDirectory() + var2, this.Net.getNumInputs());
            } catch (Exception var5) {
               JOptionPane.showMessageDialog(
                  null,
                  "Error in file Format \n Please check that the architecture you have specified \n matches the format of the data you are trying to classify.\n"
                     + var5.toString(),
                  "Alert",
                  0
               );
               return;
            }
         } else {
            try {
               this.input = new BCKFilter(var1.getDirectory() + var2, this.Net.getNumInputs() + this.Net.getNumOutputs());
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
         }

         this.filename.setText(var1.getDirectory() + var2);
         this.numOfRecords.setText(new Integer(this.input.getNumberOfRecords()).toString());
         this.numberOfRecords = this.numOfRecords.getInteger();
         this.fileNameString = this.filename.getText();
      }
   }

   public void browseOutPressed() {
      FileDialog var1 = new FileDialog(bckframe.top, "Set Output File", 0);
      var1.show();
      String var2 = var1.getFile();
      if (var2 != null) {
         this.outputFileName.setText(var1.getDirectory() + var2);
      }
   }

   public void processFilePressed() {
      String var1 = this.outputFileName.getText();
      FileWriter var4 = null;
      PrintWriter var5 = null;

      try {
         var4 = new FileWriter(var1);
         var5 = new PrintWriter(var4);

         for (int var6 = 0; var6 < this.input.getNumberOfRecords(); var6++) {
            double[] var2;
            double[] var3;
            try {
               var2 = this.input.getRecordAt(var6);
               var3 = this.Net.classify(var2);
            } catch (Exception var9) {
               JOptionPane.showMessageDialog(null, "Error" + var9.toString(), "Alert", 0);
               return;
            }

            if (this.printInputRecords) {
               for (int var7 = 0; var7 < this.Net.getNumInputs(); var7++) {
                  var5.print(var2[var7] + " ");
               }

               var5.println();
            }

            if (this.printClass) {
               var5.println(this.Net.getWinner().getName());
            } else {
               for (int var12 = 0; var12 < var3.length; var12++) {
                  var5.print(var3[var12] + " ");
               }

               var5.println("");
            }
         }
      } catch (Exception var10) {
         JOptionPane.showMessageDialog(bckframe.top, "Error Writing Data - Check your input" + var10.toString(), "Alert", 0);
      }

      try {
         var4.flush();
         var4.close();
      } catch (Exception var8) {
         JOptionPane.showMessageDialog(bckframe.top, "Error Writing Data (closing files) - Possibly Disk Full" + var8.toString(), "Alert", 0);
      }
   }

   private void setRecordNumber() {
      this.currentRecordNumber = this.recordNumber.getInteger();
   }

   public void classifyPressed() {
      try {
         double[] var1 = this.input.getRecordAt(this.currentRecordNumber);
         this.Net.classify(var1);
      } catch (Exception var2) {
         JOptionPane.showMessageDialog(null, "Error" + var2.toString(), "Alert", 0);
         return;
      }

      this.winnerName = this.Net.getWinner().getName();
      this.makePane();
      this.pack();
      this.repaint();
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
