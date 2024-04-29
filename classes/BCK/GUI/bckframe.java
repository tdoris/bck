package BCK.GUI;

import BCK.ANN.BCKNetConnection;
import BCK.ANN.BCKNeuralNetwork;
import com.sun.java.swing.ImageIcon;
import com.sun.java.swing.JButton;
import com.sun.java.swing.JFrame;
import com.sun.java.swing.JLayeredPane;
import com.sun.java.swing.JMenu;
import com.sun.java.swing.JMenuBar;
import com.sun.java.swing.JMenuItem;
import com.sun.java.swing.JOptionPane;
import com.sun.java.swing.JSeparator;
import com.sun.java.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class bckframe extends JFrame implements ActionListener, WindowListener, MouseListener {
   public static bckframe top = null;
   public static String home = System.getProperty("BCK.home");
   public static Image applicationImage;
   BCKBrainOutputDrawable brainOutput = new BCKBrainOutputDrawable();
   String theBrainFilename;
   Vector Lines = new Vector(0);
   BCKBrainDrawable theBrain;
   BorderLayout borderLayout1 = new BorderLayout();
   InternalWindowPanel bckInternal = new InternalWindowPanel();
   bckCreateHopfieldDialog CreateHopfield = new bckCreateHopfieldDialog(this);
   bckCreateKohonenDialog CreateKohonen = new bckCreateKohonenDialog(this);
   bckCreateMLPDialog CreateMLP = new bckCreateMLPDialog(this);
   bckCreateRBFDialog CreateRBF = new bckCreateRBFDialog(this);
   FileDialog bckFileDialog = new FileDialog(this);
   JMenuBar bckMenuBar = new JMenuBar();
   JMenu bckFile = new JMenu("File");
   JMenu bckBrain = new JMenu("Brain");
   JMenu bckNetwork = new JMenu("Network");
   JMenu bckOptions = new JMenu("Options");
   JMenu bckData = new JMenu("Data");
   JMenu bckHelp = new JMenu("Help");
   JMenuItem bckFileNew = new JMenuItem("New");
   JMenuItem bckFileOpen = new JMenuItem("Open");
   JMenuItem bckFileSaveAs = new JMenuItem("Save As...");
   JMenuItem bckFileSave = new JMenuItem("Save");
   JMenuItem bckFileExit = new JMenuItem("Exit");
   JMenuItem bckBrainConfigure = new JMenuItem("Configure...");
   JMenuItem bckBrainSetInputFile = new JMenuItem("Set Input File...");
   JMenuItem bckBrainClassify = new JMenuItem("Classify...");
   JMenuItem bckBrainEdit = new JMenuItem("Edit...");
   JMenuItem bckBrainOutputs = new JMenuItem("Configure Outputs");
   JMenuItem bckNetworkLoad = new JMenuItem("Load");
   JMenuItem bckNetworkSave = new JMenuItem("Save");
   JMenu bckNetworkAdd = new JMenu("Add");
   JMenuItem bckNetworkEdit = new JMenuItem("Edit");
   JMenuItem bckNetworkClassify = new JMenuItem("Classify");
   JMenuItem bckAddRBF = new JMenuItem("RBF");
   JMenuItem bckAddMLP = new JMenuItem("MLP");
   JMenuItem bckAddHopfield = new JMenuItem("Hopfield");
   JMenuItem bckAddKohonen = new JMenuItem("Kohonen");
   JMenuItem bckOptionRefresh = new JMenuItem("Refresh");
   JMenuItem bckImageConversion = new JMenuItem("Images...");
   JMenuItem bckTextConversion = new JMenuItem("Text...");
   JMenuItem bckHelpUserMan = new JMenuItem("User Manual");
   JMenuItem bckHelpAbout = new JMenuItem("About");
   JToolBar bckToolbar;

   public bckframe() {
      bckframe$1 var1 = new bckframe$1();
      this.addWindowListener(var1);
      this.enableEvents(16L);
      this.createNewBrain();
      this.drawANet(this.brainOutput);
      this.brainOutput.getImage().setLocation(new Point(800, 200));

      try {
         this.jbInit();
      } catch (Exception var3) {
         var3.printStackTrace();
         JOptionPane.showMessageDialog(top, "Error :" + var3.toString(), "Alert", 0);
      }

      top = this;
   }

   private void jbInit() throws Exception {
      applicationImage = BCKImageControl.loadImageIcon(home + "/images/BCK.JPG", "The Application Icon").getImage();
      this.getContentPane().setLayout(this.borderLayout1);
      this.setSize(new Dimension(1000, 700));
      this.setIconImage(applicationImage);
      this.getContentPane().add(this.bckInternal, "Center");
      this.bckInternal.setVisible(true);
      this.setTitle("Brain Construction Kit");
      this.bckFileDialog.setModal(true);
      this.setJMenuBar(this.bckMenuBar);
      this.bckMenuBar.add(this.bckFile);
      this.bckMenuBar.add(this.bckBrain);
      this.bckMenuBar.add(this.bckNetwork);
      this.bckMenuBar.add(this.bckOptions);
      this.bckMenuBar.add(this.bckData);
      this.bckMenuBar.add(this.bckHelp);
      this.bckFile.add(this.bckFileNew);
      this.bckFileNew.addActionListener(this);
      this.bckFile.add(this.bckFileOpen);
      this.bckFileOpen.addActionListener(this);
      this.bckFile.add(this.bckFileSaveAs);
      this.bckFileSaveAs.addActionListener(this);
      this.bckFile.add(this.bckFileSave);
      this.bckFileSave.addActionListener(this);
      this.bckFile.add(new JSeparator());
      this.bckFile.add(this.bckFileExit);
      this.bckFileExit.addActionListener(this);
      this.bckBrain.add(this.bckBrainConfigure);
      this.bckBrainConfigure.addActionListener(this);
      this.bckBrain.add(this.bckBrainSetInputFile);
      this.bckBrainSetInputFile.addActionListener(this);
      this.bckBrain.add(this.bckBrainClassify);
      this.bckBrainClassify.addActionListener(this);
      this.bckBrain.add(this.bckBrainEdit);
      this.bckBrainEdit.addActionListener(this);
      this.bckBrain.add(this.bckBrainOutputs);
      this.bckBrainOutputs.addActionListener(this);
      JMenu var1 = (JMenu)this.bckNetwork.add(this.bckNetworkAdd);
      JMenuItem var2 = var1.add(this.bckAddMLP);
      var2 = var1.add(this.bckAddRBF);
      var2 = var1.add(this.bckAddHopfield);
      var2 = var1.add(this.bckAddKohonen);
      this.bckAddMLP.addActionListener(this);
      this.bckAddRBF.addActionListener(this);
      this.bckAddHopfield.addActionListener(this);
      this.bckAddKohonen.addActionListener(this);
      this.bckNetwork.add(this.bckNetworkLoad);
      this.bckNetworkLoad.addActionListener(this);
      this.bckNetwork.add(this.bckNetworkSave);
      this.bckNetworkSave.addActionListener(this);
      this.bckNetwork.add(new JSeparator());
      this.bckNetwork.add(this.bckNetworkEdit);
      this.bckNetworkEdit.addActionListener(this);
      this.bckNetwork.add(this.bckNetworkClassify);
      this.bckNetworkClassify.addActionListener(this);
      this.bckOptions.add(this.bckOptionRefresh);
      this.bckOptionRefresh.addActionListener(this);
      this.bckData.add(this.bckImageConversion);
      this.bckImageConversion.addActionListener(this);
      this.bckData.add(this.bckTextConversion);
      this.bckTextConversion.addActionListener(this);
      this.bckHelp.add(this.bckHelpUserMan);
      this.bckHelpUserMan.addActionListener(this);
      this.bckHelp.add(this.bckHelpAbout);
      this.bckHelpAbout.addActionListener(this);
      this.bckToolbar = this.createToolbar();
      this.bckInternal.add(this.bckToolbar, "North");
   }

   private JToolBar createToolbar() {
      JToolBar var1 = new JToolBar();
      var1.setFloatable(false);
      JButton var2 = new JButton(BCKImageControl.loadImageIcon(home + "images/new.gif", "Create a New System"));
      var2.addActionListener(new bckframe$2(this));
      JButton var3 = new JButton(BCKImageControl.loadImageIcon(home + "images/save.gif", "Save the System"));
      var3.addActionListener(new bckframe$3(this));
      JButton var4 = new JButton(BCKImageControl.loadImageIcon(home + "images/open.gif", "Open"));
      var4.addActionListener(new bckframe$4(this));
      JButton var5 = new JButton(BCKImageControl.loadImageIcon(home + "images/cut.gif", "Cut the Selected Network"));
      var5.addActionListener(new bckframe$5(this));
      JButton var6 = new JButton(BCKImageControl.loadImageIcon(home + "images/copy.gif", "Copy the Selected Network"));
      var6.addActionListener(new bckframe$6(this));
      JButton var7 = new JButton(BCKImageControl.loadImageIcon(home + "images/paste.gif", "Paste the Selected Network"));
      var7.addActionListener(new bckframe$7(this));
      JButton var8 = new JButton(BCKImageControl.loadImageIcon(home + "images/HOPFIELDTOOLBAR.jpg", "Add a Hopfield Network"));
      var8.addActionListener(new bckframe$8(this));
      JButton var9 = new JButton(BCKImageControl.loadImageIcon(home + "images/MLPQPROPTOOLBAR.jpg", "Add an MLP Network"));
      var9.addActionListener(new bckframe$9(this));
      JButton var10 = new JButton(BCKImageControl.loadImageIcon(home + "images/KOHONENTOOLBAR.jpg", "Add a Kohonen Network"));
      var10.addActionListener(new bckframe$10(this));
      JButton var11 = new JButton(BCKImageControl.loadImageIcon(home + "images/RBFTOOLBAR.jpg", "Add a RBF Network"));
      var11.addActionListener(new bckframe$11(this));
      this.addTool(var1, var2);
      this.addTool(var1, var4);
      this.addTool(var1, var3);
      var1.addSeparator();
      this.addTool(var1, var5);
      this.addTool(var1, var6);
      this.addTool(var1, var7);
      var1.addSeparator();
      this.addTool(var1, var8);
      this.addTool(var1, var9);
      this.addTool(var1, var10);
      this.addTool(var1, var11);
      return var1;
   }

   public void addTool(JToolBar var1, JButton var2) {
      JButton var3 = (JButton)var1.add(var2);
      var3.setToolTipText(((ImageIcon)var2.getIcon()).getDescription());
      var3.setMargin(new Insets(0, 0, 0, 0));
   }

   public void createNewBrain() {
      this.theBrainFilename = null;
      this.theBrain = new BCKBrainDrawable();
      this.drawANet(this.theBrain);
      this.theBrain.getImage().setLocation(new Point(30, 200));
   }

   public void saveBrain(BCKBrainDrawable var1, String var2) {
      if (var2 != null) {
         try {
            FileOutputStream var3 = new FileOutputStream(var2);
            ObjectOutputStream var4 = new ObjectOutputStream(var3);
            var4.writeObject(this.theBrain);
            var4.flush();
            var4.close();
         } catch (Exception var5) {
            JOptionPane.showMessageDialog(top, "Error Saving :" + var5.toString(), "Alert", 0);
         }
      }
   }

   public void loadBrain(String var1) {
      Object var2 = null;

      FileInputStream var4;
      try {
         var4 = new FileInputStream(var1);
      } catch (Exception var8) {
         JOptionPane.showMessageDialog(top, "Error Opening File : " + var1 + " " + var8.toString(), "Alert", 0);
         this.theBrainFilename = null;
         return;
      }

      ObjectInputStream var3;
      try {
         var3 = new ObjectInputStream(var4);
      } catch (Exception var7) {
         JOptionPane.showMessageDialog(top, "Error Converting File to an Object Input Stream :" + var7.toString(), "Alert", 0);
         this.theBrainFilename = null;
         return;
      }

      try {
         var2 = var3.readObject();
         var3.close();
      } catch (Exception var6) {
         JOptionPane.showMessageDialog(top, "Error Reading Object From File :" + var6.toString(), "Alert", 0);
         this.theBrainFilename = null;
         return;
      }

      if (var2 instanceof BCKBrainDrawable) {
         this.unDrawAll();
         this.theBrain = (BCKBrainDrawable)var2;
         this.addAllNetsToPane();
         this.theBrainFilename = var1;
         this.refresh();
      } else {
         JOptionPane.showMessageDialog(top, "Could not resolve class of object read from:" + var1, "Alert", 0);
      }
   }

   public void saveNetwork(BCKNeuralNetwork var1, String var2) {
      if (var2 != null && var1 != null) {
         try {
            FileOutputStream var3 = new FileOutputStream(var2);
            ObjectOutputStream var4 = new ObjectOutputStream(var3);
            var4.writeObject(var1);
            var4.flush();
            var4.close();
         } catch (Exception var5) {
            JOptionPane.showMessageDialog(top, "Error Saving :" + var5.toString(), "Alert", 0);
         }
      }
   }

   public void loadNetwork(String var1) {
      Object var2 = null;

      try {
         FileInputStream var3 = new FileInputStream(var1);
         ObjectInputStream var4 = new ObjectInputStream(var3);
         var2 = var4.readObject();
         var4.close();
      } catch (Exception var5) {
         JOptionPane.showMessageDialog(top, "Error Reading File :" + var5.toString(), "Alert", 0);
      }

      if (var2 instanceof BCKRBFDrawable) {
         this.addANet((BCKRBFDrawable)var2);
      } else if (var2 instanceof BCKHopfieldDrawable) {
         this.addANet((BCKHopfieldDrawable)var2);
      } else if (var2 instanceof BCKMLPQpropDrawable) {
         this.addANet((BCKMLPQpropDrawable)var2);
      } else if (var2 instanceof BCKMLPBpropDrawable) {
         this.addANet((BCKMLPBpropDrawable)var2);
      } else if (var2 instanceof BCKKohonenDrawable) {
         this.addANet((BCKKohonenDrawable)var2);
      } else {
         JOptionPane.showMessageDialog(top, "Could not resolve class of object read from:" + var1, "Alert", 0);
      }
   }

   public void newPressed() {
      this.unDrawAll();
      this.createNewBrain();
   }

   public void savePressed() {
      if (this.theBrainFilename != null) {
         this.saveBrain(this.theBrain, this.theBrainFilename);
      } else {
         this.bckFileDialog.setMode(1);
         this.bckFileDialog.setTitle("Save As");
         this.bckFileDialog.show();
         this.theBrainFilename = this.bckFileDialog.getDirectory() + this.bckFileDialog.getFile();
         if (this.bckFileDialog.getFile() != null) {
            this.saveBrain(this.theBrain, this.theBrainFilename);
         } else {
            this.theBrainFilename = null;
         }
      }
   }

   public void openPressed() {
      this.bckFileDialog.setMode(1);
      this.bckFileDialog.setTitle("Open");
      this.bckFileDialog.show();
      this.theBrainFilename = this.bckFileDialog.getDirectory() + this.bckFileDialog.getFile();
      if (this.bckFileDialog.getFile() != null) {
         this.loadBrain(this.theBrainFilename);
      }
   }

   public void cutPressed() {
      for (int var1 = 0; var1 < this.theBrain.size(); var1++) {
         if (((Drawable)this.getNetAt(var1)).isSelected()) {
            ((Drawable)this.getNetAt(var1)).cutPressed();
         }
      }
   }

   public void trainPressed() {
      for (int var1 = 0; var1 < this.theBrain.size(); var1++) {
         if (((Drawable)this.getNetAt(var1)).isSelected()) {
            ((Drawable)this.getNetAt(var1)).trainPressed();
         }
      }
   }

   public void editPressed() {
      for (int var1 = 0; var1 < this.theBrain.size(); var1++) {
         if (((Drawable)this.getNetAt(var1)).isSelected()) {
            ((Drawable)this.getNetAt(var1)).editPressed();
         }
      }
   }

   public void classifyPressed() {
      for (int var1 = 0; var1 < this.theBrain.size(); var1++) {
         if (((Drawable)this.getNetAt(var1)).isSelected()) {
            ((Drawable)this.getNetAt(var1)).classifyPressed();
         }
      }
   }

   public void copyPressed() {
      for (int var1 = 0; var1 < this.theBrain.size(); var1++) {
         if (((Drawable)this.getNetAt(var1)).isSelected()) {
            this.saveNetwork(this.getNetAt(var1), "CLIPBOARD.NET");
         }
      }
   }

   public void pastePressed() {
      this.loadNetwork("CLIPBOARD.NET");
   }

   public Drawable getSelectedDrawable() {
      for (int var1 = 0; var1 < this.theBrain.size(); var1++) {
         if (((Drawable)this.getNetAt(var1)).isSelected()) {
            return (Drawable)this.getNetAt(var1);
         }
      }

      return null;
   }

   public BCKNeuralNetwork getSelectedNetwork() {
      for (int var1 = 0; var1 < this.theBrain.size(); var1++) {
         if (((Drawable)this.getNetAt(var1)).isSelected()) {
            return this.getNetAt(var1);
         }
      }

      return null;
   }

   public void mouseClicked(MouseEvent var1) {
      for (int var2 = 0; var2 < this.theBrain.size(); var2++) {
         if (var1.getSource() == ((Drawable)this.getNetAt(var2)).Draw()) {
            for (int var3 = 0; var3 < this.theBrain.size(); var3++) {
               ((Drawable)this.getNetAt(var3)).setSelected(false);
            }

            ((Drawable)this.getNetAt(var2)).setSelected(true);
         }
      }
   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }

   public void mousePressed(MouseEvent var1) {
      for (int var2 = 0; var2 < this.theBrain.size(); var2++) {
         if (var1.getSource() == ((Drawable)this.getNetAt(var2)).Draw()) {
            for (int var3 = 0; var3 < this.theBrain.size(); var3++) {
               ((Drawable)this.getNetAt(var3)).setSelected(false);
            }

            ((Drawable)this.getNetAt(var2)).setSelected(true);
         }
      }
   }

   public void mouseReleased(MouseEvent var1) {
   }

   public void actionPerformed(ActionEvent var1) {
      var1.getSource();
      if (var1.getSource() == this.bckFileOpen) {
         this.bckFileDialog.setMode(0);
         this.bckFileDialog.setTitle("Open");
         this.bckFileDialog.show();
         this.theBrainFilename = this.bckFileDialog.getDirectory() + this.bckFileDialog.getFile();
         if (this.bckFileDialog.getFile() != null) {
            this.loadBrain(this.theBrainFilename);
         }
      } else {
         if (var1.getSource() == this.bckFileSaveAs) {
            this.bckFileDialog.setMode(1);
            this.bckFileDialog.setTitle("Save As");
            this.bckFileDialog.show();
            this.theBrainFilename = this.bckFileDialog.getDirectory() + this.bckFileDialog.getFile();
            if (this.bckFileDialog.getFile() != null) {
               this.saveBrain(this.theBrain, this.theBrainFilename);
            } else {
               this.theBrainFilename = null;
            }
         }

         if (var1.getSource() == this.bckFileSave) {
            if (this.theBrainFilename != null) {
               this.saveBrain(this.theBrain, this.theBrainFilename);
            } else {
               this.bckFileDialog.setMode(1);
               this.bckFileDialog.setTitle("Save As");
               this.bckFileDialog.show();
               this.theBrainFilename = this.bckFileDialog.getDirectory() + this.bckFileDialog.getFile();
               if (this.bckFileDialog.getFile() != null) {
                  this.saveBrain(this.theBrain, this.theBrainFilename);
               } else {
                  this.theBrainFilename = null;
               }
            }
         }

         if (var1.getSource() == this.bckFileExit) {
            if (JOptionPane.showConfirmDialog(
                  top, "Are You Sure You Want To Exit The Brain Construction Kit", "Are You Sure You Want To Exit The Brain Construction Kit", 0
               )
               == 1) {
               return;
            }

            System.exit(0);
         }

         if (var1.getSource() == this.bckBrainConfigure) {
            BCKBrainConfigure var2 = new BCKBrainConfigure();
            var2.show();
         }

         if (var1.getSource() == this.bckBrainSetInputFile) {
            this.bckFileDialog.setMode(0);
            this.bckFileDialog.setTitle("Load");
            this.bckFileDialog.show();
            if (this.bckFileDialog.getFile() != null) {
               try {
                  this.theBrain.setInputFile(this.bckFileDialog.getDirectory() + this.bckFileDialog.getFile());
               } catch (Exception var4) {
                  JOptionPane.showMessageDialog(
                     top,
                     "Error Loading File \n Please ensure that the number \n of inputs plus the number \n of outputs that you \n have specified for the \n brain configuration matches the number \n of fields per record in the file.\n"
                        + var4.toString(),
                     "Alert",
                     0
                  );
               }
            }
         }

         if (var1.getSource() == this.bckBrainClassify) {
            this.theBrain.classifyPressed();
         }

         if (var1.getSource() == this.bckBrainEdit) {
            this.theBrain.editPressed();
         }

         if (var1.getSource() == this.bckBrainOutputs) {
            BCKConfigureBrainOutputs var5 = new BCKConfigureBrainOutputs(this.theBrain);
            var5.show();
         }

         if (var1.getSource() == this.bckNetworkLoad) {
            this.bckFileDialog.setMode(0);
            this.bckFileDialog.setTitle("Load A Network ");
            this.bckFileDialog.show();
            if (this.bckFileDialog.getFile() != null) {
               this.loadNetwork(this.bckFileDialog.getDirectory() + this.bckFileDialog.getFile());
            }
         }

         if (var1.getSource() == this.bckNetworkSave) {
            BCKNeuralNetwork var6 = this.getSelectedNetwork();
            if (var6 == null) {
               JOptionPane.showMessageDialog(top, "You must select a network first.", "Alert", 0);
               return;
            }

            this.bckFileDialog.setMode(1);
            this.bckFileDialog.setTitle("Save A Network ");
            this.bckFileDialog.show();
            if (this.bckFileDialog.getFile() != null) {
               this.saveNetwork(this.getSelectedNetwork(), this.bckFileDialog.getDirectory() + this.bckFileDialog.getFile());
            }
         }

         if (var1.getSource() == this.bckNetworkEdit) {
            this.editPressed();
         }

         if (var1.getSource() == this.bckNetworkClassify) {
            this.classifyPressed();
         }

         if (var1.getSource() == this.bckAddKohonen) {
            this.CreateKohonen.show();
            BCKKohonenDrawable var12 = this.CreateKohonen.getNet();
            if (var12 != null) {
               this.addANet(var12);
            }
         } else {
            if (var1.getSource() == this.bckAddMLP) {
               this.CreateMLP.show();
               BCKNeuralNetwork var7 = this.CreateMLP.getNet();
               if (var7 instanceof BCKMLPQpropDrawable) {
                  BCKMLPQpropDrawable var3 = (BCKMLPQpropDrawable)var7;
                  this.addANet(var3);
               } else {
                  if (!(var7 instanceof BCKMLPBpropDrawable)) {
                     return;
                  }

                  BCKMLPBpropDrawable var13 = (BCKMLPBpropDrawable)var7;
                  this.addANet(var13);
               }
            }

            if (var1.getSource() == this.bckAddRBF) {
               this.CreateRBF.show();
               BCKRBFDrawable var8 = this.CreateRBF.getNet();
               if (var8 == null) {
                  return;
               }

               this.addANet(var8);
            }

            if (var1.getSource() == this.bckAddHopfield) {
               this.CreateHopfield.show();
               BCKHopfieldDrawable var9 = this.CreateHopfield.getNet();
               if (var9 == null) {
                  return;
               }

               this.addANet(var9);
            }

            if (var1.getSource() == this.bckOptionRefresh) {
               this.refresh();
            }

            if (var1.getSource() == this.bckTextConversion) {
               BCKTextConversion var10 = new BCKTextConversion();
               var10.show();
            }

            if (var1.getSource() == this.bckImageConversion) {
               BCKImageConversion var11 = new BCKImageConversion();
               var11.show();
            }

            if (var1.getSource() == this.bckHelpUserMan) {
               JOptionPane.showMessageDialog(top, "The user manual is located at : " + home + "Help", "Help", 1, new ImageIcon(applicationImage));
            }

            if (var1.getSource() == this.bckHelpAbout) {
               JOptionPane.showMessageDialog(top, "Written By Thomas Doris and Eoin Whelan \n CA4 Project '97/'97", "About", 1, new ImageIcon(applicationImage));
            }
         }
      }
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowClosed(WindowEvent var1) {
      System.exit(0);
   }

   public void windowClosing(WindowEvent var1) {
      System.exit(0);
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowOpened(WindowEvent var1) {
   }

   public void addANet(Drawable var1) {
      this.theBrain.addNetwork((BCKNeuralNetwork)var1);
      this.drawANet(var1);
   }

   public void refresh() {
      this.drawConnections();
      this.repaint();
   }

   public void drawANet(Drawable var1) {
      this.bckInternal.lc.add(var1.Draw(), JLayeredPane.DEFAULT_LAYER);
      var1.Draw().addMouseListener(this);
      this.drawConnections();
   }

   public void unDraw(Drawable var1) {
      var1.setVisible(false);
      this.bckInternal.lc.remove(var1.Draw());
      this.refresh();
   }

   public void unDrawAll() {
      for (int var1 = 0; var1 < this.theBrain.size(); var1++) {
         this.unDraw((Drawable)this.getNetAt(var1));
      }
   }

   public void addAllNetsToPane() {
      for (int var1 = 0; var1 < this.theBrain.size(); var1++) {
         this.drawANet((Drawable)this.getNetAt(var1));
      }
   }

   public BCKNeuralNetwork getNetAt(int var1) {
      return this.theBrain.elementAt(var1);
   }

   public void removeNetwork(BCKNeuralNetwork var1) {
      this.theBrain.removeElement(var1);
      this.unDraw((Drawable)var1);
      this.refresh();
   }

   public void paint(Graphics var1) {
      super.paint(var1);
      this.drawConnections();

      for (int var3 = 0; var3 < this.Lines.size(); var3++) {
         frameLine var2 = (frameLine)this.Lines.elementAt(var3);
         var1.setColor(Color.white);
         var1.drawLine(var2.x1, var2.y1, var2.x2, var2.y2);
      }
   }

   public void drawConnection(int var1, int var2) {
      if (var1 >= 0 && var1 < this.theBrain.size() && var2 > 0 && var2 < this.theBrain.size()) {
         Point var3 = this.getImage(var1).getCenter();
         Point var4 = this.getImage(var2).getCenter();
         this.Lines.addElement(new frameLine(var3.x + 45, var3.y + 90, var4.x - 35, var4.y + 90));
      }

      if (var1 >= 0 && var1 < this.theBrain.size() && var2 == 0) {
         Point var5 = this.getImage(var1).getCenter();
         Point var6 = this.brainOutput.getImage().getCenter();
         this.Lines.addElement(new frameLine(var5.x + 45, var5.y + 90, var6.x - 35, var6.y + 90));
      }
   }

   public void drawConnections() {
      this.Lines.removeAllElements();
      Vector var1 = this.theBrain.proxyInputs;

      for (int var3 = 0; var3 < var1.size(); var3++) {
         Vector var4 = (Vector)var1.elementAt(var3);

         for (int var5 = 0; var5 < var4.size(); var5++) {
            BCKNetConnection var2 = (BCKNetConnection)var4.elementAt(var5);
            this.drawConnection(var2.getSourceNet(), var3);
         }
      }
   }

   public BCKImageControl getImage(int var1) {
      return ((Drawable)this.theBrain.getNet(var1)).getImage();
   }
}
