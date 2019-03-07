package edu.uga.cs;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

/**
 * How Can I Use MODIFY to update triples
 * http://vos.openlinksw.com/owiki/wiki/VOS/VirtTipsAndTricksGuideModifyUsageAsInsert
 * 
 * 
 * How to update 
 * 
 * @author abbas
 *
 */
public class ReadFromEndPoint {
	
	final static String ENDPOINT = "http://localhost:8890/sparql";
	//final static String GRAPH = "http://prokino.uga.edu";
	final static String oGRAPH = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar";
	
	public static void main(String[] args) {
		String selectQuery4 = "prefix : <http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#>"
        		+ " select ?p ?o FROM <" + oGRAPH + "> where {:cast074 ?p ?o} LIMIT 100";
		selectQuery2(selectQuery4);
		selectQuery(selectQuery4);
	}
	
	public static void selectQuery(String queryString) {
		RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create()
                .destination(ENDPOINT)
                .queryEndpoint("sparql")            
                .updateEndpoint(null)
                .gspEndpoint(null);      
		
        Query query = QueryFactory.create(queryString);

        try ( RDFConnection conn = builder.build() ) { 
            conn.queryResultSet(query, ResultSetFormatter::toList);
        }
        System.out.println("Done.");

	}
	
	public static void selectQuery2(String queryString) {
		QueryExecution qexec = QueryExecutionFactory.sparqlService(ENDPOINT, queryString);
		ResultSet rs = qexec.execSelect();
		while(rs.hasNext()) {
			QuerySolution sol = rs.nextSolution();
			System.out.println( sol.getResource("p").toString() + " :: " + sol.getResource("o").toString() );
			
		}
		
	}

}
