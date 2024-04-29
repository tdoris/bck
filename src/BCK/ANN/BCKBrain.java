package BCK.ANN;

import java.util.*;
import java.io.*;

/** The Brain class is a container for Neural Networks. It also specifies exactly how the output of one neural network is used as the input to another neural network. This is the toplevel class in the system, may be implemented as a singleton*/

public class BCKBrain extends BCKRBFDDA implements Serializable{ 

    protected Vector Nets;  //vector of component neural networks

    public Vector proxyInputs;  

    /** Default Constructor */
    public BCKBrain(){
        super();
        Nets=new Vector(0);
        proxyInputs=new Vector(0);
        this.idNumber = 0;
        addNetwork(this);
    }


    public void setNumInputs(int inputs){
        numInputs = inputs;
    }

    public void setNumOutputs(int outputs){
        numOutputs = outputs;
    }

    /** add a neural network to the brain*/
    public void addNetwork(BCKNeuralNetwork n){
        n.setId(Nets.size());
        Nets.addElement(n);
        //add a vector to store proxyInputs:
        proxyInputs.addElement(new Vector(0));
    }

    /**Get network at index i*/
    public BCKNeuralNetwork getNet(int i){
        return ((BCKNeuralNetwork)Nets.elementAt(i));
    }


    public void forwardPass(){
        //propogate input through network of networks
        for(int i=1;i<Nets.size();i++){
            setInputsForNet(i);
            ((BCKNeuralNetwork)Nets.elementAt(i)).forwardPass();         
        }
        //then evaluate brain's own non-input neurons
        super.forwardPass();
        //then override with any proxy inputs to the brain output layer:
        setInputsForNet(0);
    }

    /** Use the output values of a neuron in another network as the input data to be classified by another network
     */
    public void setProxyInput(int sourceNet, int sourceNeuron, int targetNet, int targetNeuron, double weight,int delay){
        Vector targetNetConnections = ((Vector)proxyInputs.elementAt(targetNet));
        //its a little redundant storing the target net here, but hey...
        targetNetConnections.addElement(new BCKNetConnection(sourceNet, sourceNeuron, targetNet, targetNeuron, weight, delay));
    }

    /** Retrieve the input vector for the specified network and set the specified 'input' nodes' outputs to the appropriate values
     */
    private void setInputsForNet(int n){
        Vector proxyConnections = ((Vector)proxyInputs.elementAt(n));
        BCKNeuralNetwork net = ((BCKNeuralNetwork)Nets.elementAt(n));
        BCKNetConnection Conn;
        double []inputArray= new double[net.getNumInputs()];
        double input;

        for(int i=0;i<proxyConnections.size();i++){
            Conn = ((BCKNetConnection)proxyConnections.elementAt(i));     
            if(Conn.sourceNet == 0 ) { //read from brain inputs
                input=getState(Conn.sourceNeuron);
            } 
            else { //read from other network neuron: 
                input=getState(Conn.sourceNet, Conn.sourceNeuron, Conn.delay); 
            }
            setOutput(Conn.targetNet, Conn.targetNeuron, input*Conn.weight);
        }
    }


    /**get the next input data record
     *@exception Exception thrown if inputData file not specified previously*/
    public double[] getNextInputRecord(BCKFilter CurrentInput) throws Exception{
        if(CurrentInput==null){
            throw new Exception("Cannot get next input record until input file has been set");
        }  
        return CurrentInput.getNextRecord();
    }

    /** Get the output of neuron in network */
    public double getState(int network, int neuron, int delay){
        BCKNeuralNetwork net = ((BCKNeuralNetwork)Nets.elementAt(network));
        return net.getState(neuron,delay);
    }

    public void setOutput(int network, int neuron,double output){
        BCKNeuralNetwork net = ((BCKNeuralNetwork)Nets.elementAt(network));
        net.setOutput(neuron,output);
    }

    /** Retrieve the number of networks in the brain*/
    public int size(){
        return Nets.size();
    }

    /**Retrieve the network at position i*/
    public BCKNeuralNetwork elementAt(int i){
        return ((BCKNeuralNetwork)Nets.elementAt(i));
    }

    /**Remove the specified network from the brain*/
    public void removeElement(BCKNeuralNetwork n){
        Nets.removeElement(n);
        BCKNetConnection nc;
        //remove the proxy input vector entries corresponding to this network 
        int removeNetId = n.idNumber;
        proxyInputs.removeElementAt(removeNetId);
        //renumber all other networks    
        for(int i=0;i<proxyInputs.size();i++){
            ((BCKNeuralNetwork)Nets.elementAt(i)).idNumber=i;
            Vector targetNetConnections = ((Vector)proxyInputs.elementAt(i));
            for(int j=0;j<targetNetConnections.size();j++){
                nc=((BCKNetConnection)targetNetConnections.elementAt(j));

                if(nc.sourceNet == removeNetId || nc.targetNet == removeNetId){
                    //remove this entry
                    targetNetConnections.removeElement(nc);  
                } 
                else{          
                    //renumber network connections
                    if(nc.sourceNet > removeNetId){        
                        nc.sourceNet--;
                    }
                    if(nc.targetNet > removeNetId){        
                        nc.targetNet--;
                    }
                }            
            }   
        }
    }

}

