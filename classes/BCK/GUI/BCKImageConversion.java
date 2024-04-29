package BCK.GUI;

import com.sun.java.swing.JButton;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JList;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JScrollPane;
import com.sun.java.swing.JTextField;
import com.sun.java.swing.event.ListSelectionEvent;
import com.sun.java.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

public class BCKImageConversion extends BCKDialog implements ListSelectionListener {
   bckNumberBox size = new bckNumberBox();
   bckNumberBox classId = new bckNumberBox();
   private JList fileList = new JList();
   private JTextField outputVector = new JTextField(25);
   private JTextField outputFile = new JTextField(30);
   private FileDialog file = new FileDialog(bckframe.top);
   private Vector inputFiles = new Vector(0);
   private BCKPicture picture = new BCKPicture();
   private int[] pixels;
   private String outputFileName;
   private JTextField dimensions = new JTextField(20);

   public BCKImageConversion() {
      super(bckframe.top, "Image Converter", true);
      this.fileList.addListSelectionListener(this);
      this.getContentPane().setLayout(new ColumnLayout());
      JPanel var1 = this.buildTop();
      JPanel var2 = this.buildBottom();
      this.getContentPane().add(var1);
      this.getContentPane().add(var2);
      this.centerDialog();
      this.pack();
      this.repaint();
   }

   public JPanel buildTop() {
      JPanel var1 = new JPanel();
      var1.setLayout(new FlowLayout());
      var1.add(this.buildInputPanel());
      var1.add(this.buildOutputPanel());
      return var1;
   }

   public JPanel buildBottom() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JPanel var2 = new JPanel();
      var2.setLayout(new FlowLayout());
      JPanel var3 = new JPanel();
      var3.setLayout(new FlowLayout());
      var2.add(new JLabel("Output File"));
      var2.add(this.outputFile);
      JButton var4 = new JButton("Browse");
      var4.addActionListener(new BCKImageConversion$1(this));
      var2.add(var4);
      JButton var5 = new JButton("Done");
      var5.addActionListener(new BCKImageConversion$2(this));
      JButton var6 = new JButton("Write Output");
      var6.addActionListener(new BCKImageConversion$3(this));
      var3.add(var6);
      var3.add(var5);
      var1.add(var2);
      var1.add(var3);
      return var1;
   }

   public JPanel buildInputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      JButton var2 = new JButton("Add");
      var2.addActionListener(new BCKImageConversion$4(this));
      JButton var3 = new JButton("Remove");
      var3.addActionListener(new BCKImageConversion$5(this));
      var1.add(new JLabel("File List"));
      this.fileList.setListData(this.inputFiles);
      JScrollPane var4 = new JScrollPane(this.fileList);
      var4.setPreferredSize(new Dimension(400, 250));
      var1.add(var4);
      JPanel var5 = new JPanel();
      var5.setLayout(new FlowLayout());
      var5.add(var2);
      var5.add(var3);
      var1.add(var5);
      return var1;
   }

   public JPanel buildOutputPanel() {
      JPanel var1 = new JPanel();
      var1.setLayout(new ColumnLayout());
      var1.add(new JLabel("Output Vector Size :"));
      var1.add(this.size);
      var1.add(new JLabel("Set element to 1 :"));
      var1.add(this.classId);
      JButton var2 = new JButton("Generate");
      var1.add(var2);
      var2.addActionListener(new BCKImageConversion$6(this));
      var1.add(new JLabel("Output Vector :"));
      var1.add(this.outputVector);
      var1.add(new JLabel("Picture"));
      var1.add(this.picture);
      var1.add(new JLabel("Dimensions"));
      var1.add(this.dimensions);
      this.dimensions.setEditable(false);
      this.pack();
      return var1;
   }

   public void valueChanged(ListSelectionEvent var1) {
      if (this.fileList.isSelectionEmpty()) {
         this.picture.setImage(null);
      } else {
         this.picture.setImage((String)this.fileList.getSelectedValue());
         this.dimensions.setText(this.picture.getImage().getWidth(null) + " x " + this.picture.getImage().getHeight(null));
      }
   }

   public void addPressed() {
      this.file.setMode(0);
      this.file.setTitle("Add a File");
      this.file.show();
      String var1 = this.file.getFile();
      if (var1 != null) {
         this.inputFiles.addElement(this.file.getDirectory() + var1);
         this.fileList.setListData(this.inputFiles);
         this.repaint();
         this.pack();
      }
   }

   public void removePressed() {
      if (!this.fileList.isSelectionEmpty()) {
         this.inputFiles.removeElementAt(this.fileList.getSelectedIndex());
         this.fileList.setListData(this.inputFiles);
         this.repaint();
      }
   }

   public void browsePressed() {
      this.file.setMode(0);
      this.file.setTitle("Find Output File");
      this.file.show();
      this.outputFile.setText(this.file.getDirectory() + this.file.getFile());
      this.outputFileName = this.outputFile.getText();
   }

   public void writeOutputPressed() {
      if (this.outputVector.getText() == "") {
         JOptionPane.showMessageDialog(bckframe.top, "You must specify an Output Vector", "Alert", 0);
      } else {
         for (int var2 = 0; var2 < this.inputFiles.size(); var2++) {
            this.fileList.setSelectedIndex(var2);
            String var1 = (String)this.fileList.getSelectedValue();
            if (!var1.endsWith(".jpg") && !var1.endsWith(".JPG") && !var1.endsWith(".GIF") && !var1.endsWith(".gif")) {
               JOptionPane.showMessageDialog(
                  bckframe.top, "You must specify a GIF or a JPG as an input file\nInput " + (var2 + 1) + " is not an GIF or JPG file", "Alert", 0
               );
               return;
            }
         }

         DirectColorModel var3 = new DirectColorModel(32, 16711680, 65280, 255, -16777216);
         OutputStreamWriter var4 = null;
         PrintWriter var5 = null;

         try {
            var4 = new FileWriter(this.outputFileName);
            var5 = new PrintWriter(var4);

            for (int var7 = 0; var7 < this.inputFiles.size(); var7++) {
               this.fileList.setSelectedIndex(var7);
               this.picture.setImage((String)this.fileList.getSelectedValue());
               Image var8 = this.picture.getImage();
               MediaTracker var9 = new MediaTracker(this);
               var9.addImage(var8, 0);
               var9.waitForID(0);
               int var10 = var8.getWidth(null);
               int var11 = var8.getHeight(null);
               this.pixels = new int[var10 * var11];
               PixelGrabber var12 = new PixelGrabber(var8, 0, 0, var10, var11, this.pixels, 0, var10);
               var12.grabPixels();
               int[] var6 = this.halve(this.pixels, var10, var11);
               var6 = this.halve(var6, var10 / 2, var11 / 2);
               int var13 = var10 / 4;

               for (int var14 = 0; var14 < var6.length; var14++) {
                  if (var14 % var13 == 0 && var14 != 0) {
                     var5.println("");
                  }

                  var5.print(" " + var3.getRed(var6[var14]));
               }

               var5.println("");
               var5.println(this.outputVector.getText());
               var5.println("");
            }

            var4.flush();
            var4.close();
         } catch (Exception var15) {
            JOptionPane.showMessageDialog(
               bckframe.top, "Error :" + var15.toString() + "\nPlease Ensure you have named a valid file and an output vector", "Alert", 0
            );
         }
      }
   }

   public int[] halve(int[] var1, int var2, int var3) {
      int[] var6 = new int[var1.length / 4];
      int var7 = 0;

      for (byte var8 = 0; var8 < var3; var8 += 2) {
         int var5 = var8 * var2;

         for (byte var9 = 0; var9 < var2; var9 += 2) {
            int var4 = var1[var5 + var9] + var1[var5 + var9 + 1] + var1[var5 + var2 + var9] + var1[var5 + var2 + var9 + 1];
            var4 /= 4;
            var6[var7++] = var4;
         }
      }

      return var6;
   }

   public void CancelPressed() {
      this.setVisible(false);
   }

   public void OKPressed() {
   }
}
