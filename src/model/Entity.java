package model;

import model.DiagramPart;
import java.awt.*;

public class Entity extends DiagramPart{
	
	private String name;
	private Rectangle location;
	
	public Entity(int uid, String name, Rectangle location){
		super(uid);
		this.name = name;
		this.location = location;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Rectangle getRect(){
		return (Rectangle)this.location.clone();
	}
	
	public boolean contains(Point p){
		return this.location.contains(p);
	}
	
	void setName(String name){
		this.name = name;
	}
	
	void move(int dx, int dy){
		this.location.translate(dx, dy);
	}
	
	Rectangle getLocation(){
		return this.location;
	}
}
