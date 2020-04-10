package command.meta;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Args extends ArrayList<String> {

	public Args(String[] args) {
		
		super();
		
		for (String s : args) {
			add(s);
		}
		
	}
	
	public String getText(){
		String full = "";
		for (String arg : this){
			full = full + arg + " ";
		}
		return full.length()==0 ? full : full.substring(0, full.length() - 1);
	}
	
	public String getText(String separator){
		String full = "";
		for (String arg : this){
			full = full + arg + separator;
		}
		return full.length()==0 ? full : full.substring(0, full.length() - separator.length());
	}
	

	public String getNonThrowing(int i) {
		try{
			return this.get(i);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
}
