package org.runextbus.com.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
	
	
	public String dirTag;
	public String dirTitle;
	
	public ArrayList<String> extraTag = new ArrayList<String>();
	public ArrayList<String> extraTitle = new ArrayList<String>();
	
	ArrayList<String> value1=new ArrayList<String>();
	ArrayList<String> value2=new ArrayList<String>();
	ArrayList<String> value3=new ArrayList<String>();
	ArrayList<String> value4=new ArrayList<String>();
	
	//HashMap to save Tag:Title of Stops
	HashMap<String,String> stopTitles = new HashMap<String,String>();
		
	//HashMap to be returned has dirTag : <dirTitle,stpTags,stpTitles>
	public HashMap<String,Direction> dirStopTagmap=new HashMap<String,Direction>();
	private Direction dirList;
	private boolean flag; 
	private boolean flag2;
	int i=0;
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
	
		buffer.setLength(0);

		if(qName.equals ("direction")){	
	
		dirList = new Direction();
		dirTag =atts.getValue("tag");
	    dirTitle =atts.getValue("title");
		flag=true;
		
		/*if(!(myTag.contains(dirTag))){
			//direction Tag
			myTag.add(dirTag);
		}
		
	    if(!(myTitle.contains(dirTitle))){
	    	//direction Title
			myTitle.add(dirTitle);
		}*/
	   
		}
			
		//if stop tag encountered inside dire2ction 
		
		else if(flag&&qName.equals("stop")){
				flag2=true;
				String x = atts.getValue("tag");
				//no title available here 
				extraTag.add(x);
		}
		
		//if stop tag encountered outside 
		
		else if(!(flag2)&&qName.equals("stop")){
		//tag and title need to be saved 
			String x = atts.getValue("tag");
			String y= atts.getValue("title");
			//flag2=true;
			//HashMap of stop Tags:Title 
			stopTitles.put(x,y);
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		}

			
		
}
	
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		
	if (qName.equals("direction")) {
    	System.out.println("Direction stop handler");	
    
    	if(flag2&&dirTag!=null&&dirTitle!=null){
    		
    		
    		
    		//extraTag is filled -- stops Tag
    		if(i==0){
    			Direction dirObj=new Direction();
        		dirObj.dirTitle=dirTitle;
    		
    		value1.addAll(extraTag);
    		dirObj.stopTag=value1;
    		
    		//extraTitle need to be filled from stopTitles hashMap 
    		extraTitle=getStopTitles(value1);
    		
    		value2.addAll(extraTitle);
    		dirObj.stopTitle=value2;
    		dirStopTagmap.put(dirTag, dirObj);
    		System.out.println("Inserted into HashMap::::"+dirTag +":::"+ dirTitle +":::"+dirObj.stopTag+ ":::"+dirObj.stopTitle );
    		flag=false;
    		extraTag.clear();
    		extraTitle.clear();
    		i++;
    		
    		}
    		
    		else if(i==1){
    			
    		Direction dirObj2=new Direction();
        	dirObj2.dirTitle=dirTitle;
    		value3.addAll(extraTag);
    		dirObj2.stopTag=value3;

    		//extraTitle need to be filled from stopTitles hashMap 
    		extraTitle=getStopTitles(value3);
 
    		value4.addAll(extraTitle);
    		dirObj2.stopTitle=value4;
    		dirStopTagmap.put(dirTag, dirObj2);
    		System.out.println("Inserted into HashMap::::"+dirTag +":::"+ dirTitle +":::"+dirObj2.stopTag+ ":::"+dirObj2.stopTitle );
    		flag=false;
    		
    		}
    		
    		//value1.clear();
    		//value2.clear();
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
	
	/*public HashMap<String,ArrayList<String>> retrieveDirStopTag() {
		
		return dirStopTagmap;
	}*/
	
	
public HashMap<String,Direction> retrieveDirStop() {
		return dirStopTagmap;
	}

	/*public HashMap<String,ArrayList<String>> retrieveDirStopTitle() {
		return dirStopTitlemap;
	}*/
	
public ArrayList<String> getStopTitles(ArrayList<String> stopTags){
	
		ArrayList<String> stitles = new ArrayList<String>();
		Iterator <String> itr= stopTags.iterator();
		while(itr.hasNext()){
			String key=itr.next();
			stitles.add(stopTitles.get(key));
			System.out.println("getStopTitles KEY::::: "+key+"%%%%%%"+stopTitles.get(key));
		}	
		return stitles;
		
	}
	
	
}
 