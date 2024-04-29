package BCK.GUI;
import javax.swing.*;

import BCK.ANN.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**This dialog facilitates the connection of networks. Networks are connected at the neuron level. The neurons in the target network are given the same output as the specified source neurons in the source network
 */
public class BCKConnectNetworks extends BCKDialog{

    BCKNeuralNetwork Net;
    protected int targetNet;
    protected int sourceNet=0;
    protected int targetNeuron;
    protected int sourceNeuron;
    protected double weight = 1;
    protected double delay = 0;
    protected JPanel tablePanel;
    protected bckNumberBox sourceNetworkNumber;
    /**Constructors*/
    public BCKConnectNetworks(BCKNeuralNetwork net){
        super(bckframe.top,"Connect Networks",true);
        this.Net = net;
        this.targetNet = net.getId();
        makePane();
        pack();
        centerDialog();
    }

    public void makePane(){  
        JPanel container = new JPanel();
        container.setLayout(new ColumnLayout()); 
        JPanel netIdsPanel = createNetIdsPanel();
        container.add(netIdsPanel);
        tablePanel = createTable();
        tablePanel.setLayout( new ColumnLayout());
        container.add(tablePanel);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton apply = new JButton("Apply");
        apply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyPressed(); 
            }
        }
        );	 	 
        buttonsPanel.add(apply);     
        JButton done = new JButton("Done");
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                donePressed(); 
            }
        }
        );	 	 
        buttonsPanel.add(done);
        container.add(buttonsPanel);
        getContentPane().removeAll();
        getContentPane().add(container);
    }

    public JPanel createNetIdsPanel(){
        JPanel p = new JPanel();
        p.setLayout(new ColumnLayout());
        p.add(new JLabel("Network ID :"+targetNet));     
        JPanel sourcePanel = new JPanel();   
        sourcePanel.setLayout(new FlowLayout());
        sourcePanel.add(new JLabel("Source Network Id number:"));  
        sourceNetworkNumber = new bckNumberBox();     
        sourceNetworkNumber.setText(""+sourceNet);
        sourcePanel.add(sourceNetworkNumber);
        JButton load = new JButton("Load");
        sourcePanel.add(load);
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sourceNet = sourceNetworkNumber.getInteger();

                if(sourceNet<0 || sourceNet >= bckframe.top.theBrain.size()) {
                    sourceNet=0;
                    JOptionPane.showMessageDialog(null,"Illegal value for source net","Alert",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                makePane();
                pack();
                repaint();	 
            }
        }
        );
        JButton connectAll = new JButton("Connect All");
        connectAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectAll();
                makePane();
                pack();
                repaint();	 
            }
        }
        );     
        p.add(sourcePanel);    
        p.add(connectAll);
        return p;
    }

    public JPanel createTable(){  
        JPanel p = new BCKNetConnectTable(Net, sourceNet);
        return p;
    }
    /**Connect all inputs in the target network to all outputs in the source network*/
    public void connectAll(){
        JTable t = (JTable)((BCKNetConnectTable)tablePanel).tableView;
        String s=(String)t.getValueAt(0,0);
        Integer I;
        Double D;
        int sNode;
        int tNode;
        double weight;
        int delay;
        int rows = t.getRowCount();
        for(int i=0;i<rows;i++){         
            t.editCellAt(0,0);  //set the active cell to 0,0
            s=(String)t.getValueAt(i,1);
            try{
                I=Integer.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 1 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }

            sNode = I.intValue();           

            tNode =i;

            s=(String)t.getValueAt(i,3);
            try{
                D=Double.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 3 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }

            weight = D.doubleValue();

            s=(String)t.getValueAt(i,4);
            try{
                I=Integer.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 4 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
            delay = I.intValue();
            if(tNode>=0){
                //create the proxy input and add to brain:
                bckframe.top.theBrain.setProxyInput(sourceNet,sNode,targetNet,tNode,weight,delay);     
            }
        }
        bckframe.top.drawConnections();
        bckframe.top.repaint();
    }
    public void applyPressed(){
        JTable t = (JTable)((BCKNetConnectTable)tablePanel).tableView;
        String s=(String)t.getValueAt(0,0);
        Integer I;
        Double D;
        int sNode;
        int tNode;
        double weight;
        int delay;
        int rows = t.getRowCount();
        for(int i=0;i<rows;i++){         
            t.editCellAt(0,0);  //set the active cell to 0,0
            s=(String)t.getValueAt(i,1);
            try{
                I=Integer.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 1 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }

            sNode = I.intValue();
            s=(String)t.getValueAt(i,2);

            try{
                I=Integer.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 2 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }

            tNode =I.intValue();

            s=(String)t.getValueAt(i,3);
            try{
                D=Double.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 3 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }

            weight = D.doubleValue();

            s=(String)t.getValueAt(i,4);
            try{
                I=Integer.valueOf(s);
            }
            catch(NumberFormatException ne){
                JOptionPane.showMessageDialog(null,"Illegal value at row "+i+" column 4 "+ne.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
            delay = I.intValue();
            if(tNode>=0){
                //create the proxy input and add to brain:
                bckframe.top.theBrain.setProxyInput(sourceNet,sNode,targetNet,tNode,weight,delay);     
            }
        }
        bckframe.top.drawConnections();
        bckframe.top.repaint();
    }

    public void donePressed(){
        this.setVisible(false);
    } 
    public void CancelPressed(){
        this.setVisible(false);
    }
}









