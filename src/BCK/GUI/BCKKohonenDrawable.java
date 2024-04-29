//Author:       Eoin Whelan
//Company:      DCU
//Description:  none


package BCK.GUI;
import javax.swing.*;

import BCK.ANN.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class BCKKohonenDrawable extends BCKKohonen implements Drawable,MouseListener, ActionListener{

    private transient BCKKohonenTrain train;
    private transient BCKPopup popup;
    private transient BCKImageControl image;
    private transient BCKEdit editinternal=null;

    public BCKKohonenDrawable(){
        super();
        setImage();
    } 

    public BCKKohonenDrawable(int i,int j) throws Exception{
        super(i,j);
        setImage();
    }

    /**handle custom deserialization*/
    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException {
        in.defaultReadObject();
        this.setImage();
    }
    private void setImage(){
        image = new BCKImageControl(getName(),BCKImageControl.loadImageIcon(bckframe.home+"images/KOHONEN.jpg","hello"));
        image.addMouseListener(this);
        popup = new BCKPopup();
        popup.edit.addActionListener(this);
        popup.train.addActionListener(this);
        popup.classify.addActionListener(this);
        popup.cut.addActionListener(this);
        train = new BCKKohonenTrain(this);
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
            image.setImage(bckframe.home+"images/KOHONENSELECTED.jpg");
        }
        else{
            image.setImage(bckframe.home+"images/KOHONEN.jpg");
        }
    }

    public void setVisible(boolean visible){
        image.setVisible(visible);
    }
    public BCKImageControl getImage(){
        return image;
    } 

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == popup.edit) {   
            editPressed();
        }     


        if(e.getSource() == popup.train){
            trainPressed();
        }

        if(e.getSource() == popup.cut){
            cutPressed();
        } 

        if(e.getSource() == popup.classify){
            classifyPressed(); 
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

}




/**This class implements the dialog for Kohonen training*/
class BCKKohonenTrain extends BCKDialog {
    JTextField max;
    BCKKohonenDrawable Net;
    JTextField filename = new JTextField(25);
    JButton train, stop;  
    bckNumberBox numEpochs;
    JTextField globalError;
    bckRealNumberBox eta = new bckRealNumberBox();
    bckNumberBox neighbourhood = new bckNumberBox();  
    Graph graphPane;

    public BCKKohonenTrain(BCKKohonenDrawable net){
        super(bckframe.top);
        filename.setEditable(false);
        Net = net;
        setTitle("Train Kohonen");
        JPanel container = new JPanel();
        container.setLayout( new ColumnLayout());
        JPanel trainfile = createTrainFilePanel();    
        JPanel params = createParametersPanel();
        JPanel buttons = createButtonsPanel();
        JPanel trainEpochs = createTrainPanel();    
        JPanel graph = createGraph();

        container.add(trainfile);
        container.add(params);
        container.add(trainEpochs);
        container.add(graph);    
        container.add(buttons);
        this.getContentPane().add(container);    
        this.pack(); 
        centerDialog();

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

    public JPanel createParametersPanel(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());

        p.add( new JLabel("Learning Rate :"));    
        p.add(eta);
        eta.setText("0.9");
        p.add( new JLabel("Neighbourhood :"));
        neighbourhood.setText(""+Net.getNeighbourhood());
        p.add(neighbourhood);
        return p;    
    } 

    public JPanel createTrainFilePanel(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());

        JLabel lab = new JLabel("Trianing File");

        JButton browse = new JButton("Browse");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browsePressed();
            }
        }
        );
        p.add(lab);
        p.add(filename);
        p.add(browse);
        return p;    
    }

    public JPanel createButtonsPanel(){
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());  
        JButton Cancel = new JButton("Cancel");
        JButton OK = new JButton("OK");
        p.add(OK);
        OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKPressed();
            }
        }
        );
        p.add(Cancel);
        Cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancelPressed();
            }
        }
        );
        return p;    

    }


    public JPanel createTrainPanel(){
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());  
        p.add(new JLabel("Epochs :"));
        numEpochs = new bckNumberBox();
        p.add(numEpochs);
        train = new JButton("Train");
        train.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trainPressed();
            }
        }
        );       
        p.add(train);
        return p;    
    }

    public void trainPressed(){
        int epochs=numEpochs.getInteger();
        double GE = 0.0;
        if(epochs<1){
            JOptionPane.showMessageDialog(null,"Illegal Number of Epochs:  "+epochs,"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        double etastart=eta.getRealNumber();
        if( Double.isNaN(etastart) || etastart<0.0){
            JOptionPane.showMessageDialog(null,"Illegal Value for Learning rate:  "+etastart,"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        int neighSize=neighbourhood.getInteger();
        if(neighSize<0|| neighSize > (int)Math.ceil((0.5)*Net.side) ){
            JOptionPane.showMessageDialog(null,"Illegal Neighbourhood:  "+neighbourhood,"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        Net.setEta(etastart);
        Net.setNeighbourhood(neighSize);

        try{       
            for(int epnum = 0; epnum < epochs;epnum++){
                Net.train(1);
                GE = Net.getGlobalError();
                globalError.setText(""+Net.getGlobalError());
                graphPane.newPoint(GE);
            }
        }
        catch(Exception te){
            JOptionPane.showMessageDialog(null,te.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
        }
    }


    public void CancelPressed(){  
        this.setVisible(false); 
    }

    public void OKPressed(){  
        this.setVisible(false); 
    }
    public void browsePressed(){
        //create file dialog
        FileDialog file = new FileDialog(bckframe.top,"Load Training File",FileDialog.LOAD);
        file.show();
        String strfilename = file.getFile();
        if(strfilename!=null){
            filename.setText(file.getDirectory()+strfilename);
            try{
                Net.setTrainingFile(file.getDirectory()+strfilename);
            } 
            catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Error Setting Training File: "+ex,"Alert",JOptionPane.ERROR_MESSAGE);
            }

        }
    }

}
