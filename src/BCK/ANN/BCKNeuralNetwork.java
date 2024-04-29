package BCK.ANN;

import java.util.*;
import java.io.*;

/**This class implements a basic network of neurons;
 * and provides an abstract interface for training
 * it is meant to provide a base object independent of
 * any learning algorithm or architecture 
 */

public class BCKNeuralNetwork implements Serializable, BCKObservable {
    public int idNumber;
    protected int numInputs; //this is needed to feed data into the network
    protected int numOutputs;
    /**The neurons in this network, note that connections are modelled within the BCKNeuron objects*/
    protected Vector neurons; 
    protected String name = new String("No Name"); 
    /**More convenient model of connections*/
    protected transient Vector connections;
    protected transient Vector observers;
    protected transient BCKFilter trainData;  //interface to the training data
    protected transient BCKFilter testData;   //interface to the test data 
    protected transient BCKFilter inputData;  // interface to the input data

    //default constructor, protected to prevent use
    protected BCKNeuralNetwork(){
        neurons = new Vector(0);
        connections = new Vector(0);
        observers = new Vector(0);
    }

    //constructor accepting the number and type of neurons to create
    //MemorySize indicates the number of states each neuron remembers
    public BCKNeuralNetwork(int numneurons, String type){
        neurons = new Vector(0);
        connections = new Vector(0);
        observers = new Vector(0);
        for(int i =0;i<numneurons;i++){
            addNamedNeuron(type);
        }
    }

    /**Sets the name of the Neural Network*/
    public void setName(String name){
        this.name = name;
    }

    /**Returns the name of the Neural Network*/
    public String getName(){
        return name;
    }

    /**The neural network may be observed by other objects, this method allows observers to indicate that they wish to be notified of changes*/
    public void registerObserver(BCKObserver ob){
        if(observers == null)
            observers=new Vector(0);
        observers.addElement(ob);
    }

    public void removeObserver(BCKObserver ob){
        observers.removeElement(ob);
    }

    /**This method calls the updateObserver() method of each observer*/
    public void notifyObservers(){
        for(int i=0;i<observers.size();i++){
            if(observers.elementAt(i) != null)
                ((BCKObserver)observers.elementAt(i)).updateObserver();  
        }
    }

    /** Insert neuron n at position i 
     *@exception Exception thrown if index is illegal*/
    public void insertNeuron(BCKNeuron n, int i) throws Exception{
        if(i<0||i>neurons.size()){
            throw new Exception("Attempt to insert neuron with illegal index : "+i);
        }
        n.id=neurons.size(); 
        neurons.insertElementAt(n,i);//renumber neurons:
        for(int j=0;j<neurons.size();j++){
            ((BCKNeuron)neurons.elementAt(j)).id=j;
        }
        renewConnections();
    }

    /** Append neuron n to network */
    public void addNeuron(BCKNeuron n){
        n.id=neurons.size();
        neurons.addElement(n);
    }

    //**************** Morphing Methods ***************
    /**Add a new neuron of a specified type, valid types are "Sigmoid" "Binary" "Bipolar" "Linear" and "Radial"*/
    public void addNamedNeuron(String type){
        int id = neurons.size();
        if(type.equals("Sigmoid")){
            BCKSigmoidNeuron newNeuron = new BCKSigmoidNeuron();  
            newNeuron.id=id;
            neurons.addElement(newNeuron);
        } 
        else if(type.equals("Binary")){
            BCKBinaryNeuron newNeuron = new BCKBinaryNeuron();
            newNeuron.id=id;
            neurons.addElement(newNeuron);
        } 
        else if(type.equals("Bipolar")){
            BCKBipolarNeuron newNeuron = new BCKBipolarNeuron();
            newNeuron.id=id;
            neurons.addElement(newNeuron);
        } 
        else if(type.equals("Linear")){
            BCKLinearNeuron newNeuron = new BCKLinearNeuron();
            newNeuron.id=id;
            neurons.addElement(newNeuron);
        }
        else if(type.equals("Radial")){
            BCKRadialNeuron newNeuron = new BCKRadialNeuron();
            newNeuron.id=id;
            neurons.addElement(newNeuron);
        } 
        else {
            System.out.println("Error in addNamedNeuron in BCKNeuralNetwork - invalid neuron type");
            return;
        }
    }


    /**Get the neurons which form the inputs to neuron n */
    public BCKNeuron[] getInputArray(int n){//get the input neurons of neuron n
        return ((BCKNeuron)neurons.elementAt(n)).getInputArray();
    }

    /**Get the ids of neurons which feed into neuron i
     *@exception Exception thrown if i is an invalid index*/
    public int[] getInputArrayIds(int i) throws Exception{
        BCKNeuron n = getBCKNeuron(i);
        return n.getInputArrayIds();
    }


    /** Connect twon neurons whithin this network 
     *@exception Exception thrown if neurons do not exist*/
    public void connectInternal(int pre, int post,double weight, int delay) throws Exception{
        //connect output of pre to input of post, where 
        //pre and post are indexes into the vector
        if(!(validIndex(pre)&&validIndex(post))){
            throw new Exception("Illegal values passed to connectInternal in BCKNeuralNetwork - pre = "+pre+" post = "+post+" size of neurons vector = "+neurons.size());
        }
        //all ok here, connect post to pre
        BCKNeuron postNeuron = getBCKNeuron(post);
        BCKNeuron preNeuron = getBCKNeuron(pre);
        BCKNeuralNetwork.connect(preNeuron,postNeuron,weight,delay);
        connections.addElement(new BCKConnection(pre,post,weight,delay));
    }

    /** Return the BCKNeuron object at index i*/
    private BCKNeuron getBCKNeuron(int i) throws Exception{
        if( neurons.elementAt(i) instanceof BCKNeuron){
            return (BCKNeuron)neurons.elementAt(i);
        }
        throw new Exception("Non BCKNeuron object in Neural Nework - FATAL ERROR");
    }

    //connect a neuron in this network to a neuron in another network:
    public void connectExternal(BCKNeuron pre, int post, double weight, int delay) throws Exception{
        if(validIndex(post)){
            BCKNeuron postn = (BCKNeuron)neurons.elementAt(post);
            BCKNeuralNetwork.connect(pre,postn,weight,delay);
        }
    }

    /**connect two neurons
     *@exception Exception thrown if the two neurons cannot be connected
     */
    public static void connect(BCKNeuron pre, BCKNeuron post, double weight, int delay) throws Exception{
        post.connect(pre,weight,delay);
    }

    /** Set the name of neuron n to name*/
    public void setNeuronName(int n, String name){
        getNeuron(n).setName(name);
    }

    public String getNeuronName(int n){
        return getNeuron(n).getName();
    }

    // change the weight of the connection between neurons pre and post
    // which has delay d by dw
    //post synaptic neurons are always in this network, presynaptic neurons
    //may not be, as we can model inputs from neurons in other networks.
    public void deltaWeight(BCKNeuron preNeuron, int post, int delay, double dw) throws Exception{
        BCKNeuron postn = ((BCKNeuron)neurons.elementAt(post));
        postn.deltaWeight(preNeuron,delay,dw);
    }


    /**specify a file which contains training data and create a BCKFilter object to handle it:
     *@exception Exception thrown when file not found error occurs
     */
    public void setTrainingFile(String name) throws Exception {
        trainData = new BCKFilter(name,numInputs+numOutputs);
    }

    /**return the name of the training file*/
    public String getTrainingFile(){
        return trainData.getFileName();
    }

    /**Specify a file of test data
     *@exception Exception thrown when file not found error occurs
     */
    public void setTestingFile(String name) throws Exception{
        testData = new BCKFilter(name,numInputs+numOutputs);
    }

    public String getTestingFile(){
        return testData.getFileName();
    }
    /** Specify an input data file
     *@exception Exception thrown when file not found error occurs
     */
    public void setInputFile(String name) throws Exception{
        inputData = new BCKFilter(name,numInputs+numOutputs);
    }

    public String getInputFile(){
        return inputData.getFileName();
    }


    /**get the next input data record
     *@exception Exception thrown if inputData file not specified previously*/
    public double[] getNextInputRecord() throws Exception{
        if(inputData==null){
            throw new Exception("Cannot get next input record until input file has been set");
        }  
        return inputData.getNextRecord();
    }

    //************************ Processor Methods
    /**Assume that inputs have been set, calculate outputs */

    public void forwardPass(){
        double tmp;
        //propogate input through network
        try{
            for(int i=numInputs;i<neurons.size();i++){
                tmp=calcState(i);   
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }



    /** Take supplied input vector into network, and classify it, returning the output neurons' output vector This method is critical for multi network brain systems - override as necessary
     *@exception Exception thrown if record size does not match Network inputs*/
    public double[] classify(double []inputVector) throws Exception{
        //feed input into network:
        setInput(inputVector);
        //propogate input through network
        forwardPass();
        return getOutputs();
    }


    /** Take input vector from file into network, and classify it, returning the network's output vector - loads the next record and calls classify()
     *@exception Exception thrown if record size is incorrect
     */
    public double[] classifyNextRecord() throws Exception{
        return classify(getNextInputRecord());
    }

    /** feed the supplied input vector into the first numInputs neurons in the network:
     */
    public void setInput(double[] inputVector){
        for(int i=0;i< numInputs; i++){
            setOutput(i, inputVector[i]);
        }
    }

    //calculate the new output of neuron n, and return it
    public double calcState(int n) throws Exception{
        if(validIndex(n)){
            return ((BCKNeuron)neurons.elementAt(n)).calcState();
        } 
        else {
            throw new Exception("Invalid Index in calcState");
        }
    }

    //recalculate the connections vector data structure to reflect 
    //current network state
    public void renewConnections(){
        BCKNeuron currentNeuron;
        BCKSynapse currentSynapse;
        BCKSynapse[] synapseArray;

        connections=null;  //delete old structure
        connections = new Vector(0);
        for(int i=0;i<neurons.size();i++){
            currentNeuron = ((BCKNeuron)neurons.elementAt(i));
            synapseArray=currentNeuron.getSynapseArray();
            for(int j=0;j<synapseArray.length;j++){
                currentSynapse=synapseArray[j];      
                connections.addElement(new BCKConnection(currentSynapse.output.id,i,currentSynapse.weight,currentSynapse.delay));
            }

        }
    }

    //************** Accessor Methods **************

    //the getOutputs method should be implemented by all subclasses,
    //since the filter object needs this method to get the output of
    //nets which feed into other nets, it should return an array of doubles
    //representing the current outputs of all output neurons.
    public double[] getOutputs(){
        double[] outputVector = new double[numOutputs];  
        int j=0;
        for(int i=neurons.size()-numOutputs;j<numOutputs;j++){
            outputVector[j]=getState(i+j);
        }
        return outputVector;
    }

    /** Returns this.numInputs */
    public int getNumInputs(){
        return numInputs;
    }
    /** Returns this.numOutputs */
    public int getNumOutputs(){
        return numOutputs;
    }

    public BCKNeuron getNeuron(int i){
        return ((BCKNeuron)neurons.elementAt(i));
    }

    public double getPrime(int n){ // return the derivative of the transfer function of neuron n
        return ((BCKNeuron)neurons.elementAt(n)).fprime();
    }
    /**set the output of a neuron n to o, this is useful for dummy neurons
     *      such as input neurons:*/
    public void setOutput(int n, double o){
        if(validIndex(n)){
            ((BCKNeuron)neurons.elementAt(n)).setOutput(o);
        }
        return;
    }


    //return the output of a given neuron, as it was t moments ago:
    public double getState(int n, int t) {
        if(validIndex(n)){
            try {
                return ((BCKNeuron)neurons.elementAt(n)).getState(t);
            }
            catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return 0.0;

    }

    //get the current state of a neuron
    public double getState(int n){
        return getState(n,0);
    }

    //return the number of a neuron in this network:
    public int getNeuronNumber(BCKNeuron n){
        BCKNeuron neuron;
        for(int i=0;i< neurons.size(); i++){
            neuron = ((BCKNeuron)neurons.elementAt(i));
            if(neuron==n){
                return i;
            }
        }
        return -1;
    }

    //return true if neuron pre feeds into neuron post, else false
    public boolean areConnected(int pre,int post){
        if(validIndex(pre) && validIndex(post)){
            BCKNeuron postn =  ((BCKNeuron)neurons.elementAt(post));
            BCKNeuron pren =  ((BCKNeuron)neurons.elementAt(pre));
            return postn.isConnectedTo(pren);
        }
        return false;
    }


    /**return connection between neurons pre and post*/
    public BCKSynapse getSynapse(int pre, int post){
        if(validIndex(pre) && validIndex(post)){
            BCKNeuron postn = ((BCKNeuron)neurons.elementAt(post));
            BCKNeuron pren =  ((BCKNeuron)neurons.elementAt(pre));
            return postn.getSynapse(pren);
        }
        return null;
    }

    /**return weight of connection between neurons pre and post*/
    public double getWeight(int pre, int post){
        BCKSynapse synapse;
        if(validIndex(pre) && validIndex(post)){
            BCKNeuron postn = ((BCKNeuron)neurons.elementAt(post));
            BCKNeuron pren =  ((BCKNeuron)neurons.elementAt(pre));
            synapse = postn.getSynapse(pren);
            if(synapse==null) return 0.0; //there is no connection ie weight = 0;
            return synapse.getWeight();
        }
        return 0.0;  
    }

    //return the number of neurons in the network
    public int getNumberOfNeurons(){
        return neurons.size();
    }

    //************** Miscellaneous Methods  *****************
    private boolean validIndex(int i){
        if(i<0 || i>neurons.size()){
            return false;
        }
        else {
            return true;
        }
    }

    /** In some architectures, winner take all schemes are implemented for o/p neurons, do this here, by overriding this method in subclasses*/
    public void normaliseOutputNeurons(){
        double max = -100000;
        int maxIndex = 0;
        int j=0;
        for(int i=neurons.size()-numOutputs;j<numOutputs;j++){
            if(max < getState(i+j)){
                max = getState(i+j);
                maxIndex = i+j;
            }
        }

        j=0;
        for(int i=neurons.size()-numOutputs;j<numOutputs;j++){
            if(i+j != maxIndex){
                setOutput(i+j,0.0);
            } 
            else {
                setOutput(maxIndex,1.0);
            }      
        }
    }

    /**return the winning neuron (the neuron in the output layer with the highest output)*/
    public BCKNeuron getWinner(){
        double max=-1000;
        BCKNeuron winner = null;
        for(int i = neurons.size()-1; i > neurons.size() - numOutputs-1;i--){
            if(max < getState(i)){
                winner = getNeuron(i);
                max = getState(i);
            }
        }
        return winner;
    }

    /** Reads in the test data file and compares the network's output with the desired output. If the difference between actual and desired output divided by desired output is > .2 then the network is deemed to have incorrectly classified an input. The method returns the percentage of correct classifications
     *@exception Exception thrown if a file of test data has not been specified
     */
    public double test() throws Exception{
        double[] rec;
        double[] inputVector = new double[numInputs];
        double[] outputVector;
        double[] desiredOutput = new double[numOutputs];
        int correct = 0;
        boolean thisRecordCorrect = false;
        if(testData == null){
            throw new Exception("You must specify a test file before testing");
        }
        int NumberOfRecords = testData.getNumberOfRecords();
        for(int i = 0;i<NumberOfRecords;i++){
            rec = testData.getNextRecord();  //get the next record  
            for(int j=0;j<numInputs;j++) inputVector[j]=rec[j];  //extract inputs
            for(int j=numInputs;j<numOutputs+numInputs;j++) desiredOutput[j-numInputs]=rec[j];  //extract desired outputs
            outputVector=classify(inputVector);
            //compare outputs with desired outputs:
            thisRecordCorrect=true;
            for(int j=0;j<numOutputs;j++){
                if(Math.abs(desiredOutput[j]-outputVector[j])>.2){
                    thisRecordCorrect=false;
                }
            }
            if(thisRecordCorrect){
                correct++;
            }
        }
        double percent = correct*100/NumberOfRecords;
        return percent;
    }


    public void setId(int d){
        this.idNumber=d;
    }

    public int getId(){
        return this.idNumber;
    }
}
