package edu.uga.cs.model;

import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;


public class TestCaseQuery {
	final static String oGRAPH = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar";
	private int limit;
	private boolean distinct;
	private String resultVariables;
	private String where;
	private RDFNode node;
	
	public TestCaseQuery() {
		super();
	}
	
	public TestCaseQuery(RDFNode n) {
		node = n;
	}
	
	public String getClasue () {
		//http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#tc18Query
		String node = oGRAPH + "#" + "tc22Query";
		//System.out.println(node);
		String queryString = "Select ?p ?o Where { <" + node.toString() + "> ?p ?o} ";
		DataStoreConnection conn = new DataStoreConnection();
		List<QuerySolution> list = conn.executeQuery(queryString);
		
		list.stream()			
			.forEach(this::retrieveSPARQLClauses);

		StringBuilder sb = new StringBuilder();
		String distinctString = (distinct)?"DISCTINCT":"" ;
		sb.append("SELECT "  + distinctString + resultVariables );
		sb.append(" WHERE " + where);
		sb.append("LIMIT " + limit);
				
		return sb.toString();		
		
	}
	
	public void retrieveSPARQLClauses(QuerySolution querySolution) {

		Resource predicate = querySolution.get("p").asResource();
		
		RDFNode object = querySolution.get("o");
		
		switch (predicate.getLocalName()) {
		case "where":
			WhereClause wc = new WhereClause(object);
			where = wc.getClasue();			
			System.out.println("WhereClause: " + where);
			break;
		case "resultVariables":
			
			ResultVariable rv = new ResultVariable(object);
			resultVariables =  rv.getClasue();
			System.out.println("resultVariables: " + resultVariables);			
			break;
		case "limit":
			limit = object.asLiteral().getInt();
			System.out.println("limit was " + limit + ".");
			break;			
		case "distinct" :
			distinct = true;
			System.out.println("Distinct: " + ((distinct)? "true": "false"));
			break;
		}
		

	}
	
	public static void main(String[] args) {
		TestCaseQuery tcq = new TestCaseQuery();
		System.out.println(tcq.getClasue());
	}
	
	
}
