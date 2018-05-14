package ui;

import model.Arrow;
import model.ERModel;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ArrowView extends JPanel implements Observer{
	private ERModel model;
	private JLabel label = new JLabel("Arrows");
	private ArrowTable aTable = new ArrowTable();
	private JTable table = new JTable(aTable);
	
	private boolean updatingSelections = false;
	
	public ArrowView(ERModel model){
		this.model = model;
		table.setPreferredScrollableViewportSize(new Dimension(200, 70));
		table.setFillsViewportHeight(true);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(this.label);
		this.add(new JScrollPane(this.table));
		this.table.getSelectionModel().addListSelectionListener(new 
				ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e){
				
				if(!updatingSelections){
					if(!e.getValueIsAdjusting()){
						model.selectArrow(aTable.uids[table.getSelectedRow()]);
					}
				}
			}
		});
		
		this.model.addObserver(this);
	}
	
	public void update(Observable obs, Object obj){
		if (obj == (model.ADD_REMOVE_DATA)){
			this.aTable.refreshUids();
		}
		
		ListSelectionModel lsm = this.table.getSelectionModel();
		this.updatingSelections = true;
		lsm.clearSelection();
		for(int i=0; i<this.aTable.uids.length; i++){
			if(this.model.getArrow(this.aTable.uids[i]).isSelected()){
				lsm.addSelectionInterval(i, i);
				
			}
		}
		this.updatingSelections = false;
	}
	
	private class ArrowTable extends AbstractTableModel{
		private int[] uids = new int[0];
		void refreshUids(){
			this.uids = model.getArrowUids();
			updatingSelections = true;
			this.fireTableDataChanged();
			updatingSelections = false;
		}
		
		@Override
		public int getRowCount(){
			return uids.length;
		}
		
		@Override
		public int getColumnCount(){
			return 3;
		}
		
		@Override
		public String getColumnName(int colIndex){
			switch (colIndex){
			case 0: return "UID";
			case 1 :return "From Entity";
			case 2: return "To Entity";
			default: return "";
				
			}
		}
		
		public Object getValueAt(int rowIndex, int columnIndex){
			Arrow arrow = model.getArrow(this.uids[rowIndex]);
			assert(arrow != null);
			switch (columnIndex){
			case 0:
				return arrow.getUid();
			case 1:
				return arrow.fromName();
			case 2:
				return arrow.toName();
			}
			throw new Error("Fell through case with columnIndex = " + columnIndex);
		}
	}
}
