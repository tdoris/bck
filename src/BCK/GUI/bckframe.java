package BCK.GUI;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import BCK.ANN.*;

/** This is the class which implements the GUI main frame. It is complex because it handles all of the network and brain manipulation event dispatchers
 */
public class bckframe extends JFrame implements ActionListener, WindowListener ,MouseListener {
    /**This static member is set to 'this' to enable upward referencing*/
    public static bckframe top = null;
    /**Read in the home directory specified in the command line*/
    public static String home = System.getProperty("BCK.home");
    public static Image applicationImage;
    /**The brain output object represents the output of the brain graphically*/
    BCKBrainOutputDrawable brainOutput = new BCKBrainOutputDrawable();
    String theBrainFilename;
    Vector Lines = new Vector(0);
    BCKBrainDrawable theBrain; //the single brain object
    BorderLayout borderLayout1 = new BorderLayout();  

    InternalWindowPanel bckInternal = new InternalWindowPanel();
    //Create Hopfield Dialog
    bckCreateHopfieldDialog CreateHopfield = new bckCreateHopfieldDialog(this);

    //Create Kohonen Dialog
    bckCreateKohonenDialog CreateKohonen = new bckCreateKohonenDialog(this);

    //Create MLP Dialog
    bckCreateMLPDialog CreateMLP = new bckCreateMLPDialog(this);

    //Create RBF Dialog
    bckCreateRBFDialog CreateRBF = new bckCreateRBFDialog(this);

    //File Dialog
    FileDialog bckFileDialog = new FileDialog(this);

    //Menu Bar
    JMenuBar bckMenuBar = new JMenuBar();

    //Menus
    JMenu bckFile = new JMenu("File");
    JMenu bckBrain = new JMenu("Brain");
    JMenu bckNetwork = new JMenu("Network");
    JMenu bckOptions = new JMenu("Options");
    JMenu bckData = new JMenu("Data");
    JMenu bckHelp = new JMenu("Help");

    //Menu Items
    JMenuItem bckFileNew = new JMenuItem("New");
    JMenuItem bckFileOpen = new JMenuItem("Open");
    JMenuItem bckFileSaveAs = new JMenuItem("Save As...");
    JMenuItem bckFileSave = new JMenuItem("Save");
    JMenuItem bckFileExit = new JMenuItem("Exit");

    JMenuItem bckBrainConfigure    = new JMenuItem("Configure...");
    JMenuItem bckBrainSetInputFile = new JMenuItem("Set Input File...");
    JMenuItem bckBrainClassify     = new JMenuItem("Classify...");
    JMenuItem bckBrainEdit         = new JMenuItem("Edit...");
    JMenuItem bckBrainOutputs      = new JMenuItem("Configure Outputs");

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

    /**Construct the frame*/
    public bckframe() {
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        this.addWindowListener(l);
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        createNewBrain();
        drawANet(brainOutput);
        brainOutput.getImage().setLocation(new Point(800,200));
        try {
            jbInit();
        } 
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(bckframe.top,"Error :"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);    
        }
        top=this;
    }

    /**Component initialization*/
    private void jbInit() throws Exception{
        bckframe.applicationImage = (BCKImageControl.loadImageIcon(bckframe.home + "/images/BCK.JPG","The Application Icon")).getImage();
        this.getContentPane().setLayout(borderLayout1);    
        this.setSize(new Dimension(1000,700)); 
        setIconImage(bckframe.applicationImage);
        this.getContentPane().add(bckInternal,BorderLayout.CENTER);
        bckInternal.setVisible(true);

        this.setTitle("Brain Construction Kit");
        //File Dialog
        this.bckFileDialog.setModal(true);
        this.setJMenuBar(bckMenuBar);
        bckMenuBar.add(bckFile);
        bckMenuBar.add(bckBrain);
        bckMenuBar.add(bckNetwork);
        bckMenuBar.add(bckOptions);
        bckMenuBar.add(bckData);
        bckMenuBar.add(bckHelp);

        //bckMenuBar.setHelpMenu(bckHelp);
        bckFile.add(bckFileNew);
        bckFileNew.addActionListener(this);
        bckFile.add(bckFileOpen);
        bckFileOpen.addActionListener(this);
        bckFile.add(bckFileSaveAs);
        bckFileSaveAs.addActionListener(this);
        bckFile.add(bckFileSave);
        bckFileSave.addActionListener(this);
        bckFile.add(new JSeparator());
        bckFile.add(bckFileExit);
        bckFileExit.addActionListener(this);

        bckBrain.add(bckBrainConfigure);
        bckBrainConfigure.addActionListener(this);
        bckBrain.add(bckBrainSetInputFile);
        bckBrainSetInputFile.addActionListener(this);
        bckBrain.add(bckBrainClassify);
        bckBrainClassify.addActionListener(this); 

        bckBrain.add(bckBrainEdit);
        bckBrainEdit.addActionListener(this); 

        bckBrain.add(bckBrainOutputs);
        bckBrainOutputs.addActionListener(this);  

        JMenu letterMenu;
        JMenuItem subMenu;
        JMenu tmpMenu;
        letterMenu = (JMenu) bckNetwork.add(bckNetworkAdd);
        subMenu =  letterMenu.add(bckAddMLP);
        subMenu =  letterMenu.add(bckAddRBF);
        subMenu =  letterMenu.add(bckAddHopfield);
        subMenu =  letterMenu.add(bckAddKohonen);

        bckAddMLP.addActionListener(this);
        bckAddRBF.addActionListener(this);
        bckAddHopfield.addActionListener(this);
        bckAddKohonen.addActionListener(this);

        bckNetwork.add(bckNetworkLoad);
        bckNetworkLoad.addActionListener(this);
        bckNetwork.add(bckNetworkSave);
        bckNetworkSave.addActionListener(this);
        bckNetwork.add(new JSeparator());
        bckNetwork.add(bckNetworkEdit);
        bckNetworkEdit.addActionListener(this);
        bckNetwork.add(bckNetworkClassify);
        bckNetworkClassify.addActionListener(this);

        bckOptions.add(bckOptionRefresh);
        bckOptionRefresh.addActionListener(this);

        bckData.add(bckImageConversion);
        bckImageConversion.addActionListener(this);

        bckData.add(bckTextConversion);
        bckTextConversion.addActionListener(this);

        bckHelp.add(bckHelpUserMan);
        bckHelpUserMan.addActionListener(this);

        bckHelp.add(bckHelpAbout);
        bckHelpAbout.addActionListener(this);

        bckToolbar = createToolbar();
        bckInternal.add(bckToolbar,BorderLayout.NORTH);
    }

    /**Constructs the Tool Bar.Creates the tool bar buttons, give them images,
     *   create event handlers for them and add them to the toolbar*/
    private JToolBar createToolbar(){  
        JToolBar t = new JToolBar();
        t.setFloatable(false);

        JButton btnNew = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/new.gif","Create a New System"));
        btnNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPressed();
            }
        }
        );
        JButton btnSave = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/save.gif","Save the System"));
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                savePressed();
            }
        }
        );
        JButton btnOpen = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/open.gif","Open"));
        btnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openPressed();
            }
        }
        );
        JButton btnCut = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/cut.gif","Cut the Selected Network"));
        btnCut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cutPressed();
            }
        }
        );

        JButton btnCopy = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/copy.gif","Copy the Selected Network"));
        btnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copyPressed();
            }
        }
        );
        JButton btnPaste = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/paste.gif","Paste the Selected Network"));
        btnPaste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pastePressed();
            }
        }
        );
        JButton btnHopfield = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/HOPFIELDTOOLBAR.jpg","Add a Hopfield Network"));
        btnHopfield.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateHopfield.setVisible(true);
                BCKHopfieldDrawable newNet = CreateHopfield.getNet();
                if(newNet==null){
                    return;
                }
                else {
                    //add the net:
                    addANet(newNet);     
                }
            }
        }
        );
        JButton btnMLP = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/MLPQPROPTOOLBAR.jpg","Add an MLP Network"));
        btnMLP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateMLP.setVisible(true);
                BCKNeuralNetwork net = CreateMLP.getNet();
                if(net instanceof BCKMLPQpropDrawable){
                    BCKMLPQpropDrawable newqprop = (BCKMLPQpropDrawable)net;
                    addANet(newqprop);	  
                }
                else if(net instanceof BCKMLPBpropDrawable){ 
                    BCKMLPBpropDrawable newbprop = (BCKMLPBpropDrawable)net;
                    addANet(newbprop);	  
                } 
                else {
                    return;
                }
            }
        }
        );
        JButton btnKohonen = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/KOHONENTOOLBAR.jpg","Add a Kohonen Network"));
        btnKohonen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateKohonen.setVisible(true);
                BCKKohonenDrawable net = CreateKohonen.getNet();
                if(net == null){
                    return;
                } 
                else {
                    addANet(net);
                }
                return;
            }
        }
        );
        JButton btnRBF = new JButton(BCKImageControl.loadImageIcon(bckframe.home + "images/RBFTOOLBAR.jpg","Add a RBF Network"));
        btnRBF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateRBF.setVisible(true);
                BCKRBFDrawable newNet = CreateRBF.getNet();
                if(newNet==null){
                    return;
                }
                else {
                    //add the net:
                    addANet(newNet);     
                }
            }
        }
        );

        addTool(t,btnNew);
        addTool(t,btnOpen);
        addTool(t,btnSave);
        t.addSeparator();
        addTool(t,btnCut);
        addTool(t,btnCopy);
        addTool(t,btnPaste);
        t.addSeparator();
        addTool(t,btnHopfield);
        addTool(t,btnMLP);
        addTool(t,btnKohonen);
        addTool(t,btnRBF);

        return t;  
    }

    /**Adds the specified Button to the specified tool bar*/
    public void addTool(JToolBar toolBar, JButton button) {
        JButton b = (JButton) toolBar.add(button);
        b.setToolTipText(((ImageIcon)(button.getIcon())).getDescription());
        b.setMargin(new Insets(0,0,0,0));	
    }

    /**Create a new Brain object, the old one is lost*/
    public void createNewBrain(){
        theBrainFilename = null;
        theBrain = new BCKBrainDrawable();
        drawANet(theBrain);
        theBrain.getImage().setLocation(new Point(30,200));
    }

    /**This method serializes the specified brain object to the specified file*/
    public void saveBrain(BCKBrainDrawable brain, String filename){
        if(filename!=null){
            try{
                FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(theBrain);
                out.flush();
                out.close();     
            } 
            catch(Exception e){
                JOptionPane.showMessageDialog(bckframe.top,"Error Saving :"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**This method deserialises the specified file and attempts to resolve the object therein to a BCKBrain*/
    public void loadBrain(String filename){
        Object newbrain = null;
        ObjectInputStream in;
        FileInputStream fis;
        try{
            fis = new FileInputStream(filename); 
        } 
        catch(Exception e){
            JOptionPane.showMessageDialog(bckframe.top,"Error Opening File : "+filename+" "+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            theBrainFilename=null;
            return;
        }
        try{
            in = new ObjectInputStream(fis); 
        } 
        catch(Exception e){
            JOptionPane.showMessageDialog(bckframe.top,"Error Converting File to an Object Input Stream :"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            theBrainFilename=null;
            return;
        }
        try{
            newbrain = in.readObject();
            in.close();
        } 
        catch(Exception e){
            JOptionPane.showMessageDialog(bckframe.top,"Error Reading Object From File :"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            theBrainFilename=null;
            return;
        }

        if(newbrain instanceof BCKBrainDrawable){
            //remove the old nets from the UI and add the new:
            unDrawAll();  
            theBrain=(BCKBrainDrawable)newbrain;
            addAllNetsToPane();
            theBrainFilename = filename;
            refresh();
            return;
        }
        JOptionPane.showMessageDialog(bckframe.top,"Could not resolve class of object read from:"+filename,"Alert",JOptionPane.ERROR_MESSAGE);
    }

    /**This method serializes the specified network object to the specified file*/
    public void saveNetwork(BCKNeuralNetwork net, String filename){
        if(filename!=null && net !=null ){
            try{
                FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fos);      
                out.writeObject(net);
                out.flush();
                out.close();     
            } 
            catch(Exception e){
                JOptionPane.showMessageDialog(bckframe.top,"Error Saving :"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /**This method deserialises the specified file and attempts to resolve the object therein to a known network, which it then adds to the current brain object*/
    public void loadNetwork(String filename){
        Object newnet = null;
        try{
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            newnet = in.readObject();
            in.close();
        } 
        catch(Exception e){
            JOptionPane.showMessageDialog(bckframe.top,"Error Reading File :"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
        }

        if(newnet instanceof BCKRBFDrawable){
            addANet((BCKRBFDrawable)newnet);
            return;
        }
        if(newnet instanceof BCKHopfieldDrawable){
            addANet((BCKHopfieldDrawable)newnet);
            return;
        }  
        if(newnet instanceof BCKMLPQpropDrawable){
            addANet((BCKMLPQpropDrawable)newnet);
            return;
        }
        if(newnet instanceof BCKMLPBpropDrawable){
            addANet((BCKMLPBpropDrawable)newnet);
            return;
        }  
        if(newnet instanceof BCKKohonenDrawable){
            addANet((BCKKohonenDrawable)newnet);
            return;
        }
        JOptionPane.showMessageDialog(bckframe.top,"Could not resolve class of object read from:"+filename,"Alert",JOptionPane.ERROR_MESSAGE);
    }


    /**Create a new brain System :*/
    public void newPressed(){
        //remove the old nets from the UI and add the new:
        unDrawAll();    
        createNewBrain();
    }

    /**Save the current brain object*/
    public void savePressed(){
        if(theBrainFilename!=null){
            saveBrain(theBrain,theBrainFilename);
        } 
        else {
            bckFileDialog.setMode(FileDialog.SAVE);
            bckFileDialog.setTitle("Save As");
            bckFileDialog.show();
            theBrainFilename =bckFileDialog.getDirectory()+bckFileDialog.getFile();
            if(bckFileDialog.getFile()!=null){
                saveBrain(theBrain,theBrainFilename);
            }	
            else {
                theBrainFilename = null;
            }

        }
    }
    /**Open a saved brain*/
    public void openPressed(){	 
        bckFileDialog.setMode(FileDialog.SAVE);
        bckFileDialog.setTitle("Open");
        bckFileDialog.show();
        theBrainFilename =bckFileDialog.getDirectory()+bckFileDialog.getFile();
        if(bckFileDialog.getFile()!=null){
            loadBrain(theBrainFilename);
        }		   
    }

    public void cutPressed(){
        for(int i=0;i<theBrain.size();i++){
            if (((Drawable)getNetAt(i)).isSelected() ){
                ((Drawable)getNetAt(i)).cutPressed();
            }
        }
    }
    public void trainPressed(){
        for(int i=0;i<theBrain.size();i++){
            if (((Drawable)getNetAt(i)).isSelected() ){
                ((Drawable)getNetAt(i)).trainPressed();
            }
        }
    }

    public void editPressed(){
        for(int i=0;i<theBrain.size();i++){
            if (((Drawable)getNetAt(i)).isSelected() ){
                ((Drawable)getNetAt(i)).editPressed();
            }
        }
    }

    public void classifyPressed(){
        for(int i=0;i<theBrain.size();i++){
            if (((Drawable)getNetAt(i)).isSelected() ){
                ((Drawable)getNetAt(i)).classifyPressed();
            }
        }
    }

    public void copyPressed(){
        for(int i=0;i<theBrain.size();i++){
            if (((Drawable)getNetAt(i)).isSelected() ){
                saveNetwork(getNetAt(i),"CLIPBOARD.NET");
            }
        }
    }
    public void pastePressed(){
        loadNetwork("CLIPBOARD.NET");
    }

    public Drawable getSelectedDrawable(){
        for(int i=0;i<theBrain.size();i++){
            if(((Drawable)getNetAt(i)).isSelected()){
                return ((Drawable)getNetAt(i));
            }
        }
        return null;
    }

    public BCKNeuralNetwork getSelectedNetwork(){
        for(int i=0;i<theBrain.size();i++){
            if(((Drawable)getNetAt(i)).isSelected()){
                return ((BCKNeuralNetwork)getNetAt(i));
            }
        }
        return null;
    }
    public void mouseClicked(MouseEvent me) {    
        for(int i=0;i<theBrain.size();i++){
            if (me.getSource()==((Drawable)getNetAt(i)).Draw()){       
                for(int j=0;j<theBrain.size();j++)
                    ((Drawable)getNetAt(j)).setSelected(false);
                ((Drawable)getNetAt(i)).setSelected(true);
            }
        }
    }


    public void mouseEntered(MouseEvent me){
    }
    public void mouseExited(MouseEvent me){
    }
    public void mousePressed(MouseEvent me){
        for(int i=0;i<theBrain.size();i++){
            if (me.getSource()==((Drawable)getNetAt(i)).Draw()){       
                for(int j=0;j<theBrain.size();j++)
                    ((Drawable)getNetAt(j)).setSelected(false);
                ((Drawable)getNetAt(i)).setSelected(true);
            }
        }
    }
    public void mouseReleased(MouseEvent me){
    }

    public void actionPerformed(ActionEvent e) {

        /*File MenuItem Events*/
        if (e.getSource() == bckFileNew) {

        }

        if (e.getSource() == bckFileOpen){
            bckFileDialog.setMode(FileDialog.LOAD);
            bckFileDialog.setTitle("Open");
            bckFileDialog.show();
            theBrainFilename =bckFileDialog.getDirectory()+bckFileDialog.getFile();
            if(bckFileDialog.getFile()!=null)
                loadBrain(theBrainFilename);	
            return;
        }

        if (e.getSource() == bckFileSaveAs){
            bckFileDialog.setMode(FileDialog.SAVE);
            bckFileDialog.setTitle("Save As");
            bckFileDialog.show();
            theBrainFilename = bckFileDialog.getDirectory()+bckFileDialog.getFile();
            if(bckFileDialog.getFile()!=null){
                saveBrain(theBrain,theBrainFilename);
            } 
            else {
                theBrainFilename=null;
            }

        }

        if (e.getSource() == bckFileSave){
            if(theBrainFilename!=null){
                saveBrain(theBrain,theBrainFilename);
            } 
            else {
                bckFileDialog.setMode(FileDialog.SAVE);
                bckFileDialog.setTitle("Save As");
                bckFileDialog.show();
                theBrainFilename =bckFileDialog.getDirectory()+ bckFileDialog.getFile();
                if(bckFileDialog.getFile()!=null){
                    saveBrain(theBrain,theBrainFilename);
                }	
                else {
                    theBrainFilename = null;
                }

            }
        }


        if (e.getSource() == bckFileExit){       
            if((JOptionPane.showConfirmDialog(bckframe.top,"Are You Sure You Want To Exit The Brain Construction Kit","Are You Sure You Want To Exit The Brain Construction Kit",JOptionPane.YES_NO_OPTION))==1) {
                return;
            }
            System.exit(0);        
        }


        /*Brain Menu Item Events*/   
        if(e.getSource() == bckBrainConfigure){
            BCKBrainConfigure bc = new BCKBrainConfigure();
            bc.show();

        }

        if (e.getSource() == bckBrainSetInputFile){
            bckFileDialog.setMode(FileDialog.LOAD);
            bckFileDialog.setTitle("Load");
            bckFileDialog.show();       
            if(bckFileDialog.getFile()!=null)
            try{
                theBrain.setInputFile(bckFileDialog.getDirectory()+bckFileDialog.getFile());
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(bckframe.top,"Error Loading File \n Please ensure that the number \n of inputs plus the number \n of outputs that you \n have specified for the \n brain configuration matches the number \n of fields per record in the file.\n"+ex.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == bckBrainClassify){
            ((Drawable)theBrain).classifyPressed();
        }

        if (e.getSource() == bckBrainEdit){
            ((Drawable)theBrain).editPressed();
        }
        if (e.getSource() == bckBrainOutputs){
            BCKConfigureBrainOutputs co = new BCKConfigureBrainOutputs(theBrain);
            co.show();
        }

        /*Network Menu Item Events*/ 
        if (e.getSource() == bckNetworkLoad) {
            bckFileDialog.setMode(FileDialog.LOAD);
            bckFileDialog.setTitle("Load A Network ");
            bckFileDialog.show();       
            if(bckFileDialog.getFile()!=null)
                loadNetwork(bckFileDialog.getDirectory()+bckFileDialog.getFile());
        }

        if (e.getSource() == bckNetworkSave) {
            Object net = getSelectedNetwork();
            if(net == null){
                JOptionPane.showMessageDialog(bckframe.top,"You must select a network first.","Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }	 	
            bckFileDialog.setMode(FileDialog.SAVE);
            bckFileDialog.setTitle("Save A Network ");
            bckFileDialog.show();
            if(bckFileDialog.getFile()!=null)
                saveNetwork(getSelectedNetwork(),bckFileDialog.getDirectory()+bckFileDialog.getFile());

        }

        if (e.getSource() == bckNetworkEdit){
            editPressed();
        }	 
        if (e.getSource() == bckNetworkClassify){
            classifyPressed();
        }

        /*Add Sub Menu Events*/
        if (e.getSource() == bckAddKohonen)
        {
            CreateKohonen.show();
            BCKKohonenDrawable net = CreateKohonen.getNet();
            if(net == null){
                return;
            } 
            else {
                addANet(net);
            }
            return;
        }

        if (e.getSource() == bckAddMLP)
        {
            //an mlp can be either qprop or bprop - need to determine which
            CreateMLP.show();       
            BCKNeuralNetwork net = CreateMLP.getNet();
            if(net instanceof BCKMLPQpropDrawable){
                BCKMLPQpropDrawable newqprop = (BCKMLPQpropDrawable)net;
                addANet(newqprop);	  
            }
            else if(net instanceof BCKMLPBpropDrawable){ 
                BCKMLPBpropDrawable newbprop = (BCKMLPBpropDrawable)net;
                addANet(newbprop);	  
            } 
            else {
                return;
            }
        }

        if (e.getSource() == bckAddRBF)
        {
            CreateRBF.show();
            BCKRBFDrawable newNet = CreateRBF.getNet();
            if(newNet==null){
                return;
            }
            else {
                //add the net:
                addANet(newNet);     
            }

        }

        if (e.getSource() == bckAddHopfield)
        {
            CreateHopfield.show();
            BCKHopfieldDrawable newNet = CreateHopfield.getNet();
            if(newNet==null){
                return;
            }
            else {
                //add the net:
                addANet(newNet);     
            }
        }

        /* Options menu event Handlers */
        if(e.getSource() == bckOptionRefresh){	
            refresh();
        }

        /* Data Menu event Handlers */
        if(e.getSource() == bckTextConversion){
            BCKTextConversion textConverter = new BCKTextConversion();	
            textConverter.show();
        }

        if(e.getSource() == bckImageConversion){
            BCKImageConversion imageConverter = new BCKImageConversion();	
            imageConverter.show();
        }

        if(e.getSource() == bckHelpUserMan){
            JOptionPane.showMessageDialog(bckframe.top,"The user manual is located at : "+bckframe.home+"Help","Help",JOptionPane.INFORMATION_MESSAGE,new ImageIcon(bckframe.applicationImage));
        }

        if(e.getSource() == bckHelpAbout){
            JOptionPane.showMessageDialog(bckframe.top,"Written By Thomas Doris and Eoin Whelan \n CA4 Project '97/'97","About",JOptionPane.INFORMATION_MESSAGE,new ImageIcon(bckframe.applicationImage));
        }


    }

    public void windowActivated(WindowEvent we){
    }
    public void windowClosed(WindowEvent we){
        System.exit(0);
    }
    public void windowClosing(WindowEvent we){	
        System.exit(0);
    }  
    public void windowDeactivated(WindowEvent we){
    }   
    public void windowDeiconified(WindowEvent we){
    }
    public void windowIconified(WindowEvent we){
    }
    public void windowOpened(WindowEvent we){
    }

    /*Add the specified object to the Brain*/
    public void addANet(Drawable net){  
        theBrain.addNetwork((BCKNeuralNetwork)net); 
        drawANet(net);
    }
    /**Repaint the Desktop*/
    public void refresh(){
        drawConnections();
        repaint();
    }
    /**Add the specifed object's image to the internal panel*/
    public void drawANet(Drawable net){
        bckInternal.lc.add(net.Draw(), JLayeredPane.DEFAULT_LAYER);
        net.Draw().addMouseListener(this);      
        drawConnections();   
    }

    /**removes a Net from the UI */
    public void unDraw(Drawable net){
        ((Drawable)net).setVisible(false);
        bckInternal.lc.remove(net.Draw());
        refresh();
    }

    /**Removes all nets from pane*/
    public void unDrawAll(){
        for(int i=0;i<theBrain.size();i++){
            unDraw(((Drawable)getNetAt(i)));
        }
    }

    /**Add the nets in the current brain object to the GUI*/
    public void addAllNetsToPane(){
        for(int i=0;i<theBrain.size();i++){
            drawANet((Drawable)getNetAt(i));
        }
    }
    /*Retreive the network at index i*/
    public BCKNeuralNetwork getNetAt(int i){
        return theBrain.elementAt(i);
    }
    /**Remove the specified network*/
    public void removeNetwork(BCKNeuralNetwork net){
        theBrain.removeElement(net);
        unDraw((Drawable)net);
        refresh();
    }

    /**Draw the inter network connections*/
    public void paint (Graphics g){
        super.paint(g); 
        drawConnections();
        frameLine l;
        for(int i=0;i<Lines.size();i++){
            l=(frameLine)Lines.elementAt(i);
            g.setColor(Color.white);
            g.drawLine(l.x1,l.y1,l.x2,l.y2); 
        }
    }

    /**Draw a line connecting network pre to network post */
    public void drawConnection(int pre, int post){
        if(pre >=0 && pre < theBrain.size() && post>0 && post < theBrain.size()){
            Point centerPre =getImage(pre).getCenter();
            Point centerPost = getImage(post).getCenter();  
            Lines.addElement(new frameLine(centerPre.x+45,centerPre.y+90,centerPost.x-35,centerPost.y+90));  
        }
        if(pre >=0 && pre < theBrain.size() && post==0){
            Point centerPre =getImage(pre).getCenter();
            Point centerPost = brainOutput.getImage().getCenter();  
            Lines.addElement(new frameLine(centerPre.x+45,centerPre.y+90,centerPost.x-35,centerPost.y+90));  
        }
    }

    /**Find any inter Network connections in the current system*/
    public void drawConnections(){
        Lines.removeAllElements();
        Vector inputs = theBrain.proxyInputs;
        BCKNetConnection nc;
        for(int i=0;i<inputs.size();i++){
            Vector targetNetConnections = ((Vector)inputs.elementAt(i));
            for(int j=0;j<targetNetConnections.size();j++){
                nc=((BCKNetConnection)targetNetConnections.elementAt(j));
                drawConnection(nc.getSourceNet(),i);
            }   
        }
    }

    /**Retreive the image corresponding to network i*/
    public BCKImageControl getImage(int i){
        return ((BCKImageControl)(((Drawable)theBrain.getNet(i)).getImage()));
    }

}
/**This simple class provides a convenient store for inter network connection lines*/
class frameLine implements Serializable{
    int x1,y1,x2,y2;
    public frameLine(int x1,int y1, int x2, int y2){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
    }
}








