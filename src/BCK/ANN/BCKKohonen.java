package BCK.ANN;

import java.util.*;
import java.io.*;

/**This class implements the Kohonen Self Organising Feature map
 *   architecture, with a square planar output lattice */

public class BCKKohonen extends BCKNeuralNetwork {

    /** The number of neurons in along the side of the square output lattice*/
    public int side; 
    protected double globalError;
    /**Eta is the learning rate*/
    double currentEta = .9;
    double startEta = 0.9;
    double etaShrink = 0.1;
    int startNeighbourhood ;
    protected  BCKKohonen(){
        super();
    }
    /** Standard Constructor
     *@exception Exception thrown when outputs parameter specifies that the output layer should contain a number of outputs that is not a perfect square
     */
    public BCKKohonen(int inputs, int outputs) throws Exception{
        super();
        double weight;
        this.numInputs = inputs;
        this.numOutputs = outputs;
        this.side = (int)Math.sqrt(outputs);

        if(Math.sqrt(outputs)!=side){
            throw new Exception ("You must have a square number of output neurons in Kohonen");
        }

        startNeighbourhood = (int)Math.ceil((0.5)*side);
        //add the required neurons
        for(int i=0;i<inputs;i++)
            addNeuron( new BCKLinearNeuron());
        // use radial basis function neurons for o/p layer:
        for(int o=0;o<outputs;o++)
            addNeuron( new BCKRadialNeuron(1));
        //connect every output to every input:
        for(int post = inputs;post<inputs+outputs;post++){
            for(int pre=0;pre<inputs;pre++){
                weight = 2-4*Math.random();
                connectInternal(pre,post,weight,0);
            }
        }

    }

    public void setEta(double e){
        startEta = e;
    }
    public double getEta(){
        return startEta;
    }

    public void setNeighbourhood(int n){
        startNeighbourhood = n;
    }

    public int getNeighbourhood(){
        return startNeighbourhood;
    }

    /**specify a file which contains training data and create a BCKFilter object to handle it:
     *@exception Exception thrown when file not found error occurs
     */
    public void setTrainingFile(String name) throws Exception {
        trainData = new BCKFilter(name,numInputs);
    }
    /**Attach to the specified file for training data
     *@exception Exception thrown when file not found error occurs
     */
    public void setTestingFile(String name) throws Exception{
        testData = new BCKFilter(name,numInputs);
    }
    /**Attach to the specified file for input data
     *@exception Exception thrown when file not found error occurs
     */
    public void setInputFile(String name) throws Exception{
        inputData = new BCKFilter(name,numInputs);
    }


    /**Train the network on the supplied training data, there are distinct 'intervals in the Kohonen training algorithm, during which the component parameters of the algorithm assume different orders of magnitude - see literature for details.
     * Usually there will be only 2 intervals, the first 1000 iterations == interval 1, and the next 10000 iterations == interval 2, if the network hasn't settled to some degree by then there is probably something wrong with the chosen architecture or data set
     *@exception Exception thrown when training file has not been specified
     * 
     */

    public void train(int Iterations) throws Exception{
        currentEta = startEta;
        int time = 0;
        int intervalLength = 1000;      //first interval is 1000 time steps
        int intervalIncreaseFactor=10;  //interval increases by a factor of 10
        int intervalNumber = 1;         //track current interval number
        int WinnerX = 0;   //x-co-ordinate of winning neuron
        int WinnerY = 0;   //y-co-ordinate of winning neuron
        int indexWinner = 0;  //index in output layer of winning neuron
        double[] inputVector;
        int Neighbourhood = startNeighbourhood;
        int left, right, top, bottom;
        double[] weightArray;

        if(trainData==null){
            throw new Exception("You must specify a training file before attempting to train the network.");
        }
        int NumberOfRecords = trainData.getNumberOfRecords();

        for(int e=0;e<Iterations;e++){      
            //initialisation:
            globalError = 0.0;
            for(int r=0;r<NumberOfRecords;r++){     
                //get the next record	
                inputVector = trainData.getNextRecordRandomly();    
                //feed into network:
                classify(inputVector);
                //Find winning unit
                indexWinner = getWinningNeuron()-numInputs;
                //calculate global error:
                weightArray = ((BCKNeuron)getNeuron(getWinningNeuron())).getWeights();
                for(int f=0;f<numInputs;f++){
                    double d = weightArray[f] - inputVector[f];
                    globalError += Math.sqrt(d*d)/NumberOfRecords*this.numInputs;
                }     
                //find x & y co-ords:
                WinnerX  = indexWinner % side;
                WinnerY  = indexWinner / side;
                left = Math.max(0, WinnerX-Neighbourhood);       //calculate the position of the square
                right = Math.min(side-1, WinnerX+Neighbourhood);   //neighbourhood area around the 
                top = Math.max(0,WinnerY-Neighbourhood);         //winning neuron
                bottom = Math.min(side-1, WinnerY+Neighbourhood);
                //alter weights of winner:
                for(int w=0;w<inputVector.length;w++){
                    BCKNeuron n = getNeuron(getWinningNeuron());
                    weightArray = n.getWeights();
                    double deltaW = currentEta * (inputVector[w] - weightArray[w]);
                    n.deltaWeight(w,0,deltaW);
                }       
                //for each neighbour, alter weights accordingly:   
                for(int y=top;y<=bottom;y++){ 
                    for(int x=left;x<=right;x++){
                        int neuron = numInputs + side*y + x;
                        BCKNeuron n = getNeuron(neuron);
                        weightArray = n.getWeights();

                        for(int w=0;w<inputVector.length;w++){
                            double deltaW = currentEta * (inputVector[w] - weightArray[w]);
                            n.deltaWeight(w,0,deltaW);
                        }       
                    }
                }
            }
            //update interval tracking data:
            time++;
            //shrink Neighbourhood
            Neighbourhood = startNeighbourhood * (1 - (time/intervalLength));
            //shrink eta
            currentEta = startEta*(1 - (time/intervalLength));
            if(time>intervalLength){ // advance to next interval
                time=0;
                intervalLength *= intervalIncreaseFactor;
                Neighbourhood = 0;                  
                startEta *= etaShrink;
                currentEta = startEta; 
                intervalNumber++;
            } 
        }//end of iterations loop

    }

    /** Alter the weights of the inputs to a neuron so that they are normalised, ie the vector that they describe is of unit length
     *@exception Exception thrown if indexing error occurs
     * 
     */
    public void normaliseWeights(int neuron) throws Exception{
        double d=0;
        double r=0;
        BCKNeuron n = getNeuron(neuron);
        for(int pre = 0;pre < numInputs; pre++){
            r=getWeight(pre,neuron);
            d+=r*r;
        }
        d=Math.sqrt(d);
        //dividing each weight by the length gives a unit vector:
        for(int i=0;i<numInputs;i++){
            n.setWeight(i,0,getWeight(i,neuron)/d);
        }
    }

    /**Examines the outputs and returns the index of the output neuron with
     *    the largest activation*/ 
    public int getWinningNeuron(){
        double max=-1;
        double currOutput;
        int indexOfMax=0;
        int j=0;
        for(int i=numInputs; j < numOutputs; j++){
            currOutput=getState(i+j);
            if(currOutput>max){
                indexOfMax=i+j;
                max=currOutput;
            }
        }
        return indexOfMax;
    }

    /**Examines the outputs and sets winner to 1.0, all others to 0.0*/ 
    public void normaliseOutputNeurons(){
        int indexOfMax=0;
        int current;
        int j=0;
        indexOfMax=getWinningNeuron();
        for(int i=numInputs; j < numOutputs; j++){   
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
        return getWinningNeuron()-numInputs;
    }

    public double getGlobalError(){
        return globalError;
    }


}
