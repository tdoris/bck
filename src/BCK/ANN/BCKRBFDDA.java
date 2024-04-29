package BCK.ANN;
import java.util.*;
import java.io.*;

/**This class provides the Dynamic Decay Adjustment training algorithm for RBF nets
 */
public class BCKRBFDDA extends BCKRBF implements Serializable {

    //must not allow construction of uninitialised network
    protected BCKRBFDDA(){
    }

    /**Constructor Creates RBF with specified number of input and output nodes
     *@exception Exception thrown if illegal parameters supplied - inputs & outputs must be >0.
     */
    public BCKRBFDDA(int inputs, int outputs) throws Exception{
        super(inputs,outputs);
    }
    /**Examines the outputs and returns the index of the output neuron with
     *    the largest activation*/ 
    public int getWinningNeuron(){
        double max=-1;
        double currOutput;
        int indexOfMax=0;
        int j=0;
        for(int i=numInputs+numHidden; j < numOutputs; j++){
            currOutput=getState(i+j);
            if(currOutput>max){
                indexOfMax=i+j;
                max=currOutput;
            }
        }
        return indexOfMax;
    }
    /**We need to override the normal forwardpass so that the outputs are notmalised (all set to 0.0 bar the winner which is set to 1.0)*/
    public void forwardPass(){
        super.forwardPass();
        normaliseOutputNeurons();
    }

    /**Examines the outputs and sets winner to 1.0, all others to 0.0*/ 
    public void normaliseOutputNeurons(){
        int indexOfMax=0;
        int current;
        int j=0;
        indexOfMax=getWinningNeuron();
        for(int i=numInputs+numHidden; j < numOutputs; j++){   
            current=i+j;
            if(current==indexOfMax){
                setOutput(current,1.0);      
            } 
            else {
                setOutput(current,0.0);
            }
        }
    }
    public int getWinningClass(){
        return getWinningNeuron()-numInputs-numHidden;
    }

    /** implements the Dynamic Decay Adjustment Algorithm for RBF nets
     *@exception Exception thrown if training data not properly supplied*/
    public void train(int numEpochs) throws Exception{
        /**Empirically derived best value for ThetaPlus 0.4*/
        double ThetaPlus = 0.4;
        /**Empirically derived best value for ThetaMinus is 0.2*/
        double ThetaMinus = 0.2;
        double LogThetaMinus = -1.66; //ln .19
        double[] rec = new double[this.numOutputs+this.numInputs]; //the current training record
        double[] inputVector = new double[this.numInputs];
        double[] outputVector = new double[this.numOutputs];
        double[] desiredOutput = new double[numOutputs]; //the desired output vector
        double x=0; //activation of neuron
        double newStdev=0;
        BCKNeuron desiredWinner;
        BCKRadialNeuron neighbour;
        int desiredWinnerIndex = 0;
        int hiddenWinner = -1;
        int []inputArrayOfWinner;
        int []inputArray;
        double[] outputWeights;
        int NumberOfRecords = trainData.getNumberOfRecords();
        int hidden=0;
        boolean []potentialNeighbours;

        if(trainData==null){
            throw new Exception("You must specify a training file before attempting to train the network.");  
        }

        if(trainData.getRecordSize()!=rec.length){
            throw new Exception("ERROR - Training file has record of length : "+trainData.getRecordSize()+" the current network requires records of length: "+rec.length);  
        }

        //start epoch
        for(int epochnumber = 0; epochnumber < numEpochs; epochnumber++){
            globalError=0.0;
            for(int recordNumber=0;recordNumber < NumberOfRecords;recordNumber++){
                //initialisation:
                hiddenWinner = -1;
                //get hidden node output weights
                outputWeights = new double[numHidden];
                for(int h=numInputs+numHidden;h<numInputs+numHidden+numOutputs;h++){
                    inputArray = ((BCKNeuron)neurons.elementAt(h)).getInputArrayIds();
                    for(int o=0;o<inputArray.length;o++){
                        outputWeights[inputArray[o]-numInputs] = getWeight(inputArray[o],h);
                    }
                }

                //get the next record	
                rec = trainData.getNextRecord();
                //extract inputs
                for(int i=0;i<numInputs;i++) {
                    inputVector[i]=rec[i];
                }

                //extract desired winning class
                for(int i=numInputs;i<numOutputs+numInputs;i++) {
                    if(rec[i]==1.0){
                        int classNumber = i-numInputs;
                        desiredWinnerIndex=numInputs+numHidden+classNumber;                 
                    }
                } 
                desiredWinner = (BCKNeuron)neurons.elementAt(desiredWinnerIndex);
                //get the RBF hidden nodes which feed into the desired winner:
                inputArrayOfWinner = desiredWinner.getInputArrayIds();        

                //get all nodes not feeding into winner:
                potentialNeighbours = new boolean[numHidden];

                for(int i=0;i<potentialNeighbours.length;i++){
                    potentialNeighbours[i]=true;
                }

                for(int i=0;i<inputArrayOfWinner.length;i++){
                    potentialNeighbours[inputArrayOfWinner[i]-numInputs]=false;
                }

                //feed input vector into network
                setInput(inputVector);
                //evaluate network's reaction to input
                forwardPass();

                //Check whether there is a node connected to the winning class
                //with activation > ThetaPlus, ignore those with less activation,
                //as we don't want any activations in the area of conflict   
                for(int h=0;h<inputArrayOfWinner.length;h++){
                    hidden=inputArrayOfWinner[h];
                    if((((BCKRadialNeuron)neurons.elementAt(hidden)).getState(0))>ThetaPlus){
                        //hidden node number 'hidden' has above threshold output
                        //strengthen weight from hidden to desired winner by +1.0:
                        hiddenWinner = hidden;
                        desiredWinner.deltaWeight(hidden,0,+1.0);      
                    }
                }

                //shrink neighbouring nodes:    
                for(int h=numInputs;h<numInputs+numHidden;h++){
                    if(potentialNeighbours[h-numInputs]){
                        if(ThetaMinus<((BCKNeuron)neurons.elementAt(h)).getState(0)){
                            //need to shrink neighbour h	  
                            neighbour=((BCKRadialNeuron)neurons.elementAt(h));
                            x=neighbour.getActivation();	   
                            newStdev = Math.sqrt(Math.abs(-x/(Math.log(outputWeights[h-numInputs])-LogThetaMinus)));	   
                            neighbour.setStdev(newStdev);
                        }
                    }
                }

                //if no hiddenWinner found, we must create one:
                if(hiddenWinner == -1){
                    //create new hidden node centred on current input
                    addHiddenNode(inputVector,desiredWinnerIndex);           
                }

            }
        }//end of epoch run

    }//end of train 


}
