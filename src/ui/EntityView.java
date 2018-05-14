package ui;

import model.ERModel;
import model.Entity;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class EntityView extends JPanel implements Observer{
	
	private ERModel model;
	private JLabel label = new JLabel("Entities");
	private EntityTable eTable = new EntityTable();
	private JTable table = new JTable(eTable);
	
	private boolean updatingSelections = false;
	
	public EntityView(ERModel model){
		this.model = model;
		
		table.setPreferredScrollableViewportSize(new Dimension(200, 70));
		table.setFillsViewportHeight(true);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(this.label);
		this.add(new JScrollPane(this.table));
		
		
		this.table.getSelectionModel().addListSelectionListener(new 
				ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!updatingSelections) {
					if (!e.getValueIsAdjusting()) {
						model.selectEntity(eTable.uids[table.getSelectedRow()]);
					}
				}
			}
		});
		
		this.model.addObserver(this);
	}
	
	public void update(Observable obs, Object obj){
		this.eTable.refreshUids();
		
		ListSelectionModel lsm = this.table.getSelectionModel();
		this.updatingSelections = true;
		lsm.clearSelection();
		for (int i=0; i<this.eTable.uids.length; i++){
			if (this.model.getEntity(this.eTable.uids[i]).isSelected()){
				lsm.addSelectionInterval(i, i);
			}
		}
		this.updatingSelections = false;
	}
	
	private class EntityTable extends AbstractTableModel {
		int[] uids = new int[0];
		
		void refreshUids(){
			this.uids = model.getEntityUids();
			updatingSelections = true;
			this.fireTableDataChanged();
			updatingSelections = false;
		}
		
		@Override
		public int getRowCount(){
			return uids.length;
		}
		
		@Override
		public String getColumnName(int colIndex){
			switch (colIndex){
			case 0:
				return "UID";
			case 1:
				return "ENtity Name";
			default:
				return "";
			}
		}
		
		@Override
		public boolean isCellEditable(int row, int col){
			return col == 1;
		}
		
		@Override
		public int getColumnCount(){
			return 2;
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex){
			Entity ent = model.getEntity(this.uids[rowIndex]);
			assert (ent!=null);
			
			switch (columnIndex) {
			case 0:
				return ent.getUid();
			case 1:
				return ent.getName();
			}
			throw new Error("Fell through switch with columnIndex = " + columnIndex);
		}
		
		@Override
		public void setValueAt(Object val, int rowIndex, int colIndex){
			if (colIndex == 1){
				model.setEntityName(this.uids[rowIndex], val.toString());
			}
		}
	}
}
