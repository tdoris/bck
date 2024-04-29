
package BCK.GUI;
import BCK.ANN.*;
import com.sun.java.swing.*;
import com.sun.java.swing.table.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/* Implements a table displaying the connections incident upon a given neuron in a given Network.
 * @author Tom Doris
 * @author Eoin Whelan
 */
public class BCKOutputTable extends JPanel {
    JTable      tableView;
    JScrollPane scrollpane;
    Dimension   origin = new Dimension(0, 0);

    JPanel      mainPanel;
    JPanel      controlPanel;
    JScrollPane tableAggregate;

    int outputs;
    int firstOutput;
    public BCKOutputTable(BCKNeuralNetwork net) {
        super();
        this.outputs=net.getNumOutputs();
        this.firstOutput = net.getNumberOfNeurons() - outputs;
        setLayout(new BorderLayout());
        mainPanel = this;       
        // Create the table.
        tableAggregate = createTable(net);
        tableAggregate.setPreferredSize(new Dimension(400,100));
        mainPanel.add(tableAggregate, BorderLayout.CENTER);
    }

    public JScrollPane createTable(BCKNeuralNetwork net) {

        String[] neuronNames = new String[outputs];
        for(int i=firstOutput;i<net.getNumberOfNeurons();i++){
            neuronNames[i-firstOutput] = ((BCKNeuron)net.getNeuron(i)).getName();
        }	
        // final
        final String[] names = {
            "Neuron Name","  ID  "," Output "        };

        String[][] stringData;
        //get the data for the neuron

        stringData = new String[outputs][names.length];
        for(int i=0;i<outputs;i++){	  
            BCKNeuron n = ((BCKNeuron)net.getNeuron(firstOutput+i));
            stringData[i][0] = ""+n.getName();
            stringData[i][1] = ""+n.id;
            stringData[i][2] = ""+n.getState(0);
        }

        final Object[][] data = stringData;
        // Create a model of the data.
        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() { 
                return names.length; 
            }
            public int getRowCount() { 
                return data.length;
            }
            public Object getValueAt(int row, int col) {
                return data[row][col];
            }
            public String getColumnName(int column) {
                return names[column];
            }
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
            public boolean isCellEditable(int row, int col) {
                return getColumnClass(col) == String.class;
            } 
            public void setValueAt(Object aValue, int row, int column) { 
                data[row][column] = aValue; 
            }
        };


        // Create the table
        tableView = new JTable(dataModel);

        // Show colors by rendering them in their own color. 
        DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) { 
                if (value instanceof Color) {
                    Color c = (Color)value; 
                    setForeground(c);
                    setText(c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
                }
            } 

        }; 

        colorRenderer.setHorizontalAlignment(JLabel.RIGHT); 
        //        tableView.getColumn("Favorite Color").setCellRenderer(colorRenderer); 

        tableView.setRowHeight(20);

        scrollpane = JTable.createScrollPaneForTable(tableView);
        return scrollpane;
    }
}

