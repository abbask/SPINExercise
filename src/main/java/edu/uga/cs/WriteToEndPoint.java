package edu.uga.cs;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class WriteToEndPoint {

	public static void main(String[] args) {
		//https://stackoverflow.com/questions/47312174/how-to-do-insert-delete-remotely-through-a-sparql-endpoint?noredirect=1&lq=1
		
		//example from https://jena.apache.org/documentation/rdfconnection/
		try ( RDFConnection conn = RDFConnectionFactory.connect("http://128.192.62.253:8890/sparql") ) {
		    conn.load("data.ttl") ;
		
		    conn.querySelect("SELECT DISTINCT ?s { ?s ?p ?o }", qs -> {
		    	Resource subject = qs.getResource("s") ;
		    	System.out.println("Subject: "+subject) ;
		    }) ;
		}
		
		//may be like discover 
		QueryEngineHTTP qeHTTP = new QueryEngineHTTP("", "");
		qeHTTP.execConstruct();	
		
		//https://www.programcreek.com/java-api-examples/?api=org.apache.jena.rdf.model.Resource
		//https://www.programcreek.com/java-api-examples/?api=org.apache.jena.update.UpdateRequest
	}

}
