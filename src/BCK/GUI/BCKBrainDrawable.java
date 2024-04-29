
package BCK.GUI;
import javax.swing.*;

import BCK.ANN.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import java.awt.*;


/** The Brain class is a container for Neural Networks. It also specifies exactly how the output of one neural network is used as the input to another neural network. This is the toplevel class in the system, may be implemented as a singleton. This class extends the BCKBrain class, allowing the serialization of the layout of the component networks on the GUI
 *@author Eoin Whelan 
 *@author Tom Doris
 */

public class BCKBrainDrawable extends BCKBrain implements Serializable, Drawable{ 

    private transient BCKImageControl image;
    Vector locations = new Vector(0);
    public BCKBrainDrawable(){
        super();
        setImage();
    }

    private void setImage(){
        image = new BCKImageControl(BCKImageControl.loadImageIcon(bckframe.home+"images/hal.jpg","hello"));
        image.setToolTipText("Brain Inputs");
    }
    /**handle custom deserialization*/
    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException {
        in.defaultReadObject();
        setImage();
        //set the locations of the components
        for(int i=0;i<size();i++){
            Point p = (Point)locations.elementAt(i);
            ((Drawable)getNet(i)).getImage().setLocation(p.x,p.y);    
        }
    }


    /** Handle custom serialization */
    private void writeObject(ObjectOutputStream out) throws IOException{
        //get the locations of the components
        for(int i=0;i<size();i++){
            Point p = ((Drawable)getNet(i)).getImage().getLocation();
            locations.addElement(p);
        }
        out.defaultWriteObject();
    }


    public BCKImageControl Draw(){
        return image; 
    }
    public boolean isSelected(){
        return false; //the brain object cannot be selected like other nets
    }
    public void setSelected(boolean select){
    }
    public void setVisible(boolean visiblity){
    }
    public BCKImageControl getImage(){
        return image;
    }
    public void trainPressed(){
    }
    public void editPressed(){
        BCKEdit editinternal = new BCKEdit(this);
        editinternal.setPreferredSize(new Dimension(300,200));
        editinternal.setLocation(10,10);
        bckframe.top.bckInternal.lc.add(editinternal,JLayeredPane.PALETTE_LAYER);
    }
    public void classifyPressed(){
        BCKClassify classify = new  BCKClassify(this);
        classify.setVisible(true); 
    }
    public void cutPressed(){
    }

}






