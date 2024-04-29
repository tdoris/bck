

package BCK.GUI;
import javax.swing.*;

import BCK.ANN.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class BCKNeuronPopup extends JPopupMenu {
    JMenuItem connect = new JMenuItem("Connect to");
    JMenuItem details = new JMenuItem("Show Details");


    public BCKNeuronPopup(){
        add(connect);
        add(details);
    }


}
