
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


public class bckCreateMLPDialog extends JDialog {

    JPanel panel1 = new JPanel();

    BCKNeuralNetwork Net = null;   
    public JButton btnOK;
    JButton btnCancel = new JButton();
    private JComboBox algorithms;
    private JPanel algorithm;
    private JPanel hiddens;
    private bckNumberBox inField;
    private bckNumberBox outField;
    private bckNumberBox hidField;
    private bckNumberBox hiddenLayer1Neurons;
    private bckNumberBox[] hiddenLayerNeurons;  

    public bckCreateMLPDialog(Frame f) {
        super(f, "Create an MLP Neural Network", true);
        JPanel container = new JPanel();
        container.setLayout( new ColumnLayout() );

        algorithm = buildAlgorithmPanel();
        JPanel inputs = buildInputPanel();
        JPanel hidden1 = new JPanel();
        hidden1.setLayout( new ColumnLayout());

        hiddens = new JPanel();
        hiddens.setLayout(new ColumnLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel lab = new JLabel("Neurons in hidden layer 1",JLabel.RIGHT);
        hiddenLayer1Neurons = new bckNumberBox();    
        panel.add(lab);
        panel.add(hiddenLayer1Neurons);
        hidden1.add(panel);

        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout ( new FlowLayout(FlowLayout.RIGHT) );

        btnOK = new JButton("OK");

        buttonPanel.add( btnOK );
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKPressed();
            }
        }
        );
        getRootPane().setDefaultButton(btnOK);
        btnOK.setToolTipText("This is the OK button");

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancelPressed();
            }
        }
        );
        buttonPanel.add( cancel );

        container.add(algorithm);
        container.add(inputs);
        container.add(hidden1);
        JScrollPane scroll = new JScrollPane(hiddens);
        scroll.setPreferredSize(new Dimension(400,200));

        container.add(scroll);
        container.add(buttonPanel);


        getContentPane().add(container);

        pack();
        centerDialog();
        setSize(400,200);            
        setResizable(false);	
        pack();
    }


    public JPanel buildAlgorithmPanel(){
        JPanel p = new JPanel();     
        p.setLayout( new FlowLayout());
        JLabel alLabel = new JLabel("Algorithm:", JLabel.RIGHT);         	
        algorithms = new JComboBox();
        algorithms.addItem("Quickprop");     
        algorithms.addItem("Backprop"); 
        algorithms.setPreferredSize(new Dimension(120,20));
        p.add(alLabel);
        p.add(algorithms);

        return p;
    }


    public JPanel buildInputPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new GridLayout(3,2,5,5) );

        JLabel inLabel = new JLabel("Number of Input Neurons:", JLabel.RIGHT);
        inField = new bckNumberBox();
        inField.requestFocus();
        p.add(inLabel);
        p.add(inField);


        JLabel outLabel = new JLabel("Number of Output Neurons:", JLabel.RIGHT);
        outField = new bckNumberBox();       
        p.add(outLabel);
        p.add(outField);

        JLabel hidLabel = new JLabel("Number of Hidden Layers:", JLabel.RIGHT);
        hidField = new bckNumberBox();
        hidField.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {	       
                CreateHiddenArray();
            }
            public void focusGained(FocusEvent e) {
            }
        }
        );
        p.add(hidLabel);
        p.add(hidField);

        return p;
    }

    public void CancelPressed(){
        Net = null;
        this.setVisible(false);
    }

    public void OKPressed(){
        //check the number of neurons field to see that it is valid:
        int ins = inField.getInteger();
        if(ins<1){
            JOptionPane.showMessageDialog(null,"You must enter a number of inputs >0","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }

        int outs = outField.getInteger();
        if(outs<1){
            JOptionPane.showMessageDialog(null,"You must enter a number of outputs >0","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }

        int hidlayers = hidField.getInteger();
        if(hidlayers<1){
            JOptionPane.showMessageDialog(null,"You must enter a number of hiden layers >0","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }

        int hid1  = hiddenLayer1Neurons.getInteger();
        if(hid1<1){
            JOptionPane.showMessageDialog(null,"You must enter a number of neurons >0 in hidden layer 1","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }

        for(int i=0;i<hiddenLayerNeurons.length;i++){
            int hiddenNeurons = hiddenLayerNeurons[i].getInteger();
            int pq = i+2;
            if (hiddenNeurons<1){
                JOptionPane.showMessageDialog(null,"You must enter a number of neurons >0 in hidden layer "+pq,"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        int[] Neurons = new int[3+hiddenLayerNeurons.length];
        Neurons[0] = ins;
        Neurons[1] = hid1;
        for(int i=0;i<hiddenLayerNeurons.length;i++)
            Neurons[2+i] = hiddenLayerNeurons[i].getInteger();

        Neurons[2+hiddenLayerNeurons.length] = outs;

        //everything ok - create the net and hide
        int a = algorithms.getSelectedIndex();
        if(a==0){
            Net = new BCKMLPQpropDrawable(Neurons);      
        } 
        else  if (a==1){
            Net = new BCKMLPBpropDrawable(Neurons);
        } 
        else {
            return;
        }
        this.setVisible(false);    
    }

    public void CreateHiddenArray(){
        int n = hidField.getInteger();
        if(n<1){
            JOptionPane.showMessageDialog(bckframe.top,"You must enter an integer>=1","Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        hiddenLayerNeurons = new bckNumberBox[n-1];
        n--; //already have box for 1 hidden layer
        hiddens.removeAll();
        //create a panel for each hidden layer
        for(int p=0;p<n;p++){
            int layer = p+2;
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());
            JLabel lab = new JLabel("Neurons in hidden layer "+layer,JLabel.RIGHT);
            hiddenLayerNeurons[p] = new bckNumberBox();       
            panel.add(lab);
            panel.add(hiddenLayerNeurons[p]);
            hiddens.add(panel);
        }
        pack();
        repaint();
    }
    public BCKNeuralNetwork getNet(){
        return Net;
    }
    protected void centerDialog() {
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

}


