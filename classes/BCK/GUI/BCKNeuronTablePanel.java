package BCK.GUI;

import BCK.ANN.BCKNeuralNetwork;
import BCK.ANN.BCKSynapse;
import com.sun.java.swing.JCheckBox;
import com.sun.java.swing.JComponent;
import com.sun.java.swing.JLabel;
import com.sun.java.swing.JPanel;
import com.sun.java.swing.JScrollPane;
import com.sun.java.swing.JSlider;
import com.sun.java.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class BCKNeuronTablePanel extends JPanel {
   JTable tableView;
   JScrollPane scrollpane;
   Dimension origin = new Dimension(0, 0);
   JCheckBox isColumnReorderingAllowedCheckBox;
   JCheckBox showHorizontalLinesCheckBox;
   JCheckBox showVerticalLinesCheckBox;
   JCheckBox isColumnSelectionAllowedCheckBox;
   JCheckBox isRowSelectionAllowedCheckBox;
   JCheckBox isRowAndColumnSelectionAllowedCheckBox;
   JLabel interCellSpacingLabel;
   JLabel rowHeightLabel;
   JSlider interCellSpacingSlider;
   JSlider rowHeightSlider;
   JComponent selectionModeButtons;
   JComponent resizeModeButtons;
   JPanel mainPanel;
   JPanel controlPanel;
   JScrollPane tableAggregate;

   public BCKNeuronTablePanel(int var1, BCKNeuralNetwork var2) {
      this.setLayout(new BorderLayout());
      this.mainPanel = this;
      this.tableAggregate = this.createTable(var1, var2);
      this.tableAggregate.setPreferredSize(new Dimension(400, 100));
      this.mainPanel.add(this.tableAggregate, "Center");
   }

   public JScrollPane createTable(int var1, BCKNeuralNetwork var2) {
      String[] var3 = new String[]{"Neuron Id", "Weight", "Delay"};
      BCKSynapse[] var5 = var2.getNeuron(var1).getSynapseArray();
      String[][] var4 = new String[var5.length][var3.length];

      for (int var6 = 0; var6 < var5.length; var6++) {
         var4[var6][0] = String.valueOf(var5[var6].output.id);
         var4[var6][1] = String.valueOf(var5[var6].weight);
         var4[var6][2] = String.valueOf(var5[var6].delay);
      }

      BCKNeuronTablePanel$1 var8 = new BCKNeuronTablePanel$1(var4, var3);
      this.tableView = new JTable(var8);
      BCKNeuronTablePanel$2 var9 = new BCKNeuronTablePanel$2();
      var9.setHorizontalAlignment(4);
      this.tableView.setRowHeight(20);
      this.scrollpane = JTable.createScrollPaneForTable(this.tableView);
      return this.scrollpane;
   }
}
