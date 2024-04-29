
package BCK.GUI;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import BCK.ANN.*;



import java.beans.*;


public class BCKConnectNeurons extends BCKDialog {

    JPanel panel1 = new JPanel();
    JButton btnCancel = new JButton();
    private bckNumberBox neuronidBox;
    private bckRealNumberBox weightBox;
    private bckNumberBox delayBox;
    private BCKNeuralNetwork Net;
    int postNeuron;
    int preNeuron;
    double weight;
    int delay;

    public BCKConnectNeurons(Frame f, int postNeuron, BCKNeuralNetwork net) {
        super(f, "Connect "+postNeuron, true);
        Net=net;
        this.postNeuron = postNeuron;
        setSize(300,80);     
        JPanel container = new JPanel();
        container.setLayout( new BorderLayout() );
        JPanel inputs = buildInputPanel();

        JPanel buttonPanel = new JPanel();


        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKPressed();
            }
        }
        );
        buttonPanel.add( btnOK );
        getRootPane().setDefaultButton(btnOK);
        btnOK.setToolTipText("This is the OK button");

        buttonPanel.setLayout ( new FlowLayout(FlowLayout.RIGHT) );
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancelPressed();
            }
        }
        );
        buttonPanel.add( cancel );


        container.add(inputs, BorderLayout.CENTER);

        container.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(container);
        pack();
        centerDialog();
        setResizable(false);	
    }

    public JPanel buildInputPanel(){
        JPanel p = new JPanel();
        p.setLayout( new ColumnLayout());

        neuronidBox = new bckNumberBox();
        weightBox = new bckRealNumberBox();
        delayBox = new bckNumberBox();
        JPanel neuronPanel = new JPanel();
        neuronPanel.setLayout(new FlowLayout());
        neuronPanel.add(new JLabel("Neuron : "));
        neuronPanel.add(neuronidBox);

        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new FlowLayout());
        weightPanel.add(new JLabel("Weight : "));
        weightPanel.add(weightBox);

        JPanel delayPanel = new JPanel();
        delayPanel.setLayout(new FlowLayout());
        delayPanel.add(new JLabel("Delay : "));
        delayPanel.add(delayBox);

        p.add(neuronPanel);
        p.add(weightPanel);
        p.add(delayPanel);
        return p;
    }

    public void CancelPressed(){
        this.setVisible(false);
    }

    public void OKPressed(){
        //check the number of neurons field to see that it is valid:    
        if(neuronidBox.getInteger()<0 ||neuronidBox.getInteger()>Net.getNumberOfNeurons() ){
            JOptionPane.showMessageDialog(null,"You must enter a valid of neuron Id number.","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }
        if(Double.isNaN(weightBox.getRealNumber())){
            JOptionPane.showMessageDialog(null,"You must enter a valid weight.","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }
        if(delayBox.getInteger() < 0 || delayBox.getInteger() > 9){
            JOptionPane.showMessageDialog(null,"You must enter a valid delay (0 to 9).","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }

        this.preNeuron =neuronidBox.getInteger() ;
        this.weight=weightBox.getRealNumber();
        this.delay = delayBox.getInteger();
        //everything ok - hide now
        this.setVisible(false);
    }

}






