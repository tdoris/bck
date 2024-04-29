package BCK.GUI;

import BCK.ANN.BCKNeuralNetwork;
import BCK.ANN.BCKNeuron;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JScrollPane;
import com.sun.java.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class BCKOutputTable extends JPanel {
   JTable tableView;
   JScrollPane scrollpane;
   Dimension origin = new Dimension(0, 0);
   JPanel mainPanel;
   JPanel controlPanel;
   JScrollPane tableAggregate;
   int outputs;
   int firstOutput;

   public BCKOutputTable(BCKNeuralNetwork var1) {
      this.outputs = var1.getNumOutputs();
      this.firstOutput = var1.getNumberOfNeurons() - this.outputs;
      this.setLayout(new BorderLayout());
      this.mainPanel = this;
      this.tableAggregate = this.createTable(var1);
      this.tableAggregate.setPreferredSize(new Dimension(400, 100));
      this.mainPanel.add(this.tableAggregate, "Center");
   }

   public JScrollPane createTable(BCKNeuralNetwork var1) {
      String[] var2 = new String[this.outputs];

      for (int var3 = this.firstOutput; var3 < var1.getNumberOfNeurons(); var3++) {
         var2[var3 - this.firstOutput] = var1.getNeuron(var3).getName();
      }

      String[] var4 = new String[]{"Neuron Name", "  ID  ", " Output "};
      String[][] var5 = new String[this.outputs][var4.length];

      for (int var6 = 0; var6 < this.outputs; var6++) {
         BCKNeuron var7 = var1.getNeuron(this.firstOutput + var6);
         var5[var6][0] = String.valueOf(var7.getName());
         var5[var6][1] = String.valueOf(var7.id);
         var5[var6][2] = String.valueOf(var7.getState(0));
      }

      BCKOutputTable$1 var8 = new BCKOutputTable$1(var5, var4);
      this.tableView = new JTable(var8);
      BCKOutputTable$2 var9 = new BCKOutputTable$2();
      var9.setHorizontalAlignment(4);
      this.tableView.setRowHeight(20);
      this.scrollpane = JTable.createScrollPaneForTable(this.tableView);
      return this.scrollpane;
   }
}
