package org.runextbus.com;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.runextbus.com.Global;
import org.runextbus.com.GetPrediction;
import org.runextbus.com.listingFav.DialogButtonClickHandler;
import org.runextbus.com.listingFav.DialogSelectionClickHandler;

public class ShowTime extends Activity implements OnClickListener  {

	    private TextView output;
	    static Spinner spinner = null;
	    //Button ButtonUpdate;
	    ImageButton ButtonUpdate;
	   // Button ButtonFav;
	    ImageButton ButtonFav;
	    ImageButton ButtonAlarm;
	    ImageButton OptionsButton;
	    ServerInterface sobj = new ServerInterface();
	    XmlParser xobj= new XmlParser();
	    String timeXml=null;
	  //  private DataHelper dbobj1;
	    private Global gObj;
	    String rr;
	    String ss;
	    
	    
	    private List<String> lv_arr=new ArrayList<String>();    
	    
		public CharSequence[] _options; 
		public boolean[] _selections ;	
		public int manage=0;
	    
/**
 * onCreate	  
 */
	  
public void onCreate(Bundle savedInstanceState) {
   
	super.onCreate(savedInstanceState);
    //setContentView(R.layout.showtime);
    setContentView(R.layout.test);
    //Global.dbobj=new DataHelper(this);
    lv_arr.add("10");
	lv_arr.add("20");
	lv_arr.add("30");
	
    _options = lv_arr.toArray(new CharSequence[lv_arr.size()]);
    displayTime(Global.time);    
   
}
 
/**
 * @description displayTime
 * @param time
 */

void displayTime(ArrayList <String> time){
	
	output = (TextView)findViewById(R.id.out_text);
	rr=Global.route;
	ss=Global.stop;
	String times= null;
	StringBuilder sb = new StringBuilder();
	times = "\n ROUTE	"+Global.route+"\n\n";
	sb.append(times);
	times = " STOP  "+Global.stop+"\n\n";
	sb.append(times);
	times = " "+Global.direction+"\n\n";
	sb.append(times);
	
	times="  NEXT BUS ARRIVAL IN  \n\n";
	sb.append(times);
	
	//display only the first two times 
	times="	"+(String)time.get(0)+" min"+"     ";
	sb.append(times);
	
	if(time.size()>1){
	if((String)time.get(1)!=null){
		times="	"+(String)time.get(1)+" min"+"      ";
		sb.append(times);
	}
	}
	
	if(time.size()>2){
	if((String)time.get(2)!=null){
		//String timeThree = (String)time.get(2);
		times=" "+(String)time.get(2)+" min"+"       ";
		sb.append(times);
	}
	}
	
	/*Iterator<String> itr= time.iterator();
	while(itr.hasNext()){
	
		times="						"+ itr.next()+"min"+"\n";
		sb.append(times);
		
	}*/
		
	this.output.setText(sb.toString());
	
	//wait for update to be clicked 
	//can make it auto update 
	
	ButtonUpdate=(ImageButton)findViewById(R.id.ButtonUpdate);
	ButtonUpdate.setOnClickListener(this);
	
	ButtonFav=(ImageButton)findViewById(R.id.ButtonFav);
	ButtonFav.setOnClickListener(this);
	
	ButtonAlarm=(ImageButton)findViewById(R.id.ButtonAlarm);
	ButtonAlarm.setOnClickListener(this);
	
	 /*final CheckBox ButtonCheck=(CheckBox)findViewById(R.id.ButtonCheck);
	 ButtonCheck.setOnClickListener(this);
	 ButtonCheck.setChecked(false);*/

} // end of displayTime


/**
 * onClick of update button 
 */

public void onClick(View v) {
		// TODO Auto-generated method stub
	//Global.dbobj=new DataHelper(this);
	
	switch(v.getId())
		{	
		case R.id.ButtonUpdate:
		
			List<String> routeTag=Global.dbobj.getRouteTag(rr);

			List<String> stopTag=Global.dbobj.getStopTag(rr,ss);
			List<String> dirTag=Global.dbobj.getDirTag(rr,ss);
			
			
			Global.temprouteTag=routeTag.get(0);
			Global.tempstopTag=stopTag.get(0);
			Global.tempdirTag=dirTag.get(0);
		
			System.out.println("R and S:"+Global.tempstopTag+":"+Global.tempstopTag);
			
			updateTime(Global.temprouteTag, Global.tempstopTag,Global.tempdirTag);
			break;		
	
		/*case R.id.ButtonCheck:
	 if (((CheckBox) v).isChecked()) {*/      
         //   System.out.println("MARKING AS FAVORITE \n");
	 case R.id.ButtonFav:
		 Global.dbobj.addFav(Global.route,Global.stop,Global.direction);   
    	Toast.makeText(ShowTime.this, "MARKED AS FAVORITE", Toast.LENGTH_SHORT).show();
    	break;
    	
	 case R.id.ButtonAlarm:
         //dbobj1.addFav(Global.route,Global.stop,Global.direction);   
    	Toast.makeText(ShowTime.this, "Notify Activate", Toast.LENGTH_SHORT).show();
    	showDialog(manage);
		break;
		
	//}
} // end of switch case 
}// end of onClick


@Override
protected Dialog onCreateDialog( int id ) 
{
	return 
	new AlertDialog.Builder( this )
    	.setTitle( "CHOOSE TIME" )
    	//read the selected into options array and selections array
    	.setMultiChoiceItems( _options, _selections, new DialogSelectionClickHandler() )
  
    	.setPositiveButton( "SAVE", new DialogButtonClickHandler() )
    	.setNegativeButton("Cancel", new DialogButtonClickHandler() )
    	.create(); 
	
    	 
    	
    	
    	//.setPositiveButton( "RESET", new DialogButtonClickHandler() )
    	
} // end of dialog on create


public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener
{
	public void onClick( DialogInterface dialog, int clicked, boolean selected )
	{
		Log.i( "FAVORITES LIST SELECTION", _options[ clicked ] + " selected: " + clicked);
	}
	
}// end of class DialogselectionclickHandler


//if Manage Favorites is pressed then handle it

public class DialogButtonClickHandler implements DialogInterface.OnClickListener
{
	public void onClick( DialogInterface dialog, int clicked )
	{
		switch( clicked )
		{
			case DialogInterface.BUTTON_POSITIVE:
				System.out.println("BUTTON CLICKED ::: "+ clicked + dialog);
				extra();
				 
				break;
		}
	}
}// end of dialog handler








/**
 * updateTime
 * @param route
 * @param stop
 */

public void updateTime(String route, String stop,String dir ){

   /** All the URL's for invoking API on nextBus.com 
 	*	Make http request with stop and route given*/
    
  String timeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=predictions&a="+Global.agency+"&r="+route+"&s="+stop+"&d="+dir;
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

protected void extra(){
	System.out.println("PAUSE OPERATION ::::::::::::::: MANAGE");
	  manage=manage+1;	
}

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

