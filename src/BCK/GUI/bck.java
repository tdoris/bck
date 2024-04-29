

package BCK.GUI;
import javax.swing.*;


/** This is the top level class, it creates the main frame 
 */
public class bck {

    /**Construct the application*/
    public bck() {
        bckframe frame = new bckframe();    
        frame.setVisible(true);
    }

    /**Main method*/
    static public void main(String[] args) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){
        }
        new bck();
    }
}

