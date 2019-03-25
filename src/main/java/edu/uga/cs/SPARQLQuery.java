package edu.uga.cs;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

public class SPARQLQuery {
	
	private String endpoint; 
	
	public SPARQLQuery(String endpoint) {
		this.endpoint = endpoint;
	}

	public void selectQuery(String queryString) {
		RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create()
                .destination(endpoint)
                .queryEndpoint("sparql")            
                .updateEndpoint(null)
                .gspEndpoint(null);      
		
        Query query = QueryFactory.create(queryString);

        try ( RDFConnection conn = builder.build() ) { 
            conn.queryResultSet(query, ResultSetFormatter::toList);
        }

	}
}
