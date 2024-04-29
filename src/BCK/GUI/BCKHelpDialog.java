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

public class BCKHelpDialog extends BCKDialog{

    public BCKHelpDialog(){
        super(bckframe.top,"HELP",true);
        setSize(400,200);
        centerDialog();
        JPanel helpPanel = createHelpPanel();
        getContentPane().add(helpPanel); 

    }
    public JPanel createHelpPanel(){
        JPanel p = new JPanel();
        p.add(new JLabel("The user manual is located at : "+bckframe.home+"Help"));
        return p;
    }


}
