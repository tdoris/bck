

package BCK.GUI;

import BCK.ANN.*;
import java.util.*;
import java.io.IOException;

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

/** This is the pane which displays a graphical representation of a neural network, with neurons represented by square icons, and interconnections by color and thickness coded lines
 *@author Tom Doris
 *@author Eoin Whelan
 */

public class BCKNetworkEditor extends JDesktopPane implements Scrollable, BCKObserver {

    Vector images = new Vector(0);
    BCKNeuralNetwork Net;
    Vector Lines = new Vector(0);
    int MaxLines = 1000;
    int numNodes;
    int xgap;
    int firstColumn;
    int maxInColumn;
    int preferredX;
    int preferredY;
    boolean ShowLines;
    public BCKNetworkEditor(BCKNeuralNetwork net){
        super();
        ShowLines = false;
        preferredX=1000;
        preferredY=1000;
        setPreferredSize(new Dimension(preferredX,preferredY));
        setBackground(Color.black);
        this.Net=net;
        maxInColumn=10;
        firstColumn=10;
        xgap=100;
        numNodes = Net.getNumberOfNeurons();
        for(int i=0;i<numNodes;i++){
            images.addElement( new BCKNeuronImage(i,this, Net.getNeuron(i), Net));
            add(getImage(i),JLayeredPane.PALETTE_LAYER);
        }
        //this object observes changes in the BCKNeuralNetwork
        Net.registerObserver((BCKObserver)this);
    }

    protected void finalize(){
        Net.removeObserver((BCKObserver)this);
    }

    public void updateObserver(){ 
        //RBF can add nodes as it learns, so we must reflect this:
        if (this.Net instanceof BCKRBF){
            numNodes = Net.getNumberOfNeurons();
            images = new Vector(0);
            removeAll();
            for(int i=0;i<numNodes;i++){
                images.addElement( new BCKNeuronImage(i,this, Net.getNeuron(i), Net));
                add(getImage(i),JLayeredPane.PALETTE_LAYER);
            }
            drawRBF((BCKRBF)Net);
        }
        drawConnections();
        repaint();
    }
    public void setShowLines(boolean b){
        ShowLines = b;
        if(ShowLines) drawConnections();
        repaint();
    }


    public void drawNeurons(){
        if(this.Net instanceof BCKMLP){
            drawMLP((BCKMLP)Net);
        } 
        else if (this.Net instanceof BCKHopfield){
            drawHopfield();
        } 
        else if (this.Net instanceof BCKRBF){
            drawRBF((BCKRBF)Net);
        } 
        else if (this.Net instanceof BCKKohonen){
            drawKohonen();
        }
        drawConnections();
        repaint();
    }

    public void paint (Graphics g){
        super.paint(g);
        if(ShowLines){
            Line l;
            for(int i=0;i<Lines.size();i++){
                l=(Line)Lines.elementAt(i);
                g.setColor(l.color);
                for(int t=0;t<l.thickness;t++) //draw multiple lines for thickness
                    g.drawLine(l.x1,l.y1+t,l.x2,l.y2+t); 
            }
        }
    }


    /**Draw a line connecting neuron pre to neuron post */
    public void drawConnection(int pre, int post, double weight){
        if(!ShowLines) return;
        Point centerPre =getImage(pre).getCenter();
        Point centerPost = getImage(post).getCenter();
        Color c;
        int thickness = 1;  
        //convert weight to greyscale colour
        thickness = ((int)Math.abs(Math.floor(weight)));
        thickness++;
        if(weight < 0){
            c= new Color(0,0,1.0f);  //inhibitory links are blue
        } 
        else {
            c= new Color(1.0f,0.3f,0.3f); //excitatory links are red
        }
        Lines.addElement(new Line(centerPre.x+20,centerPre.y,centerPost.x-15,centerPost.y,c,thickness));

    }

    public void drawConnections(){
        Lines.removeAllElements();
        int[] inputs;
        if(!ShowLines) return;
        for(int i=0;i<numNodes;i++){
            try{
                inputs = Net.getInputArrayIds(i);   
                for(int j=0;j<inputs.length;j++){
                    if(inputs[j]>=0 && inputs[j]<numNodes)  //bias id = -1
                        drawConnection(inputs[j],i,Net.getWeight(inputs[j],i));
                    //check that we have not exceeded the maximum number of allowed lines
                    if(Lines.size() > MaxLines){
                        JOptionPane.showMessageDialog(bckframe.top, "Too many connections \n risk exceeding memory allocation \n","Alert",JOptionPane.ERROR_MESSAGE);
                        ShowLines = false;
                        return;
                    }

                }
            } 
            catch(Exception e){
                System.out.println(e.toString());
            }
        }
    }

    public void drawKohonen(){
        int inputs = Net.getNumInputs();
        int outputs = Net.getNumOutputs();
        int xincr=0;
        int y=20;
        int x=30;
        int columnNumber=0;
        int node=0;
        int side = (int)Math.sqrt(outputs);
        //draw input neuron array:
        for(int i=0; i<inputs; i++){
            if(columnNumber < (i/maxInColumn)){ //move on to next column
                y = 20;
                x+=34;
            }
            columnNumber = (i/maxInColumn);
            getImage(node).setLocation(x, y);
            y+=34;
            node++;
        }
        //expand size of canvas if necessary
        if(x>preferredX){
            preferredX=x+100;
        }
        if(y>preferredY){
            preferredY=y;
        }
        x+=100;
        y=20;
        int leftBound=x;
        //outputs in Kohonen create a 2-d square map, so draw it that way
        for(int i=0;i<side;i++){ //row
            for(int j=0;j<side;j++){ //nodes
                getImage(node).setLocation(x, y);
                x+=34;
                if(x>preferredX){
                    preferredX=x+100;
                }
                node++;  
            }
            x=leftBound;
            y+=34;
        }
        if(y>preferredY){
            preferredY=y+100;
        }
        setPreferredSize(new Dimension(preferredX,preferredY));
    }


    public void drawMLP(BCKMLP net){
        int y=20;
        int columnNumber=0;
        int node=0;
        int x=30;
        //draw layers:
        for(int i=0;i<net.getNumberOfLayers();i++){
            for(int j=0;j<net.getNumberOfNeuronsInLayer(i);j++){
                if(columnNumber < (j/maxInColumn)){ //move on to next column
                    y = 20;
                    x+=34;
                }
                columnNumber = (j/maxInColumn);
                getImage(node).setLocation(x, y);
                y+=34;
                node++;
            }
            x+=100;
            y=20;
        }
        if(x>preferredX){
            preferredX=x+100;
        }
        if(y>preferredY){
            preferredY=y+100;
        }
        setPreferredSize(new Dimension(preferredX,preferredY));
    }

    public void drawRBF(BCKRBF net){
        int inputs = net.getNumInputs();
        int outputs = net.getNumOutputs();
        int numNodesInLayer=0;
        int node=0;
        int y=20;
        int x=30;
        int columnNumber=0;
        int hidden=net.getNumHidden();

        for(int i=0;i<3;i++){
            if(i==0){
                numNodesInLayer=inputs;
            } 
            else if(i==1) {
                numNodesInLayer=hidden;
            } 
            else if(i==2){
                numNodesInLayer=outputs;
            }
            for(int j=0;j<numNodesInLayer;j++){  
                if(columnNumber < (j/maxInColumn)){ //move on to next column
                    y = 20;
                    x+=34;
                }
                columnNumber = (j/maxInColumn);
                getImage(node).setLocation(x, y);
                y+=34;
                node++;
            }
            x+=100;
            y=20;
        }
        if(x>preferredX){
            preferredX=x+100;
        }
        if(y>preferredY){
            preferredY=y+100;
        }
        setPreferredSize(new Dimension(preferredX,preferredY));
    }

    public void drawHopfield(){
        int numNeurons = Net.getNumberOfNeurons();
        int y=20;
        int x=30;
        int node=0;
        int leftBound=x;
        int rowNumber=0;
        int side = (int)Math.sqrt(numNeurons);
        //nodes in Hopfield create a planar lattice:
        for(int i=0;i<numNeurons;i++){     
            if(rowNumber < (i/side)){ //move on to next column
                y +=60;
                x =leftBound;
            }
            rowNumber = (i/side);
            getImage(i).setLocation(x, y);
            x+=60;	
            if(x>preferredX){
                preferredX=x+100;
            }
        }
        if(y>preferredY){
            preferredY=y+100;
        }
        setPreferredSize(new Dimension(preferredX,preferredY));
    }

    public BCKNeuronImage getImage(int i){
        return ((BCKNeuronImage)images.elementAt(i));
    }

    /**   Returns the preferred size of the viewport for a view component. For example the
     *        preferredSize of a JList component is the size required to acommodate all of the cells
     *        in its list however the value of preferredScrollableViewportSize is the size required for
     *        JList.getVisibleRowCount() rows. A component without any properties that would
     *        effect the viewport size should just return getPreferredSize() here. 
     * 
     *        Returns: 
     *               The preferredSize of a JViewport whose view is this Scrollable. 
     *        See Also: 
     *               getPreferredSize 
     * 
     *    getScrollableUnitIncrement */
    public Dimension getPreferredScrollableViewportSize(){
        return getPreferredSize();
    }

    /**  Components that display logical rows or columns should compute the scroll increment
     *        that will completely expose one new row or column, depending on the value of
     *        orientation. Ideally, components should handle a partially exposed row or column by
     *        returning the distance required to completely expose the item. 
     * 
     *        Scrolling containers, like JScrollPane, will use this method each time the user requests
     *        a unit scroll. 
     * 
     *        Parameters: 
     *               visibleRect - The view area visible within the viewport 
     *               orientation - Either SwingConstants.VERTICAL or
     *               SwingConstants.HORIZONTAL. 
     *               direction - Less than zero to scroll up/left, greater than zero for down/right. 
     *        Returns: 
     *               The "unit" increment for scrolling in the specified direction 
     *        See Also: 
     *               setUnitIncrement 
     * 
     *    getScrollableBlockIncrement */ 

    public  int getScrollableUnitIncrement(Rectangle visibleRect,
    int orientation,
    int direction){
        return 55;
    } 


    /** Components that display logical rows or columns should compute the scroll increment
     *        that will completely expose one block of rows or columns, depending on the value of
     *        orientation. 
     * 
     *        Scrolling containers, like JScrollPane, will use this method each time the user requests
     *        a block scroll. 
     * 
     *        Parameters: 
     *               visibleRect - The view area visible within the viewport 
     *               orientation - Either SwingConstants.VERTICAL or
     *               SwingConstants.HORIZONTAL. 
     *               direction - Less than zero to scroll up/left, greater than zero for down/right. 
     *        Returns: 
     *               The "block" increment for scrolling in the specified direction. 
     *        See Also: 
     *               setBlockIncrement 
     * 
     *    getScrollableTracksViewportWidth */
    public int getScrollableBlockIncrement(Rectangle visibleRect,
    int orientation,
    int direction){
        return 55;					
    }


    /** Return true if a viewport should always force the width of this Scrollable to match the
     *        width of the viewport. For example a noraml text view that supported line wrapping
     *        would return true here, since it would be undesirable for wrapped lines to disappear
     *        beyond the right edge of the viewport. Note that returning true for a Scrollable whose
     *        ancestor is a JScrollPane effectively disables horizontal scrolling. 
     * 
     *        Scrolling containers, like JViewport, will use this method each time they are validated.
     * 
     *        Returns: 
     *               True if a viewport should force the Scrollables width to match its own. 
     * 
     *    getScrollableTracksViewportHeight 
     */
    public boolean getScrollableTracksViewportWidth(){
        return false;
    }

    /**   
     *        Return true if a viewport should always force the height of this Scrollable to match the
     *        height of the viewport. For example a columnar text view that flowed text in left to
     *        right columns could effectively disable vertical scrolling by returning true here. 
     * 
     *        Scrolling containers, like JViewport, will use this method each time they are validated.
     * 
     *        Returns: 
     *               True if a viewport should force the Scrollables height to match its own. 
     * 	      */
    public boolean getScrollableTracksViewportHeight(){
        return false;
    }



}


class Line {
    int x1,y1,x2,y2;
    public Color color;
    int thickness;
    public Line(int x1,int y1, int x2, int y2,Color c, int thick){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.color=c;
        this.thickness = thick;
    }
}
