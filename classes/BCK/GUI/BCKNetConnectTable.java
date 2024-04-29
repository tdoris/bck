package BCK.GUI;

import BCK.ANN.BCKNetConnection;
import BCK.ANN.BCKNeuralNetwork;
import BCK.ANN.BCKNeuron;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JScrollPane;
import com.sun.java.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

public class BCKNetConnectTable extends JPanel {
   JTable tableView;
   JScrollPane scrollpane;
   Dimension origin = new Dimension(0, 0);
   JPanel mainPanel;
   JPanel controlPanel;
   JScrollPane tableAggregate;
   BCKNeuralNetwork targetNet;
   BCKNeuralNetwork sourceNet;
   int sourceNetId;
   int targetNetId;
   int outputs;
   int firstOutput;

   public BCKNetConnectTable(BCKNeuralNetwork var1, int var2) {
      this.sourceNetId = var2;
      this.targetNetId = var1.getId();
      this.sourceNet = bckframe.top.getNetAt(var2);
      if (this.sourceNetId != 0) {
         this.outputs = this.sourceNet.getNumOutputs();
         this.firstOutput = this.sourceNet.getNumberOfNeurons() - this.outputs;
      } else {
         this.outputs = this.sourceNet.getNumInputs();
         this.firstOutput = 0;
      }

      this.setLayout(new BorderLayout());
      this.mainPanel = this;
      this.tableAggregate = this.createTable();
      this.tableAggregate.setPreferredSize(new Dimension(400, 100));
      this.mainPanel.add(this.tableAggregate, "Center");
   }

   public JScrollPane createTable() {
      String[] var1 = new String[]{"Source Name", "Source ID", "Target ID", "Weight", "Delay"};
      Vector var2 = (Vector)bckframe.top.theBrain.proxyInputs.elementAt(this.targetNetId);
      String[] var5 = new String[this.outputs];

      for (int var6 = this.firstOutput; var6 < this.firstOutput + this.outputs; var6++) {
         var5[var6 - this.firstOutput] = this.sourceNet.getNeuron(var6).getName();
      }

      String[][] var4 = new String[this.outputs][var1.length];

      for (int var7 = 0; var7 < this.outputs; var7++) {
         BCKNeuron var8 = this.sourceNet.getNeuron(this.firstOutput + var7);
         var4[var7][0] = String.valueOf(var8.getName());
         var4[var7][1] = String.valueOf(var8.id);
         var4[var7][2] = "-1";
         var4[var7][3] = "1";
         var4[var7][4] = "0";

         for (int var9 = 0; var9 < var2.size(); var9++) {
            BCKNetConnection var3 = (BCKNetConnection)var2.elementAt(var9);
            if (var3.sourceNet == this.sourceNetId && var3.sourceNeuron == var8.id) {
               var4[var7][2] = String.valueOf(var3.targetNeuron);
               var4[var7][3] = String.valueOf(var3.weight);
               var4[var7][4] = String.valueOf(var3.delay);
            }
         }
      }

      BCKNetConnectTable$1 var11 = new BCKNetConnectTable$1(var4, var1);
      this.tableView = new JTable(var11);
      BCKNetConnectTable$2 var10 = new BCKNetConnectTable$2();
      var10.setHorizontalAlignment(4);
      this.tableView.setRowHeight(20);
      this.scrollpane = JTable.createScrollPaneForTable(this.tableView);
      return this.scrollpane;
   }
}
