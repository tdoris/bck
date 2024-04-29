
package BCK.GUI;
import javax.swing.*;

import BCK.ANN.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.accessibility.*;;

import java.util.*;
/** This class implements the RBFDDA training dialog box.
 *@author Eoin Whelan
 *@author Tom Doris
 */
public class BCKRBFTrain extends BCKDialog {
    int epochsSinceRestart;
    JTextField epochCount;
    JTextField trainfilename = new JTextField(25);
    JTextField testfilename = new JTextField(25);
    JButton OK = new JButton("OK");
    bckNumberBox trainForEpochs;  
    BCKRBFDrawable Net;  
    JTextField globalError;
    JTextField testScore;
    JTextField max;
    BCKProgressPanel progress;
    TrainThread trainThread;
    Object lock = new Object();
    boolean shouldStop = false;

    public BCKRBFTrain(BCKRBFDrawable Net){
        super(bckframe.top,"RBF Dynamic Decay Adjustment",true);
        this.Net = Net;
        JPanel container = new JPanel();
        container.setLayout( new ColumnLayout());
        trainfilename.setEditable(false);
        testfilename.setEditable(false);
        JPanel trainfile = createTrainFilePanel();

        JPanel testfile = createTestFilePanel();

        JPanel trainForEpochs = createTrainForEpochs();

        JPanel restartTraining = createRestart();

        JPanel testPanel = createTestPanel();

        progress = new BCKProgressPanel();

        JPanel buttons = new JPanel();
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

        container.add(trainfile);
        container.add(testfile);
        container.add(trainForEpochs);
        container.add(progress);

        container.add(restartTraining);
        container.add(testPanel);
        container.add(buttons);
        this.getContentPane().add(container);    
        this.pack(); 
        centerDialog();

        testScore.setEditable(false);    
    }

    public JPanel createTrainFilePanel(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());

        JLabel lab = new JLabel("Training File");

        JButton browse = new JButton("Browse");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseTrainPressed();
            }
        }
        );
        p.add(lab);
        p.add(trainfilename);
        p.add(browse);
        return p;    
    }

    public JPanel createTestFilePanel(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());

        JLabel lab = new JLabel("Testing File ");

        JButton browse = new JButton("Browse");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseTestPressed();
            }
        }
        );
        p.add(lab);
        p.add(testfilename);
        p.add(browse);
        return p;    
    }

    public JPanel createTrainForEpochs(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout() );

        JLabel lab = new JLabel(" Train For :                        ");

        trainForEpochs = new bckNumberBox();

        JLabel ep = new JLabel(" Epochs ");

        JButton train = new JButton("Train");
        train.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shouldStop = false;
                trainForEpochsPressed();		      
            }
        }
        );   
        JButton stop = new JButton("Stop");
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shouldStop = true;      		       
            }
        }
        );
        p.add(lab);
        p.add(trainForEpochs);
        p.add(ep);
        p.add(train);
        p.add(stop);
        return p;
    }

    public JPanel createRestart(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());         
        p.add(new JLabel("Epoch Count :"));
        epochCount = new JTextField(10);
        epochCount.setEditable(false);
        p.add(epochCount);
        return p;
    }

    public JPanel createTestPanel(){
        JPanel p = new JPanel();
        p.setLayout( new FlowLayout());
        JButton test = new JButton("Test");
        p.add(test);
        test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testPressed();
            }
        }
        );		   
        p.add(new JLabel("Test Score % : "));
        testScore = new JTextField(10);
        p.add(testScore);
        return p;
    }


    public void OKPressed(){
        this.setVisible(false); 
    }

    public void CancelPressed(){  
        this.setVisible(false); 
    }

    public void trainForEpochsPressed(){
        int e=trainForEpochs.getInteger();
        double GE;
        int GlobalError;
        if(e<1){
            JOptionPane.showMessageDialog(null,"Illegal Number of Epochs:  "+e,"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        trainThread = new TrainThread();
        trainThread.start();
        return;
    }

    public void testPressed(){
        double score=0;
        try{ 
            score = Net.test();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(bckframe.top,e.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }
        testScore.setText(""+score);
    }

    public void browseTrainPressed(){
        //create file dialog
        FileDialog file = new FileDialog(bckframe.top,"Load Training File",FileDialog.LOAD);
        file.show();
        String strfilename = file.getFile();
        if(strfilename!=null){
            trainfilename.setText(file.getDirectory()+strfilename);
            try{ 
                Net.setTrainingFile(file.getDirectory()+strfilename);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null,"Error Reading file \n"+e,"Alert",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void browseTestPressed(){
        //create file dialog
        FileDialog file = new FileDialog(bckframe.top,"Load Training File",FileDialog.LOAD);
        file.show();
        String strfilename = file.getFile();
        if(strfilename!=null){
            testfilename.setText(file.getDirectory()+strfilename);
            try{ 
                Net.setTestingFile(file.getDirectory()+strfilename);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null,"Error Reading file \n"+e,"Alert",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**This Class implements a thread which handles the network training and updates the relevant GUI components. A thread is needed so that the GUI can be updated while training continues
     *@author Tom Doris
     */
    class TrainThread extends Thread {
        int currentEpoch=0;
        int maxEpoch;

        TrainThread() {
            super();
        }

        public void run () {	    	   	   
            int e=trainForEpochs.getInteger();
            if(e<1){
                JOptionPane.showMessageDialog(null,"Illegal Number of Epochs:  "+e,"Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }

            progress.setMaximum(e);
            progress.setValue(0);
            //training cycle : 
            for(int i=0;i<e;i++){
                try{
                    epochsSinceRestart++;
                    epochCount.setText(""+epochsSinceRestart);
                    Net.train(1);  //train for one epoch
                    Net.notifyObservers();
                    progress.setValue(i+1);	       
                } 
                catch (Exception ex){
                    JOptionPane.showMessageDialog(null,ex.toString(),"Alert",JOptionPane.ERROR_MESSAGE);
                    return;
                }       
                synchronized(lock) {
                    if(shouldStop)
                        break;
                    try {
                        lock.wait(1);
                    } 
                    catch (java.lang.InterruptedException exp) { 
                    }
                } 
            }     
            trainThread = null;
        }
    }

}





