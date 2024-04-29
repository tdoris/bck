package BCK.ANN;

import java.util.*;
import java.io.*;

/**Objects of this class model connections between cascaded networks, and therefore must specify which neurons in the first net feed into which input neurons in the second net*/

public class BCKNetConnection implements Serializable{
    //public visibilty for all these, more or less a struct
    public int sourceNet;
    public int sourceNeuron;
    public int targetNet;
    public int targetNeuron;
    public double weight;
    public int delay;

    protected BCKNetConnection(){
    }

    protected BCKNetConnection(int sourceNet, int sourceNeuron, int targetNet, int targetNeuron, double weight, int delay){
        this.sourceNet = sourceNet;
        this.sourceNeuron = sourceNeuron;
        this.targetNet=targetNet;
        this.targetNeuron = targetNeuron;
        this.weight = weight;
        this.delay = delay;
    }

    public int getSourceNet(){
        return sourceNet;
    }
    public int getTargetNet(){
        return targetNet;
    }
    public int getSourceNeuron(){
        return sourceNeuron;
    }
    public int getTargetNeuron(){
        return targetNeuron;
    }
}
