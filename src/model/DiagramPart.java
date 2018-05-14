package model;

public class DiagramPart {

	private int uid;
	private boolean selected = false;
	
	public DiagramPart(int uid){
		this.uid = uid;
	}
	
	public int getUid(){
		return this.uid;
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	
	void setSelected(boolean val){
		this.selected = val;
	}
}
