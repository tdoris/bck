
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



public class bckNumberBox extends JTextField implements KeyListener{

    public bckNumberBox() {
        super(10);
        setBackground(Color.white);
        setToolTipText("Enter an Integer here");
        addKeyListener(this);
    }
    public void keyPressed(KeyEvent e)
    {
        if (!((Character.isDigit ( e.getKeyChar() )|| Character.isISOControl(e.getKeyChar()) ) ))
        {
            e.consume();
        }

    }
    public void keyReleased(KeyEvent e)
    {

    }

    public void keyTyped(KeyEvent e)
    {
        if (!((Character.isDigit ( e.getKeyChar() )|| Character.isISOControl(e.getKeyChar()) ) ))
        {
            e.consume();
        }
    }

    /** Return whether the contents is a non-null number */
    public boolean isInteger(){
        Integer I;
        String text = getText();

        if(text==null) return false;

        try{
            I = Integer.valueOf(text);
        }
        catch(NumberFormatException e){
            return false;
        }

        return true;
    }
    /**return the integer in the box, -1 if no integer available*/
    public int getInteger(){
        if(isInteger()){
            Integer I;
            String text = getText();
            I = Integer.valueOf(text);
            int i = I.intValue();
            return i;
        } 
        return -1;
    }


}
