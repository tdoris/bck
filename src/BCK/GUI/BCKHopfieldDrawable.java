

package BCK.GUI;

import BCK.ANN.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

public class BCKHopfieldDrawable extends BCKHopfield implements Drawable,MouseListener, ActionListener{

    private transient BCKHopfieldTrain train;
    private transient BCKPopup popup;
    private transient BCKImageControl image;
    private transient BCKEdit editinternal;

    public BCKHopfieldDrawable(){
        super();
        setImage();
    }

    public BCKHopfieldDrawable(int i){
        super(i);
        setImage();
    }

    /**handle custom deserialization*/
    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException {
        in.defaultReadObject();
        this.setImage();
    }

    private void setImage(){
        image = new BCKImageControl(getName(),BCKImageControl.loadImageIcon(bckframe.home+"images/HOPFIELD.jpg","hello"));
        image.addMouseListener(this);
        popup = new BCKPopup();
        popup.edit.addActionListener(this);
        popup.train.addActionListener(this);
        popup.classify.addActionListener(this);
        popup.map.setEnabled(false);
        popup.cut.addActionListener(this);    
        popup.connect.addActionListener(this);
        train = new BCKHopfieldTrain(this);
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
            image.setImage(bckframe.home+"images/HOPFIELDSELECTED.jpg");
        }
        else{
            image.setImage(bckframe.home+"images/HOPFIELD.jpg");
        }

    }

    public void setVisible(boolean visible){
        image.setVisible(visible);
    }

    public BCKImageControl getImage(){
        return image;
    } 

    public void actionPerformed(ActionEvent e){

        if(e.getSource() == popup.edit){
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

        if(e.getSource() == popup.connect){
            BCKConnectNetworks con = new  BCKConnectNetworks(this);
            con.setVisible(true);   
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
        BCKHopfieldClassify classify = new  BCKHopfieldClassify(this);
        classify.setVisible(true);   
    }

    public void cutPressed(){
        bckframe.top.saveNetwork(this,"CLIPBOARD.NET");
        bckframe.top.removeNetwork(this);
    }

}


class BCKHopfieldTrain extends BCKDialog {

    JTextField filename = new JTextField(25);
    JButton OK = new JButton("Train");
    BCKHopfieldDrawable Net;
    public BCKHopfieldTrain(BCKHopfieldDrawable net){
        super(bckframe.top,"Train Hopfield", true);
        Net = net;
        filename.setEditable(false);
        JPanel container = new JPanel();
        container.setLayout( new ColumnLayout());
        JPanel trainfile = createTrainFilePanel();

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
        container.add(buttons);
        this.getContentPane().add(container);    
        this.pack(); 
        centerDialog();

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

    public void OKPressed(){
        try{
            Net.setTrainingFile(filename.getText());
            Net.train();
            setVisible(false);
        }
        catch(Exception te){
            JOptionPane.showMessageDialog(null,te.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
        }      
    }

    public void CancelPressed(){  
        this.setVisible(false); 
    }

    public void browsePressed(){
        //create file dialog
        FileDialog file = new FileDialog(bckframe.top,"Load Training File",FileDialog.LOAD);
        file.show();
        String strfilename = file.getFile();
        if(strfilename!=null){
            filename.setText(file.getDirectory()+strfilename);
        }
    }



}




