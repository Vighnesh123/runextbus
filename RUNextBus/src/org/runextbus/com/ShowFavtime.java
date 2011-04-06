package org.runextbus.com;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import org.runextbus.com.Global;

public class ShowFavtime extends Activity implements OnClickListener  {

	    private TextView output;
	    static Spinner spinner = null;
	    Button ButtonUpdate;
	    Button OptionsButton;
	    ServerInterface sobj = new ServerInterface();
	    XmlParser xobj= new XmlParser();
	    String timeXml=null;
	    
	   
/**
 * onCreate	  
 */
	  
public void onCreate(Bundle savedInstanceState) {
   
	super.onCreate(savedInstanceState);
    setContentView(R.layout.showtime);
    displayTime(Global.time);    
  
}
  
/**
 * @description displayTime
 * @param time
 */

void displayTime(ArrayList <String> time){
	
	output = (TextView)findViewById(R.id.out_text);    	        
	String times= null;
	StringBuilder sb = new StringBuilder();
	times="\n"+"\n";
	sb.append(times);
	times = "	ROUTE	:	"+Global.route+"\n";
	sb.append(times);
	times = "	STOP	:	"+Global.stop+"\n";
	sb.append(times);
	
	//display only the first two times 
	String timeOne = (String)time.get(0);
	times="						"+ timeOne+"min"+"\n";
	sb.append(times);
	if((String)time.get(1) != null){
	String timeTwo = (String)time.get(1);
	times="						"+ timeTwo+"min"+"\n";
	sb.append(times);
	}
	/*Iterator<String> itr= time.iterator();
	while(itr.hasNext()){
	
		times="						"+ itr.next()+"min"+"\n";
		sb.append(times);
		
	}*/
		
	this.output.setText(sb.toString());
	
	//wait for update to be clicked 
	//can make it auto update 
	
	ButtonUpdate=(Button)findViewById(R.id.ButtonUpdate);
	ButtonUpdate.setOnClickListener(this);
	
	//wait for options to be clicked
	
	
	 

} // end of displayTime


/**
 * onClick of update button 
 */

public void onClick(View v) {
		// TODO Auto-generated method stub

	switch(v.getId())
		
		{
		
		case R.id.ButtonUpdate:
	// call update with database returned values
	//change the Global.route and Global.stop values
		
			updateTime(Global.routeTag, Global.stopTag);
			break;
					
			
		}	
	       	
}// end of onClick

/**
 * updateTime
 * @param route
 * @param stop
 */

public void updateTime(String route, String stop){

   /** All the URL's for invoking API on nextBus.com 
 	*	Make http request with stop and route given*/
    
  String timeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=predictions&a="+Global.agency+"&r="+route+"&s="+stop;
  timeXml = sobj.retrieve(timeUrl);
  ArrayList<String> timeMinutes = xobj.parseTimeResponse(timeXml);
  int count = timeMinutes.size();
  if(count!=0){
  // pass the entire arrayList
	  displayTime(timeMinutes);}
  
  else{
	  displayError();
  }

}//end of update
	
/**
 * displayError()
 * @author Sangeetha
 */
	
public void displayError(){
	 	
			//calls the error screen 
     	
	Intent i=new Intent(this,PredictionFailure.class);
	startActivity(i);

	
	 } // end of displayError
	
}// end of Showtime class



