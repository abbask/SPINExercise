package edu.uga.cs.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.RDFNode;
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
	
	public DataStoreConnection() {
		
	}

	public DataStoreConnection(String serviceURI, String graphName) {
		if (serviceURI != null) 
			this.serviceURI = serviceURI;
		if (graphName != null)
			this.graphName = graphName;
		
	}
	
	private static void materialize(QuerySolution qs) {
        for ( Iterator<String> iter = qs.varNames() ; iter.hasNext() ; ) {
            String vn = iter.next();
            RDFNode n = qs.get(vn) ;
        }
    }

	public List<QuerySolution> executeQuery(String queryString) {
		
		RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create().destination(serviceURI)
				.updateEndpoint("sparql");
				
		List<QuerySolution> list = new ArrayList<>() ;
		try ( RDFConnection conn = builder.build() ) { 
			//conn.queryResultSet(queryString, ResultSetFormatter::toList);
			ResultSet rs = conn.query(queryString).execSelect();
			
	        for ( ; rs.hasNext() ; ) {
	            QuerySolution result = rs.nextSolution() ;
	            materialize(result);
	            list.add(result) ;
	        }
	        
		
            
        } catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}	
}
