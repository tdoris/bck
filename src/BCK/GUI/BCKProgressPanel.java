package BCK.GUI;
import javax.swing.*;

import javax.accessibility.*;;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * Progress Bar Panel
 *
 * @author Eoin Whelan
 * @author Tom Doris
 */
public class BCKProgressPanel extends JPanel 
{
    JProgressBar progressBar;                     

    public BCKProgressPanel() {         
        setLayout(new BorderLayout());
        JPanel progressPanel = new JPanel();
        add(progressPanel, BorderLayout.SOUTH);
        progressBar = new JProgressBar() {
            public Dimension getPreferredSize() {
                return new Dimension(300, super.getPreferredSize().height);
            }
        };
        progressBar.getAccessibleContext().setAccessibleName("Text loading progress");
        progressPanel.add(progressBar);      
        progressBar.setValue(0);
        progressBar.setMinimum(0);	    
    }

    public Insets getInsets() {
        return new Insets(10,10,10,10);
    }

    public void setMaximum(int max){
        progressBar.setMaximum(max);
    }   

    public void setValue(int val){
        progressBar.setValue(val);
    }   

}
