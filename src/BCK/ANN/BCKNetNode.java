package BCK.ANN;

import java.util.*;
import java.io.*;

/**The net node class provides a simple data structure for referencing neurons in networks*/

public class BCKNetNode {
    int Net;
    int Node;

    protected BCKNetNode(){
    }
    public BCKNetNode(int net, int node){
        Net=net;
        Node=node;
    }

}
