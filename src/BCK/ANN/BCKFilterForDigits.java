//The BCKFilter objects will provide formatting of input and output
//data to and from files and neural networks

package BCK.ANN;

import java.util.*;
import java.io.*;

//the filter provides an interface to test and training data.


public class BCKFilterForDigits extends BCKFilter implements Serializable{ 

    int inputs;
    int outputs;
    public BCKFilterForDigits(String input_file,int inputs, int outputs) throws Exception{
        this.inputNet = null;
        inputIsANN = false;
        this.RecordSize=inputs+outputs;
        tokens = new Hashtable();
        FileReader is;
        this.inputs=inputs;
        this.outputs=outputs;
        //allocate for data:
        data = new Vector(0);	
        this.index=0;
        //open the file and read it in   
        try{ 
            is = new FileReader(input_file);    		
        }
        catch(FileNotFoundException e){
            throw new Exception("Could not find file"+input_file);
        }      		       
        ReadFile(input_file, RecordSize);  
        is.close();       
        numRecords=data.size();
        randomMapping = new int[numRecords];
        for(int i=0;i<randomMapping.length;i++){
            randomMapping[i]=i;
        }
        randomIndex=0;

    }


    private void ReadFile(String input_file,int recordSize) throws Exception{
        boolean atEOF = false;
        this.RecordSize = -1;
        int fieldsInFileRecord=inputs+1;
        int TokenCount = 0;
        int RecordCount = 0;
        int currToken = 0;
        int linenum = 0;
        double value;
        String tag;
        double x;
        Double n;
        double[] outputVector;
        this.RecordSize = recordSize;
        //open file:
        //open the file and read it in    
        FileReader inp = new FileReader(input_file);
        //open stream & tokenizer
        BCKStreamTokenizer stoke = new BCKStreamTokenizer(inp);
        stoke.eolIsSignificant(false);
        stoke.parseNumbers();

        //read in the data
        BCKRecord record = new BCKRecord(RecordSize);

        while(!atEOF){
            for(int i=0; i<fieldsInFileRecord;i++){
                currToken = stoke.nextToken();
                switch(currToken) {
                case StreamTokenizer.TT_NUMBER:   //token is a number, so store it  
                    x = (double)stoke.nval;
                    if(i<fieldsInFileRecord-1){
                        record.data[i]=x;
                    } 
                    else {
                        outputVector=intToVector((int)x);
                        for(int q=0;q<outputVector.length;q++){
                            record.data[i+q]=outputVector[q];
                        }
                    }
                    break;
                case StreamTokenizer.TT_EOF:  //at end of file, so quit
                    if(i!=0){
                        throw new IOException("Unexpected EOF");
                    }            
                    //System.out.println("Read in Data OK");
                    this.numRecords = RecordCount;
                    inp.close();  //close input file
                    return;
                }
                TokenCount++;
            }
            //save the record:
            data.addElement(record);
            //create new record:
            record = new BCKRecord(RecordSize);  
            RecordCount++;  
        }

    }
    /**Convert int x to a 10 element vector*/
    public double[] intToVector(int x){
        double[] v = new double[10];
        for(int i=0;i<10;i++){
            if(x==i){
                v[i] =0;
            } 
            else {
                v[i]=1;
            }
        }
        return v;
    }


    public double[] getInputVectorAt(int i){
        double[] v = getRecordAt(i);
        double[] iv = new double[inputs];
        for(int j=0;j<inputs;j++)
            iv[j] = v[j];

        return iv;
    }

    public double[] getOutputVectorAt(int i){
        double[] v = getRecordAt(i);
        double[] ov = new double[outputs];
        int k=0;
        for(int j=inputs;j<outputs+inputs;j++)
            ov[j-inputs] = v[j];
        return ov;
    }
}


