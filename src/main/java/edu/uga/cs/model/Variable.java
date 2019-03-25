package edu.uga.cs.model;

import org.apache.jena.rdf.model.RDFNode;

public class Variable {
	
	public static final String VAR_NAME = "sp:varName";
	private String clause;
	private RDFNode node;
		
	public Variable(RDFNode node) {
		this.node = node; 
	}
	
	public String getClause(String variableName) {
		
		clause = node + " " +  VAR_NAME + "\"" + variableName + "\"^^xsd:string";
		
		return clause;
	}
	

}
