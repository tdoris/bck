
package BCK.ANN;

import java.util.*;
import java.io.*;

/** 
 * The filter provides an interface to test and training data for Neural Networks
 * it handles the conversion of text tags to numeric values. It also handles the randomisation of the order of records.
 */
public class BCKFilter{

    protected int RecordSize; 	//the number of fields per record
    protected Vector data;		//RAM storage for file - a Vector of Records
    protected String input_file = null;	//file name
    protected BCKNeuralNetwork inputNet;  //the input ANN
    protected boolean inputIsANN;
    protected int index;
    protected int numRecords;
    protected Hashtable tokens;     //maps tags to real values
    protected int randomIndex;
    protected int[] randomMapping;
    //default constructor
    protected BCKFilter() {
        this.index=0;
    }


    /**standard constructor to connect to a file, given the expected size of records.
     *@exception Exception thrown when file not found error occurs
     */
    public BCKFilter(String input_file, int recordSize) throws Exception{
        this.inputNet = null;
        inputIsANN = false;
        this.RecordSize=recordSize;
        tokens = new Hashtable();
        FileReader is;
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
        int TokenCount = 0;
        int RecordCount = 0;
        int currToken = 0;
        int linenum = 0;
        double value;
        String tag;
        double x;
        Double n;
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

        currToken = stoke.nextToken(); //read 'TOKENDEFS' or the first field of the file

        String word = "";
        if(currToken == StreamTokenizer.TT_NUMBER){
            stoke.pushBack();
        } 
        else {
            word = stoke.sval.toString();
            if(!(word.equals("TOKENDEFS"))){
                throw new Exception ("The first field in the file must be either a numeric value or the 'TOKENDEFS' keyword");
            }
            ;
        }
        while(word.equals("TOKENDEFS")){       
            //read token name
            tag = stoke.getWord();	
            if(tag.equals("STARTDATA")){
                //stoke.eatEOL();
                break;
            }
            //read '='
            stoke.eatChar('=');
            //read number corresponding to token
            value=stoke.getNumber();
            linenum++;
            //store token & value in hashtable
            tokens.put(tag,new Double(value));
        }

        //finished token defs, now on main data area
        while(!atEOF){
            for(int i=0; i<RecordSize;i++){
                currToken = stoke.nextToken();
                switch(currToken) {

                case StreamTokenizer.TT_NUMBER:   //token is a number, so store it  
                    x = (double)stoke.nval;
                    record.data[i]=x;
                    break;
                case StreamTokenizer.TT_WORD:  //token is a word, so convert to number
                    n = (Double)tokens.get(stoke.sval.toString());
                    if(n==null){
                        throw new IOException("Undefined token : '"+stoke.sval.toString()+"' at line : "+linenum);
                    }
                    x = n.doubleValue();
                    record.data[i]=x;
                    break;  
                case StreamTokenizer.TT_EOF:  //at end of file, so quit
                    if(i!=0){
                        throw new IOException("Unexpected EOF");
                    }                          
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

    //standard constructor to connect to a file:
    public BCKFilter(String input_file) throws Exception{
        this.inputNet = null;
        inputIsANN = false;
        tokens = new Hashtable();
        FileReader is;
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
        ReadFile(input_file);  
        is.close();       
        numRecords=data.size();
        randomMapping = new int[numRecords];
        for(int i=0;i<randomMapping.length;i++){
            randomMapping[i]=i;
        }
        randomIndex=0;
    }


    private void ReadFile(String input_file) throws Exception{

        boolean atEOF = false;
        this.RecordSize = -1;
        int TokenCount = 0;
        int RecordCount = 0;
        int currToken = 0;
        int linenum = 0;
        double value;
        String tag;
        double x;
        Double n;
        //open file:
        //open the file and read it in    
        FileReader inp = new FileReader(input_file);
        //open stream & tokenizer
        BCKStreamTokenizer stoke = new BCKStreamTokenizer(inp);
        stoke.eolIsSignificant(false);
        stoke.parseNumbers();
        //the first line of the file should specify the number of fields per record:
        try{
            stoke.eatString("FIELDS");
        }
        catch (IOException e){ 
            throw new IOException("The first line of the file should specify the number of fields per record, 'FIELDS = N' 'FIELDS' not found"+e.toString());
        }

        try{
            stoke.eatChar('=');
        } 
        catch(IOException e){
            throw new IOException("The first line of the file should specify the number of fields per record, 'FIELDS = N' '=' not found");
        }

        try{
            RecordSize=(int)stoke.getNumber();
        } 
        catch(IOException e) { 
            throw new IOException("The first line of the file should specify the number of fields per record, 'FIELDS = N' number 'N' not found");
        }

        linenum=2;  //we're now on the second line


        String word = stoke.getWord(); //read 'TOKENDEFS' or 'STARTDATA'

        //stoke.eatEOL();

        while(word.equals("TOKENDEFS")){       
            //read token name
            tag = stoke.getWord();	
            if(tag.equals("STARTDATA")){
                //stoke.eatEOL();
                break;
            }
            //read '='
            stoke.eatChar('=');
            //read number
            value=stoke.getNumber();       
            //increment linecount
            linenum++;
            //store token / value in hashtable
            tokens.put(tag,new Double(value));
        }
        //finished token defs, now on main data area
        //read in the data
        BCKRecord record = new BCKRecord(RecordSize);

        while(!atEOF){
            for(int i=0; i<RecordSize;i++){
                currToken = stoke.nextToken();
                switch(currToken) {

                case StreamTokenizer.TT_NUMBER:   //token is a number, so store it  
                    x = (double)stoke.nval;
                    record.data[i]=x;
                    break;
                case StreamTokenizer.TT_WORD:  //token is a word, so convert to number
                    n = (Double)tokens.get(stoke.sval.toString());
                    if(n==null){
                        throw new IOException("Undefined token : '"+stoke.sval.toString()+"' at line : "+linenum);
                    }
                    x = n.doubleValue();
                    record.data[i]=x;
                    break;  
                case StreamTokenizer.TT_EOF:  //at end of file, so quit
                    if(i!=0){
                        throw new IOException("Unexpected EOF");
                    }               
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


    //******************* the real accessor code starts here :

    /**Return the next record in the current set */
    public double[] getNextRecord(){
        if(data.size()<=index){
            index=0;
        }  
        return ((BCKRecord)data.elementAt(index++)).getData();
    }

    /**Return the next record in the current set, converted to -1 and +1 values primarily for use by Hopfield  */
    public double[] getNextRecordBipolar(){
        double[] bipolarRecord = new double[RecordSize];
        double[] normalRecord=getNextRecord();
        for(int i=0;i<RecordSize;i++){
            if(normalRecord[i]<1){
                bipolarRecord[i]=-1;
            } 
            else {
                bipolarRecord[i]=1;
            }
        }
        return bipolarRecord;
    }


    /**return the next record as a normalised vector :*/
    public double[] getNextRecordNormalised(){
        double[] rec = getNextRecord();
        double d=0;
        for(int i=0;i<rec.length;i++){
            d+=rec[i]*rec[i];
        }
        if(d==0){
            return rec;
        } 
        else {
            d=Math.sqrt(d);
            for(int i=0;i<rec.length;i++){
                rec[i]=rec[i]/d;
            }
        }
        return rec;
    }

    /** return a record chosen at random */
    public double[] getNextRecordRandomly(){
        int rindex = randomMapping[randomIndex];
        randomIndex++;
        if(randomIndex==randomMapping.length){
            randomIndex=0;
            scrambleRandomMapping();
        }
        return ((BCKRecord)data.elementAt(rindex)).getData();
    }

    /**return a specified record*/
    public double[] getRecordAt(int i){
        return ((BCKRecord)data.elementAt(i)).getData();
    }



    /** Return the number of records read from file */
    public int getNumberOfRecords(){
        if(data==null){	       
            return 0;
        }
        return this.numRecords;
    }

    /** Return the number of fields per record */
    public int getRecordSize(){
        return RecordSize;
    }

    /**Return the file name */
    public String getFileName(){
        return input_file;
    }

    public void scrambleRandomMapping(){
        int rindex = (int)Math.floor((Math.random() * data.size()));
        if(rindex==data.size()) rindex--;
        if(rindex<0) rindex=0;
        int tmp = randomMapping[0];
        randomMapping[0]=randomMapping[rindex];
        randomMapping[rindex]=tmp;
    }

}



