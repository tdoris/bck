
//Title        BCK
//Version
//Copyright:    Copyright (c) 1997
//Author:       Eoin Whelan
//Company:      DCU
//Description:  none

package BCK.GUI;

import BCK.ANN.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

public class BCKMLPQpropDrawable extends BCKQprop implements Drawable,MouseListener, ActionListener, Serializable{

    private transient BCKMLPQpropTrain train;
    private transient BCKPopup popup;
    private transient BCKImageControl image;
    private transient BCKEdit editinternal;

    public BCKMLPQpropDrawable(){
        super();
        setImage();
    }


    public BCKMLPQpropDrawable(int[] i){
        super(i);
        setImage();
    }

    public BCKImageControl getImage(){
        return image;
    } 

    /**handle custom deserialization*/
    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException {
        in.defaultReadObject();
        this.setImage();
    }

    private void setImage(){
        image = new BCKImageControl(getName(),BCKImageControl.loadImageIcon(bckframe.home+"images/MLPQPROP.jpg","hello"));
        image.addMouseListener(this);
        popup = new BCKPopup();
        popup.edit.addActionListener(this);
        popup.train.addActionListener(this);
        popup.classify.addActionListener(this);
        popup.cut.addActionListener(this);
        popup.map.addActionListener(this);
        popup.connect.addActionListener(this);
        train = new BCKMLPQpropTrain(this);
        train.OK.addActionListener(this);
        setText("No Name");
    }

    public void setText(String name){
        setName(name);
        image.setText(""+idNumber+" "+name);
        image.setToolTipText(""+idNumber+" "+name);
    }

    public BCKImageControl Draw(){
        return image;
    }

    public boolean isSelected(){
        return image.isSelected();
    } 

    public void setSelected(boolean select){
        image.setSelected(select);
        if(select){
            image.setImage(bckframe.home+"images/MLPQPROPSELECTED.jpg");
        }
        else{
            image.setImage(bckframe.home+"images/MLPQPROP.jpg");
        }
    }

    public void setVisible(boolean visible){
        image.setVisible(visible);
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == popup.edit){
            editPressed();   
        }

        if(e.getSource() == popup.cut){
            cutPressed();
        } 


        if(e.getSource() == popup.train){
            trainPressed();
        }

        if(e.getSource() == popup.classify){
            classifyPressed(); 
        }

        if(e.getSource() == popup.map){
            BCKMap map = new  BCKMap(this);
            map.setVisible(true);   
        }

        if(e.getSource() == popup.connect){
            BCKConnectNetworks con = new  BCKConnectNetworks(this);
            con.setVisible(true);   
        }

        if(e.getSource() == train.OK){  
            try{
                int epochs = 10;
                this.setTrainingFile(train.trainfilename.getText());
                this.train(10);
                train.setVisible(false);
            }
            catch(Exception te){
                JOptionPane.showMessageDialog(null,te.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            }
        } 
    }

    public void mouseClicked(MouseEvent me) {    
        if(me.isAltDown()){
            if(me.getSource() == image){    
                popup.show(this.image,me.getX(),me.getY());  
            }       
        }
        if(me.getClickCount()==2){
            BCKSetNetName setnet = new BCKSetNetName(getName());
            setnet.setVisible(true);
            if(setnet.isOk())
            {
                setText(setnet.getName());
            }

        }
    }

    public void mouseEntered(MouseEvent me)  {  
    }
    public void mouseExited(MouseEvent me)  {  
    }
    public void mousePressed(MouseEvent me)  {  
    }
    public void mouseReleased(MouseEvent me) {  
    }


    public void trainPressed(){
        train.show();
    }
    public void editPressed(){
        editinternal = new BCKEdit(this);
        editinternal.setPreferredSize(new Dimension(300,200));
        editinternal.setLocation(10,10);
        bckframe.top.bckInternal.lc.add(editinternal,JLayeredPane.PALETTE_LAYER);   
    }

    public void classifyPressed(){
        BCKClassify classify = new  BCKClassify(this);
        classify.setVisible(true);  
    }

    public void cutPressed(){
        bckframe.top.saveNetwork(this,"CLIPBOARD.NET");
        bckframe.top.removeNetwork(this);
    }

    /**Write this object to the specified file using serialization mechanism*/
    public void save(String file) throws IOException{
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(this);
        out.flush();
        out.close();
    }


}



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

    public BCKMLPQpropTrain(BCKMLPQpropDrawable Net){
        super(bckframe.top,"Train MLP Using QuickProp",true);
        trainfilename.setEditable(false);
        testfilename.setEditable(false);
        this.Net = Net;
        JPanel container = new JPanel();
        container.setLayout( new ColumnLayout());

        JPanel trainfile = createTrainFilePanel();

        JPanel testfile = createTestFilePanel();

        JPanel params = createParametersPanel();

        JPanel trainForEpochs = createTrainForEpochs();

        JPanel untilError = createUntilError();

        JPanel restartTraining = createRestart();

        JPanel testPanel = createTestPanel();

        JPanel graph = createGraph();

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKPressed();
            }
        }
        );

        JButton Cancel = new JButton("Cancel");
        buttons.add(OK);
        buttons.add(Cancel);
        Cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancelPressed();
            }
        }
        );

        container.add(trainfile);
        container.add(testfile);
        container.add(params);
        container.add(trainForEpochs);
        container.add(untilError);
        container.add(restartTraining);
        container.add(testPanel);
        container.add(graph);
        container.add(buttons);
        this.getContentPane().add(container);    
        this.pack(); 
        centerDialog();

        globalError.setEditable(false);
        testScore.setEditable(false);
        max.setEditable(false);

    }

    public JPanel createTrainFilePanel(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());

        JLabel lab = new JLabel("Trianing File");

        JButton browse = new JButton("Browse");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseTrainPressed();
            }
        }
        );
        p.add(lab);
        p.add(trainfilename);
        p.add(browse);
        return p;    
    }

    public JPanel createTestFilePanel(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());

        JLabel lab = new JLabel("Testing File ");

        JButton browse = new JButton("Browse");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseTestPressed();
            }
        }
        );
        p.add(lab);
        p.add(testfilename);
        p.add(browse);
        return p;    
    }

    public JPanel createParametersPanel(){
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Mu :"));
        mu = new bckRealNumberBox();
        p.add(mu);
        p.add(new JLabel("Epsilon :"));
        epsilon = new bckRealNumberBox();
        p.add(epsilon);
        mu.setText(".5");
        epsilon.setText("1.1");
        return p;
    }

    public JPanel createTrainForEpochs(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout() );

        JLabel lab = new JLabel(" Train For :                        ");

        trainForEpochs = new bckNumberBox();

        JLabel ep = new JLabel(" Epochs ");

        JButton train = new JButton("Train");
        train.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trainForEpochsPressed();
                Net.notifyObservers();
            }
        }
        );
        p.add(lab);
        p.add(trainForEpochs);
        p.add(ep);
        p.add(train);
        return p;
    }

    public JPanel createUntilError(){
        JPanel p = new JPanel();
        p.setLayout( new ColumnLayout());
        JPanel global = new JPanel();
        JPanel stop = new JPanel();

        global.setLayout( new FlowLayout());
        global.add(new JLabel("Train until Global Error < "));
        trainUntilGE = new bckRealNumberBox();
        global.add(trainUntilGE);
        JButton train = new JButton("Train");
        global.add(train);
        train.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trainUntilPressed();
                Net.notifyObservers();
            }
        }
        );

        stop.setLayout(new FlowLayout());
        stop.add(new JLabel("Stop After                        "));
        stopAfter = new bckNumberBox();
        stop.add(stopAfter);
        stop.add(new JLabel(" Epochs"));
        p.add(global);
        p.add(stop);
        return p;	  
    }

    public JPanel createRestart(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());

        JButton restart = new JButton("Restart");
        p.add(restart);
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartPressed();
                epochsSinceRestart=0;
                epochCount.setText(""+epochsSinceRestart);
                Net.notifyObservers();
            }
        }
        );

        p.add(new JLabel("Epoch Count :"));
        epochCount = new JTextField(10);
        epochCount.setEditable(false);
        p.add(epochCount);
        return p;
    }

    public JPanel createTestPanel(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());
        JButton test = new JButton("Test");
        p.add(test);
        test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPressed();
            }
        }
        );		   
        p.add(new JLabel("Test Score % : "));
        testScore = new JTextField(10);
        p.add(testScore);
        return p;
    }

    public JPanel createGraph(){                
        JPanel p = new JPanel();
        p.setLayout(new ColumnLayout());     
        p.add( new JLabel("     Graph of Global Error"));

        JPanel zoomPanel = new JPanel();
        zoomPanel.setLayout( new FlowLayout());
        JButton zoomIn = new JButton("Zoom In");
        JButton zoomOut = new JButton("Zoom Out");
        zoomPanel.add(new JLabel("Max : "));
        max = new JTextField(10);
        zoomPanel.add(max);
        zoomPanel.add(zoomIn);
        zoomPanel.add(zoomOut);
        p.add(zoomPanel);
        zoomIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoomInPressed();
            }
        }
        );	 
        zoomOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoomOutPressed();
            }
        }
        );
        graphPane = new Graph(bckframe.top,400,100);
        graphPane.setBackground(Color.white);
        graphPane.setSize(400,100);
        p.add(graphPane);
        JPanel globalerr = new JPanel();
        globalerr.setLayout(new FlowLayout());
        globalerr.add(new JLabel("Global Error :"));
        globalError = new JTextField(20);
        globalerr.add(globalError);
        p.add(globalerr);
        return p; 
    } 
    public void zoomInPressed(){
        graphPane.zoomIn();
        double scale = graphPane.getScale();
        double maximum = graphPane.getHeight()/scale;
        max.setText(""+maximum);
    }
    public void zoomOutPressed(){
        graphPane.zoomOut();
        double scale = graphPane.getScale();
        double maximum = graphPane.getHeight()/scale;
        max.setText(""+maximum);
    }

    public void OKPressed(){
        this.setVisible(false); 
    }

    public void CancelPressed(){  
        this.setVisible(false); 
    }

    public void trainForEpochsPressed(){
        int e=trainForEpochs.getInteger();
        double GE;
        int GlobalError;
        if(e<1){
            JOptionPane.showMessageDialog(null,"Illegal Number of Epochs:  "+e,"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        Net.setEpsilon(epsilon.getRealNumber());
        Net.setMu(mu.getRealNumber());
        //go ahead and train : 
        for(int i=0;i<e;i++){
            try{
                epochsSinceRestart++;
                epochCount.setText(""+epochsSinceRestart);
                Net.train(1);
            } 
            catch (Exception ex){
                JOptionPane.showMessageDialog(null,ex.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
            GE=Net.getGlobalError(); 
            GlobalError = (int)Math.ceil(GE);
            graphPane.newPoint(GE);
            globalError.setText(""+GE);
        }
    }

    public void trainUntilPressed(){
        double minError=trainUntilGE.getRealNumber();
        int ep = stopAfter.getInteger();
        double GE;
        int GlobalError;
        if(ep<1){
            JOptionPane.showMessageDialog(bckframe.top,"Illegal Number of Epochs in Stop After Field:  "+ep,"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        Net.setEpsilon(epsilon.getRealNumber());
        Net.setMu(mu.getRealNumber());
        //go ahead and train : 
        for(int i=0;i<ep;i++){
            try{
                epochsSinceRestart++;
                epochCount.setText(""+epochsSinceRestart);
                Net.train(1);
            } 
            catch (Exception ex){
                JOptionPane.showMessageDialog(bckframe.top,ex.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
            GE=Net.getGlobalError();
            GlobalError = (int)Math.ceil(GE);
            graphPane.newPoint(GE);
            globalError.setText(""+GE);
            if(GE<minError){
                JOptionPane.showMessageDialog(bckframe.top,"Training Complete after "+i+" epochs, global Error reached : "+GE,"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }

        }
        JOptionPane.showMessageDialog(bckframe.top,"Training Complete, Target Global Error was NOT ACHIEVED","Alert",JOptionPane.ERROR_MESSAGE);
    }


    public void restartPressed(){
        Net.randomiseWeights();
    }
    public void testPressed(){
        double score=0;
        try{ 
            score = Net.test();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(bckframe.top,e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        testScore.setText(""+score);
    }

    public void browseTrainPressed(){
        //create file dialog
        FileDialog file = new FileDialog(bckframe.top,"Load Training File",FileDialog.LOAD);
        file.show();
        String strfilename = file.getFile();
        if(strfilename!=null){
            trainfilename.setText(file.getDirectory()+strfilename);
            try{ 
                Net.setTrainingFile(file.getDirectory()+strfilename);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null,"Error Reading file \n"+e,"Alert",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void browseTestPressed(){
        //create file dialog
        FileDialog file = new FileDialog(bckframe.top,"Load Training File",FileDialog.LOAD);
        file.show();
        String strfilename = file.getFile();
        if(strfilename!=null){
            testfilename.setText(file.getDirectory()+strfilename);
            try{ 
                Net.setTestingFile(file.getDirectory()+strfilename);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null,"Error Reading file \n"+e,"Alert",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}






