
package BCK.GUI;

import java.awt.*;
import java.awt.event.*;

import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;
import com.sun.java.swing.AbstractButton;

import java.beans.*;
import com.sun.java.swing.plaf.metal.*;


public class bckCreateRBFDialog extends BCKDialog {

    /**The network we may create with this dialog : */
    BCKRBFDrawable Net = null; 
    JPanel panel1 = new JPanel();
    JButton btnCancel = new JButton();
    private bckNumberBox inField;
    private bckNumberBox outField;

    public bckCreateRBFDialog(Frame f) {
        super(f, "Create an RBF Neural Network", true);
        setSize(300,80);     
        JPanel container = new JPanel();

        container.setLayout( new BorderLayout() );
        JPanel inputs = buildInputPanel();
        JPanel outputs = buildOutputPanel();

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

        container.add(inputs,BorderLayout.NORTH);
        container.add(outputs,BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(container);
        pack();
        centerDialog();
        setResizable(false);	
    }


    public JPanel buildInputPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout(FlowLayout.RIGHT) );

        JLabel inLabel = new JLabel("Inputs:              ", JLabel.RIGHT);
        inField = new bckNumberBox();
        inField.requestFocus();
        p.add(inLabel);
        p.add(inField);
        return p;
    }
    public JPanel buildOutputPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout(FlowLayout.RIGHT) );

        JLabel outLabel = new JLabel("Outputs:             ", JLabel.RIGHT);
        outField = new bckNumberBox();
        p.add(outLabel);
        p.add(outField);
        return p;
    }


    public void CancelPressed(){
        Net = null;    
        this.setVisible(false);
    }

    public void OKPressed(){
        //check the number of neurons field to see that it is valid:
        int i = inField.getInteger();
        int j = outField.getInteger();
        if(i<1){
            JOptionPane.showMessageDialog(null,"You must enter a number of inputs >0","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }   
        if(j<1){
            JOptionPane.showMessageDialog(null,"You must enter a number of outputs >0","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }
        //everything ok - create the net and hide
        try{
            Net = new BCKRBFDrawable(i,j);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error Creating RBF : "+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE); 
            ;
        }
        this.setVisible(false);
    }


    public BCKRBFDrawable getNet(){
        return Net;
    }


}












