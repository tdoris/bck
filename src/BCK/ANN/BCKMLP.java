//Copyright Tom Doris 1997

package BCK.ANN;
import java.util.*;
import java.io.*;

/**This class implements a Multi Layer Perceptron */
public class BCKMLP extends BCKNeuralNetwork implements Serializable{

    protected int numNeurons;             //the total number of neurons
    protected transient double globalError;       //the squared error
    protected transient double[][] lastWeightChange;//remembers the last weight change
    protected int[] numNeuronsInLayer;    //the number of neurons in each layer
    protected BCKNeuron bias;
    //default constructor: privatised to prevent use
    protected BCKMLP(){
    }

    //parameterised constructor, numNeuronsPerLayer contains an entry describing
    //the number of neurons in each layer, the network is constructed fully connected:
    public BCKMLP(int[] numNeuronsPerLayer){
        super(0,"Sigmoid");
        numNeurons=0;
        BCKNeuron[] inputArray;
        numNeuronsInLayer = new int[numNeuronsPerLayer.length];
        numOutputs = numNeuronsPerLayer[numNeuronsPerLayer.length-1];
        numInputs = numNeuronsPerLayer[0];

        for(int i=0;i < numNeuronsPerLayer.length; i++){
            numNeuronsInLayer[i]=numNeuronsPerLayer[i];
            numNeurons+=numNeuronsPerLayer[i];
        }

        lastWeightChange = new double[numNeurons][numNeurons];

        double weight;
        int preStart=0;
        int postStart = numInputs;
        int fanIn = 0 ;	//the number of inputs to the current post-synaptic node
        double range = 0; //the computed range
        //create the required neurons
        for(int i = 0; i < numNeurons;i++){   
            addNeuron(new BCKSigmoidNeuron());
        }
        //create a dummy neuron whose output is always one in order to model bias
        bias = new BCKNeuron();
        bias.setOutput(1.0);
        bias.id=-1;

        //connect each neuron in a layer to all neurons in previous layer:
        for(int layer=1;layer < numNeuronsInLayer.length; layer++) { //for each layer 
            fanIn = numNeuronsInLayer[layer-1]; 
            range= 6.8/fanIn;
            for(int post=0; post < numNeuronsInLayer[layer]; post++) {//for each postsynaptic neuron...
                //connect to the bias neuron
                try{
                    connectExternal(bias,postStart+post,0.5,0);
                }
                catch(Exception e){
                    System.out.println("Error connecting to bias neuron"+e.toString());
                }

                for(int pre=0; pre < numNeuronsInLayer[layer-1]; pre++){//for each presynaptic neuron...
                    int nnum = preStart+pre;
                    int nnum2 = postStart+post;
                    //with random weight and no (zero) signal delay:      
                    weight=range*(0.5-Math.random());  
                    try {
                        connectInternal(preStart+pre,postStart+post,weight,0);
                    }
                    catch (Exception e){
                        System.out.println("Error in MLP constructor while connecting neurons: "+e.toString());
                    }
                }
            }
            preStart+=numNeuronsInLayer[layer-1];
            postStart+=numNeuronsInLayer[layer];
        }

    }


    public BCKNeuron getBias(){
        return bias;
    }

    //**************** Processor Methods ***********************

    //execute a forward pass through the network:
    public void forwardPass(){
        try{
            for(int i=numInputs; i < getNumberOfNeurons(); i++){   
                calcState(i);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void setInput(double[] inputVector){
        for(int i=0;i< inputVector.length; i++){
            setOutput(i, inputVector[i]);
        }
    }


    //************* Accessor Methods *************************
    public double[] getOutputs(){ //return current output of output neurons
        double[] outs = new double[numOutputs];
        int j=0;
        for(int i=numNeurons - numOutputs; j < numOutputs; j++){
            outs[j] = getState(i+j);
        }
        return outs;
    }

    /**return the current global error*/
    public double getGlobalError(){
        return globalError;
    }
    /** Reset all weights in the network to random values */
    public void randomiseWeights(){
        BCKSynapse[] synarray;
        for(int i=0;i<neurons.size();i++){  
            BCKNeuron node = (BCKNeuron)neurons.elementAt(i);    
            synarray = node.getSynapseArray();
            for(int s=0;s<synarray.length;s++){       
                synarray[s].weight = 3*(0.5-Math.random());       
            }
        }
        //must reset lastWeightChange to 0
        for(int i=0;i<lastWeightChange.length;i++){
            for(int j=0;j<lastWeightChange[i].length;j++){
                lastWeightChange[i][j]=0;
            }
        }

    }

    public int getNumberOfLayers(){
        return numNeuronsInLayer.length;
    }

    public int getNumberOfNeuronsInLayer(int i){
        return numNeuronsInLayer[i];
    }


}
