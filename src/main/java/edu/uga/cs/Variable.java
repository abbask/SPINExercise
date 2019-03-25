package edu.uga.cs;

public class Variable {
	
	private String clause;
	private String uri; 
	
	public Variable() {	
	}
	
	public Variable(String uri) {
		this.uri = uri;	
	}
	
	public String getClause(String strNode) {
		clause = uri + strNode;
		return clause;
	}
	

}
