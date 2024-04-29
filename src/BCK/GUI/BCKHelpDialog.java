//Title       Your Product Name
//Version

//Description  Your description

package BCK.GUI;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


import java.beans.*;

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
