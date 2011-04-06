package org.runextbus.com;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/*
 * previous function
public class PredictionFailure extends Activity {
	
		 
	public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.error);

		       // System.out.println("starting the activity\n");
		       ImageView image = (ImageView) findViewById(R.id.ImageView01);
		    }
	  

}

*/




import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import org.runextbus.com.Global;
import org.runextbus.com.ShowTime;



public class PredictionFailure extends Activity implements OnClickListener  {

	    private TextView output;
	    static Spinner spinner = null;
	    Button ButtonUpdate;
	    Button OptionsButton;
	    ServerInterface sobj = new ServerInterface();
	    XmlParser xobj= new XmlParser();
	    String timeXml=null;
	    public ShowTime stobj;
	   
/**
 * onCreate	  
 */
	  
public void onCreate(Bundle savedInstanceState) {
   
	super.onCreate(savedInstanceState);
    setContentView(R.layout.predictionerror);
    displayError();    
  
}
  
/**
 * @description displayTime
 * @param time
 */

void displayError(){
	
	output = (TextView)findViewById(R.id.out_text);    	        
	String times= null;
	StringBuilder sb = new StringBuilder();
	times = "  ROUTE	:	"+Global.route+"\n";
	sb.append(times);
	times = "  at "+Global.stop+"\n";
	sb.append(times);
	
	times= "\n\n	PREDICTION\n " +
		   "	CURRENTLY\n " +
		   "	UNAVAILABLE\n";
		sb.append(times);
			
	this.output.setText(sb.toString());
	
	//wait for update to be clicked 
	//can make it auto update 
	
	ButtonUpdate=(Button)findViewById(R.id.ButtonUpdate);
	ButtonUpdate.setOnClickListener(this);
	

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
  
  if(timeXml!=null){
  // pass the entire arrayList
	  stobj.displayTime(timeMinutes);}
  
  else{
	  displayError();
  }

}//end of update
	
	
}// end of PredictionFailure class




