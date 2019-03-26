package edu.uga.cs.model;

import org.apache.jena.rdf.model.RDFNode;

public class TestCaseQuery {
	
	private int limit;
	private boolean distinct;
	private RDFNode node;
	
	public TestCaseQuery() {
		super();
	}
	
	public TestCaseQuery(RDFNode n) {
		node = n;
	}
	
	public void executeQuery() {
		
	}
	
}
