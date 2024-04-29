//Copyright Thomas Doris 1998


package BCK.ANN;
import java.util.*;
import java.io.*;

//this class implements neurons whose outputs are always either +1 or -1
public class BCKBipolarNeuron extends BCKNeuron implements Serializable{


    public BCKBipolarNeuron(){
        super();
        type="Bipolar";
    }

    protected double transfer(){
        timeAdvance();
        if(activation>0)
            StateHistory[tick]=1;
        else
            StateHistory[tick]=-1;
        return StateHistory[tick];
    }


}


