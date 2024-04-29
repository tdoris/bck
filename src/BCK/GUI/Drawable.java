
package BCK.GUI;
import java.awt.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;
/** This interface specifies the methods required for Drawable objects, ie Drawable networks, so that they can be used and manipulated by the rest of the GUI code
 *@author Eoin Whelan
 */
public interface Drawable{

    public BCKImageControl Draw();
    public boolean isSelected();
    public void setSelected(boolean select);
    public void setVisible(boolean visiblity);
    public BCKImageControl getImage();
    public void trainPressed();
    public void editPressed();
    public void classifyPressed();
    public void cutPressed();
}
