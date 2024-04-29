package BCK.ANN;

import java.io.Serializable;
import java.util.Vector;

public class BCKNeuralNetwork implements Serializable, BCKObservable {
   public int idNumber;
   protected int numInputs;
   protected int numOutputs;
   protected Vector neurons;
   protected String name = new String("No Name");
   protected transient Vector connections;
   protected transient Vector observers;
   protected transient BCKFilter trainData;
   protected transient BCKFilter testData;
   protected transient BCKFilter inputData;

   protected BCKNeuralNetwork() {
      this.neurons = new Vector(0);
      this.connections = new Vector(0);
      this.observers = new Vector(0);
   }

   public BCKNeuralNetwork(int var1, String var2) {
      this.neurons = new Vector(0);
      this.connections = new Vector(0);
      this.observers = new Vector(0);

      for (int var3 = 0; var3 < var1; var3++) {
         this.addNamedNeuron(var2);
      }
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void registerObserver(BCKObserver var1) {
      if (this.observers == null) {
         this.observers = new Vector(0);
      }

      this.observers.addElement(var1);
   }

   public void removeObserver(BCKObserver var1) {
      this.observers.removeElement(var1);
   }

   public void notifyObservers() {
      for (int var1 = 0; var1 < this.observers.size(); var1++) {
         if (this.observers.elementAt(var1) != null) {
            ((BCKObserver)this.observers.elementAt(var1)).updateObserver();
         }
      }
   }

   public void insertNeuron(BCKNeuron var1, int var2) throws Exception {
      if (var2 >= 0 && var2 <= this.neurons.size()) {
         var1.id = this.neurons.size();
         this.neurons.insertElementAt(var1, var2);
         int var3 = 0;

         while (var3 < this.neurons.size()) {
            ((BCKNeuron)this.neurons.elementAt(var3)).id = var3++;
         }

         this.renewConnections();
      } else {
         throw new Exception("Attempt to insert neuron with illegal index : " + var2);
      }
   }

   public void addNeuron(BCKNeuron var1) {
      var1.id = this.neurons.size();
      this.neurons.addElement(var1);
   }

   public void addNamedNeuron(String var1) {
      int var2 = this.neurons.size();
      if (var1.equals("Sigmoid")) {
         BCKSigmoidNeuron var7 = new BCKSigmoidNeuron();
         var7.id = var2;
         this.neurons.addElement(var7);
      } else if (var1.equals("Binary")) {
         BCKBinaryNeuron var6 = new BCKBinaryNeuron();
         var6.id = var2;
         this.neurons.addElement(var6);
      } else if (var1.equals("Bipolar")) {
         BCKBipolarNeuron var5 = new BCKBipolarNeuron();
         var5.id = var2;
         this.neurons.addElement(var5);
      } else if (var1.equals("Linear")) {
         BCKLinearNeuron var4 = new BCKLinearNeuron();
         var4.id = var2;
         this.neurons.addElement(var4);
      } else if (var1.equals("Radial")) {
         BCKRadialNeuron var3 = new BCKRadialNeuron();
         var3.id = var2;
         this.neurons.addElement(var3);
      } else {
         System.out.println("Error in addNamedNeuron in BCKNeuralNetwork - invalid neuron type");
      }
   }

   public BCKNeuron[] getInputArray(int var1) {
      return ((BCKNeuron)this.neurons.elementAt(var1)).getInputArray();
   }

   public int[] getInputArrayIds(int var1) throws Exception {
      BCKNeuron var2 = this.getBCKNeuron(var1);
      return var2.getInputArrayIds();
   }

   public void connectInternal(int var1, int var2, double var3, int var5) throws Exception {
      if (this.validIndex(var1) && this.validIndex(var2)) {
         BCKNeuron var6 = this.getBCKNeuron(var2);
         BCKNeuron var7 = this.getBCKNeuron(var1);
         connect(var7, var6, var3, var5);
         this.connections.addElement(new BCKConnection(var1, var2, var3, var5));
      } else {
         throw new Exception(
            "Illegal values passed to connectInternal in BCKNeuralNetwork - pre = "
               + var1
               + " post = "
               + var2
               + " size of neurons vector = "
               + this.neurons.size()
         );
      }
   }

   private BCKNeuron getBCKNeuron(int var1) throws Exception {
      if (this.neurons.elementAt(var1) instanceof BCKNeuron) {
         return (BCKNeuron)this.neurons.elementAt(var1);
      } else {
         throw new Exception("Non BCKNeuron object in Neural Nework - FATAL ERROR");
      }
   }

   public void connectExternal(BCKNeuron var1, int var2, double var3, int var5) throws Exception {
      if (this.validIndex(var2)) {
         BCKNeuron var6 = (BCKNeuron)this.neurons.elementAt(var2);
         connect(var1, var6, var3, var5);
      }
   }

   public static void connect(BCKNeuron var0, BCKNeuron var1, double var2, int var4) throws Exception {
      var1.connect(var0, var2, var4);
   }

   public void setNeuronName(int var1, String var2) {
      this.getNeuron(var1).setName(var2);
   }

   public String getNeuronName(int var1) {
      return this.getNeuron(var1).getName();
   }

   public void deltaWeight(BCKNeuron var1, int var2, int var3, double var4) throws Exception {
      BCKNeuron var6 = (BCKNeuron)this.neurons.elementAt(var2);
      var6.deltaWeight(var1, var3, var4);
   }

   public void setTrainingFile(String var1) throws Exception {
      this.trainData = new BCKFilter(var1, this.numInputs + this.numOutputs);
   }

   public String getTrainingFile() {
      return this.trainData.getFileName();
   }

   public void setTestingFile(String var1) throws Exception {
      this.testData = new BCKFilter(var1, this.numInputs + this.numOutputs);
   }

   public String getTestingFile() {
      return this.testData.getFileName();
   }

   public void setInputFile(String var1) throws Exception {
      this.inputData = new BCKFilter(var1, this.numInputs + this.numOutputs);
   }

   public String getInputFile() {
      return this.inputData.getFileName();
   }

   public double[] getNextInputRecord() throws Exception {
      if (this.inputData == null) {
         throw new Exception("Cannot get next input record until input file has been set");
      } else {
         return this.inputData.getNextRecord();
      }
   }

   public void forwardPass() {
      try {
         for (int var3 = this.numInputs; var3 < this.neurons.size(); var3++) {
            double var1 = this.calcState(var3);
         }
      } catch (Exception var4) {
         System.out.println(var4.toString());
      }
   }

   public double[] classify(double[] var1) throws Exception {
      this.setInput(var1);
      this.forwardPass();
      return this.getOutputs();
   }

   public double[] classifyNextRecord() throws Exception {
      return this.classify(this.getNextInputRecord());
   }

   public void setInput(double[] var1) {
      for (int var2 = 0; var2 < this.numInputs; var2++) {
         this.setOutput(var2, var1[var2]);
      }
   }

   public double calcState(int var1) throws Exception {
      if (this.validIndex(var1)) {
         return ((BCKNeuron)this.neurons.elementAt(var1)).calcState();
      } else {
         throw new Exception("Invalid Index in calcState");
      }
   }

   public void renewConnections() {
      this.connections = null;
      this.connections = new Vector(0);

      for (int var4 = 0; var4 < this.neurons.size(); var4++) {
         BCKNeuron var1 = (BCKNeuron)this.neurons.elementAt(var4);
         BCKSynapse[] var3 = var1.getSynapseArray();

         for (int var5 = 0; var5 < var3.length; var5++) {
            BCKSynapse var2 = var3[var5];
            this.connections.addElement(new BCKConnection(var2.output.id, var4, var2.weight, var2.delay));
         }
      }
   }

   public double[] getOutputs() {
      double[] var1 = new double[this.numOutputs];
      int var2 = 0;

      for (int var3 = this.neurons.size() - this.numOutputs; var2 < this.numOutputs; var2++) {
         var1[var2] = this.getState(var3 + var2);
      }

      return var1;
   }

   public int getNumInputs() {
      return this.numInputs;
   }

   public int getNumOutputs() {
      return this.numOutputs;
   }

   public BCKNeuron getNeuron(int var1) {
      return (BCKNeuron)this.neurons.elementAt(var1);
   }

   public double getPrime(int var1) {
      return ((BCKNeuron)this.neurons.elementAt(var1)).fprime();
   }

   public void setOutput(int var1, double var2) {
      if (this.validIndex(var1)) {
         ((BCKNeuron)this.neurons.elementAt(var1)).setOutput(var2);
      }
   }

   public double getState(int var1, int var2) {
      if (this.validIndex(var1)) {
         try {
            return ((BCKNeuron)this.neurons.elementAt(var1)).getState(var2);
         } catch (Exception var4) {
            System.out.println(var4.toString());
         }
      }

      return 0.0;
   }

   public double getState(int var1) {
      return this.getState(var1, 0);
   }

   public int getNeuronNumber(BCKNeuron var1) {
      for (int var3 = 0; var3 < this.neurons.size(); var3++) {
         BCKNeuron var2 = (BCKNeuron)this.neurons.elementAt(var3);
         if (var2 == var1) {
            return var3;
         }
      }

      return -1;
   }

   public boolean areConnected(int var1, int var2) {
      if (this.validIndex(var1) && this.validIndex(var2)) {
         BCKNeuron var3 = (BCKNeuron)this.neurons.elementAt(var2);
         BCKNeuron var4 = (BCKNeuron)this.neurons.elementAt(var1);
         return var3.isConnectedTo(var4);
      } else {
         return false;
      }
   }

   public BCKSynapse getSynapse(int var1, int var2) {
      if (this.validIndex(var1) && this.validIndex(var2)) {
         BCKNeuron var3 = (BCKNeuron)this.neurons.elementAt(var2);
         BCKNeuron var4 = (BCKNeuron)this.neurons.elementAt(var1);
         return var3.getSynapse(var4);
      } else {
         return null;
      }
   }

   public double getWeight(int var1, int var2) {
      if (this.validIndex(var1) && this.validIndex(var2)) {
         BCKNeuron var4 = (BCKNeuron)this.neurons.elementAt(var2);
         BCKNeuron var5 = (BCKNeuron)this.neurons.elementAt(var1);
         BCKSynapse var3 = var4.getSynapse(var5);
         return var3 == null ? 0.0 : var3.getWeight();
      } else {
         return 0.0;
      }
   }

   public int getNumberOfNeurons() {
      return this.neurons.size();
   }

   private boolean validIndex(int var1) {
      return var1 >= 0 && var1 <= this.neurons.size();
   }

   public void normaliseOutputNeurons() {
      double var1 = -100000.0;
      int var3 = 0;
      int var4 = 0;

      for (int var5 = this.neurons.size() - this.numOutputs; var4 < this.numOutputs; var4++) {
         if (var1 < this.getState(var5 + var4)) {
            var1 = this.getState(var5 + var4);
            var3 = var5 + var4;
         }
      }

      var4 = 0;

      for (int var6 = this.neurons.size() - this.numOutputs; var4 < this.numOutputs; var4++) {
         if (var6 + var4 != var3) {
            this.setOutput(var6 + var4, 0.0);
         } else {
            this.setOutput(var3, 1.0);
         }
      }
   }

   public BCKNeuron getWinner() {
      double var1 = -1000.0;
      BCKNeuron var3 = null;

      for (int var4 = this.neurons.size() - 1; var4 > this.neurons.size() - this.numOutputs - 1; var4--) {
         if (var1 < this.getState(var4)) {
            var3 = this.getNeuron(var4);
            var1 = this.getState(var4);
         }
      }

      return var3;
   }

   public double test() throws Exception {
      double[] var2 = new double[this.numInputs];
      double[] var4 = new double[this.numOutputs];
      int var5 = 0;
      boolean var6 = false;
      if (this.testData == null) {
         throw new Exception("You must specify a test file before testing");
      } else {
         int var7 = this.testData.getNumberOfRecords();

         for (int var8 = 0; var8 < var7; var8++) {
            double[] var1 = this.testData.getNextRecord();

            for (int var9 = 0; var9 < this.numInputs; var9++) {
               var2[var9] = var1[var9];
            }

            for (int var10 = this.numInputs; var10 < this.numOutputs + this.numInputs; var10++) {
               var4[var10 - this.numInputs] = var1[var10];
            }

            double[] var3 = this.classify(var2);
            var6 = true;

            for (int var11 = 0; var11 < this.numOutputs; var11++) {
               if (Math.abs(var4[var11] - var3[var11]) > 0.2) {
                  var6 = false;
               }
            }

            if (var6) {
               var5++;
            }
         }

         return (double)(var5 * 100 / var7);
      }
   }

   public void setId(int var1) {
      this.idNumber = var1;
   }

   public int getId() {
      return this.idNumber;
   }
}
