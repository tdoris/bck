//Copyright Thomas Doris 1998


package BCK.ANN;
import java.util.*;
import java.io.*;

/** BCKRadialNeuron implements neurons whose transfer function is a parameterised Gaussian Bell curve and whose activation is calculated as the euclidean distance of the input weight vector from the input vector - these type of neurons are used in the hidden layer of RBF nets
 */
public class BCKRadialNeuron extends BCKNeuron implements Serializable{
    /** stdev - standard deviation of Gaussian transfer Function*/
    protected double stdev; 


    /**Default Constructor*/
    public BCKRadialNeuron(){
        super();
        type="Radial";
        stdev=1000;
    }

    /**Constructor with supplied value for standard deviation*/
    public BCKRadialNeuron(double s){
        super ();
        type="Radial";
        stdev = s;
    }

    /**Calculates the derivative of the transfer function at the current activation level:
     */
    public double fprime(){
        return -Math.exp(-activation);
    }

    /**Calculate activation using n-dimensional Euclidean distance
     *@exception Exception thrown if illegal delay specified in synapse
     */
    public double calcActivation() throws Exception{
        double d;
        this.activation=0;
        BCKSynapse syn;
        for(int i=0;i<inputs.size();i++){
            syn=(BCKSynapse)inputs.elementAt(i);
            d=(syn.output.getState(syn.delay)-syn.weight);
            this.activation+=d*d;
        }

        return this.activation;
    }

    /**using the current activation, calculate the neuron's new output via a Gaussian curve : e^(-activation/r)
     */
    protected double transfer(){
        timeAdvance();
        double q=1/(stdev*stdev);
        StateHistory[tick]=Math.exp(-(q*(this.activation)));
        return StateHistory[tick];
    }

    public void setStdev(double r){
        this.stdev = r;
    }
    public double getStdev(){
        return stdev;
    }
}
