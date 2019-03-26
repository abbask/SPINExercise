package edu.uga.cs.model;

import org.apache.jena.rdf.model.RDFNode;

public class FunctionCall {
	
	public static final String exNS = "ex:";
	public static final String spNS = "sp:";
	private RDFNode node;
		
	public FunctionCall(RDFNode n ) {
		node = n;
	}
	
	public String getClause(String funcName, String arg1, String arg2) {
		StringBuilder sb = new StringBuilder();
		sb.append(node + " a " + exNS +  funcName + " ");
		sb.append(node + spNS + "arg1 " + arg1 + " ");
		sb.append(node + spNS + "arg2 " + arg2 + " ");
		return sb.toString();
	}

}
