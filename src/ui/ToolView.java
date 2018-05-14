package ui;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static ui.ViewMode.*;

public class ToolView extends JPanel{
	private JButton addENtity = new JButton("Add Entity");
	private JButton addArrow = new JButton("Add Arrow");
	
	ViewMode viewMode = NORMAL;
	
	public ToolView(){
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.add(this.addENtity);
		this.add(this.addArrow);
		
		this.addENtity.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				viewMode = ADD_ENTITY;
			}
		});
		
		this.addArrow.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				viewMode = ADD_ARROW;
			}
		});
	}
	
	public ViewMode getViewMode(){
		return this.viewMode;
	}
}
