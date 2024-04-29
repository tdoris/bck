/**
 * //Title       Your Product Name
 * //Version
 * //Copyright    Copyright (c) 1997
 * //Author       Your Name
 * //Company      Your Company
 * //Description  Your description
 */
package BCK.GUI;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


import java.beans.*;


public class bckCreateKohonenDialog extends BCKDialog {

    /**The network we may create with this dialog : */
    BCKKohonenDrawable Net = null; 
    JPanel panel1 = new JPanel();
    JButton btnCancel = new JButton();
    private bckNumberBox inField;
    private bckNumberBox outField;

    public bckCreateKohonenDialog(Frame f) {
        super(f, "Create a Kohonen Neural Network", true);
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


        container.add(inputs, BorderLayout.NORTH);
        container.add(outputs, BorderLayout.CENTER);

        container.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(container);
        pack();
        centerDialog();
        setResizable(false);	
    }

    public JPanel buildInputPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout(FlowLayout.RIGHT) );

        JLabel inLabel = new JLabel("Inputs:            ", JLabel.RIGHT);
        inField = new bckNumberBox();
        inField.requestFocus();
        p.add(inLabel);
        p.add(inField);
        return p;
    }
    public JPanel buildOutputPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout(FlowLayout.RIGHT) );

        JLabel outLabel = new JLabel("Outputs:            ", JLabel.RIGHT);
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
            Net = new BCKKohonenDrawable(i,j);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"ERROR \n"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE); 
            return;
        }
        this.setVisible(false);
    }


    public BCKKohonenDrawable getNet(){
        return Net;
    }


}












