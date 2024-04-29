
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
public class BCKNetConnectTable extends JPanel {
    JTable      tableView;
    JScrollPane scrollpane;
    Dimension   origin = new Dimension(0, 0);

    JPanel      mainPanel;
    JPanel      controlPanel;
    JScrollPane tableAggregate;

    BCKNeuralNetwork targetNet;
    BCKNeuralNetwork sourceNet;

    int sourceNetId;
    int targetNetId;

    int outputs;
    int firstOutput;
    public BCKNetConnectTable(BCKNeuralNetwork net, int srcNet) {
        super();
        this.sourceNetId=srcNet;
        this.targetNetId=net.getId();

        sourceNet = bckframe.top.getNetAt(srcNet);
        if(sourceNetId != 0){
            this.outputs=sourceNet.getNumOutputs();
            this.firstOutput = sourceNet.getNumberOfNeurons() - outputs;
        } 
        else {//source network is brain net
            this.outputs=sourceNet.getNumInputs();
            this.firstOutput = 0;
        }
        setLayout(new BorderLayout());
        mainPanel = this;       
        // Create the table.
        tableAggregate = createTable();
        tableAggregate.setPreferredSize(new Dimension(400,100));
        mainPanel.add(tableAggregate, BorderLayout.CENTER);
    }

    public JScrollPane createTable() {     
        final String[] names = {
            "Source Name","Source ID","Target ID","Weight","Delay"        };
        Vector inputs = (Vector)bckframe.top.theBrain.proxyInputs.elementAt(targetNetId);
        BCKNetConnection nc;   
        String[][] stringData;
        String[] neuronNames = new String[outputs];

        for(int i=firstOutput;i<firstOutput+outputs;i++){
            neuronNames[i-firstOutput] = ((BCKNeuron)sourceNet.getNeuron(i)).getName();
        }	

        //get the data for the table
        stringData = new String[outputs][names.length];
        for(int i=0;i<outputs;i++){	  
            BCKNeuron n = ((BCKNeuron)sourceNet.getNeuron(firstOutput+i));
            stringData[i][0] = ""+n.getName(); //op neuron name
            stringData[i][1] = ""+n.id;        //op neuron id
            stringData[i][2] = "-1"; //target node id
            stringData[i][3] = "1";               //weight
            stringData[i][4] = "0";               //delay
            //check for an existing link to this node:
            for(int j=0;j<inputs.size();j++){	      
                nc=((BCKNetConnection)inputs.elementAt(j));        
                if(nc.sourceNet == sourceNetId){
                    if(nc.sourceNeuron == n.id){
                        stringData[i][2] = ""+nc.targetNeuron; //target node id
                        stringData[i][3] = ""+nc.weight;               //weight
                        stringData[i][4] = ""+nc.delay;               //delay
                    }
                }
            }   

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

