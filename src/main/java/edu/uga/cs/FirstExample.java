package edu.uga.cs;

import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileUtils;
import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.model.Select;
import org.topbraid.spin.system.SPINModuleRegistry;


public class FirstExample {

	public static void main(String[] args) {
		SPINModuleRegistry.get().init();
		Model model = ModelFactory.createDefaultModel();
		String query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
		
		Query arqQuery = ARQFactory.get().createQuery( model, query );
		ARQ2SPIN arq2SPIN = new ARQ2SPIN( model );
		Select sparqlQuery = (Select) arq2SPIN.createQuery( arqQuery, null );
		
		System.out.println( "SPARQL Query:\n" + sparqlQuery );
		System.out.println( "\nSPIN Representation:" );
		model.write( System.out, FileUtils.langXML );
		
		// model can write in RDF/XML by model.write(outOWL, "RDF/XML-ABBREV");
		

	}

}
