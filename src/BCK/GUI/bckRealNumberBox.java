
//Title:        Your Product Name
//Version:      
//Copyright:    Copyright (c) 1997
//Author:       Your Name
//Company:      Your Company
//Description:  Your description


package BCK.GUI;
import javax.swing.*;

import java.awt.*;
import java.lang.Character;
import java.awt.event.*;



public class bckRealNumberBox extends JTextField implements KeyListener{

    public bckRealNumberBox() {
        super(10);
        setBackground(Color.white);
        setToolTipText("Enter a Number here");
        addKeyListener(this);
    }
    public void keyPressed(KeyEvent e)
    { 
        char c = e.getKeyChar();
        if (!(Character.isDigit(c)|| Character.isISOControl(c) || c == '.' || c=='-' ))
        {
            e.consume();
        }

    }
    public void keyReleased(KeyEvent e)
    {

    }

    public void keyTyped(KeyEvent e)
    {
        char c = e.getKeyChar();
        if (!(Character.isDigit(c)|| Character.isISOControl(c) || c == '.'|| c=='-'   ))
        {
            e.consume();
        }
    }

    /** Return whether the contents is a non-null number */
    public boolean isRealNumber(){
        Double D;
        String text = getText();

        if(text==null) return false;

        try{
            D = Double.valueOf(text);
        }
        catch(NumberFormatException e){
            return false;
        }

        return true;
    }
    /**return the Number in the box, returns NaN if no number available*/
    public double getRealNumber(){
        if(isRealNumber()){
            Double D;
            String text = getText();
            D = Double.valueOf(text);
            double i = D.doubleValue();
            return i;
        } 
        return Double.NaN;
    }


}
