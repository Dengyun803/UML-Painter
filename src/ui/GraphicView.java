package ui;

import model.Arrow;
import model.ERModel;
import model.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import static ui.ViewMode.*;

public class GraphicView extends JComponent implements Observer{
	private ERModel model;
	private Point mouseDownPoint = null;
	private Point currPoint = null;
	private Entity selectedEntity = null;
	private Arrow selectedArrow = null;
	private ToolView toolView;
	private double factor = 1.0f;
	private double tx = 0.0f;
	private double ty = 0.0f;
	
	public GraphicView(ERModel model, ToolView toolView){
		this.model = model;
		this.toolView = toolView;
		
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				super.mousePressed(e);
				switch(toolView.getViewMode()){
					case NORMAL:
						mouseDownPoint = e.getPoint();
						mouseDownPoint.x -= tx;
						mouseDownPoint.y -= ty;
						mouseDownPoint.x /= factor;
						mouseDownPoint.y /= factor;
						
						selectedEntity = model.getEntity(mouseDownPoint);
						selectedArrow = model.getArrow(mouseDownPoint);
						break;
						
					case ADD_ENTITY:
					case ADD_ARROW:
						mouseDownPoint = e.getPoint();
						mouseDownPoint.x -= tx;
						mouseDownPoint.y -= ty;
						mouseDownPoint.x /= factor;
						mouseDownPoint.y /= factor;
						break;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				super.mouseReleased(e);
				switch(toolView.getViewMode()){
					case NORMAL:
						Point p = e.getPoint();
						p.x -= tx;
						p.y -= ty;
						p.x /= factor;
						p.y /= factor;
						
						Entity ent = model.getEntity(p);
						if (ent != null){
							setFocusable(true);
							requestFocusInWindow();
							model.selectEntity(ent.getUid());
						}
						break;
						
					case ADD_ENTITY:
						Point pn = e.getPoint();
						pn.x -= tx;
						pn.y -= ty;
						pn.x /= factor;
						pn.y /= factor;
						int uid = model.addEntity("", mouseDownPoint, pn);
						JTextField name = new JTextField(15);
						name.setLocation(mouseDownPoint.x, mouseDownPoint.y);
						name.setSize(pn.x - mouseDownPoint.x, 25);
						name.addFocusListener(new FocusAdapter(){
							@Override
							public void focusLost(FocusEvent fe){
								model.setEntityName(uid, name.getText());
								remove(name);
								repaint();
							}
						});
						name.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent ae){
								model.setEntityName(uid, name.getText());
								remove(name);
								repaint();
							}
						});
						add(name);
						name.requestFocus();
						repaint();
						break;
						
					case ADD_ARROW:
						Point pa = e.getPoint();
						pa.x -= tx;
						pa.y -= ty;
						pa.x /= factor;
						pa.y /= factor;
						Entity endEntity = model.getEntity(pa);
						Entity startEntity = model.getEntity(mouseDownPoint);
						if (endEntity != null && startEntity != null){
							model.addArrow(startEntity, endEntity);
						} else {
							repaint();
						}
						break;
						
				}
				selectedEntity = null;
				toolView.viewMode = NORMAL;
			}
			
			
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				super.mouseDragged(e);
				switch (toolView.getViewMode()){
				case NORMAL:
					Point p = e.getPoint();
					p.x -= tx;
					p.y -= ty;
					p.x /= factor;
					p.y /= factor;
					int dX = p.x - mouseDownPoint.x;
					int dY = p.y - mouseDownPoint.y;
					if (selectedEntity != null){
						model.moveEntity(selectedEntity.getUid(), dX, dY);
						mouseDownPoint = p;
					}else if (selectedArrow != null){
						
						model.moveArrow(selectedArrow.getUid(), dX, dY);
						mouseDownPoint = p;
					} else {
						tx += dX;
						ty += dY;
						repaint();
					}
					break;
				case ADD_ENTITY:
				case ADD_ARROW:
					currPoint = e.getPoint();
					currPoint.x -= tx;
					currPoint.y -= ty;
					currPoint.x /= factor;
					currPoint.y /= factor;
					repaint();
					break;
				}
			}
		});
		
		this.addMouseWheelListener(new MouseAdapter(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent e){
				String message;
				int notches = e.getWheelRotation();
				if (notches < 0) {
					message = "Mouse wheel moved UP " + -notches + " notch(es)" + '\n';
					if (factor < 2.05) factor += 0.1;
					repaint();
				} else {
				    message = "Mouse wheel moved DOWN " + notches + " notch(es)" + '\n';
				    if (factor >0.55) factor -= 0.1;
				    repaint();
				}
				System.out.print(message);
			}

			
		});
		
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_DELETE){
					model.deleteSelectedEntity();
				}
				
				if (e.isControlDown() && e.getKeyChar() != '[' && e.getKeyCode() == 91) {
					if (factor < 2.05) factor += 0.1;
					System.out.println(factor);
					repaint();
				}
				
				if (e.isControlDown() && e.getKeyChar() != ']' && e.getKeyCode() == 93) {
					if (factor >0.55) factor -= 0.1;
					System.out.println(factor);
					repaint();
				}
			}
		});
		
		this.model.addObserver(this);
	}
	
	public void update(Observable obs, Object obj){
		repaint();
	}
	
	private Stroke regular = new BasicStroke(1.0F);
	private Stroke hilite = new BasicStroke(2.0F);
	private Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(tx, ty);
		g2.scale(factor, factor);
		g2.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
		
		Iterator<Arrow> ait = this.model.arrowIterator();
		
		while (ait.hasNext()) {
			Arrow a = ait.next();
			Point[] ep = a.getEp();
			if (a.isSelected()){
				g2.setStroke(hilite);
				
			} else {
				g2.setStroke(regular);
				
			}
			
			g2.drawLine(ep[0].x, ep[0].y, ep[1].x, ep[1].y);
			g2.drawLine(ep[1].x, ep[1].y, ep[2].x, ep[2].y);
			g2.drawLine(ep[2].x, ep[2].y, ep[3].x, ep[3].y);
			if (ep[1].x != ep[2].x || ep[1].y != ep[2].y){
				g2.drawRect(ep[1].x-4, ep[1].y-4, 8, 8);
				g2.drawRect(ep[2].x-4, ep[2].y-4, 8, 8);
			}
			g2.drawOval(ep[3].x-4, ep[3].y-4, 8, 8);
		}
		
		Iterator<Entity> eit = this.model.entityIterator();
		
		while (eit.hasNext()){
			Entity e = eit.next();
			Rectangle r = e.getRect();
			g2.setColor(Color.WHITE);
			g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
			
			g2.setColor(Color.BLACK);
			if (e.isSelected()){
				g2.setStroke(hilite);
				g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
				g2.setStroke(regular);
			} else {
				g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
			}
			g2.drawString(e.getName(), r.x + 4, r.y + r.height/2);
		}
		
		if (this.toolView.getViewMode() == ADD_ENTITY){
			Point pp = this.mouseDownPoint;
			Point cp = this.currPoint;
			if (pp!=null && cp!=null){
				g2.setStroke(dashed);
				g2.drawRoundRect(pp.x, pp.y, cp.x-pp.x, cp.y-pp.y, 10, 10);
			}
		} else if (this.toolView.getViewMode() == ADD_ARROW){
			Point pp = this.mouseDownPoint;
			Point cp = this.currPoint;
			if (pp!=null && model.getEntity(pp)!=null){
				g2.setStroke(dashed);
				g2.drawLine(pp.x, pp.y, cp.x, cp.y);
				g2.drawOval(cp.x-4, cp.y-4, 8, 8);
			}
		}
	}
}
