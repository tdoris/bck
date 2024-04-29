//this is an base classs implementing a generalised neuron
//with a sigmoid transfer function
//which accomodates normal neuron functionality,
//as well as providing for additional Time Delay functionality

//Note: the word 'moment' as used here is defined as 
// a single time unit, or the separation between 
// consecutive evaluations of a neuron's output

package BCK.ANN;

import java.util.*;
import java.io.*;

public class BCKNeuron implements Serializable{

    public int id;
    protected String type;   
    protected String Name;
    protected int MemorySize;        //specifies the size of the history array
    protected double[] StateHistory; //a record of the state of the neuron, and the current state
    protected Vector inputs;         //a vector synapses to other neurons
    protected int tick;              //rotating counter
    protected double activation;     //currently received activation

    //default constructor
    public BCKNeuron(){
        inputs = new Vector(0);
        MemorySize = 10;                       //default memory size
        StateHistory = new double[MemorySize];  //storage for old states
        tick=0;
        activation=0;
        type = "Neuron";
        Name = "No Name";
    }


    //************* Morphing Methods *******************

    /**Remove all inputs */
    public void removeInputs(){
        inputs = new Vector(0);
    }

    /**connect to neuron n with weight w and delay 'delay'
     *@exception Exception thrown when 'n' does not exist, or delay <0 */
    public void connect(BCKNeuron n, double w, int delay) throws Exception{
        if(n==null){
            throw new Exception("Attempt to connect to null neuron in connect in BCKNeuron");
        } 
        if(delay < 0){
            throw new Exception("Attempt to connect to neuron with negative delay in connect in BCKNeuron");
        }
        //add a synapse to inputs
        inputs.addElement(new BCKSynapse(n,w,delay));
    }

    /**change the weight of the connection to neuron pre with delay d by dw
     *@exception Exception thrown when attempt made to change weight of non-existent connection.
     */
    public void deltaWeight(BCKNeuron pre, int delay, double dw) throws Exception {
        //first find the number of the synapse modelling this connection
        BCKSynapse syn;
        for(int i=0;i<inputs.size();i++){
            syn=(BCKSynapse)inputs.elementAt(i);
            if(pre==syn.output && delay==syn.delay){
                syn.weight+=dw;
                return;
            }   
        }
        //couldn't find synapse, so throw an exception
        throw new Exception("Attempt to alter the weight of a non-existent synapse in deltaWeight in BCKNeuron");
    }


    /**change the weight of the connection to neuron with id 'neuronid' with delay d by dw
     *@exception Exception thrown when attempt made to change weight of non-existent connection.
     */
    public void deltaWeight(int neuronid, int delay, double dw) throws Exception{
        //first find the number of the synapse modelling this connection
        BCKSynapse syn;
        for(int i=0;i<inputs.size();i++){
            syn=(BCKSynapse)inputs.elementAt(i);
            if(neuronid==syn.output.id && delay==syn.delay){
                syn.weight+=dw;
                return;
            }   
        }
        //couldn't find synapse, so throw an exception
        throw new Exception("Attempt to alter the weight of a non-existent synapse in deltaWeight in BCKNeuron");
    }

    public void setWeight(int neuronid, int delay, double newWeight) throws Exception{ 
        //first find the number of the synapse modelling this connection
        BCKSynapse syn;
        for(int i=0;i<inputs.size();i++){
            syn=(BCKSynapse)inputs.elementAt(i);
            if(neuronid==syn.output.id && delay==syn.delay){
                syn.weight=newWeight;
                return;
            }   
        }
        //couldn't find synapse, so throw an exception
        throw new Exception("Attempt to alter the weight of a non-existent synapse in setWeight in BCKNeuron");
    }


    //***************   Processing Methods *******************
    //calculate the current activation level
    protected double calcActivation() throws Exception{ 
        this.activation=0;
        BCKSynapse syn;
        for(int i=0;i<inputs.size();i++){
            syn=(BCKSynapse)inputs.elementAt(i);
            this.activation+=syn.output.getState(syn.delay)*syn.weight;
        }
        return this.activation;
    }

    public double fprime(){
        //return the derivative of the transfer function at the current activation level:
        return StateHistory[tick]*(1-StateHistory[tick]);
    }

    //using the current activation, calculate the neuron's new output
    protected double transfer(){
        //sigmoid transfer function
        timeAdvance();
        StateHistory[tick]=(1.0/(1.0+Math.exp(-(this.activation-0.5))));
        return StateHistory[tick];
    }

    //advance the clock of this neuron:
    protected void timeAdvance(){
        this.tick=(this.tick+1)%MemorySize;
    }

    //calculate the neuron's new output
    public double calcState() {
        try {
            calcActivation();
        }
        catch (Exception x) { 
            System.out.println(x.toString());
        }
        return transfer();
    }

    //******************** Accessor Methods ************************
    //return an array of input weights:
    public double[] getWeights(){
        double []array = new double[inputs.size()];
        for(int i=0;i<inputs.size();i++){
            array[i]=((BCKSynapse)inputs.elementAt(i)).weight;
        }
        return array;
    }

    //return an array of all the neurons that feed into this one
    public BCKNeuron[] getInputArray(){
        BCKNeuron[] array = new BCKNeuron[inputs.size()];
        BCKSynapse syn;
        for(int i=0;i<inputs.size();i++){
            syn=(BCKSynapse)inputs.elementAt(i);
            array[i]=syn.output;
        }
        return array;
    }

    //return all synapses:
    public BCKSynapse[] getSynapseArray(){
        BCKSynapse[] array = new BCKSynapse[inputs.size()];
        for(int i=0;i<inputs.size();i++){ 
            array[i]=(BCKSynapse)inputs.elementAt(i);
        }
        return array;
    }

    public int[] getInputArrayIds(){
        int[] array = new int[inputs.size()];
        BCKSynapse syn;
        for(int i=0;i<inputs.size();i++){
            syn=(BCKSynapse)inputs.elementAt(i);
            array[i]=syn.output.id;
        }
        return array;
    }

    public double getActivation(){
        return this.activation;
    }
    public String getType(){
        return type;
    }

    /**set the output of the neuron and shift the history left*/
    public void setOutput(double o){
        timeAdvance();
        StateHistory[tick]=o;
    }

    /**Specify a name for this neuron*/
    public void setName(String n){
        this.Name = n;
    }

    /**retrieve the name of this neuron*/
    public String getName(){
        return this.Name;
    }

    /**return the state as it was t moments ago*/
    public double getState(int t){ 
        if(t >= MemorySize || t<0) {
            return 0.0;
        }
        else{
            if (tick>=t){
                return StateHistory[tick-t];
            }
            else{
                return StateHistory[MemorySize-(t-tick)];
            }
        }
    }


    /*return true if this is connected to neuron n*/
    public boolean isConnectedTo(BCKNeuron n){
        BCKSynapse syn;
        //find the connection:
        for(int i=0;i< inputs.size(); i++){
            syn = (BCKSynapse)inputs.elementAt(i);
            if(((BCKNeuron)syn.output)==n){
                return true;
            }
        }
        return false;
    }
    /*return true if this is connected to neuron n*/
    public boolean isConnectedTo(BCKNeuron n, int delay){
        BCKSynapse syn;
        //find the connection:
        for(int i=0;i< inputs.size(); i++){
            syn = (BCKSynapse)inputs.elementAt(i);
            if(((BCKNeuron)syn.output)==n && syn.delay == delay){
                return true;
            }
        }
        return false;
    }
    /**return the connection to neuron n*/
    public BCKSynapse getSynapse(BCKNeuron n){
        BCKSynapse syn;
        //find the connection:
        for(int i=0;i< inputs.size(); i++){
            syn = (BCKSynapse)inputs.elementAt(i);
            if(((BCKNeuron)syn.output)==n){
                return syn;
            }
        }
        return null;

    }

}


