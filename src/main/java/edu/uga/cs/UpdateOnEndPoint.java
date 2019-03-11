package edu.uga.cs;

import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

/**
 * Update jena
 * https://jena.apache.org/documentation/query/update.html
 * @author abbas
 *
 */
public class UpdateOnEndPoint {
	
	final static String ENDPOINT = "http://localhost:8890/sparql";
	final static String oGRAPH = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar";

	public static void main(String[] args) {
		
		String updateString = "PREFIX owl:<http://www.w3.org/2002/07/owl#> PREFIX oscar: <http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#>";
		//updateString+= "INSERT DATA { GRAPH <" + oGRAPH + "> { <oscar:price2> a owl:DatatypeProperty. } }";
		updateString+= "INSERT DATA { GRAPH <" + oGRAPH + "> { <oscar:price2> <http://www.w3.org/2000/01/rdf-schema#domain> <_:1c9f2f38-e13c-470a-bdda-7637b4dfe040> } }";
		
		System.out.println(updateString);
		RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create()
                .destination(ENDPOINT)
                .updateEndpoint("sparql");             
		
        try ( RDFConnection conn = builder.build() ) { 
            conn.update(updateString);
        }
        System.out.println("Updated.");



	}

}
