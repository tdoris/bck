

package BCK.GUI;

import BCK.ANN.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

public class BCKPopup extends JPopupMenu {
    JMenuItem edit = new JMenuItem("Edit...");
    JMenuItem train = new JMenuItem("Train...");
    JMenuItem cut = new JMenuItem("cut");
    JMenuItem classify =new JMenuItem("Classify");
    JMenuItem map =new JMenuItem("2-d map");
    JMenuItem connect =new JMenuItem("Connect");

    public BCKPopup(){
        add(edit);
        add(train);
        add(classify);
        add(map);
        add(new JSeparator());
        add(connect);
        add(new JSeparator());
        add(cut);
    }


}
