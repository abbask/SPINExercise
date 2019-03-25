package edu.uga.cs;

import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import org.apache.jena.util.FileUtils;

import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.model.Select;
import org.topbraid.spin.system.SPINModuleRegistry;

public class SPINProcess {
	
	final static String ENDPOINT = "http://localhost:8890/sparql";
	final static String oGRAPH = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar";

	public static void main(String[] args) {

		printToConsole();
				
	}
	
	public static void storeToEndpoint() {
		
	}
	
	public static void printToConsole() {
		SPINModuleRegistry.get().init();
		
		String spURI = "http://spinrdf.org/sp#";
		String oscarURI = oGRAPH + "#";
						
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("sp", spURI);
		model.setNsPrefix("oscar",oscarURI);
		
		String query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
		Query arqQuery = ARQFactory.get().createQuery(model, query); // convert string to Query
		
		ARQ2SPIN arq2SPIN = new ARQ2SPIN(model); // creates var2Resources.
		arq2SPIN.createQuery(arqQuery, null);
			
			
		Resource selectResource =  model.createResource(spURI + ":Select");
		model.removeAll();
		//System.out.println("select: " + selectResource);
		
		Resource resource  = model.createResource(oscarURI + "testCase");
		
		Property p = model.createProperty(oscarURI + "queryBy");
		//System.out.println("r: " + resource);
		
		resource.addProperty(p, selectResource);	
				
		Select sparqlQuery = (Select) arq2SPIN.createQuery( arqQuery, null );

		model.write( System.out, FileUtils.langXMLAbbrev );
	}
	
	public static void listAllResources(Resource r) {
		StmtIterator iter = r.listProperties();
		while (iter.hasNext()) {
			Statement stmt = iter.next();
			System.out.printf("triple: %s %s %s%n", stmt.getSubject(),stmt.getPredicate(), stmt.getObject());
		}
	}
	
	public static void listAllResources(Model m) {
		StmtIterator iter = m.listStatements();
		while (iter.hasNext()) {
			Statement stmt = iter.next();
			System.out.printf("triple: %s %s %s%n", stmt.getSubject(),stmt.getPredicate(), stmt.getObject());
		}
	}

}
