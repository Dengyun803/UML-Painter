package model;

public class ERModelError extends Error{
	
	public ERModelError(String msg){
		super(msg);
		
	}
	
	public ERModelError(String msg, Throwable cause){
		super(msg, cause);
	}
}
