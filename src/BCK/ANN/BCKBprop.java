package BCK.ANN;
import java.util.*;
import java.io.*;


public class BCKBprop extends BCKMLP  implements Serializable{
    private double LearningRate;
    private double Momentum;

    //must not allow construction of uninitialised network
    protected BCKBprop(){
        LearningRate = .9;
        Momentum = .7;
    }


    public BCKBprop(int[] numNeuronsPerLayer){
        super(numNeuronsPerLayer);
        LearningRate = .9;
        Momentum = .7;
    }

    public void setLearningRate(double lr){
        this.LearningRate=lr;
    }
    public void setMomentum(double mom){
        this.Momentum=mom;
    }

    public void train(int numEpochs) throws Exception{
        double[] rec = new double[numOutputs+numInputs]; //the current training record
        double[] inputVector = new double[numInputs];    //the current input vector
        double[] error = new double[numNeurons];         //the error at each neuron
        double[] desiredOutput = new double[numOutputs]; //the desired output vector
        double[] gradient = new double[numNeurons];      //the error gradient at each neuron
        double dw=0.0;                                   //the change in wieght
        double o = 0.0;	  

        BCKNeuron[] inputArray;
        //the standard backprop requires that we add a momentum term, which in turn
        //requires that we 'remember' the weights as they were in the last training iteration

        if(trainData==null){
            throw new Exception("You must specify a training file before attempting to train the network.");	       
        }
        int NumberOfRecords = trainData.getNumberOfRecords();
        for(int epochnumber = 0; epochnumber < numEpochs; epochnumber++){
            globalError=0.0;
            for(int recordNumber=0;recordNumber < NumberOfRecords;recordNumber++){
                //get the next record	
                rec = trainData.getNextRecord();
                //extract inputs
                for(int i=0;i<numInputs;i++) inputVector[i]=rec[i];
                //extract desired outputs
                for(int i=numInputs;i<numOutputs+numInputs;i++) desiredOutput[i-numInputs]=rec[i];
                //feed input vector into network
                setInput(inputVector);
                //evaluate network's reaction to input
                forwardPass();
                //calculate the error measure and gradients on each output neuron
                //and change weights from hidden to output layer as appropriate:
                int index=0;
                boolean isOutput;
                double sum = 0.0;
                double yi=0.0;  
                double fprime = 0.0; 
                for(int post=numNeurons-1;post>numInputs-1; post--){
                    isOutput = (numNeurons - post) <= numOutputs;//rule is different for output neurons
                    o=getState(post);                        //current neuron's output
                    fprime = getPrime(post);

                    if(isOutput){
                        index=(numNeurons-post-1);                   //going backwards
                        error[post] = desiredOutput[index]-o;	   //calculate error gradient
                        globalError+=error[post]*error[post];	   //track global error
                        gradient[post]=error[post]*fprime;

                    } 
                    else { //post is not an output neuron:
                        //calculate the sum of the weights by the gradients of neurons 
                        //into which the current neuron feeds its output
                        sum=0.0;
                        for(int postpost=numNeurons-1;postpost>post; postpost--){
                            sum+=(getWeight(post,postpost)+lastWeightChange[postpost][post])*gradient[postpost];	
                        }      
                        gradient[post] = fprime*sum;
                    }

                    inputArray = getInputArray(post);
                    for(int pre=0;pre< inputArray.length ;pre++){ //for each neuron that feeds into post:
                        //alter the weight:
                        try{
                            yi = inputArray[pre].getState(0);
                        }
                        catch(Exception e)
                        {
                            System.out.println("Error getting output of "+pre+"th input to Neuron number "+post+" "+e.toString());
                        }
                        dw=Momentum*lastWeightChange[post][pre]+LearningRate*gradient[post]*yi;
                        //save the weight change :
                        lastWeightChange[post][pre] = dw;	    
                        //change the weight:
                        try{
                            deltaWeight(inputArray[pre],post,0,dw);
                        } 
                        catch (Exception e){ 
                            System.out.println("Error while updating weights "+e.toString());
                        }

                    }
                }
            }//end of record iteration
        }//end of epoch iteration
        //System.out.println("finished training run update, Global Error is now : " + globalError);
    }


}


