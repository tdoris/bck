

package BCK.GUI;

import BCK.ANN.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

public class BCKRBFDrawable extends BCKRBFDDA implements Drawable,MouseListener, ActionListener, Serializable{

    private transient BCKRBFTrain train;
    private transient BCKPopup popup;
    private transient BCKImageControl image;
    private transient BCKEdit editinternal;

    public BCKRBFDrawable(){
        super();
        setImage();
    }

    public BCKRBFDrawable(int i,int j) throws Exception{
        super(i,j);
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
        image = new BCKImageControl(getName(),BCKImageControl.loadImageIcon(bckframe.home+"images/RBF.jpg","hello"));
        image.addMouseListener(this);
        popup = new BCKPopup();
        popup.edit.addActionListener(this);
        popup.train.addActionListener(this);
        popup.classify.addActionListener(this);
        popup.cut.addActionListener(this);
        popup.map.addActionListener(this);
        popup.connect.addActionListener(this);
        train = new BCKRBFTrain(this);
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
            image.setImage(bckframe.home+"images/RBFSELECTED.jpg");
        }
        else{
            image.setImage(bckframe.home+"images/RBF.jpg");
        }
    }

    public void setVisible(boolean visible){
        image.setVisible(visible);
    }
    public void actionPerformed(ActionEvent e){

        if(e.getSource() == popup.edit){
            editPressed(); 
        }

        if(e.getSource() == popup.train){
            trainPressed();
        }

        if(e.getSource() == popup.classify){
            classifyPressed();
        }

        if(e.getSource() == popup.cut){
            cutPressed();
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

    public void mouseEntered(MouseEvent me){
    }
    public void mouseExited(MouseEvent me){
    }
    public void mousePressed(MouseEvent me){
    }
    public void mouseReleased(MouseEvent me){
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

