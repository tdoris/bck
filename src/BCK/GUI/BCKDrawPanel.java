package BCK.GUI; 
import javax.swing.*;
import BCK.ANN.*;
import java.awt.*;               // ScrollPane, PopupMenu, MenuShortcut, etc.
import java.awt.datatransfer.*;  // Clipboard, Transferable, DataFlavor, etc.
import java.awt.event.*;         // New event model.
import java.io.*;                // Object serialization streams.
import java.util.zip.*;          // Data compression/decompression streams.
import java.util.Vector;         // To store the scribble in.
import java.util.Properties;     // To store printing preferences in.

import javax.accessibility.*;;
import java.beans.*;


/**
 * This class is a custom component that supports scribbling.  It also has
 * a popup menu that allows the scribble color to be set and provides access
 * to printing, cut-and-paste, and file loading and saving facilities.
 * Note that it extends Component rather than Canvas, making it "lightweight."
 */
public class BCKDrawPanel extends JComponent {
    protected short last_x, last_y;                // Coordinates of last click.
    protected int currentx;
    protected Vector lines = new Vector(256,256);  // Store the Graphs.
    protected Color current_color = Color.black;   // Current drawing color.
    protected int width, height;                   // The preferred size.
    protected Frame frame;                         // The frame we are within.
    protected double[] yvals;
    protected int index;
    protected double scale;
    protected int deltax;
    /** This constructor requires a Frame and a desired size */
    public BCKDrawPanel(Frame frame, int width, int height) {
        this.frame = frame;
        this.width = width;
        this.height = height;
        this.currentx=0;
        deltax=5;
        yvals = new double[width/deltax];
        scale=50;
        index=0;   
        for(int i=0;i<yvals.length;i++){
            yvals[i]=this.height+1;
        }    
        // We handle scribbling with low-level events, so we must specify
        // which events we are interested in.
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
        setBackground(Color.white);
    }


    /** Specifies big the component would like to be.  It always returns the
     *  preferred size passed to the Graph() constructor */
    public Dimension getPreferredSize() { 
        return new Dimension(width, height); 
    }



    /** Draw all the saved lines of the Graph */
    public void paint(Graphics g) {
        int lastx = 0;
        int lasty =(int)Math.ceil(this.height- scale*yvals[0]);
        double y;
        int iy;
        g.setColor(Color.white);
        g.fillRect(0,0,this.width,this.height);
        for(int i = 0; i < yvals.length; i++) { 
            if(yvals[i]==this.height+1) return;
            y=yvals[i];
            y=y*scale;
            //reverse orientation:
            iy = (int)Math.ceil(this.height - y);      
            g.setColor(current_color);
            g.drawLine(lastx, lasty, lastx+deltax, iy);
            lastx = lastx+deltax;
            lasty = iy;      
        }
        setBackground(Color.white);
        repaint();
    }


    public void processMouseEvent(MouseEvent e) {
        if (e.isAltDown()){

        } 
        else if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            last_x = (short)e.getX(); 
            last_y = (short)e.getY(); // Save position.
        }
        else super.processMouseEvent(e);  // Pass other event types on.
    }

    /**
     * This method is called for mouse motion events.  It adds a line to the
     * Graph, on screen, and in the saved representation
     */
    public void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);  // Important!
    }

    public void newPoint(double y){

        yvals[index]=y;
        index++;
        if(index==yvals.length){
            index--;
            shiftLeft();
        }
        paint(getGraphics());
    }

    public void newPoint(int y){
        //reverse orientation:
        y=y*10;
        int iy = this.height - y;
        if(iy<0) iy=0;
        yvals[index]=iy;
        index++;
        if(index==yvals.length){
            index--;
            shiftLeft();
        }
        paint(getGraphics());
    }

    public void shiftLeft(){
        int tmp;
        for(int i=0;i<yvals.length-1;i++){   
            yvals[i]=yvals[i+1];
        }
    }
    public double getScale(){
        return scale;
    }

    public void zoomIn(){
        this.scale*=2;
        paint(getGraphics());
    }
    public void zoomOut(){
        this.scale/=2;
        paint(getGraphics());
    }
}




