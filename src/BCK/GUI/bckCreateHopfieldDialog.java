
//Title       Your Product Name
//Version

//Description  Your description

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


public class bckCreateHopfieldDialog extends BCKDialog {

    /**The network we may create with this dialog : */
    BCKHopfieldDrawable Net = null; 
    JPanel panel1 = new JPanel();
    JButton btnCancel = new JButton();
    private bckNumberBox inField;

    public bckCreateHopfieldDialog(Frame f) {
        super(f, "Create a Hopfield Neural Network", true);
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
        p.setLayout( new FlowLayout(FlowLayout.RIGHT) );

        JLabel inLabel = new JLabel("Number of Neurons:            ", JLabel.RIGHT);
        inField = new bckNumberBox();
        inField.requestFocus();
        p.add(inLabel);
        p.add(inField);
        return p;
    }

    public void CancelPressed(){
        Net = null;
        this.setVisible(false);
    }

    public void OKPressed(){
        //check the number of neurons field to see that it is valid:
        int i = inField.getInteger();
        if(i<1){
            JOptionPane.showMessageDialog(null,"You must enter a number of neurons >0","Alert",JOptionPane.ERROR_MESSAGE);
            return; 
        }
        //everything ok - create the net and hide
        Net = new BCKHopfieldDrawable(i);
        this.setVisible(false);
    }


    public BCKHopfieldDrawable getNet(){
        return Net;
    }


}






