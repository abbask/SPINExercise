package edu.uga.cs;

import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileUtils;
import org.apache.jena.vocabulary.RDF;
import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.model.Select;
import org.topbraid.spin.system.SPINModuleRegistry;

public class WritingSPIN {

	final static String ENDPOINT = "http://localhost:8890/sparql";
	final static String oGRAPH = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar";
	
	public static void main(String[] args) {
		
		
		//model.setNsPrefix("rdf", RDF.getURI());
		
		SPINModuleRegistry.get().init();
		Model model = ModelFactory.createDefaultModel();
		String query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
		
		Query arqQuery = ARQFactory.get().createQuery( model, query );
		ARQ2SPIN arq2SPIN = new ARQ2SPIN( model );
		Select sparqlQuery = (Select) arq2SPIN.createQuery( arqQuery, null );
		
		System.out.println( "SPARQL Query:\n" + sparqlQuery );
		System.out.println( "\nSPIN Representation:" );
		model.write( System.out, FileUtils.langXML );

	}

}
