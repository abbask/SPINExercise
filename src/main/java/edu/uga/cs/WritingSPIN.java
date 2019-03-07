package edu.uga.cs;

import java.io.Writer;

import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
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
//
//		NodeIterator iter = model.listObjects();
//
//		while (iter.hasNext()) {
//			RDFNode rdf = iter.next();
//			// System.out.println(rdf);
//
//		}

		StmtIterator iterStmt = model.listStatements();
		while (iterStmt.hasNext()) {
			Statement stmt = iterStmt.next();
			System.out.println(stmt.getSubject() + " -- " + stmt.getPredicate() + " -- " + stmt.getObject());
//			if (stmt.getSubject().isResource())
//				System.out.println("heeeeeeeeeeeeeeeeeeeeeeee");
//			else
//				System.out.println(stmt.getSubject());
			//System.out.println( stmt.getSubject().);
		}
//		String updateString = "PREFIX owl:<http://www.w3.org/2002/07/owl#> PREFIX oscar: <http://www.semanticweb.org/abbas/ontologies/2015/2/oscar#>";
//		updateString+= "INSERT DATA { GRAPH <" + oGRAPH + "> { <oscar:price2> a owl:DatatypeProperty. } }";
//		System.out.println(updateString);
//		RDFConnectionRemoteBuilder builder = RDFConnectionRemote.create()
//                .destination(ENDPOINT)
//                .updateEndpoint("sparql");             
//		
//        try ( RDFConnection conn = builder.build() ) { 
//            conn.update(updateString);
//        }
//        System.out.println("Updated.");

		model.write(System.out, FileUtils.langXML);

	}

}
