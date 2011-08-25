package org.runextbus.com.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.runextbus.com.model.Direction;

/**
 * 
 * @author Sangeetha
 * @Description 
 * from them xml message
 */
public class DirectionHandler extends DefaultHandler {
	
	private StringBuffer buffer = new StringBuffer();
	public ArrayList<String> myTag = new ArrayList<String>();
	public ArrayList<String> myTitle = new ArrayList<String>();
	public ArrayList<String> dirTag = new ArrayList<String>();
	public ArrayList<String> dirTitle = new ArrayList<String>();
	public ArrayList<String> extraTag = new ArrayList<String>();
	public ArrayList<String> extraTitle = new ArrayList<String>();
		ArrayList<String> value1=new ArrayList<String>();
		ArrayList<String> value2=new ArrayList<String>();
	public HashMap<String,ArrayList<String>> dirStopTagmap=new HashMap<String,ArrayList<String>>();
	public HashMap<String,ArrayList<String>> dirStopTitlemap=new HashMap<String,ArrayList<String>>();
	
	private Direction dirList;
	private boolean flag; 
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		buffer.setLength(0);
		
		if(qName.equals ("direction")){	
		dirList = new Direction();
		dirList.tag =atts.getValue("tag");
	    dirList.title =atts.getValue("title");
		flag=true;
	    if(!(myTag.contains(dirList.tag))){
			myTag.add(dirList.tag);
		}
		
	    if(!(myTitle.contains(dirList.title))){
			myTitle.add(dirList.title);
		}
	    
	    System.out.println("DIRLIST "+ dirList.tag +":::::"+dirList.title);
		}
			
		else if(flag&&qName.equals("stop")){
					String x = atts.getValue("tag");
					String y=atts.getValue("title");
		//	System.out.println("******"+x);
			extraTag.add(x);
			System.out.println("******"+x);
			extraTitle.add(y);
			
		}
		
		
		
}
	
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		String key=null;
	if (qName.equals("direction")) {
    	System.out.println("Direction stop handler");
    	if(dirList.tag!=null){
    		
    		value1.addAll(extraTag);
    		value2.addAll(extraTitle);
    		
    	dirStopTagmap.put(dirList.tag,value1);
    	dirStopTitlemap.put(dirList.title,value2);
    	flag=false;
    	//System.out.println("DIR ::::::"+key+"::::::"+extraTag);
    	extraTag.clear();
    	extraTitle.clear();
    	}
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
	
	public HashMap<String,ArrayList<String>> retrieveDirStopTag() {
		//System.out.println("***************"+value2.size());
		return dirStopTagmap;
	}
	

	public HashMap<String,ArrayList<String>> retrieveDirStopTitle() {
		
		return dirStopTitlemap;
	}
	
	
	
}
 