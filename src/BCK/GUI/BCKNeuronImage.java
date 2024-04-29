

package BCK.GUI;

import BCK.ANN.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

/** This is the graphical object which represents a neuron, it contains a reference to the neuron which it represents and a graphic image object which te user sees in the BCKNetworkEditor.
 *@author Eoin Whelan
 *@author Tom Doris
 */
public class BCKNeuronImage extends BCKImageControl implements MouseListener, ActionListener {


    BCKNetworkEditor parentObject;
    int id;
    BCKNeuron neuron;  //the neuron that this image represents
    BCKNeuralNetwork Net;
    BCKNeuronPopup popup;
    BCKConnectNeurons connect;
    BCKNeuronDetails details;

    static ImageIcon NeuronImageIcon = BCKImageControl.loadImageIcon(bckframe.home+"images/NEURON.jpg","This is a Neuron");

    public BCKNeuronImage(int i, BCKNetworkEditor cont, BCKNeuron n, BCKNeuralNetwork net){
        super(NeuronImageIcon);
        parentObject = cont;
        id=i;
        neuron=n;
        Net=net;
        addMouseListener(this);
        popup= new BCKNeuronPopup();
        popup.connect.addActionListener(this);
        popup.details.addActionListener(this);
        this.setLayout(new FlowLayout());
        JLabel label = new JLabel((new Integer(id)).toString());
        label.setOpaque(false);
        this.add(label);
        this.setSize(50,50);
    }

    public Point getCenter(){
        return new Point(getLocation().x+getSize().width/2, getLocation().y+getSize().height/2);
    }

    /**Update the position of a neuron in response to a drag event */
    public void mouseDragged(MouseEvent e){
        if (e.getY() + getLocation().y-getSize().width/2 > maxHeight)
        {
            setLocation(e.getX() + getLocation().x-getSize().width/2,e.getY() + getLocation().y-getSize().height/2);
        }
        else  {
            setLocation(e.getX() + getLocation().x-getSize().width/2,maxHeight);
        } 
        parentObject.drawConnections();
        parentObject.repaint();
    }

    public void mouseMoved(MouseEvent e){
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == popup.details){
            details = new BCKNeuronDetails(id, Net);
            details.setVisible(true);
        }
        //connect this neuron to another one
        if(e.getSource() == popup.connect){
            connect=new BCKConnectNeurons(bckframe.top,id,Net);
            connect.setVisible(true);      
            try{
                Net.connectInternal(connect.preNeuron,connect.postNeuron,connect.weight,connect.delay);     
            } 
            catch(Exception ex){
                JOptionPane.showMessageDialog(null,ex.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            }
        }
        parentObject.drawConnections();
        parentObject.repaint();
    }

    public void mouseClicked(MouseEvent me) {    
        if(me.isAltDown()){
            if(me.getSource() == this){    
                popup.show(this,me.getX(),me.getY());
            }       
        } 
    }

    public void mouseEntered(MouseEvent me)  {  
    }
    public void mouseExited(MouseEvent me)  {  
    }
    public void mousePressed(MouseEvent me)  {  
    }
    public void mouseReleased(MouseEvent me) {  
    }
}

