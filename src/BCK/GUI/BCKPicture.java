
//Title        BCK
//Version      
//Copyright    Copyright (c) 1997
//Author       Eoin Whelan and Tom Doris
//Company      DCU
//Description  A JPanel that is used to display a picture. You can pass the
//             file as a string to the constructor or to the method setImage();

package BCK.GUI;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

/**Description -  A JPanel that is used to display a picture. You can pass the
 *             file as a string to the constructor or to the method setImage();*/

public class BCKPicture extends JPanel{

    /**The current image that is to be displayed*/
    private ImageIcon image;

    //Constructors
    /**Constructor. Pass the picture file to display on the JPanel*/
    public BCKPicture(String file){
        super();
        setImage(file);
        setPreferredSize(new Dimension(100,100));
        setVisible(true);  
    }

    /**Constructor*/
    public BCKPicture(){
        super();
        setPreferredSize(new Dimension(100,100)); 
        setVisible(true);
    }

    /**Paints the current image on to the JPanel*/
    public void paintComponent(Graphics g){ 
        int x = getPreferredSize().width;
        int y = getPreferredSize().height;
        if (image != null){
            g.drawImage(getImage(),0,0,x,y,this);  
        }
        else{
            g.fillRect(0,0,x,y);
        }

    }

    /**Set the image to display on the JPanel. Pass in the Path to the file
     *   concatinated with the File as a string to the method.*/
    public void setImage(String file){
        if(file == null){
            image=null;
        }
        else{
            image = new ImageIcon(file);
        }
        repaint();
    }

    /**Returns the AWT Image that is displayed on the JPanel*/
    public Image getImage(){
        return image.getImage();  
    }

}



