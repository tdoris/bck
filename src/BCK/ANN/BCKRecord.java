
package BCK.ANN;
import java.io.*;
/** This class models individual file records composed of fields of doubles
 * @author Tom Doris
 */
public class BCKRecord implements Serializable{

    double[] data;
    int size;	//the number of fields in this record

    /**Default constructor - don't use*/
    protected BCKRecord(){
    }

    /**
     *Copy constructor
     */
    public BCKRecord(BCKRecord r){
        this.size=r.size;		//records must be of same size
        this.data = new double[r.size];	//allocate data storage
        for(int i=0;i<this.size;i++)	//copy data fields
            this.data[i]=r.data[i];
    }

    /**Construct record with specified size*/
    public BCKRecord(int size){
        this.size=size;		//set size to that specified
        data=new double[size];	//allocate data storage
    }	   	

    /** Retrieve data values stored in record.
     *@return an array containing the data in this record
     */
    public double[] getData(){
        return this.data;
    }
}







