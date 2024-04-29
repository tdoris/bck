
package BCK.GUI;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;
import com.sun.java.swing.AbstractButton;

import java.beans.*;
import com.sun.java.swing.plaf.metal.*;


public class ColumnLayout implements LayoutManager, Serializable{

    int xInset = 5;
    int yInset = 5;
    int yGap = 2;

    public void addLayoutComponent(String s, Component c) {
    }

    public void layoutContainer(Container c) {
        Insets insets = c.getInsets();
        int height = yInset + insets.top;

        Component[] children = c.getComponents();
        Dimension compSize = null;
        for (int i = 0; i < children.length; i++) {
            compSize = children[i].getPreferredSize();
            children[i].setSize(compSize.width, compSize.height);
            children[i].setLocation( xInset + insets.left, height);
            height += compSize.height + yGap;
        }

    }

    public Dimension minimumLayoutSize(Container c) {
        Insets insets = c.getInsets();
        int height = yInset + insets.top;
        int width = 0 + insets.left + insets.right;

        Component[] children = c.getComponents();
        Dimension compSize = null;
        for (int i = 0; i < children.length; i++) {
            compSize = children[i].getPreferredSize();
            height += compSize.height + yGap;
            width = Math.max(width, compSize.width + insets.left + insets.right + xInset*2);
        }
        height += insets.bottom;
        return new Dimension( width, height);
    }

    public Dimension preferredLayoutSize(Container c) {
        return minimumLayoutSize(c);
    }

    public void removeLayoutComponent(Component c) {
    }

}
