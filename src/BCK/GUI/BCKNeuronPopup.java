

package BCK.GUI;

import BCK.ANN.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

public class BCKNeuronPopup extends JPopupMenu {
    JMenuItem connect = new JMenuItem("Connect to");
    JMenuItem details = new JMenuItem("Show Details");


    public BCKNeuronPopup(){
        add(connect);
        add(details);
    }


}
