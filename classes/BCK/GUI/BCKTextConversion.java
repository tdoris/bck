package BCK.GUI;

import BCK.ANN.BCKFilter;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JTextField;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.io.FileWriter;
import java.io.PrintWriter;

public class BCKTextConversion extends BCKDialog {
   private JButton done;
   private JButton writeOutput;
   private JTextField inputFile = new JTextField(25);
   private JTextField outputFile = new JTextField(25);
   private JButton browseInput;
   private JButton browseOutput;
   private bckNumberBox numberOfFields = new bckNumberBox();
   private bckNumberBox EOLAfter = new bckNumberBox();
   private bckNumberBox replaceField = new bckNumberBox();
   private JTextField outputVector = new JTextField(25);
   private int fieldsPerRecord;
   private int opEOL;
   private int numToReplace;
   private String opString;
   private String inputFileName;
   private String outputFileName;
   private BCKFilter inData;

   public BCKTextConversion() {
      super(bckframe.top, "Convert Textual Data", true);
      this.setSize(300, 80);
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      var1.add(this.buildNumberOfFieldsPanel());
      var1.add(this.buildEOLPanel());
      var1.add(this.buildReplacePanel());
      var1.add(this.buildWithPanel());
      var1.add(this.buildInputPanel());
      var1.add(this.buildOutputPanel());
      var1.add(this.buildButtonsPanel());
      this.getContentPane().add(var1);
      this.pack();
      this.centerDialog();
      this.setResizable(false);
   }

   public JPanel buildInputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(new JLabel("Input File:", 4));
      this.browseInput = new JButton("Browse");
      this.browseInput.addActionListener(new BCKTextConversion$1(this));
      var1.add(this.inputFile);
      var1.add(this.browseInput);
      return var1;
   }

   public JPanel buildOutputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(new JLabel("Output File : ", 4));
      this.browseOutput = new JButton("Browse");
      this.browseOutput.addActionListener(new BCKTextConversion$2(this));
      var1.add(this.outputFile);
      var1.add(this.browseOutput);
      return var1;
   }

   public JPanel buildNumberOfFieldsPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(new JLabel("Number of Fields Per Record : ", 4));
      this.numberOfFields = new bckNumberBox();
      var1.add(this.numberOfFields);
      return var1;
   }

   public JPanel buildEOLPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(new JLabel("Output EOL After : ", 4));
      this.EOLAfter = new bckNumberBox();
      var1.add(this.EOLAfter);
      return var1;
   }

   public JPanel buildReplacePanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(new JLabel("Replace Field : ", 4));
      var1.add(this.replaceField);
      return var1;
   }

   public JPanel buildWithPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(new JLabel(" With : ", 4));
      var1.add(this.outputVector);
      return var1;
   }

   public JPanel buildButtonsPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      this.writeOutput = new JButton("Write Output");
      this.writeOutput.addActionListener(new BCKTextConversion$3(this));
      this.done = new JButton("Done");
      this.done.addActionListener(new BCKTextConversion$4(this));
      var1.add(this.writeOutput);
      var1.add(this.done);
      return var1;
   }

   public void browseOutputPressed() {
      FileDialog var1 = new FileDialog(bckframe.top, "Select Output File", 0);
      var1.show();
      String var2 = var1.getFile();
      if (var2 != null) {
         this.outputFile.setText(var1.getDirectory() + var2);
      }
   }

   public void browseInputPressed() {
      if (this.validateData()) {
         FileDialog var1 = new FileDialog(bckframe.top, "Select Input File", 0);
         var1.show();
         String var2 = var1.getFile();
         if (var2 != null) {
            this.inputFile.setText(var1.getDirectory() + var2);
            this.validateData();

            try {
               this.inData = new BCKFilter(this.inputFileName, this.fieldsPerRecord);
            } catch (Exception var4) {
               JOptionPane.showMessageDialog(bckframe.top, "Error Reading Data - Check your input file" + var4.toString(), "Alert", 0);
            }
         }
      }
   }

   public void writeOutputPressed() {
      if (this.validateData()) {
         FileWriter var2 = null;
         PrintWriter var3 = null;

         try {
            var2 = new FileWriter(this.outputFileName);
            var3 = new PrintWriter(var2);

            for (int var4 = 0; var4 < this.inData.getNumberOfRecords(); var4++) {
               double[] var1 = this.inData.getRecordAt(var4);

               for (int var5 = 0; var5 < this.fieldsPerRecord; var5++) {
                  if (var5 == this.numToReplace - 1) {
                     var3.println("");
                     var3.println(this.opString);
                  } else {
                     var3.print(" " + var1[var5]);
                  }

                  if (var5 % this.opEOL == 0 && var5 != 0) {
                     var3.println("");
                  }
               }
            }
         } catch (Exception var7) {
            JOptionPane.showMessageDialog(bckframe.top, "Error Writing Data - Check your input" + var7.toString(), "Alert", 0);
         }

         try {
            var2.flush();
            var2.close();
         } catch (Exception var6) {
            JOptionPane.showMessageDialog(bckframe.top, "Error Writing Data (closing files) - Possibly Disk Full Error" + var6.toString(), "Alert", 0);
         }
      }
   }

   public boolean validateData() {
      this.fieldsPerRecord = this.numberOfFields.getInteger();
      this.opEOL = this.EOLAfter.getInteger();
      this.numToReplace = this.replaceField.getInteger();
      if (this.fieldsPerRecord >= 0 && this.opEOL >= 0 && this.numToReplace >= 0) {
         this.opString = this.outputVector.getText();
         this.inputFileName = this.inputFile.getText();
         this.outputFileName = this.outputFile.getText();
         return true;
      } else {
         JOptionPane.showMessageDialog(bckframe.top, "Please enter Valid Data", "Alert", 0);
         return false;
      }
   }
}
