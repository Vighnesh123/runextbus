package org.runextbus.com.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.runextbus.com.model.Time;
/**
 * 
 * @author Sangeetha
 * @Description This is responsible to extract the Route Tag and Title fields
 * from them xml message
 */
public class TimeHandler extends DefaultHandler {
	
	private StringBuffer buffer = new StringBuffer();
	public ArrayList<String> myTime = new ArrayList<String>();
	public ArrayList<String> inMinutes = new ArrayList<String>();
	private Time timeList;
	
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		buffer.setLength(0);
		
		if(localName.equals ("prediction")){
			timeList = new Time();
		timeList.minutes =atts.getValue("minutes");
		
		//add all tags to this 
		myTime.add(timeList.minutes);
		
		}
		
		/*else 
		{			//handle the no prediction scenario
			
			timeList.minutes=null;
			myTime.add(timeList.minutes);
			
			
		} // end of else*/
		
		
	}
	
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
	
    if (localName.equals("direction")) {
		inMinutes.addAll(myTime);
		
	    System.out.println("contents of : inMinutes " +  inMinutes );
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
	
	public ArrayList<String> retrieveTime() {
		// return the list of Routes to calling program
		return inMinutes;
	}
	
	
}
 