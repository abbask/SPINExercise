package edu.uga.cs.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

public class TestFive {
	
	

	public static void main(String[] args) {
		
//		loopQuery();
		for(int i= 0 ; i < 60 ; i++)
			oneQuery();
		/*oneQuery();
		oneQuery();
		oneQuery();
		oneQuery();
		oneQuery();
		oneQuery();
		
		for(int i= 0 ; i < 6 ; i++)
			olderApproach();
		*/
		
	}
	
	public static void olderApproach() {
		List<QuerySolution> list = new ArrayList<>();
		String node = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#tc22Query";
		//String queryString = "Select ?p ?o Where { <" + node.toString() + "> ?p ?o} ";
		String queryString = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:8890/sparql", queryString);
		ResultSet rs = qexec.execSelect();	
		while (rs.hasNext()){
			
			QuerySolution result = rs.nextSolution();
			list.add(result);
			
		}
		
		qexec.close();
		
		System.out.println(list.size());
	}
	
	public static void oneQuery() {
		String node = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#tc22Query";
		String queryString = "Select ?p ?o Where { <" + node.toString() + "> ?p ?o} ";
		
		RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create().destination("http://localhost:8890/sparql")
				.updateEndpoint("sparql");

		List<QuerySolution> list = new ArrayList<>();
		
		RDFConnection conn = builder.build();
		try {
			System.out.println(queryString);

			QueryExecution qe = conn.query(queryString);
			ResultSet rs = qe.execSelect();

			for (; rs.hasNext();) {
				QuerySolution result = rs.nextSolution();
				list.add(result);
			}

			qe.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
			conn = null;
			builder = null;
		}
		System.out.println(list.size());
	
	}
	
	public static void loopQuery() {
		String node = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#tc22Query";
		String queryString = "Select ?p ?o Where { <" + node.toString() + "> ?p ?o} ";
		for (int i = 0 ; i < 10 ; i++) {
	
			RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create().destination("http://localhost:8890/sparql")
					.updateEndpoint("sparql");
			
			List<QuerySolution> list = new ArrayList<>() ;
			try ( RDFConnection conn = builder.build() ) { 
				System.out.println(queryString);

				QueryExecution qe = conn.query(queryString);
				ResultSet rs = qe.execSelect();
				
		        for ( ; rs.hasNext() ; ) {
		            QuerySolution result = rs.nextSolution() ;
		            list.add(result) ;
		        }		        		
		       
	        } catch (Exception e) {
				e.printStackTrace();
			}

			
			System.out.println(list.size());
		}
	}

}
