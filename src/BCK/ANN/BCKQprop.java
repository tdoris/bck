package BCK.ANN;
import java.util.*;
import java.io.*;
//the quickprop algorithm is a heuristic advance on the standard Backprop algorithm,
//therefore I am subclassing the BCKQprop class from the BCKMLPBprop class, and
//simply augmenting the train() method, as everything else remains the same (almost)


public class BCKQprop extends BCKMLP {

    double startMu;
    double startEpsilon;

    protected BCKQprop(){
    }

    public BCKQprop(int[] numNeuronsPerLayer){
        super(numNeuronsPerLayer);
        startMu = .9;
        startEpsilon = 1.1;
    }

    public void setEpsilon(double e){
        startEpsilon = e;
    }
    public void setMu(double m){
        startMu = m;
    }


    //the augmented training method, implements Quickprop
    //converted from Fahlman's own lisp implementation

    public void train(int numEpochs) throws Exception{
        double[] rec = new double[numOutputs+numInputs]; //current training record
        double[] inputVector = new double[numInputs];    //current input vector
        double[] error = new double[numNeurons];         //error at each neuron
        double[] desiredOutput = new double[numOutputs]; //desired output vector
        double[] gradient = new double[numNeurons];      //error gradient at each neuron
        double dw;
        double o;
        //Fahlman-named variables:
        double mu = startMu;
        double epsilon = startEpsilon;
        double shrinkFactor = mu/(mu+1.0);

        double[][] Slope = new double[numNeurons][numNeurons]; //The slope associated with each weight at t
        double[][] oldSlope = new double[numNeurons][numNeurons]; //The slope associated with each weight at t-1

        BCKNeuron[] inputArray;

        if(trainData==null){
            throw new Exception ("You must specify a training file before attempting to train the network.");		
        }

        int NumberOfRecords = trainData.getNumberOfRecords();
        for(int epochnumber=0;epochnumber < numEpochs;epochnumber++){

            globalError=0.0;

            for(int recordNumber=0;recordNumber < NumberOfRecords;recordNumber++){
                rec = trainData.getNextRecordRandomly();  //get the next record  
                for(int i=0;i<numInputs;i++) inputVector[i]=rec[i];  //extract inputs
                for(int i=numInputs;i<numOutputs+numInputs;i++) desiredOutput[i-numInputs]=rec[i];  //extract desired outputs
                setInput(inputVector);  //feed input vector into network
                forwardPass();    //evaluate network's reaction to input

                int index=0;
                boolean isOutput;
                double sum = 0.0;
                double yi=0.0;  
                double fprime = 0.0; //derivative of current neuron's transfer function

                for(int post=numNeurons-1;post>numInputs-1; post--){
                    isOutput = (numNeurons - post) <= numOutputs;//rule is different for output neurons
                    o=getState(post);                        //current neuron's output
                    fprime = getPrime(post);	

                    if(isOutput){
                        index=(numNeurons-post-1);                   //going backwards
                        error[post] = desiredOutput[index]-o;
                        globalError+=error[post]*error[post];
                        gradient[post]=error[post]*fprime;
                    } 
                    else { //post is not an output neuron:
                        //calculate the sum of the weights by the gradients 
                        sum=0.0;
                        for(int postpost=numNeurons-1;postpost>post; postpost--){
                            sum+=getWeight(post,postpost)*gradient[postpost];	
                        }      
                        gradient[post] = fprime*sum;
                    }

                    inputArray = (getNeuron(post)).getInputArray();
                    for(int pre=0;pre< inputArray.length ;pre++){ //for each input to post
                        yi = inputArray[pre].getState(0);	   		
                        Slope[post][pre] -=  gradient[post]*yi;  //calculate Slope addition
                    }
                }
            }//end of record iteration

            //update weights
            for(int post=numNeurons-1;post>numInputs-1; post--){
                inputArray = (getNeuron(post)).getInputArray();
                for(int pre=0;pre< inputArray.length ;pre++){ //for each neuron that feeds into post:            
                    double weight = getWeight(pre,post);
                    double delta = lastWeightChange[post][pre];
                    double slope = Slope[post][pre];
                    double pSlope = oldSlope[post][pre];
                    double decay = -0.0001;
                    dw=quickprop(weight, delta, slope, pSlope, epsilon, decay, mu, shrinkFactor );
                    //the change in weight has now been calculated, implement it now:
                    //save the weight change :
                    lastWeightChange[post][pre] = dw;
                    //change the weight:	  
                    deltaWeight(inputArray[pre],post,0,dw);
                    //Save and reset the slope
                    oldSlope[post][pre] = Slope[post][pre];
                    Slope[post][pre]=0.0; 
                }
            }

        }//end of epochs iteration
    }

    /*	QUICKPROP -  Perform a weight update on the specified weight using
     * 	Scott Fahlman's QuickProp algorithm.  Weights, deltas, slopes and
     * 	previous slopes are passed to quickprop.Epsilon, decay, mu, and shrink factor are just passed in raw.
     */

    private double  quickprop  ( double weight, double delta,double slope, 
    double pSlope, double epsilon, double decay, double mu, 
    double shrinkFactor )
    {
        double w,		//  Weight being updated  
        d,		//  Delta value for this weight  
        s,		//  Slope for this weight  
        p,		//  Previous slope for this weight  
        nextStep = 0.0;	//  Step to be taken  

        w = weight;			//  Initialize local variables  
        s = slope + decay * w;	//  Add decay to the slope	
        d = delta;
        p = pSlope;

        if  ( d < 0.0 )  {   //if last change  was negative...
            if  ( s > 0.0 )		 //if slope is now positive...
                nextStep -= epsilon * s;	 

            if  ( s >= ( shrinkFactor * p ) ) 
                nextStep += mu * d;		

            else				
                nextStep += d * s / (p - s);	
        }
        else if  ( d > 0.0 )  {
            if  ( s < 0.0 )			
                nextStep -= epsilon * s;		
            if  ( s <= ( shrinkFactor * p ) )	
                nextStep += mu * d;		
            else				
                nextStep += d * s / (p - s);
        }
        else
            nextStep -= epsilon * s;	//  Last step was zero, so only use linear   
        // term					     
        return nextStep;  //return the amount to change the weight by
    }

}


