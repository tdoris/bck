
package BCK.GUI;
import javax.swing.*;
import javax.swing.event.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;


import java.beans.*;

/** This is the class which is used to read in JPG files and GIF files and convert them to a format which can be used by BCK's Neural Network.
 */

public class BCKImageConversion extends BCKDialog implements ListSelectionListener{

    /**The network we may create with this dialog : */

    /*Instance Variables*/ 	
    bckNumberBox size = new bckNumberBox();
    bckNumberBox classId = new bckNumberBox();
    private JList fileList = new JList();
    private JTextField outputVector = new JTextField(25);
    private JTextField outputFile = new JTextField(30);
    private FileDialog file = new FileDialog(bckframe.top);
    private Vector inputFiles = new Vector(0); 
    private BCKPicture picture = new BCKPicture(); 
    private int pixels[];
    private String outputFileName;
    private JTextField dimensions = new JTextField(20);
    /*Constructor*/
    public BCKImageConversion() {
        super(bckframe.top,"Image Converter" , true);
        fileList.addListSelectionListener(this);
        this.getContentPane().setLayout(new ColumnLayout());
        JPanel top = buildTop();
        JPanel bottom = buildBottom();

        getContentPane().add(top);
        getContentPane().add(bottom); 
        centerDialog();    
        pack();

        repaint();
    }

    /**A method which creates an returns a panel which contains the components in the top half of the dialog*/
    public JPanel buildTop(){
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(buildInputPanel());
        p.add(buildOutputPanel());
        return p;
    }

    /**A method which creates an returns a panel which contains the components in the bottom half of the dialog*/
    public JPanel buildBottom(){  
        JPanel p = new JPanel();
        p.setLayout(new ColumnLayout());

        JPanel t = new JPanel();
        t.setLayout(new FlowLayout());
        JPanel b = new JPanel();
        b.setLayout(new FlowLayout()); 

        t.add(new JLabel("Output File"));
        t.add(outputFile);
        JButton browse = new JButton("Browse");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browsePressed();
            }
        }
        ); 
        t.add(browse);

        JButton done = new JButton("Done");
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        }
        ); 
        JButton writeOutput = new JButton("Write Output");
        writeOutput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writeOutputPressed();
            }
        }
        );

        b.add(writeOutput);
        b.add(done); 

        p.add(t);
        p.add(b);
        return p;
    }

    /**A method which creates an returns a panel which contains the components in the input part of the dialog*/    
    public JPanel buildInputPanel(){
        JPanel p = new JPanel();     

        p.setLayout(new ColumnLayout());
        JButton add = new JButton("Add");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPressed();
            }
        }
        );
        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removePressed();
            }
        }
        );

        p.add(new JLabel("File List"));

        fileList.setListData(inputFiles);

        JScrollPane scrollPane = new JScrollPane(fileList);
        scrollPane.setPreferredSize(new Dimension(400,250));
        p.add(scrollPane);
        JPanel arbuttons = new JPanel();
        arbuttons.setLayout(new FlowLayout());
        arbuttons.add(add);
        arbuttons.add(remove);
        p.add(arbuttons);      
        return p;
    }

    /**A method which creates and returns a panel which contains the components in the output part of the dialog*/    
    public JPanel buildOutputPanel(){
        JPanel p = new JPanel();       
        p.setLayout(new ColumnLayout());
        p.add(new JLabel("Output Vector Size :"));
        p.add(size);
        p.add(new JLabel("Set element to 1 :"));	
        p.add(classId);
        JButton generate = new JButton("Generate");
        p.add(generate);
        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //generate the output vector
                String ovtext = "";
                int vectorSize = size.getInteger();
                int classIdNumber = classId.getInteger();
                if(vectorSize <1 || classIdNumber <1){
                    JOptionPane.showMessageDialog(bckframe.top,"You must specify valid size and class parameters","Alert",JOptionPane.ERROR_MESSAGE);
                }

                for(int i=0;i<vectorSize;i++){
                    if(i+1 == classIdNumber){
                        ovtext = ovtext+"1 "; 
                    } 
                    else {
                        ovtext = ovtext+"0 ";
                    }
                    outputVector.setText(ovtext);
                }

            }
        }
        );
        p.add(new JLabel("Output Vector :"));
        p.add(outputVector);
        p.add(new JLabel("Picture"));
        p.add(picture);
        p.add(new JLabel("Dimensions"));
        p.add(dimensions);
        dimensions.setEditable(false);
        pack();	
        return p;
    }

    /**Events that happens when the value changes the the list*/
    public void valueChanged(ListSelectionEvent e){
        if((fileList.isSelectionEmpty())){
            picture.setImage(null);
        }
        else{
            picture.setImage((String)fileList.getSelectedValue()); 
            dimensions.setText(""+picture.getImage().getWidth(null)+" x "+picture.getImage().getHeight(null));
        }
    }

    /**Event that happens when add is pressed*/    
    public void addPressed(){
        file.setMode(FileDialog.LOAD);
        file.setTitle("Add a File");    
        file.show();
        String str = file.getFile();
        /*    if (!((str.endsWith(".jpg"))||(str.endsWith(".JPG"))||(str.endsWith(".GIF"))||(str.endsWith(".gif")))){
         *     JOptionPane.showMessageDialog(bckframe.top,"You must specify a GIF or a JPG as an Input\nThis file is not an GIF or JPG file","Alert",JOptionPane.ERROR_MESSAGE);
         *     return;
         *     }*/
        if(str != null){
            inputFiles.addElement(file.getDirectory() + str);
            fileList.setListData(inputFiles);
            repaint();
            pack();
        }
    }

    public void removePressed(){
        if (!(fileList.isSelectionEmpty())){
            inputFiles.removeElementAt(fileList.getSelectedIndex());
            fileList.setListData(inputFiles);
            repaint();               
        }
    }

    public void browsePressed(){
        file.setMode(FileDialog.LOAD);
        file.setTitle("Find Output File");    
        file.show();
        outputFile.setText(file.getDirectory() + file.getFile());    
        outputFileName = outputFile.getText();
    }

    public void writeOutputPressed(){
        String outputVectorString;    

        if (outputVector.getText() ==""){
            JOptionPane.showMessageDialog(bckframe.top,"You must specify an Output Vector" ,"Alert",JOptionPane.ERROR_MESSAGE);
            return;
        }

        String str;
        for(int record = 0; record < inputFiles.size();record++){
            fileList.setSelectedIndex(record);      
            str = (String)fileList.getSelectedValue();
            if (!((str.endsWith(".jpg"))||(str.endsWith(".JPG"))||(str.endsWith(".GIF"))||(str.endsWith(".gif")))){
                JOptionPane.showMessageDialog(bckframe.top,"You must specify a GIF or a JPG as an input file\nInput " + (record +1) + " is not an GIF or JPG file","Alert",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        DirectColorModel DCM =        new DirectColorModel(32,
        0x00ff0000,       // Red
        0x0000ff00,       // Green
        0x000000ff,       // Blue
        0xff000000        // Alpha
        );

        FileWriter os = null ;
        PrintWriter ps = null;
        int[] transformedPixels;
        try{	  
            os = new FileWriter(outputFileName);
            ps = new PrintWriter(os);
            for(int record = 0; record < inputFiles.size();record++){       

                fileList.setSelectedIndex(record);

                picture.setImage((String)fileList.getSelectedValue());

                Image img = picture.getImage(); 
                MediaTracker t = new MediaTracker(this);    
                t.addImage(img,0);
                t.waitForID(0);
                int iw = img.getWidth(null);
                int ih = img.getHeight(null);
                pixels = new int[iw * ih];           
                PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
                pg.grabPixels();
                //do data transforms:
                transformedPixels=halve(pixels, iw, ih);
                transformedPixels=halve(transformedPixels,iw/2,ih/2);
                int newWidth = iw/4;
                //write the data out
                for(int i=0;i<transformedPixels.length;i++){
                    if(i%newWidth == 0 && i!=0)  //keep the image square
                        ps.println("");
                    ps.print(" "+DCM.getRed(transformedPixels[i]));
                }//end of for
                ps.println("");
                ps.println(outputVector.getText()); 
                ps.println("");
            } //end of for
            os.flush();
            os.close();
        } 
        catch(Exception e){
            JOptionPane.showMessageDialog(bckframe.top,"Error :"+e.toString() +"\nPlease Ensure you have named a valid file and an output vector" ,"Alert",JOptionPane.ERROR_MESSAGE);
        }  

    }

    public int[] halve(int [] pix, int width, int height){
        int average;
        int rowOffset;  
        int[] transformedPix = new int[pix.length/4];    
        int index=0;
        //reduce to half size:
        for(int i =0; i< height;i+=2){
            rowOffset = i*width;
            for(int j =0;j<width;j+=2){
                average=pix[rowOffset+j]+pix[rowOffset+j+1]+pix[rowOffset+width+j]+pix[rowOffset+width+j+1];
                average/=4;
                transformedPix[index++] = average;
            }
        }
        return transformedPix;
    }

    public void CancelPressed(){
        this.setVisible(false);
    }

    public void OKPressed(){
    }
}






