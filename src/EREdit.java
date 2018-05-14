import model.ERModel;
import ui.*;
import javax.swing.*;
import java.awt.*;


public class EREdit {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ERModel model = new ERModel();
		TopLevelView view = new TopLevelView(model);
		
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				JFrame f = new JFrame("EREdit");
				f.setSize(800, 500);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setContentPane(view);
				f.setVisible(true);
			}
		});
	}

}
