package model;

import java.awt.*;

public class Arrow extends DiagramPart{
	Entity fromEntity;
	Entity toEntity;
	Point[] ep = new Point[4];
	
	public Arrow(int uid, Entity fromEntity, Entity toEntity){
		super(uid);
		
		this.fromEntity = fromEntity;
		this.toEntity = toEntity;
		defaultEp();
	}
	
	public String fromName(){
		return this.fromEntity.getName();
	}
	
	public String toName(){
		return this.toEntity.getName();
	}
	
	public void defaultEp(){
		Rectangle from = this.fromEntity.getLocation();
		Rectangle to = this.toEntity.getLocation();
		
		int x1,x2,x3,x4,y1,y2,y3,y4;
		
		if (from.x +from.width <= to.x && to.y+to.height<=from.y){//from at left, out of to
			x1 = from.x + from.width/2;
			x2 = from.x + from.width/2;//(to.x-from.x-from.width)/2 + from.x + from.width;
			x3 = from.x + from.width/2;//(to.x-from.x-from.width)/2 + from.x + from.width;
			x4 = to.x;
			y1 = from.y;
			y2 = to.y + to.height/2;
			y3 = to.y + to.height/2;
			y4 = to.y + to.height/2;
		} else if (from.x +from.width <= to.x &&  from.y+from.height<=to.y/*from.x > to.x + to.width*/){//from at right, out of to
			x1 = from.x + from.width/2;
			x2 = from.x + from.width/2;//(from.x-to.x-to.width)/2 + to.x + to.width;
			x3 = from.x + from.width/2;//(from.x-to.x-to.width)/2 + to.x + to.width;
			x4 = to.x;
			y1 = from.y + from.height;
			y2 = to.y + to.height/2;
			y3 = to.y + to.height/2;
			y4 = to.y + to.height/2;
					
		} else if (from.x >= to.x + to.width && to.y+to.height<=from.y){
			x1 = from.x;
			x2 = to.x + to.width/2;
			x3 = to.x + to.width/2;
			x4 = to.x + to.width/2;
			y1 = from.y + from.height/2;
			y2 = from.y + from.height/2;
			y3 = from.y + from.height/2;
			y4 = to.y + to.height;
		} else if (from.x >= to.x + to.width && from.y+from.height<=to.y) {
			x1 = from.x;
			x2 = to.x + to.width/2;
			x3 = to.x + to.width/2;
			x4 = to.x + to.width/2;
			y1 = from.y + from.height/2;
			y2 = from.y + from.height/2;
			y3 = from.y + from.height/2;
			y4 = to.y;
		} else if (from.x +from.width <= to.x && from.y + from.height >= to.y){
			x1 = from.x + from.width;
			y1 = from.y + from.height/2;
			x2 = (to.x-from.x-from.width)/2 + from.x + from.width;
			y2 = from.y + from.height/2;
			x3 = (to.x-from.x-from.width)/2 + from.x + from.width;
			y3 = to.y + to.height/2;
			x4 = to.x;
			y4 = to.y + to.height/2;
		} else if (from.x >= to.x + to.width && from.y + from.height >= to.y){
			x1 = from.x;
			y1 = from.y + from.height/2;
			x2 = (from.x-to.x-to.width)/2 + to.x + to.width;
			y2 = from.y + from.height/2;
			x3 = (from.x-to.x-to.width)/2 + to.x + to.width;
			y3 = to.y + to.height/2;
			x4 = to.x + to.width;
			y4 = to.y + to.height/2;
		} else if (from.x + from.width >= to.x && from.y+from.height <= to.y){
			x1 = from.x + from.width/2;
			y1 = from.y + from.height;
			x2 = from.x + from.width/2;
			y2 = (to.y - from.y - from.height)/2 + from.y + from.height;
			x3 = to.x + to.width/2;
			y3 = (to.y - from.y - from.height)/2 + from.y + from.height;
			x4 = to.x + to.width/2;
			y4 = to.y;
		} else {
			x1 = from.x + from.width/2;
			y1 = from.y;
			x2 = from.x + from.width/2;
			y2 = (from.y - to.y - to.height)/2 + to.y + to.height;
			x3 = to.x + to.width/2;
			y3 = (from.y - to.y - to.height)/2 + to.y + to.height;
			x4 = to.x + to.width/2;
			y4 = to.y + to.height;
		}
		/*
		if (from.y+from.height<to.y){
			y1 = from.y + from.height;
			y2 = (to.y - from.y - from.height)/2 + from.y + from.height;
			y3 = (to.y - from.y - from.height)/2 + from.y + from.height;
			y4 = to.y;
		} else if (to.y+to.height<from.y){
			y1 = from.y;
			y2 = to.y + (from.y - to.y - to.height)/2;
			y3 = to.y + (from.y - to.y - to.height)/2;
			y4 = to.y + to.height;
		} else {
			y1 = from.y + from.height/2;
			y2 = from.y + from.height/2;
			y3 = to.y + to.height/2;
			y4 = to.y + to.height/2;
		}
		*/
		this.ep = new Point[]{new Point(x1,y1), new Point(x2,y2), new Point(x3,y3), new Point(x4,y4)};
	}
	
	public void move(int dx, int dy){
		Rectangle from = this.fromEntity.getLocation();
		Rectangle to = this.toEntity.getLocation();
		if (ep[1].x == ep[2].x){
			if (ep[1].x >= ep[0].x) {
				if (ep[1].x + dx <= from.x + from.width) {
					ep[1].x = from.x + from.width;
					ep[2].x = from.x + from.width;
				} else if (ep[1].x + dx >= to.x) {
					ep[1].x = to.x;
					ep[2].x = to.x;
				} else {
					ep[1].x += dx;
					ep[2].x += dx;
				}
			} else if (ep[1].x < ep[0].x){
				if (ep[1].x + dx <= to.x + to.width) {
					ep[1].x = to.x + to.width;
					ep[2].x = to.x + to.width;
				} else if (ep[1].x + dx >= from.x) {
					ep[1].x = from.x;
					ep[2].x = from.x;
				} else {
					ep[1].x += dx;
					ep[2].x += dx;
				}
			} else {}
		} else if (ep[1].y == ep[2].y){
			if (ep[1].y >= ep[0].y){
				if (ep[1].y + dy <= from.y + from.height) {
					ep[1].y = from.y + from.height;
					ep[2].y = from.y + from.height;
				} else if (ep[1].y + dy >= to.y) {
					ep[1].y = to.y;
					ep[2].y = to.y;
				} else {
					ep[1].y += dy;
					ep[2].y += dy;
				}
			} else if (ep[1].y < ep[0].y){
				if (ep[1].y + dy <= to.y + to.height) {
					ep[1].y = to.y + to.height;
					ep[2].y = to.y + to.height;
				} else if (ep[1].y + dy >= from.y) {
					ep[1].y = from.y;
					ep[2].y = from.y;
				} else {
					ep[1].y += dy;
					ep[2].y += dy;
				}
			} else{}
		} else {}
	}
	
	public Point[] getEp(){
		return this.ep;
	}
}
