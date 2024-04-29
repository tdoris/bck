package BCK.GUI;
import java.awt.*;
import java.awt.event.*;
import BCK.ANN.*;

import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;
import com.sun.java.swing.AbstractButton;

import java.beans.*;
import com.sun.java.swing.plaf.metal.*;


public class BCKDialog extends JDialog implements WindowListener{

    public BCKDialog(){
        super();
        addWindowListener(this);
        setModal(true);
    }
    public BCKDialog(Frame owner){
        super(owner);
        addWindowListener(this);
        setModal(true);
    }
    public BCKDialog(Frame owner, boolean modal){
        super(owner, modal);
        addWindowListener(this);
        setModal(true);
    }
    public BCKDialog(Frame owner, String title){
        super(owner, title);
        addWindowListener(this);
        setModal(true);
    }
    public BCKDialog(Frame owner, String title, boolean modal){
        super(owner, title, modal);
        addWindowListener(this);
        setModal(true);
    } 
    public void centerDialog() {
        Dimension screenSize = this.getToolkit().getScreenSize();
        Dimension size = this.getSize();
        screenSize.height = screenSize.height/2;
        screenSize.width = screenSize.width/2;
        size.height = size.height/2;
        size.width = size.width/2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        this.setLocation(x,y);
    }
    public void windowActivated(WindowEvent we){
    }
    public void windowClosed(WindowEvent we){
        bckframe.top.show();
    }
    public void windowClosing(WindowEvent we){
        bckframe.top.show();
    }
    public void windowDeactivated(WindowEvent we){
    }
    public void windowDeiconified(WindowEvent we){
    }
    public void windowIconified(WindowEvent we){
    }
    public void windowOpened(WindowEvent we){
    }


}
