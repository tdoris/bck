package BCK.ANN;

import java.util.*;
import java.io.*;

/**
 * Model a connection from one the input of one
 * neuron to the output of another neuron 
 */
public class BCKSynapse implements Serializable{

    public BCKNeuron output;  //a reference to the neuron from which the output is taken
    public int delay;         //the time delay
    public double weight;    //the weight of the connection

    public BCKSynapse () {  //default constructor
        this.output=null;
        this.weight=0.0;
        this.delay=0;
    }

    //parameterised constructor:
    public BCKSynapse(BCKNeuron output, double weight, int delay){
        this.output=output;
        this.weight=weight;
        this.delay=delay;
    }


    //copy constructer:
    public BCKSynapse(BCKSynapse s){
        this.output = s.output;
        this.weight = s.weight;
        this.delay=s.delay;
    }


    //accessor functions
    public void deltaWeight(double dw){
        this.weight += dw;
    }

    public double getWeight(){
        return weight;
    }

    //getActivation returns this synapse's contribution
    //to the activation of postsynaptic neurons
    public double getActivation() throws Exception{
        return output.getState(delay)*weight;
    }

    //return reference to presynaptic neuron:
    public BCKNeuron getNeuron(){
        return output;
    }

    //return the delay implemented:
    public int getDelay(){
        return delay;
    }


}







