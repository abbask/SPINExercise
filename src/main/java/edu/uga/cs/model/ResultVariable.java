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
		//http://www.w3.org/1999/02/22-rdf-syntax-ns#first
		//http://www.w3.org/1999/02/22-rdf-syntax-ns#rest
		String nameSpace = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String queryStringFirst = nameSpace + "Select ?o Where { <" + n.toString() + "> rdf:first ?o} ";		
		String queryStringRest = nameSpace + "Select ?o Where { <" + n.toString() + "> rdf:rest ?o} ";
				
		try {
			
			DataStoreConnection conn = new DataStoreConnection();
			System.out.println(queryStringFirst);			
			QuerySolution firstResult = conn.executeQuerySingleResult(queryStringFirst);
			if (firstResult != null)
				retrieveSPARQLClauses(firstResult.get("o"));
			System.out.println(queryStringRest);
			QuerySolution restResult = conn.executeQuerySingleResult(queryStringRest);
			if (restResult != null)
				if (!restResult.getResource("o").getLocalName().equals("nil"))
					findVariables(restResult.get("o"));
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	private void retrieveSPARQLClauses(RDFNode n) {
		String queryString = "Select ?p ?o Where { <" + n.toString() + "> ?p ?o} ";
		System.out.println(queryString);
		
		DataStoreConnection conn = new DataStoreConnection();
		List<QuerySolution> list = conn.executeQuery(queryString);
		//System.out.println("Re: Result");
		QuerySolution q = list.get(0);
		Resource predicate = q.get("p").asResource();
		
		RDFNode object = q.get("o");
		
		if (predicate.getLocalName().equals("varName"))
			resultVariables += "?" + object.asLiteral().getString(); 			
		else
			System.out.println("NOT");
		
	}

}
