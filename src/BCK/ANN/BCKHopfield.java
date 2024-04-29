
package BCK.ANN;

import java.util.*;
import java.io.*;


public class BCKHopfield extends BCKNeuralNetwork implements Serializable{

    protected int numNeurons;             //the total number of neurons

    //default constructor
    protected BCKHopfield(){
    }


    //main constructor 
    public BCKHopfield(int numNeurons){
        super(numNeurons,"Bipolar");
        this.numNeurons=numNeurons;
        this.numInputs = numNeurons;
        this.numOutputs = numNeurons;
    }
    /**specify a file which contains training data and create a BCKFilter object to handle it:
     *@exception Exception thrown when file not found error occurs
     */
    public void setTrainingFile(String name) throws Exception {
        trainData = new BCKFilter(name,numInputs);
    }
    /** Attach to the specified input file for test data
     *@exception Exception thrown when file not found error occurs
     */
    public void setTestingFile(String name) throws Exception{
        testData = new BCKFilter(name,numInputs);
    }
    /** Attach to the specified input file
     *@exception Exception thrown when file not found error occurs
     */
    public void setInputFile(String name) throws Exception{
        inputData = new BCKFilter(name,numInputs);
    }

    /**This method sets the weights of the Hopfield net
     *@exception Exception thrown when attempt to connect neurons fails - this may occur if the network structure is corrupted by editing*/

    public void train() throws Exception{
        int NumberOfRecords = trainData.getNumberOfRecords();
        double[][] records = new double[NumberOfRecords][numNeurons]; 

        for(int recordNumber=0;recordNumber < NumberOfRecords;recordNumber++){
            records[recordNumber] = trainData.getNextRecordBipolar();  //get the next record, converted to -1 and +1 fields
        }
        double sum;
        for(int i=0;i< numNeurons;i++){
            for(int j=0;j < numNeurons;j++){
                if(i!=j){
                    sum = 0.0;
                    for(int r=0;r<NumberOfRecords;r++){
                        sum+=records[r][i]*records[r][j];
                    }
                    connectInternal(j,i,sum,0);     
                }
            }
        }
    }

    /** Take supplied input vector into network, and classify it, returning the output neurons' output vector overriden from BCKNeuralNetwork
     *@exception Exception thrown if record size does not match Network inputs*/

    public double[] classify(double []inputVector) throws Exception{
        if(inputVector.length != numNeurons){
            throw new Exception("Number of fields in pattern : "+inputVector.length+"\n does not match the number of neurons :"+numNeurons+"\n in the Hopfield network");  
        }
        //set the maximum number of iterations to the number of neurons*10
        categorise(inputVector,10*numNeurons);
        //retreive the output values of the neurons
        return getOutputs();
    }

    /**This method handles the details of stochastic neuron updating during the categorisation process
     * @exception Exception thrown if network state is corrupt*/
    public void categorise(double[] pattern,int maxIterations) throws Exception{
        double[] lastState = new double[numNeurons];
        boolean stable=false;  
        int[] updateOrder= new int[numNeurons];

        //initialise the update order
        for(int i=0;i<numNeurons;i++){
            updateOrder[i]=i;
        }
        //feed in the input pattern
        for(int i=0;i<numNeurons;i++){
            setOutput(i,pattern[i]);
            lastState[i]=pattern[i];
        }

        //the main classification loop:
        int iterations = 0;
        while(!stable && iterations++ < maxIterations){
            //randomize update order
            for(int p=0;p<numNeurons;p++){
                if(Math.random()<0.5){ //half the time, swap current with another:
                    int other = (int)Math.floor(Math.random() * numNeurons);
                    int temp = updateOrder[p];
                    updateOrder[p] = updateOrder[other];
                    updateOrder[other]=temp;
                }
            }

            //update neurons
            for(int i=0; i < numNeurons; i++){     
                calcState(updateOrder[i]);      
            }
            //test for convergence on learned pattern:
            stable = true;  
            for(int n=0;n<numNeurons;n++){
                if(getState(n) != lastState[n]){
                    stable=false;
                    lastState[n]=getState(n);
                }
            }
        }
    }

    public int getBinaryState(int n){
        if(getState(n)>0.5)
            return 1;
        else 
            return 0;
    }

}
