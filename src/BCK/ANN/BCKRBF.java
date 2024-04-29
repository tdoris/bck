
package BCK.ANN;
import java.util.*;
import java.io.*;
/**This class  implements Radial Basis Function 3-Layer Network, learning algorithmes should be implemented in subclasses
 *@author Tom Doris
 */

public class BCKRBF extends BCKNeuralNetwork implements Serializable{

    protected int numNeurons;             //the total number of neurons
    protected int numHidden;
    protected double globalError;       //the squared error


    //default constructor: privatised to prevent use
    protected BCKRBF(){
    }


    //parameterised constructor, numNeuronsPerLayer contains an entry describing
    //the number of neurons in each layer, the network is constructed fully connected:
    public BCKRBF(int inputs, int outputs) throws Exception{
        this.numNeurons=inputs+outputs;
        this.numOutputs = outputs;
        this.numInputs = inputs;
        numHidden = 0;
        //create the required neurons
        for(int i = 0; i < numInputs;i++){   
            addNeuron( new BCKLinearNeuron());
        }
        for(int i=0;i< numOutputs;i++){
            addNeuron( new BCKLinearNeuron());
        }

        //this type of network starts with no connections, as it has no hidden nodes

    }


    //***************** Mutator Methods **********************
    /** Create a new hidden node
     *@param weights is the n-d point which is the centre of the new node
     *@param output is the output neuron id of the output to which this new node will be connected
     *@exception Exception thrown when weights n-d point is of wrong dimensionality
     */
    public void addHiddenNode(double []weights,int output) throws Exception{
        //validate params:
        if(weights.length!=this.numInputs){
            throw new Exception("Tried to add a hidden node in RBF with incorrect number of input weights specified");
        }
        int realop=output-numInputs-numHidden;
        //add the RBF neuron
        BCKRadialNeuron r = new BCKRadialNeuron();
        insertNeuron(r,this.numInputs+this.numHidden); //insert just after all input nodes +hiddens
        //connect to inputs with specified weights
        for(int i=0;i<numInputs;i++){
            connectInternal(i,r.id,weights[i],0);
        } 
        //connect output node to new hidden one, output id has increased by 1 due 
        //to insertion of the new hidden node:
        connectInternal(r.id,output+1,1,0);
        this.numNeurons++;
        this.numHidden++;   
    }


    //**************** Processor Methods ***********************

    public void setInput(double[] inputVector){
        for(int i=0;i< numInputs; i++){
            setOutput(i, inputVector[i]);
        }
    }



    //************* Accessor Methods *************************
    public double[] getOutputs(){ //return current output of output neurons
        double[] outs = new double[numOutputs];
        int j=0;
        for(int i=numInputs+numHidden; j < numOutputs; j++){
            outs[j] = getState(i+j);
        }
        return outs;
    }

    public int getNumHidden(){
        return this.numHidden;
    }


    //return the current global error:
    public double getGlobalError(){
        return globalError;
    }

}








