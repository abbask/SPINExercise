package edu.uga.cs.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

public class DataStoreConnection {
	
	final static String oGRAPH = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar";	
	
	private String serviceURI = "http://localhost:8890/sparql/";
	private String graphName = "<http://prokino.uga.edu>";	
	
	public String getServiceURI() {
		return serviceURI;
	}

	public void setServiceURI(String serviceURI) {
		this.serviceURI = serviceURI;
	}

	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public DataStoreConnection(String serviceURI, String graphName) {
		if (serviceURI != null) 
			this.serviceURI = serviceURI;
		if (graphName != null)
			this.graphName = graphName;
		
	}

	private ArrayList<String> executeQuery(String queryString) {
		
		RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create().destination(serviceURI)
				.updateEndpoint("sparql");

		try ( RDFConnection conn = builder.build() ) { 
			conn.queryResultSet(queryString, ResultSetFormatter::toList);
			
            
        } catch (Exception e) {
			e.printStackTrace();
		}


	}	
}
