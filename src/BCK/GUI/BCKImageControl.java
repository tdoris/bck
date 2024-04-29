
//Title        BCK
//Version      
//Copyright    Copyright (c) 1997
//Author       Eoin Whelan
//Company      DCU

package BCK.GUI;

import java.awt.event.*;
import java.awt.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

/**Description  A JButton that you get display an ImageIcon on. It may 
 *    also be picked up and moved.*/

public class BCKImageControl extends JButton implements MouseMotionListener{

    int maxHeight=0;
    boolean selected = false; //set to false when focus is lost
    //set to true when focus is gained
    String pictureFile;

    /**Static method that returns ImageIcon given a string which
     *      represents the path and the file*/
    public static ImageIcon loadImageIcon(String filename, String description) {
        return new ImageIcon(filename, description);    
    }

    /**Constructor. Pass in the ImageIcon that you wish to display on
     *      the JButton.*/ 
    public BCKImageControl(ImageIcon icon) {
        super(icon);
        setSize(new Dimension(75,75));
        setForeground(Color.white);
        this.addMouseMotionListener(this);
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setVerticalTextPosition(JButton.BOTTOM);
        setHorizontalTextPosition(JButton.CENTER);    
    }
    /**Constructor. Pass in the ImageIcon and the string that you wish
     *      to display on the JButton.*/ 
    public BCKImageControl(String num, ImageIcon icon) {
        super(num,icon);
        setSize(new Dimension(75,95));
        setForeground(Color.white);
        this.addMouseMotionListener(this);    
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setVerticalTextPosition(JButton.BOTTOM);
        setHorizontalTextPosition(JButton.CENTER); 
    }

    /**Returns the Centre of the JButton as a point*/
    public Point getCenter(){
        return new Point(getLocation().x+getSize().width/2, getLocation().y+getSize().height/2);
    }

    /**Returns true if this Component is selected*/
    public boolean isSelected(){
        return selected;
    }

    /**Set the component selected true or false*/
    public void setSelected(boolean select){
        selected = select;
    }

    /**Sets the image to display on the Component*/
    public void setImage(String file){
        setIcon(loadImageIcon(file,file));
        repaint();
    }

    /**The event that happens when the mouse is dragged over the
     *      component. In this case the Component is dragged with the mouse*/
    public void mouseDragged(MouseEvent e){
        if (e.getY() + getLocation().y-getSize().width/2 > maxHeight)
        {
            setLocation(e.getX() + getLocation().x-getSize().width/2,e.getY() + getLocation().y-getSize().height/2);
        }
        else  {
            setLocation(e.getX() + getLocation().x-getSize().width/2,maxHeight);
        }
        bckframe.top.repaint();	
    }

    /**This component implements MouseMotionListener so must implement
     *      this method*/
    public void mouseMoved(MouseEvent e){
    }
}






