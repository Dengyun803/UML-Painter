package model;

import java.awt.*;
import java.util.*;

public class ERModel extends Observable{
	public static final Object ADD_REMOVE_DATA = new Object();
	private Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	private Map<Integer, Arrow> arrows = new HashMap<Integer, Arrow>();
	private int nextUid = 100;
	
	public ERModel(){
		this.entities.put(4, new Entity(4, "Four", new Rectangle(30, 50, 100, 50)));
		this.entities.put(6, new Entity(6, "six", new Rectangle(90, 200, 100, 50)));
		this.entities.put(8, new Entity(8, "eight", new Rectangle(300, 200, 100, 50)));
		this.arrows.put(29, new Arrow(29, this.entities.get(4), this.entities.get(6)));
		this.arrows.put(30, new Arrow(30, this.entities.get(4), this.entities.get(8)));
	}
	
	public void addObserver(Observer o){
		super.addObserver(o);
		o.update(this, this.ADD_REMOVE_DATA);
	}
	
	public Entity getEntity(int uid){
		return this.entities.get(uid);
	}
	
	public void selectEntity(int uid){
		Entity ent = this.entities.get(uid);
		for (Entity e : this.entities.values()){
			e.setSelected(e.getUid() == uid);
		}
		
		for (Arrow a : this.arrows.values()){
			a.setSelected(a.fromEntity == ent);
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	public Entity getEntity(Point p){
		for (Entity e: this.entities.values()){
			if (e.contains(p)){
				return e;
			}
		}
		return null;
	}
	
	public Iterator<Entity> entityIterator(){
		return this.entities.values().iterator();
	}
	
	public int[] getEntityUids(){
		int[] uids = new int[this.entities.size()];
		
		int i = 0;
		for (Entity e:this.entities.values()){
			uids[i] = e.getUid();
			i++;
		}
		Arrays.sort(uids);
		return uids;
	}
	
	public void moveEntity(int uid, int dx, int dy){
		Entity e = this.getEntity(uid);
		e.move(dx, dy);
		for (Arrow a : this.arrows.values()){
			if (a.fromEntity.getUid() == uid || a.toEntity.getUid() == uid){
				a.defaultEp();
			}
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	public int addEntity(String name, Point topLeft, Point bottomRight){
		int uid = this.nextUid++;
		this.entities.put(uid, new Entity(uid, name, new Rectangle(topLeft.x, topLeft.y,
				bottomRight.x-topLeft.x, bottomRight.y-topLeft.y)));
		
		this.setChanged();
		this.notifyObservers(this.ADD_REMOVE_DATA);
		return uid;
	}
	
	public void setEntityName(int uid, String name){
		System.out.println("Setting entity name to " + name);
		this.entities.get(uid).setName(name);
		this.setChanged();
		this.notifyObservers();
	}
	
	public void deleteSelectedEntity(){
		Iterator<Map.Entry<Integer, Arrow>> alter = this.arrows.entrySet().iterator();
		while (alter.hasNext()){
			Map.Entry<Integer, Arrow> entry = alter.next();
			if (entry.getValue().fromEntity.isSelected()||
					entry.getValue().toEntity.isSelected()){
				alter.remove();
			}
		}
		
		Iterator<Map.Entry<Integer,Entity>> elter = this.entities.entrySet().iterator();
		while (elter.hasNext()){
			Map.Entry<Integer, Entity> entry = elter.next();
			if (entry.getValue().isSelected()){
				elter.remove();
			}
		}
		
		this.setChanged();
		this.notifyObservers(this.ADD_REMOVE_DATA);
	}
	
	public int[] getArrowUids(){
		int[] uids = new int[this.arrows.size()];
		
		int i = 0;
		for (Arrow a : this.arrows.values()){
			uids[i] = a.getUid();
			i++;
		}
		Arrays.sort(uids);
		return uids;
	}
	
	public Arrow getArrow(int uid){
		return this.arrows.get(uid);
	}
	
	public Arrow getArrow(Point p){
		for (Arrow a: this.arrows.values()){
			if ((a.ep[1].x != a.ep[2].x || a.ep[1].y != a.ep[2].y)){
				Rectangle p1 = new Rectangle(a.ep[1].x-4, a.ep[1].y-4, 8, 8);
				Rectangle p2 = new Rectangle(a.ep[2].x-4, a.ep[2].y-4, 8, 8);
				if (p1.contains(p) || p2.contains(p)) return a;
			}
		}
		return null;
	}
	
	public Arrow[] getArrows(){
		Arrow[] arrows = new Arrow[this.arrows.size()];
		this.arrows.values().toArray(arrows);
		return arrows;
	}
	
	public Iterator<Arrow> arrowIterator(){
		return this.arrows.values().iterator();
	}
	
	public void selectArrow(int uid){
		Arrow arrow = this.arrows.get(uid);
		
		for (Entity e : this.entities.values()) {
			e.setSelected(false);
		}
		
		for (Arrow a : this.arrows.values()){
			if (a.getUid() == uid){
				a.fromEntity.setSelected(true);
				a.toEntity.setSelected(true);
			}
			a.setSelected(a.getUid() == uid);
		}
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void moveArrow(int uid, int dx, int dy){
		Arrow a = this.getArrow(uid);
		a.move(dx, dy);
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void addArrow(Entity from, Entity to){
		int uid = this.nextUid++;
		this.arrows.put(uid,  new Arrow(uid, from, to));
		this.setChanged();
		this.notifyObservers(this.ADD_REMOVE_DATA);
	}
	
	
}
