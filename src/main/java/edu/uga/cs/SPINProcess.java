package edu.uga.cs;

import org.apache.jena.ontology.Individual;
import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.sparql.serializer.SerializationContext;
import org.apache.jena.sparql.util.FmtUtils;
import org.apache.jena.util.FileUtils;
import org.apache.jena.vocabulary.RDF;
import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.model.Select;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.vocabulary.SP;

public class SPINProcess {
	
	// resource stored in Virtuoso http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#testCase
			/* 
			 * 	select distinct ?p ?o where {<http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#testCase> ?p ?o} LIMIT 100
			 * 	select distinct ?p ?o where {<http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#testCaseSS> ?p ?o} LIMIT 100
			 *	select distinct ?s ?o where { ?s <http://spinrdf.org/sp#subject> ?o } LIMIT 100
			 *	(example)select distinct ?p ?o where { <_:c654a24e-f731-4348-ad7e-de97976d080d> ?p ?o } LIMIT 100
			*/
	
	final static String ENDPOINT = "http://localhost:8890/sparql";
	final static String oGRAPH = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar";

	public static void main(String[] args) {

		Model m = printToConsole();
		storeToEndpoint(m);
	}
	
	public static void storeToEndpoint(Model m) {
		StringBuilder sb = new StringBuilder();

		StmtIterator iterStmt = m.listStatements();
		while (iterStmt.hasNext()) {
			Statement stmt = iterStmt.next();
						
			RDFNode subject = stmt.getSubject();
			RDFNode predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();
			
			String subjectString;
			String predicateString;
			String objectString;

			if (subject.isURIResource())
				subjectString = FmtUtils.stringForNode(subject.asNode(), (SerializationContext) null);
			else if(subject.isLiteral())
				subjectString = FmtUtils.stringForNode(subject.asNode(), (SerializationContext) null);
			else 				
				subjectString = "<" + FmtUtils.stringForNode(subject.asNode(), (SerializationContext) null)
						+ ">";

			if (predicate.isURIResource())
				predicateString = FmtUtils.stringForNode(predicate.asNode(), (SerializationContext) null);
			else if (stmt.getPredicate().isLiteral())
				predicateString = FmtUtils.stringForNode(predicate.asNode(), (SerializationContext) null);
			else
				predicateString = "<"
						+ FmtUtils.stringForNode(predicate.asNode(), (SerializationContext) null) + ">";

			if (object.isURIResource())
				objectString = FmtUtils.stringForNode(object.asNode(), (SerializationContext) null);
			else if (stmt.getObject().isLiteral())
				objectString = FmtUtils.stringForNode(object.asNode(), (SerializationContext) null);
			else
				objectString = "<" + FmtUtils.stringForNode(object.asNode(), (SerializationContext) null)
						+ ">";

			sb.append(subjectString + " " + predicateString + " " + objectString + ".\n");

		}

		String updateString = "PREFIX owl:<http://www.w3.org/2002/07/owl#> PREFIX oscar: <http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#>";

		updateString += "INSERT DATA { GRAPH <" + oGRAPH + "> { " + sb.toString() + "} }";

		System.out.println(updateString);
		RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create().destination(ENDPOINT)
				.updateEndpoint("sparql");

		try (RDFConnection conn = builder.build()) {
			conn.update(updateString);
			System.out.println("Updated.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Model printToConsole() {
		SPINModuleRegistry.get().init();
		
		String spURI = "http://spinrdf.org/sp#";
		String oscarURI = oGRAPH + "#";
		
						
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("sp", spURI);
		model.setNsPrefix("oscar",oscarURI);
		
		String query = "select distinct ?Concept where {?s ?p ?o. ?s a <sp:something>} LIMIT 11002";
		Query arqQuery = ARQFactory.get().createQuery(model, query); // convert string to Query
		
		ARQ2SPIN arq2SPIN = new ARQ2SPIN(model); // creates var2Resources.
		Resource root = model.createResource();
		
		arq2SPIN.createQuery(arqQuery, oscarURI + "tc14Query");
	
		
		for (StmtIterator stmts = model.listStatements( null,RDF.type, SP.Select ); stmts.hasNext(); ) {
			
			Statement stmt = stmts.next();
			root = stmt.getSubject();
			Resource p = stmt.getPredicate();
			RDFNode o = stmt.getObject();
			System.out.println(root + " " + p + " " + o);
			System.out.println("Root: " + root);
		} 
		
		
		
		if (root == null)
			throw new NullPointerException();
//		model.removeAll();
		//System.out.println(selectResource);
		//System.out.println(model.getResource(spURI + "Select"));
		
		Resource resource  = model.createResource(oscarURI + "tc014");
		
		Property p = model.createProperty(oscarURI + "queryBy");
		
		resource.addProperty(p, root);					

		//model.write( System.out, FileUtils.langXMLAbbrev );
		
		return model;
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
