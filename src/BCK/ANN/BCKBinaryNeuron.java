//Copyright Thomas Doris 1998


package BCK.ANN;
import java.util.*;
import java.io.*;

//this class implements neurons whose outputs are always either +1 or -1
public class BCKBinaryNeuron extends BCKNeuron implements Serializable{
    public BCKBinaryNeuron(){
        super();
        type="Binary";
    }
    protected double transfer(){
        timeAdvance();
        if(activation>0)
            StateHistory[tick]=1;
        else
            StateHistory[tick]=0;
        return StateHistory[tick];
    }

}
