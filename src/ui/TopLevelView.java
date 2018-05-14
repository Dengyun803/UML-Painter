package ui;
import model.ERModel;
import javax.swing.*;
import java.awt.*;

public class TopLevelView extends JPanel{
	public TopLevelView(ERModel model){
		ToolView toolView = new ToolView();
		GraphicView  graphicView = new GraphicView(model, toolView);
		EntityView entityView = new EntityView(model);
		ArrowView arrowView = new ArrowView(model);
		
		JPanel lists = new JPanel(new GridLayout(2,1));
		lists.add(entityView);
		lists.add(arrowView);
		
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.add(toolView);
		left.add(Box.createVerticalStrut(30));
		left.add(lists);
		
		
		this.setLayout(new BorderLayout());
		this.add(graphicView,  BorderLayout.CENTER);
		this.add(left, BorderLayout.WEST);
	}
}
