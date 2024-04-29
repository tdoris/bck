
package BCK.GUI;
import javax.swing.*;

import BCK.ANN.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;


import java.beans.*;

/**This class implements a Dialog which can be used to speedily change the format of text data files - specifically produced here in order to change the 257th field of a data file from a scalar to a vector - the new vector will be the output vector
 *@author Tom Doris
 *@author Eoin Whelan
 */
public class BCKTextConversion extends BCKDialog {

    private JButton done;
    private JButton writeOutput;

    private JTextField inputFile = new JTextField(25);
    private JTextField outputFile = new JTextField(25);
    private JButton browseInput;
    private JButton browseOutput;

    private bckNumberBox numberOfFields = new bckNumberBox();
    private bckNumberBox EOLAfter = new bckNumberBox();
    private bckNumberBox replaceField = new bckNumberBox();

    private JTextField outputVector = new JTextField(25);

    private int fieldsPerRecord, opEOL, numToReplace;
    private String opString, inputFileName, outputFileName;
    private BCKFilter inData;

    public BCKTextConversion() {
        super(bckframe.top, "Convert Textual Data", true);
        setSize(300,80);     
        JPanel container = new JPanel();

        container.setLayout( new ColumnLayout() );	


        container.add(buildNumberOfFieldsPanel());
        container.add(buildEOLPanel());
        container.add(buildReplacePanel());
        container.add(buildWithPanel());
        container.add(buildInputPanel());
        container.add(buildOutputPanel());
        container.add(buildButtonsPanel());

        getContentPane().add(container);
        pack();
        centerDialog();
        setResizable(false);	
    }

    public JPanel buildInputPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout() );

        p.add(new JLabel("Input File:", JLabel.RIGHT));
        browseInput = new JButton("Browse");
        browseInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseInputPressed();
            }
        }
        );
        p.add(inputFile);
        p.add(browseInput);
        return p;
    }


    public JPanel buildOutputPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout() );

        p.add(new JLabel("Output File : ", JLabel.RIGHT));
        browseOutput = new JButton("Browse");
        browseOutput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseOutputPressed();
            }
        }
        );
        p.add(outputFile);
        p.add(browseOutput);
        return p;
    }

    public JPanel buildNumberOfFieldsPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout() );

        p.add(new JLabel("Number of Fields Per Record : ", JLabel.RIGHT));
        numberOfFields = new bckNumberBox();     
        p.add(numberOfFields);
        return p;
    }

    public JPanel buildEOLPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout() );
        p.add(new JLabel("Output EOL After : ", JLabel.RIGHT));
        EOLAfter = new bckNumberBox();     
        p.add(EOLAfter);
        return p;
    }


    public JPanel buildReplacePanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout() );
        p.add(new JLabel("Replace Field : ", JLabel.RIGHT));
        p.add(replaceField);    
        return p;
    }

    public JPanel buildWithPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout() );
        p.add(new JLabel(" With : ", JLabel.RIGHT));
        p.add(outputVector);
        return p;
    }

    public JPanel buildButtonsPanel(){
        JPanel p = new JPanel();       
        p.setLayout( new FlowLayout() );
        writeOutput = new JButton("Write Output");
        writeOutput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writeOutputPressed();
            }
        }
        );
        done = new JButton("Done");
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        }
        );	     
        p.add(writeOutput);
        p.add(done);
        return p;
    }

    public void browseOutputPressed(){
        //create file dialog
        FileDialog file = new FileDialog(bckframe.top,"Select Output File",FileDialog.LOAD);
        file.show();
        String strfilename = file.getFile();
        if(strfilename!=null){
            outputFile.setText(file.getDirectory()+strfilename);	   
        }
    }


    public void browseInputPressed(){
        if(!validateData()) return;
        //create file dialog
        FileDialog file = new FileDialog(bckframe.top,"Select Input File",FileDialog.LOAD);
        file.show();
        String strfilename = file.getFile();
        if(strfilename!=null){
            inputFile.setText(file.getDirectory()+strfilename);	
            validateData();   
            try{
                inData = new BCKFilter(inputFileName, fieldsPerRecord);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(bckframe.top,"Error Reading Data - Check your input file"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);      
            }	

        }
    }

    /** Read in the input file and write to the output file with appropriate conversions*/
    public void writeOutputPressed(){
        if(!validateData()) return;
        double[] rec;
        FileWriter os = null ;
        PrintWriter ps = null;
        try{	  
            os = new FileWriter(outputFileName);
            ps = new PrintWriter(os);
            for(int r=0;r< inData.getNumberOfRecords();r++){
                rec = inData.getRecordAt(r);
                for(int f=0;f<fieldsPerRecord;f++){
                    if(f==numToReplace-1){ //convert to one - based field numbering
                        ps.println("");	       
                        ps.println(opString);
                    } 
                    else {
                        ps.print(" "+rec[f]);
                    }
                    if(f%opEOL == 0 && f!=0) {
                        ps.println("");	   
                    }
                }	   
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(bckframe.top,"Error Writing Data - Check your input"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);      
        }	
        try{
            os.flush();
            os.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(bckframe.top,"Error Writing Data (closing files) - Possibly Disk Full Error"+e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);      
        }	
    }


    public boolean validateData(){
        fieldsPerRecord = numberOfFields.getInteger();
        opEOL = EOLAfter.getInteger();
        numToReplace = replaceField.getInteger();
        if( fieldsPerRecord < 0 || opEOL < 0 || numToReplace < 0){    
            JOptionPane.showMessageDialog(bckframe.top,"Please enter Valid Data","Alert",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        opString = outputVector.getText();
        inputFileName = inputFile.getText();
        outputFileName = outputFile.getText();
        return true;
    }
}












