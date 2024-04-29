
//Title        BCK
//Version
//Copyright    Copyright (c) 1997
//Author       Eoin Whelan
//Company      DCU
//Description  none

package BCK.GUI;

import BCK.ANN.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

public class BCKMap extends BCKDialog{

    bckNumberBox xNeuron;
    bckNumberBox yNeuron;
    bckNumberBox zNeuron;

    double xrangeValue;
    double yrangeValue;

    int xNeuronNumber;
    int yNeuronNumber;
    int zNeuronNumber;


    BCKPlot plotPane;

    JTextField xrange;
    JTextField yrange;

    BCKNeuralNetwork Net;
    /**Constructors*/
    public BCKMap(BCKNeuralNetwork net){
        super(bckframe.top,"2-d Map",true);
        this.Net = net;
        xrangeValue = 1;
        yrangeValue = 1;
        makePane();
        pack();
        centerDialog();
    }

    public void makePane(){  
        JPanel container = new JPanel();
        container.setLayout(new ColumnLayout());
        JPanel neuronSelection = createNeuronSelection();
        JPanel graph = createGraph();
        container.add(neuronSelection);
        container.add(graph);
        JButton done = new JButton("Done");
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);		
            }
        }
        );

        JPanel donePanel = new JPanel();
        donePanel.add(done);
        container.add(donePanel);
        getContentPane().add(container);
    }

    public JPanel createGraph(){
        JPanel p = new JPanel();
        p.setLayout(new ColumnLayout());     

        JPanel graphButtons = new JPanel();
        graphButtons.setLayout(new FlowLayout());

        JButton draw = new JButton("Draw");
        graphButtons.add(draw);
        draw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPressed();		
            }
        }
        );

        JButton zoomIn = new JButton("Zoom In");
        graphButtons.add(zoomIn);
        zoomIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoomInPressed();		
            }
        }
        );

        JButton zoomOut = new JButton("Zoom Out");
        graphButtons.add(zoomOut);
        zoomOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoomOutPressed();		
            }
        }
        );

        p.add(graphButtons);

        JPanel ranges = new JPanel();
        ranges.setLayout(new ColumnLayout());

        JPanel xrangePanel = new JPanel();
        xrangePanel.setLayout(new FlowLayout());
        xrangePanel.add(new JLabel("X range :"));
        xrange = new JTextField(15);
        xrangePanel.add(xrange);
        ranges.add(xrangePanel);

        JPanel yrangePanel = new JPanel();
        yrangePanel.setLayout(new FlowLayout());
        yrangePanel.add(new JLabel("Y range :"));
        yrange = new JTextField(15);
        yrangePanel.add(yrange);
        ranges.add(yrangePanel);

        setRangesText();

        p.add(ranges);
        plotPane = new BCKPlot(bckframe.top,200,200);
        plotPane.setBackground(Color.white);
        //     graphPane.setSize(400,100);
        p.add(plotPane);
        return p;
    }

    public JPanel createNeuronSelection(){
        JPanel p = new JPanel();
        p.setLayout(new ColumnLayout());     
        JPanel xnode = new JPanel();
        xnode.setLayout(new FlowLayout());
        xnode.add(new JLabel("Input X axis node : "));
        xNeuron = new bckNumberBox();
        xnode.add(xNeuron);
        p.add(xnode);

        JPanel ynode = new JPanel();
        ynode.setLayout(new FlowLayout());
        ynode.add(new JLabel("Input Y axis node : "));
        yNeuron = new bckNumberBox();
        ynode.add(yNeuron);
        p.add(ynode);

        JPanel znode = new JPanel();
        znode.setLayout(new FlowLayout());
        znode.add(new JLabel("Output Z axis node :"));
        zNeuron = new bckNumberBox();
        znode.add(zNeuron);
        p.add(znode);   
        return p;
    }

    public void CancelPressed(){
        this.setVisible(false);
    }

    public void setRangesText(){
        yrange.setText("-"+yrangeValue+" to +"+yrangeValue);
        xrange.setText("-"+xrangeValue+" to +"+xrangeValue);
    }

    public boolean setNeuronNumbers(){
        xNeuronNumber = xNeuron.getInteger();
        yNeuronNumber = yNeuron.getInteger();
        zNeuronNumber = zNeuron.getInteger();
        if(zNeuronNumber < 0 || zNeuronNumber >= Net.getNumberOfNeurons()){
            JOptionPane.showMessageDialog(null,"Please enter a valid neuron number for Z neuron","Alert",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(yNeuronNumber < 0 || yNeuronNumber >= Net.getNumberOfNeurons()){
            JOptionPane.showMessageDialog(null,"Please enter a valid neuron number for Y neuron","Alert",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(xNeuronNumber < 0 || xNeuronNumber >= Net.getNumberOfNeurons()){
            JOptionPane.showMessageDialog(null,"Please enter a valid neuron number for X neuron","Alert",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void drawPressed(){
        Color c = Color.white;
        double xVal;
        double yVal;
        double zVal;
        if( !(setNeuronNumbers()) ){
            return;
        }    
        for(int x = 0;x<plotPane.width;x+=2){
            for(int y = 0;y<plotPane.height;y+=2){
                //determine the color of this point:
                //	setAllZero();
                //set the x neuron to the current x point
                xVal = 2.0*xrangeValue*((x-plotPane.width/2.0)/plotPane.width);	
                Net.setOutput(xNeuronNumber,xVal);	
                //set the x neuron to the current x point
                yVal = 2.0*yrangeValue*((y-plotPane.height/2.0)/plotPane.height);
                Net.setOutput(yNeuronNumber,yVal);
                //feed the values through the net:
                forwardPass();
                //get the z value:
                zVal=Net.getState(zNeuronNumber);
                //squash z val into allowable range for RGB values
                zVal = (1.0/(1.0+Math.exp(-zVal)));	
                //use the z value to determine the color:
                c= new Color((float)zVal,(float)zVal,(float)zVal);
                plotPane.new2by2(new Point(x,y), c);
            }
        }
        plotPane.crossHair();
    }

    /**feed the inputs through the network */
    public void forwardPass(){
        try{
            int start = Math.max(yNeuronNumber,xNeuronNumber)+1;
            for(int i=start;i<Net.getNumberOfNeurons();i++){
                Net.calcState(i);
            }
        }
        catch(Exception e){ 
            JOptionPane.showMessageDialog(bckframe.top,"Error "+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Set all outputs in the current network to zero*/
    public void setAllZero(){
        int numNeurons = Net.getNumberOfNeurons();
        for(int i=0;i<numNeurons;i++)
            Net.setOutput(i,0.0);
    }
    public void zoomInPressed(){
        //multiply the ranges by .5  
        xrangeValue*=0.5;
        yrangeValue*=0.5;
        setRangesText();
    }

    public void zoomOutPressed(){
        xrangeValue*=2;
        yrangeValue*=2;
        setRangesText();
    }


}









