package edu.uga.cs.model;

import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

public class ResultVariable {
	
	private String resultVariables = "";
	private RDFNode node;
	
	public ResultVariable(RDFNode n) {
		node = n;
	}
	
	public String getClasue() {
		findVariables(node);
		return resultVariables;

	}
	
	private void findVariables(RDFNode n) {
		String queryString = "Select ?p ?o Where { <" + node.toString() + "> ?p ?o} ";

		DataStoreConnection conn = new DataStoreConnection();
		List<QuerySolution> list = conn.executeQuery(queryString);

		for (QuerySolution q : list)
		{
//				System.out.println(q.getResource("p").getLocalName());
			if (q.getResource("p").getLocalName().equals("first")) {
				retrieveSPARQLClauses(q);					
			}
			else if (q.getResource("p").getLocalName().equals("rest")){	
				System.out.println(q.getResource("o").getLocalName());
				if(!q.getResource("o").getLocalName().equals("nil")) {
					findVariables(q.get("o"));
				}
			}
		}	
	}
	
	private void retrieveSPARQLClauses(QuerySolution querySolution) {
		Resource predicate = querySolution.get("p").asResource();
		
		RDFNode object = querySolution.get("o");
		
		if (predicate.getLocalName().equals("varName"))
			resultVariables += "?" + object.asLiteral().getString(); 			
		
		
	}

}
