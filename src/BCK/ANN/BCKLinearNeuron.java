//Copyright Thomas Doris 1998


package BCK.ANN;
import java.util.*;
import java.io.*;

//this class implements neurons whose outputs are the same as the recieved activaiton
public class BCKLinearNeuron extends BCKNeuron implements Serializable{

    public BCKLinearNeuron(){
        super();
        type="Linear";
    }

    protected double transfer(){
        timeAdvance();
        StateHistory[tick]=activation;
        return StateHistory[tick];
    }


}
