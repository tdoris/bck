package BCK.GUI;
import javax.swing.*;

import BCK.ANN.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class BCKSetNetName extends BCKDialog{

    private JTextField n;
    private boolean OK = false;
    private String curName;

    public BCKSetNetName(String curName){
        super(bckframe.top, "Set Network Name", true);
        this.curName = curName;
        getContentPane().setLayout(new ColumnLayout());
        getContentPane().add(createNamePanel());
        getContentPane().add(createButtonPanel());
        repaint();
        pack();
        centerDialog();
    }

    private JPanel createNamePanel(){
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        JLabel l = new JLabel("Enter name :");
        n = new JTextField(15);
        n.setText(curName);
        p.add(l);
        p.add(n);
        return p;
    }

    private JPanel createButtonPanel(){
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OK = true;
                setVisible(false);
            }
        }
        );

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OK = false;
                setVisible(false);
            }
        }
        );
        p.add(ok);
        p.add(cancel);
        return p;
    }

    public String getName(){
        return n.getText();
    }

    public boolean isOk(){
        return OK;
    }

}

