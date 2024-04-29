

package BCK.GUI;

import BCK.ANN.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

import com.sun.java.swing.AbstractButton;
import com.sun.java.accessibility.*;
import java.beans.*;
import com.sun.java.swing.plaf.metal.*;

/** This is the internal window which displays a graphical representation of a neural network, with neurons represented by square icons, and interconnections by color and thickness coded lines
 *@author Tom Doris
 *@author Eoin Whelan
 */

public class BCKEdit extends JInternalFrame implements InternalFrameListener{

    JScrollPane spane;
    BCKNetworkEditor editor;
    //Menu Bar
    JMenuBar editMenuBar = new JMenuBar();

    JMenu Options = new JMenu("Options");
    JCheckBoxMenuItem ShowLines = new JCheckBoxMenuItem("Show Connections");

    public BCKEdit(BCKNeuralNetwork net){
        super("Edit Neural Network "+net.idNumber,true,true,true,false);
        this.getContentPane().setLayout(new BorderLayout());
        createMenu();
        editMenuBar.add(Options);
        this.setMenuBar(editMenuBar);
        setBackground(Color.white);
        editor = new BCKNetworkEditor(net); 
        editor.setSize(800,800);

        spane = new JScrollPane();    
        JViewport port = spane.getViewport();
        port.add(editor);

        this.setContentPane(spane);
        this.setPreferredSize(new Dimension(300,300));
        this.show();
        setOpaque(true);
        this.addInternalFrameListener(this);
        editor.drawNeurons();
    }

    public void createMenu(){
        ShowLines.setSelected(false);
        Options.add(ShowLines);
        ShowLines.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem ShowLines = (JCheckBoxMenuItem)e.getSource();
                if(ShowLines.isSelected()) {                     
                    editor.setShowLines(true); 
                } 
                else {
                    editor.setShowLines(false);
                }
            }
        }
        );   

    }

    //Invoked when an internal frame is activated. 
    public void internalFrameActivated(InternalFrameEvent ife){
        spane.validate();  
        repaint();
    } 
    //Invoked when an internal frame has been closed.      
    public void internalFrameClosed(InternalFrameEvent ife){
        this.setVisible(false);
        bckframe.top.bckInternal.lc.remove(this);  
    }
    //Invoked when an internal frame is in the process of being closed.       
    public void internalFrameClosing(InternalFrameEvent ife){
    }
    //Invoked when an internal frame is de-activated.      
    public void internalFrameDeactivated(InternalFrameEvent ife){
    }
    //Invoked when an internal frame is de-iconified.    
    public void internalFrameDeiconified(InternalFrameEvent ife){
    }
    //Invoked when an internal frame is iconified.     
    public void internalFrameIconified(InternalFrameEvent ife){
    }

    //Invoked when a internal frame has been opened.     
    public void  internalFrameOpened(InternalFrameEvent ife){
    }    
}

