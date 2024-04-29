package BCK.ANN;

import java.util.*;
import java.io.*;

public class BCKConnection implements Serializable{ //models a connection between two neurons
    public int post;
    public int pre;
    public double weight;
    public int delay;

    public BCKConnection(int pre, int post, double weight, int delay){
        this.post=post;
        this.pre=pre;
        this.weight=weight;
        this.delay=delay;
    }

}
