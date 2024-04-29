
package BCK.GUI;
import javax.swing.*;
import javax.swing.table.*;

import BCK.ANN.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/* Implements a table displaying the connections incident upon a given neuron in a given Network.
 * @author Tom Doris
 * @author Eoin Whelan
 */
public class BCKNeuronTablePanel extends JPanel {
    JTable      tableView;
    JScrollPane scrollpane;
    Dimension   origin = new Dimension(0, 0);

    JCheckBox   isColumnReorderingAllowedCheckBox;
    JCheckBox   showHorizontalLinesCheckBox;
    JCheckBox   showVerticalLinesCheckBox;

    JCheckBox   isColumnSelectionAllowedCheckBox;
    JCheckBox   isRowSelectionAllowedCheckBox;
    JCheckBox   isRowAndColumnSelectionAllowedCheckBox;

    JLabel      interCellSpacingLabel;
    JLabel      rowHeightLabel;

    JSlider     interCellSpacingSlider; 
    JSlider     rowHeightSlider;

    JComponent  selectionModeButtons;
    JComponent  resizeModeButtons;

    JPanel      mainPanel;
    JPanel      controlPanel;
    JScrollPane tableAggregate;

    public BCKNeuronTablePanel(int neuronId, BCKNeuralNetwork net) {
        super();

        setLayout(new BorderLayout());
        mainPanel = this;

        // Create the table.
        tableAggregate = createTable(neuronId, net);
        tableAggregate.setPreferredSize(new Dimension(400,100));
        mainPanel.add(tableAggregate, BorderLayout.CENTER);
    }

    public JScrollPane createTable(int nid, BCKNeuralNetwork net) {      
        // final
        final String[] names = {
            "Neuron Id", "Weight","Delay"        };               
        String[][] stringData;
        //get the data for the neuron

        BCKSynapse[] synapseArray = net.getNeuron(nid).getSynapseArray();      

        stringData = new String[synapseArray.length][names.length];
        for(int i=0;i<synapseArray.length;i++){	   
            stringData[i][0] = ""+synapseArray[i].output.id;
            stringData[i][1] = ""+synapseArray[i].weight;
            stringData[i][2] = ""+synapseArray[i].delay;
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

