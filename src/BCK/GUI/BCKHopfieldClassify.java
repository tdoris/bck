
//Title        BCK
//Version
//Copyright    Copyright (c) 1997
//Author       Eoin Whelan
//Company      DCU
//Description  none

package BCK.GUI;

import BCK.ANN.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.text.*;

public class BCKHopfieldClassify extends BCKDialog {

    JTextField filename; 
    BCKFilter input = null;
    BCKNeuralNetwork Net;
    BCKPlot plotPane = new BCKPlot(bckframe.top,200,200);
    BCKPlot resultPlot = new BCKPlot(bckframe.top,200,200);
    bckNumberBox numOfRecords;
    bckNumberBox recordNumber;
    bckNumberBox widthNumberBox = new bckNumberBox();
    double maxFieldValue;
    int currentRecordNumber=0;
    JPanel table;

    String winnerName;

    String fileNameString = "";
    int numberOfRecords = 0;

    /**Constructors*/
    public BCKHopfieldClassify(BCKNeuralNetwork net){
        super(bckframe.top,"Classification",true);
        this.Net = net;
        makePane();
        pack();
        centerDialog();
    }

    public void makePane(){  
        JPanel container = new JPanel();
        container.setLayout(new ColumnLayout());

        JPanel inputs = new JPanel();
        inputs.setLayout(new FlowLayout());
        inputs.add(new JLabel("Input File"));
        filename = new JTextField(25);
        filename.setText(fileNameString);

        inputs.add(filename);
        JButton browse = new JButton("Browse");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browsePressed();
            }
        }
        );
        inputs.add(browse);

        JPanel r = new JPanel();
        JLabel l = new JLabel("Number of Records is : ");
        numOfRecords = new bckNumberBox();
        numOfRecords.setText(""+numberOfRecords);
        numOfRecords.setEditable(false);
        r.add(l);
        r.add(numOfRecords);

        JPanel record = new JPanel();
        record.setLayout(new FlowLayout());
        record.add(new JLabel("Record Number :"));
        recordNumber = new bckNumberBox();
        recordNumber.setText(""+currentRecordNumber);
        record.add(recordNumber);
        JButton load = new JButton("Load");
        record.add(load);
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadPressed();
            }
        }
        );  
        JButton next = new JButton("Next");
        record.add(next);
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextPressed();
            }
        }
        );
        JPanel picturePanel = new JPanel();
        picturePanel.add(plotPane);
        picturePanel.add(resultPlot);  

        JPanel classifyButton = new JPanel();
        classifyButton.setLayout(new FlowLayout());
        JButton cbutton = new JButton("Classify");
        classifyButton.add(cbutton);
        cbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                classifyPressed();
            }
        }
        );
        JPanel tables = new JPanel();
        table = createTable();
        tables.add(table);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok);
        buttons.add(cancel);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancelPressed();
            }
        }
        );
        JPanel size = new JPanel();
        size.add(new JLabel("Width :"));
        size.add(widthNumberBox);

        container.add(inputs);
        container.add(r);
        container.add(record);
        container.add(size);
        container.add(picturePanel);
        container.add(classifyButton);
        container.add(buttons);

        getContentPane().removeAll();
        getContentPane().add(container);
    }


    public JPanel createTable(){
        JPanel p = new BCKOutputTable(Net);
        return p;
    }

    public void paint(Graphics g){
        super.paint(g);
        if(input !=null){
            int width = widthNumberBox.getInteger(); 
            if (numOfRecords.getInteger()<=recordNumber.getInteger() ||numOfRecords.getInteger()<0 ){
                return;
            }
            if(width>0){
                loadPressed();
            }
        }

    }

    public void CancelPressed()
    {
        this.setVisible(false);
    }

    public void loadPressed(){
        if (numOfRecords.getInteger()<=recordNumber.getInteger() ||numOfRecords.getInteger()<0 ){
            JOptionPane.showMessageDialog(null,"The Record Number You Choose must be less than " + numOfRecords.getInteger(),"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        displayRecord(recordNumber.getInteger());    
        currentRecordNumber = recordNumber.getInteger();
    } 

    public void nextPressed(){
        int recnum;
        //increment the record number 
        setRecordNumber();
        recnum=recordNumber.getInteger();
        if(recnum<0){
            JOptionPane.showMessageDialog(null,"Enter a valid record number first." + numOfRecords.getInteger(),"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        recnum++;  
        if(recnum>=numOfRecords.getInteger()) recnum=0;
        recordNumber.setText(""+recnum);
        loadPressed();
    }


    public void browsePressed(){
        //create file dialog
        FileDialog file = new FileDialog(bckframe.top,"Set Input File",FileDialog.LOAD);
        file.show();
        String strfilename = file.getFile();
        if(strfilename!=null){
            try{
                input = new BCKFilter(file.getDirectory()+strfilename,Net.getNumInputs());
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null,"Error in file Format \n Please check that the architecture you have specified \n matches the format of the data you are trying to classify.\n" + e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            } 
            filename.setText(file.getDirectory()+strfilename);
            numOfRecords.setText((new Integer(input.getNumberOfRecords())).toString());
            numberOfRecords = numOfRecords.getInteger();
            fileNameString = filename.getText();
        }
    }

    private void setRecordNumber(){
        currentRecordNumber = recordNumber.getInteger();
    }
    /**Classify the input and display network output*/
    public void classifyPressed(){
        double[] outputVector = null;

        try{
            double[] inputVector = input.getRecordAt(currentRecordNumber);
            outputVector=Net.classify(inputVector);
        } 
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error : " + e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        //draw the output on the result plot pane
        drawOutput(outputVector);
    }

    public void drawOutput(double[] rec){ 
        Color c;
        Point p; 
        int numIn=Net.getNumOutputs();
        int[] pixels = new int[numIn];
        int width = widthNumberBox.getInteger();
        int scale = 4;

        if(width<=0 ){
            JOptionPane.showMessageDialog(null,"Please Enter the correct width","Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int height = Net.getNumOutputs()/width;  
        for(int t=0;t<numIn;t++){
            if(rec[t]==-1)    
                pixels[t] = 0;
            else 
                pixels[t] = 1;
        }

        if( height > plotPane.height || width > plotPane.width) {
            plotPane.setPreferredSize(new Dimension(width*scale,height*scale));
            pack();
        }
        resultPlot.drawPicture(pixels,width,1,scale);
    }


    public void displayRecord(int recNum){
        double[] rec = input.getRecordAt(recNum);  
        Color c;
        Point p; 
        int numIn=Net.getNumInputs();
        int[] pixels = new int[numIn];
        int width = widthNumberBox.getInteger();
        int scale = 4;
        if(width<=0 ){
            JOptionPane.showMessageDialog(null,"Please Enter the correct width","Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        int height = Net.getNumInputs()/width;
        double max=0;
        for(int t=0;t<numIn;t++){
            max=Math.max(rec[t],max);
            pixels[t] = (int) rec[t];
        }
        if(max==0.0) max=1;
        int maxInteger = (int)max;
        if( height > plotPane.height || width > plotPane.width) {
            plotPane.setPreferredSize(new Dimension(width*scale,height*scale));
            pack();
        }
        plotPane.drawPicture(pixels,width,maxInteger,scale);
    }

}




