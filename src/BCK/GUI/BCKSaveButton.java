
//Title:        BCK
//Version:
//Copyright:    Copyright (c) 1997
//Author:       Eoin Whelan
//Company:      DCU
//Description:  none

package BCK.GUI;

import BCK.ANN.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.IOException;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

public class BCKSaveButton extends JButton{

    public BCKSaveButton(){
        super("SAVE");
    }

    public void actionPerformed(ActionEvent ae){
    }

    /**  Puts/gets one of this object's properties using the associated key. If the value has
     * changed, a PropertyChangeEvent will be sent to listeners. */
    public Object getValue(String key){
        return new String("Not Implemented YET");
    } 
    public void putValue(String key,Object value){
    }


    /**Sets/tests the enabled state of the Action. When enabled, any component associated
     * with this object is active and able to fire this object's actionPerformed method.  */
    public void setEnabled(boolean b){
    }       
    public boolean isEnabled(){
        return true;
    }


    /** Add or remove a PropertyChange listener. Containers and attached components use
     * these methods to register interest in this Action object. When its enabled state or
     * other property changes, the registered listeners are informed of the change.*/
    public void addPropertyChangeListener(PropertyChangeListener listener){
    }
    public void removePropertyChangeListener(PropertyChangeListener listener){
    }
}

