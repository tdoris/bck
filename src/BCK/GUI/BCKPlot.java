package BCK.GUI; 

import java.awt.*;               // ScrollPane, PopupMenu, MenuShortcut, etc.
import java.awt.datatransfer.*;  // Clipboard, Transferable, DataFlavor, etc.
import java.awt.event.*;         // New event model.
import java.io.*;                // Object serialization streams.
import java.util.zip.*;          // Data compression/decompression streams.
import java.util.Vector;         // To store the scribble in.
import java.util.Properties;     // To store printing preferences in.

import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;
import com.sun.java.swing.AbstractButton;
import com.sun.java.accessibility.*;
import java.beans.*;
import com.sun.java.swing.plaf.metal.*;

/** Simple component which allows to draw points in different colors on a white background*/

public class BCKPlot  extends JComponent {
    int width, height;                   // The preferred size.
    protected Frame frame;                         // The frame we are within.
    protected Point currentPoint;
    protected Color currentColor;
    protected boolean blank;

    /** This constructor requires a Frame and a desired size */
    public BCKPlot(Frame frame, int width, int height) {
        this.frame = frame;
        this.width = width;
        this.height = height;    
        currentColor = Color.white;
        currentPoint = new Point(2,1);
        setBackground(Color.white);
        setForeground(Color.white);

        blank = true;
    }

    public void setBlank(boolean b){
        blank = b;
    }
    public AccessibleContext getAccessibleContext(){
        return super.getAccessibleContext();
    }

    /** Specifies big the component would like to be.  It always returns the
     *  preferred size passed to the BCKPlot() constructor */
    public Dimension getPreferredSize() { 
        return new Dimension(width, height); 
    }

    public void setPreferredSize(Dimension d){
        height=d.height;
        width=d.width;

    }

    /** Draw the BCKPlot */
    public void paint(Graphics g) { 
    }

    public void newPoint(Point p, Color c){
        currentPoint = p;
        currentColor = c;
        Graphics g= getGraphics();
        g.setColor(currentColor);
        g.drawLine(currentPoint.x,currentPoint.y,currentPoint.x,currentPoint.y);
    }
    /** draws a 2-by-2 pixel rectangle at the specified point of the specified color*/
    public void new2by2(Point p, Color c){
        currentPoint = p;
        currentColor = c;
        Graphics g= getGraphics();
        g.setColor(currentColor);
        g.fillRect(currentPoint.x,height-currentPoint.y,2,2);
    }
    public void new4by4(Point p, Color c){
        currentPoint = p;
        currentColor = c;
        Graphics g= getGraphics();
        g.setColor(currentColor);
        g.fillRect(currentPoint.x,height-currentPoint.y,4,4);
    }
    /** Draw an image encoded in an integer array */
    public void drawPicture(int[] pixels, int w, int max,int scale){
        int h = pixels.length / w;
        Graphics g= getGraphics();
        int index=0;
        Color c;
        float grey;
        for(int j=0; j<h;j++){
            for(int i = 0; i<w;i++){      
                grey = (float)pixels[index]/(float)max;
                g.setColor(new Color(grey,grey,grey));
                g.fillRect(i*scale,j*scale,scale,scale);
                index++;
            }
        }
    }
    /** Draw an image encoded in a doubles array */
    public void drawPicture(double[] pixels, int w,int scale){
        int h = pixels.length / w;
        Graphics g= getGraphics();
        int index=0;
        Color c;
        float grey;
        double max = -100;
        //find the max value:
        for(int p=0;p<pixels.length;p++){
            if(pixels[p] > max) max = pixels[p];
        }

        for(int j=0; j<h;j++){
            for(int i = 0; i<w;i++){      
                grey = (float)pixels[index]/(float)max;
                g.setColor(new Color(grey,grey,grey));
                g.fillRect(i*scale,j*scale,scale,scale);
                index++;
            }
        }
    }

    /** Draw a crosshair on the plot*/
    public void crossHair(){
        Graphics g= getGraphics();
        g.setColor(Color.green);
        g.drawLine(width/2,0,width/2,height);
        g.drawLine(0,height/2,width,height/2);

    }
}




