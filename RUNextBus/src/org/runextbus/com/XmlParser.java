package org.runextbus.com;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.runextbus.com.handlers.*;
import org.runextbus.com.model.*;
/**
 * 
 * @author Sangeetha
 * @Description Responsible for parsing the xml data for deriving the Routes and Stops
 * @version 2.0
 */

public class XmlParser {
	
	private XMLReader initializeReader() throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// create a parser
		SAXParser parser = factory.newSAXParser();
		// create the reader (scanner)
		XMLReader xmlreader = parser.getXMLReader();
		return xmlreader;
	}
	
	public ArrayList<String> parseStopsResponseTag(String xml) {
		
		try {
			
			XMLReader xmlreader = initializeReader();
			
			StopsHandler StopsHandler = new StopsHandler();

			// assign our handler
			xmlreader.setContentHandler(StopsHandler);
			// perform the synchronous parse
			xmlreader.parse(new InputSource(new StringReader(xml)));
			return StopsHandler.retrieveStopTag();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	


	public ArrayList<String> parseStopsResponseTitle(String xml) {
		
		try {
			
			XMLReader xmlreader = initializeReader();
			
			StopsHandler StopsHandler = new StopsHandler();

			// assign our handler
			xmlreader.setContentHandler(StopsHandler);
			// perform the synchronous parse
			xmlreader.parse(new InputSource(new StringReader(xml)));
			return StopsHandler.retrieveStopTitle();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	

	
	
	
		public ArrayList<String> parseRouteResponseTag(String xml) {
		
		try {
			
			XMLReader xmlreader = initializeReader();
			RouteHandler RouteHandler = new RouteHandler();
			// assign our handler
			xmlreader.setContentHandler(RouteHandler);
			// perform the synchronous parse
			xmlreader.parse(new InputSource(new StringReader(xml)));
			
			return RouteHandler.retrieveRouteTag();			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

		

		public ArrayList<String> parseRouteResponseTitle(String xml) {
		
		try {
			
			XMLReader xmlreader = initializeReader();
			RouteHandler RouteHandler = new RouteHandler();
			// assign our handler
			xmlreader.setContentHandler(RouteHandler);
			// perform the synchronous parse
			xmlreader.parse(new InputSource(new StringReader(xml)));
			
			return RouteHandler.retrieveRouteTitle();			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

		
		
	
public ArrayList<String> parseTimeResponse(String xml) {
		
		try {
			
			XMLReader xmlreader = initializeReader();
			
			TimeHandler TimeHandler = new TimeHandler();

			// assign our handler
			xmlreader.setContentHandler(TimeHandler);
			// perform the synchronous parse
			xmlreader.parse(new InputSource(new StringReader(xml)));
			
			return TimeHandler.retrieveTime();			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	
	
	
	
}
