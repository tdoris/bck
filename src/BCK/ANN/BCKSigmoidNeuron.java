//Copyright Thomas Doris 1998


package BCK.ANN;
import java.util.*;
import java.io.*;

public class BCKSigmoidNeuron extends BCKNeuron implements Serializable{
    public BCKSigmoidNeuron(){
        super();
        type="Sigmoid";
    }

    public double fprime(){
        //return the derivative of the transfer function at the current activation level:
        return StateHistory[tick]*(1-StateHistory[tick]);
    }

    //using the current activation, calculate the neuron's new output
    protected double transfer(){
        //sigmoid transfer function
        timeAdvance();
        StateHistory[tick]=(1.0/(1.0+Math.exp(-(activation-0.5))));
        return StateHistory[tick];
    }

}
