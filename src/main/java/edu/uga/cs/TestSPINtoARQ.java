package edu.uga.cs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.util.NodeUtils;

public class TestSPINtoARQ {
	final static String oGRAPH = "http://www.semanticweb.org/abbas/ontologies/2015/2/oscar";

	public static void main(String[] args) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("SPINtoARQ"));
			String line = reader.readLine();
			while (line != null) {
				
				//read string to statement
				String[] tripleArray  = line.split(" ");
				System.out.println(line);
				Resource subject = ResourceFactory.createResource(tripleArray[0]);
				Property predicate = (Property) ResourceFactory.createResource(tripleArray[1]);
				RDFNode object = (RDFNode) ResourceFactory.createResource(tripleArray[2]);
				
//				Node s =  NodeUtils.asNode(tripleArray[0]);
//				Node p =  NodeUtils.asNode(tripleArray[1]);
//				Node o =  NodeUtils.asNode(tripleArray[2]);
				
//				statement
				
//				Triple triple = new Triple(s, p, o);
//				System.out.println(triple.toString());
				String spURI = "http://spinrdf.org/sp#";
				String oscarURI = oGRAPH + "#";
				
								
				Model model = ModelFactory.createDefaultModel();
				model.setNsPrefix("sp", spURI);
				model.setNsPrefix("oscar",oscarURI);
				model.add(subject, predicate, object);
				
				line = reader.readLine();

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
