
package BCK.GUI;

import BCK.ANN.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;


public class BCKNeuronDetails extends BCKDialog {

    JTextField NeuronName = new JTextField(25);
    JButton OK = new JButton("  OK  ");
    BCKNeuralNetwork Net;
    int neuronId; 
    JPanel table;
    JPanel addremove;
    JPanel buttons;
    JPanel container;
    BCKPlot weightPlot = new BCKPlot(bckframe.top,200,200);
    bckNumberBox matrixWidth = new bckNumberBox();

    public BCKNeuronDetails(int nodeId, BCKNeuralNetwork net){
        super(bckframe.top,"Details of Neuron "+nodeId,true);
        neuronId=nodeId;
        this.Net = net;
        container = new JPanel();
        container.setLayout( new ColumnLayout());
        JPanel namePanel = createNamePanel();
        createInputsPanel();

        addremove = new JPanel();  

        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removePressed();
            }
        }
        );  
        addremove.add(remove);

        buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKPressed();
            }
        }
        );

        JButton Cancel = new JButton("Cancel");
        buttons.add(OK);
        buttons.add(Cancel);
        Cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancelPressed();
            }
        }
        );

        JPanel plotPanel = createPlotPanel();		       
        container.add(namePanel);
        container.add(plotPanel);
        container.add(table);
        container.add(addremove);
        container.add(buttons);
        this.getContentPane().add(container);    
        this.pack(); 
        centerDialog();

    }

    public JPanel createPlotPanel(){
        JPanel p = new JPanel();
        p.setLayout(new ColumnLayout());
        JPanel widthPanel = new JPanel();
        widthPanel.add(new JLabel("Width of Matrix:"));
        widthPanel.add(matrixWidth);
        JButton draw = new JButton("Draw");
        draw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BCKNeuron node = ((BCKNeuron)Net.getNeuron(neuronId));
                //get the width
                int w= matrixWidth.getInteger();
                if(w<1){
                    JOptionPane.showMessageDialog(null,"Illegal value for width","Alert",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double[] pixels = node.getWeights();
                double min= 1000;
                for(int i = 0; i < pixels.length;i++){
                    if(min>pixels[i]) min=pixels[i];
                }
                if(min<0){ 
                    min = -min;
                    for(int i = 0; i < pixels.length;i++){
                        pixels[i]+=min;
                    }
                }
                weightPlot.drawPicture(pixels,w,5);
            }
        }
        );     
        widthPanel.add(draw);
        p.add(widthPanel);
        p.add(weightPlot);
        return p;
    }

    public JPanel createNamePanel(){
        JPanel container = new JPanel();
        container.setLayout(new ColumnLayout());    
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());        
        p.add(new JLabel("Name : "));    
        NeuronName.setText(Net.getNeuronName(neuronId)); 
        p.add(NeuronName);   

        JPanel t = new JPanel();
        t.add(new JLabel("Type : "));
        JTextField type = new JTextField(20);
        type.setText(((BCKNeuron)Net.getNeuron(neuronId)).getType());
        type.setEditable(false);

        t.add(type);
        container.add(p);
        container.add(t);
        container.add(new JLabel("Connections Details : "));
        return container;    
    }

    public JPanel createInputsPanel(){
        table = new BCKNeuronTablePanel(neuronId,Net);
        return table;
    }

    public void removePressed(){
        BCKNeuron n = ((BCKNeuron)Net.getNeuron(neuronId));
        n.setName(NeuronName.getText());

        JTable t = (JTable)((BCKNeuronTablePanel)table).tableView;
        String s = "";
        Integer I;
        Double D;
        double weight;
        int delay;  
        int sourceNode;  
        int rows = t.getRowCount();
        BCKNeuron node = ((BCKNeuron)Net.getNeuron(neuronId));    
        //get the currently selected input :
        int currentRow = t.getSelectedRow();
        //we want to remove row # 'row'
        /**remove all inputs to the neuron, in preparation of the creation of the new input links as edited by the user */
        node.removeInputs();

        for(int i=0;i<rows;i++){              
            t.editCellAt(0,0);  //set the active cell to 0,0

            s=(String)t.getValueAt(i,0);     
            try{
                I=Integer.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 0 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
            sourceNode = I.intValue();

            s=(String)t.getValueAt(i,1);
            try{
                D=Double.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 1 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }

            weight = D.doubleValue();


            s=(String)t.getValueAt(i,2);     
            try{
                I=Integer.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 2 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
            delay = I.intValue();     
            if(i!=currentRow){
                try{
                    BCKNeuron snode;
                    if(sourceNode == -1){
                        snode=((BCKMLP)Net).getBias();
                        Net.connectExternal(snode,neuronId,weight,delay);
                    } 
                    else {         	   
                        Net.connectInternal(sourceNode,neuronId,weight,delay);
                    }                     
                }
                catch (Exception ne){
                    JOptionPane.showMessageDialog(null,"Error at row "+i+" : "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        }         
        Net.notifyObservers();      
        //redraw the table:
        //remove the old table:
        container.remove(table);
        container.remove(addremove);
        container.remove(buttons);
        createInputsPanel();
        container.add(table);
        container.add(addremove);
        container.add(buttons);
        pack();
        repaint();
    }



    public void OKPressed(){
        BCKNeuron n = ((BCKNeuron)Net.getNeuron(neuronId));
        n.setName(NeuronName.getText());

        JTable t = (JTable)((BCKNeuronTablePanel)table).tableView;
        String s = "";
        Integer I;
        Double D;
        double weight;
        int delay;  
        int sourceNode;  
        int rows = t.getRowCount();
        BCKNeuron node = ((BCKNeuron)Net.getNeuron(neuronId));
        /**remove all inputs to the neuron, in preparation of the creation of the new input links as edited by the user */
        node.removeInputs();

        for(int i=0;i<rows;i++){         

            t.editCellAt(0,0);  //set the active cell to 0,0

            s=(String)t.getValueAt(i,0);     
            try{
                I=Integer.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 0 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
            sourceNode = I.intValue();

            s=(String)t.getValueAt(i,1);
            try{
                D=Double.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 1 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }

            weight = D.doubleValue();


            s=(String)t.getValueAt(i,2);     
            try{
                I=Integer.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 2 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
            delay = I.intValue();     
            try{
                BCKNeuron snode;
                if(sourceNode == -1){
                    snode=((BCKMLP)Net).getBias();
                    Net.connectExternal(snode,neuronId,weight,delay);
                } 
                else {         	   
                    Net.connectInternal(sourceNode,neuronId,weight,delay);
                }                     
            }
            catch (Exception ne){
                JOptionPane.showMessageDialog(null,"Error at row "+i+" : "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }         
        this.setVisible(false);  
    }

    public void CancelPressed(){     
        this.setVisible(false); 
    }


}
