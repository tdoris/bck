package BCK.GUI;

import com.sun.java.swing.table.AbstractTableModel;

final class BCKNeuronTablePanel$1 extends AbstractTableModel {
   public int getColumnCount() {
      return this.val$names.length;
   }

   public int getRowCount() {
      return this.val$data.length;
   }

   public Object getValueAt(int var1, int var2) {
      return this.val$data[var1][var2];
   }

   public String getColumnName(int var1) {
      return this.val$names[var1];
   }

   public Class getColumnClass(int var1) {
      return this.getValueAt(0, var1).getClass();
   }

   public boolean isCellEditable(int var1, int var2) {
      return this.getColumnClass(var2)
         == (
            BCKNeuronTablePanel.class$java$lang$String != null
               ? BCKNeuronTablePanel.class$java$lang$String
               : (BCKNeuronTablePanel.class$java$lang$String = BCKNeuronTablePanel.class$("java.lang.String"))
         );
   }

   public void setValueAt(Object var1, int var2, int var3) {
      this.val$data[var2][var3] = var1;
   }
}
