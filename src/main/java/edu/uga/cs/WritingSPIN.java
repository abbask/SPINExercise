package edu.uga.cs;

import java.io.Writer;

import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.StatementImpl;
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

public class WritingSPIN {

	final static String ENDPOINT = "http://localhost:8890/sparql";
	final static String oGRAPH = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar";

	public static void main(String[] args) {

		// model.setNsPrefix("rdf", RDF.getURI());

		SPINModuleRegistry.get().init();
		Model model = ModelFactory.createDefaultModel();
		String query = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";
		Query arqQuery = ARQFactory.get().createQuery(model, query);
		ARQ2SPIN arq2SPIN = new ARQ2SPIN(model);
		arq2SPIN.createQuery(arqQuery, null);

		StringBuilder sb = new StringBuilder();

		StmtIterator iterStmt = model.listStatements();
		while (iterStmt.hasNext()) {
			Statement stmt = iterStmt.next();
			/*
			 * String subjectString = (stmt.getSubject().isURIResource() ) ?
			 * "<"+stmt.getSubject().toString()+">" : "<_:" +stmt.getSubject()+">"; String
			 * predicateString = (stmt.getPredicate().isURIResource() ) ?
			 * "<"+stmt.getPredicate().toString()+">" : "<_:"+stmt.getPredicate()+">";
			 * String objectString = (stmt.getObject().isURIResource() ) ?
			 * "<"+stmt.getObject().toString()+">" : "<_:" +stmt.getObject()+">";
			 * 
			 * 
			 * 
			 * String subjectString = FmtUtils.stringForNode(stmt.getSubject().asNode() ,
			 * (SerializationContext)null); String predicateString =
			 * FmtUtils.stringForNode(stmt.getPredicate().asNode() ,
			 * (SerializationContext)null); String objectString =
			 * FmtUtils.stringForNode(stmt.getObject().asNode() ,
			 * (SerializationContext)null);
			 */
			
			RDFNode subject = stmt.getSubject();
			RDFNode predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();
			
			String subjectString;
			String predicateString;
			String objectString;
			
//			System.out.println(subject);
//			System.out.println(predicate);
//			System.out.println(object);

			if (subject.isURIResource())
				subjectString = FmtUtils.stringForNode(subject.asNode(), (SerializationContext) null);
			else if(subject.isLiteral())
				subjectString = FmtUtils.stringForNode(subject.asNode(), (SerializationContext) null);
			else 				
				subjectString = "<" + FmtUtils.stringForNode(subject.asNode(), (SerializationContext) null)
						+ ">";

			if (stmt.getPredicate().isURIResource())
				predicateString = FmtUtils.stringForNode(predicate.asNode(), (SerializationContext) null);
			else if (stmt.getPredicate().isLiteral())
				predicateString = FmtUtils.stringForNode(predicate.asNode(), (SerializationContext) null);
			else
				predicateString = "<"
						+ FmtUtils.stringForNode(predicate.asNode(), (SerializationContext) null) + ">";

			if (stmt.getObject().isURIResource())
				objectString = FmtUtils.stringForNode(object.asNode(), (SerializationContext) null);
			else if (stmt.getObject().isLiteral())
				objectString = FmtUtils.stringForNode(object.asNode(), (SerializationContext) null);
			else
				objectString = "<" + FmtUtils.stringForNode(object.asNode(), (SerializationContext) null)
						+ ">";

			sb.append(subjectString + " " + predicateString + " " + objectString + ".\n");

			// sb.append(FmtUtils.stringForTriple(stmt.asTriple())+ ".\n");

		}
		 System.out.println(sb.toString());

		String updateString = "PREFIX owl:<http://www.w3.org/2002/07/owl#> PREFIX oscar: <http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#>";

		updateString += "INSERT DATA { GRAPH <" + oGRAPH + "> { " + sb.toString() + "} }";
		// updateString+= "INSERT DATA { GRAPH <" + oGRAPH + "> { <oscar:price2>
		// <http://www.w3.org/2000/01/rdf-schema#domain>
		// <_:1c9f2f38-e13c-470a-bdda-7637b4dfe040> } }";

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

}
