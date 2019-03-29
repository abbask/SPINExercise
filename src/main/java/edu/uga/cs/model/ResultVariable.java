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
		String queryString = "Select ?p ?o Where { <" + n.toString() + "> ?p ?o} ";
		
		
		try {
			System.out.println( queryString);
			DataStoreConnection conn = new DataStoreConnection();
			List<QuerySolution> list = conn.executeQuery(queryString);
			//System.out.println("Result");
	
			for (QuerySolution q : list)
			{
				if (q.getResource("p").getLocalName().equals("first")) {
					retrieveSPARQLClauses(q.get("o"));					
				}
				else if (q.getResource("p").getLocalName().equals("rest")){	
					if(!q.getResource("o").getLocalName().equals("nil")) {
						findVariables(q.get("o"));
					}
				}
			}	
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
