package org.runextbus.com.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.runextbus.com.model.Route;
/**
 * 
 * @author Sangeetha
 * @Description This is responsible to extract the Route Tag and Title fields
 * from them xml message
 */
public class RouteHandler extends DefaultHandler {
	
	private StringBuffer buffer = new StringBuffer();
	public ArrayList<String> myTag = new ArrayList<String>();
	public ArrayList<String> myTitle = new ArrayList<String>();
	public ArrayList<String> routesTag = new ArrayList<String>();
	public ArrayList<String> routesTitle = new ArrayList<String>();
	private Route routesList;
	
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		buffer.setLength(0);
		if(localName.equals ("route")){
		routesList = new Route();
		routesList.tag =atts.getValue("tag");
		routesList.title =atts.getValue("title");
		//add all tags to this 
		myTag.add(routesList.tag);
		myTitle.add(routesList.title);
		}
		
		/*else{
			//handle the no prediction scenario
			
			routesList.tag=null;
			routesList.title=null;
			myTag.add(routesList.tag);
			myTitle.add(routesList.title);
			
		}*/
		
	}
	
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
	
    if (localName.equals("body")) {
		routesTag.addAll(myTag);
		routesTitle.addAll(myTitle);
	    //System.out.println("contents of : routesTag " +  routesTag );
	    //System.out.println("contents of : routesTitle " +  routesTitle );
	}	
    
}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		buffer.append(ch, start, length);
	}
	
		/**
		 * @param Null
		 * @return ArrayList<String>
		 * @author Sangeetha
		 * @version 1.0
		 */
	
	public ArrayList<String> retrieveRouteTag() {
		// return the list of Routes to calling program
		return routesTag;
	}
	
	public ArrayList<String> retrieveRouteTitle() {
		// return the list of Routes to calling program
		return routesTitle;
	}
	
}
 