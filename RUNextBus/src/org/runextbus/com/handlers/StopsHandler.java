package org.runextbus.com.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.runextbus.com.model.Stops;

/**
 * 
 * @author Sangeetha
 * @Description This is responsible to extract the Stops Tag and Title fields
 * from them xml message
 */
public class StopsHandler extends DefaultHandler {
	
	private StringBuffer buffer = new StringBuffer();
	public ArrayList<String> myTag = new ArrayList<String>();
	public ArrayList<String> myTitle = new ArrayList<String>();
	public ArrayList<String> stopTag = new ArrayList<String>();
	public ArrayList<String> stopTitle = new ArrayList<String>();
	private Stops stopList;
	
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		buffer.setLength(0);
	//	System.out.println("Stop:::::" + atts.getValue("tag") + " "+ atts.getValue("title"));
		if(localName.equals ("stop")){
			
		stopList = new Stops();
		
		stopList.tag =atts.getValue("tag");
		stopList.title =atts.getValue("title");
		//System.out.println(":::::" + stopList.tag + " "+ stopList.title);
		//add all tags to this 
		myTag.add(stopList.tag);
		myTitle.add(stopList.title);
		
		}
		
		

		/*else{
			//handle the no prediction scenario
			
			stopList.tag=null;
			stopList.title=null;
			myTag.add(stopList.tag);
			myTitle.add(stopList.title);
			
		}*/
		
		
		//System.out.println("contents of : stopTag " + myTag );
		//System.out.println("contents of : stopTitle " + myTitle );
		
	}
	
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
	
    if (localName.equals("direction")) {
		stopTag.addAll(myTag);
		stopTitle.addAll(myTitle);
	    //System.out.println("contents of : stopTag " +  stopTag );
	    //System.out.println("contents of : stopTitle " +  stopTitle );
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
	
	public ArrayList<String> retrieveStopTag() {
		// return the list of Routes to calling program
		return stopTag;
	}
	

	public ArrayList<String> retrieveStopTitle() {
		// return the list of Routes to calling program
		return stopTitle;
	}
}
 