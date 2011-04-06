package org.runextbus.com;


import java.io.IOException; 
import org.runextbus.com.FavList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.runextbus.com.XmlParser;
import org.runextbus.com.ShowTime;
import org.runextbus.com.FavPrediction;
import org.runextbus.com.Global;
import org.runextbus.com.ServerInterface;

/**
 * 
 * @author Sangeetha
 * @Class : GetPrediction
 * @Description : Serves to get the prediction time when a particular route is selected
 * @see : org.runextbus.org.ServerInterface
 *
 */

public class GetPrediction extends Activity implements OnClickListener  {
	
	
	protected CharSequence[] _options = { "A Scott Hall", "B Busch Campus Center","EE Rutgers Student Center" };
	protected boolean[] _selections =  new boolean[ _options.length ];
	
	protected Button _optionsButton;
	
	
	
	public int flag;
	//some variables
	Global gobj = new Global();
	public static SQLiteDatabase db;
    Button ButtonSubmit;
    Button ButtonFavorite;
    CheckBox ButtonCheck;
    TextView txt;
    ServerInterface sobj = new ServerInterface();
    XmlParser xobj= new XmlParser();
    String timeXml=null;
    ShowTime stobj= new ShowTime();
    private DataHelper dbobj;
    
    public List<String> someList = new ArrayList<String>();
    public List<FavList> markFav = new ArrayList<FavList>();
    //spinners 
    
    static Spinner spinner1 = null;
	static Spinner spinner2 = null;
	
	public List<String> stopList = new ArrayList<String>();
	
	
 public void onCreate(Bundle savedInstanceState) {
	 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.getprediction);
    
   // deleteDb();
    
    //startUp();
    
        //SPINNER LOGIC BEGINS HERE ..	
    
    spinner1 = (Spinner)this.findViewById(R.id.SpinnerRoute);
    spinner2 = (Spinner)this.findViewById(R.id.SpinnerStop);
    
    
	this.dbobj=new DataHelper(this);
	/* First spinner value populated */
	
	List<String> routeList = new ArrayList<String>();
    routeList= dbobj.populateRouteSpinner(); 
    
    //System.out.println("ROUTELIST : " + routeList);
    //System.out.println("THIS : " + this);
    
    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeList);
	
    spinnerArrayAdapter1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
	 spinner1.setAdapter(spinnerArrayAdapter1);
	
	 //on selection  
	 spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener());
	
	    
	    ButtonSubmit=(Button)findViewById(R.id.ButtonSubmit);
    	ButtonSubmit.setOnClickListener(this);
    	
    	final CheckBox ButtonCheck=(CheckBox)findViewById(R.id.ButtonCheck);
    		
    		 	 ButtonCheck.setOnClickListener(this); 
    		 
    		 
 		
    	ButtonFavorite=(Button)findViewById(R.id.ButtonFavorite);
    	ButtonFavorite.setOnClickListener(this);

    	
 } // end of oncreate
 
 

 
 /**
  * on selection of spinner 1 update spinner 2 
  * @author Sangeetha
  *
  */
 
public class MyOnItemSelectedListener implements OnItemSelectedListener{
	
	public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
   
//	Toast.makeText(parent.getContext(), "Route Selected : " +parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
	    	
	    	String RouteTitle = parent.getItemAtPosition(pos).toString();
	    	
	    	stopList=dbobj.populateStopSpinner(RouteTitle);
	    	
	 		//System.out.println("STOP LIST : " + stopList);
	 		//System.out.println("ANOTHER THIS : " + GetPrediction.this); 		
	 		
	 		ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(GetPrediction.this, android.R.layout.simple_spinner_item, stopList); 		
	 	 	   spinnerArrayAdapter2.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
	 		    spinner2.setAdapter(spinnerArrayAdapter2);
	    	
	    }
	    
	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	} // end of class 


void deleteDb(){
	

	this.dbobj = new DataHelper(this); // database object 
    this.dbobj.deleteAll(); // Delete data from the table RUData
    this.dbobj.deleteAll_fav(); // Delete data from the table RUData
    
}



/*
 * Redundant startup : To get the values onto database 
 */

void startUp(){
		
	this.dbobj = new DataHelper(this); // database object 
    
    String routeXml=null;
    String stopsUrl=null;
    //objects needed 
    ServerInterface sobj = new ServerInterface();
    XmlParser xobj= new XmlParser();
    
       
    
   /** All the URL's for invoking API on nextBus.com */
 
  String routeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=routeList&a="+Global.agency;
  routeXml = sobj.retrieve(routeUrl);
   	// check for favorite flag 
  
    		   	if(routeXml != null){
    		   		//System.out.println("GetPrediction Log: Retrieve http response SUCCESS\n");
    		   		
    		   		/**
    		   		 * GET THE ROUTES  
    		   		 */  		   		
    		   		ArrayList<String> routeTag = xobj.parseRouteResponseTag(routeXml);
    		//   		System.out.println(routeTag+"\n");
    		   		ArrayList<String> routeTitle = xobj.parseRouteResponseTitle(routeXml);
    		  // 		System.out.println(routeTitle+"\n");
    		       
    		   		
    		   		/**
    		   		 * GET STOPS PER ROUTE        		   		 
    		   		 */
    		   		
    		   		
    		   		//use iterator to iterate through arrayList
    		   		
    		   		Object rTagPass[]=routeTag.toArray();
    		   		Object rTitlePass[]=routeTitle.toArray();
    		   		
    		   		Iterator<String> itrTag= routeTag.iterator();
    		   		Iterator<String> itrTitle= routeTitle.iterator();
    		   		
    		   		
    		   		int i=0;
    		   		// if we use the iteratot logic to traverse
    		   		//while((itrTag.hasNext())&&(itrTitle.hasNext())){
	  
    		   		while(itrTag.hasNext()){
    		   			
    		   		// Put rTag into a variable as itrTag shall unnecessarily increment otherwise 	
    		   			
    		   		String rTag = itrTag.next();
    		   		
    		   		//String rTitle = itrTitle.next();
    		   		
    		   		//create a new url for each route 
    		   		stopsUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=routeConfig&a="+Global.agency+"&r="+rTag;
    		   		
    		   		String stops= sobj.retrieve(stopsUrl);
    		   		ArrayList<String> stopTag = xobj.parseStopsResponseTag(stops);
    		   		ArrayList<String> stopTitle = xobj.parseStopsResponseTitle(stops);
    		   	
    		   		/** 
    		   		 * Put these in database  
    		   		 * Update RUData table with column 
    		   		 */
    		   		
    	dbobj.insertAll(stopTitle,rTagPass[i],rTitlePass[i],stopTag);
    	i++;
     	 		
 } // end of while 
    
}// end of if xml not null
    		   	
    		   	
else {
		   		
		   		/* 
		   		 * Prediction Unavailable
		   		 */
		   		
	Intent myIntent = new Intent(GetPrediction.this, PredictionFailure.class);
 	GetPrediction.this.startActivity(myIntent);
		   	
		   	
} // end of else corresponding to xml null 	
    		   	
} // end of startup


 /**
 * @Description On click of get prediction button we execute this
 * @param null
 * @return prediction time screen or error screen  
 */
 		  

public void onClick(View p) {
	// TODO Auto-generated method stub
	 
	 /*This is for populating second spinner*/

	   	 // Pass the route and stop selected  to startPrediction on click of submit 
    
	Global.route = (String)spinner1.getSelectedItem();
    Global.stop=(String)spinner2.getSelectedItem();
    
		//System.out.println("STOP : " + Global.stop);
		//System.out.println("ROUTE : " + Global.route);
    
    // get the route and stop tags to be passed to get prediction 
    List<String> listRouteTag = new ArrayList<String>();
    
   listRouteTag=dbobj.getRouteTag(Global.route);
   Global.routeTag = listRouteTag.get(0);
   
    
    List<String> listStopTag = new ArrayList<String>();
    listStopTag=dbobj.getStopTag(Global.route,Global.stop);
    Global.stopTag = listStopTag.get(0);

    
    switch(p.getId())
	
	{
	
	case R.id.ButtonFavorite:	
			
		Intent i=new Intent(this,listingFav.class);
		startActivity(i);
		
		//favPrediction();  
		/*call list view 
		Intent i=new Intent(this,listingFav.class);
		startActivity(i);*/
		/*List<String> list = new ArrayList<String>();
	    list=dbobj.getFavs();
		System.out.println(list);
		showDialog(0);*/	
	 	break;
	 	
	     
	
	case R.id.ButtonSubmit:
	// values to be read from spinner : route and stop 	 
		//ButtonCheck.setChecked(false);	
		startPrediction(Global.routeTag,Global.stopTag);
		break;
		
	case R.id.ButtonCheck:
		
		
		/**
		 * 01. Set the favorite flag to 1
		 * 02. Over Write Favorite route and stop in Global file with Route and Stop 
		 * 
		 */
	// uncheck check box here 
		//ButtonCheck.setChecked(false);

		 if (((CheckBox) p).isChecked()) {
			 //((CheckBox) p).setChecked(true);
	          
			 Toast.makeText(GetPrediction.this, "Marking as Favorite", Toast.LENGTH_SHORT).show();
	            System.out.println("MARKING AS FAVORITE \n");
	            dbobj.addFav(Global.route,Global.stop);
	            updateFavData();
	    	//((CheckBox) p).setChecked(false);
		 
		 }
		 
		 else {
				((CheckBox) p).setChecked(false);
				 //dbobj.deleteFav(Global.route,Global.stop);
				
				//Toast.makeText(GetPrediction.this, "Favorite cannot be undone", Toast.LENGTH_SHORT).show();
	        }
		
		break;
		
} // end of switch case 
}// end of onClick

/*
 * updateFavData
 */


@Override
protected Dialog onCreateDialog( int id ) 
{
	return 
	new AlertDialog.Builder( this )
    	.setTitle( "Favorites" )
    	//read the selected into options array and selections array
    	.setMultiChoiceItems( _options, _selections, new DialogSelectionClickHandler() )
    	
    	.setPositiveButton( "OK", new DialogButtonClickHandler() )
    	.create();
}


public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener
{
	public void onClick( DialogInterface dialog, int clicked, boolean selected )
	{
		//Log.i( "FAVORITES LIST SELECTION", _options[ clicked ] + " selected: " + selected );
	}
}


//if submit id pressed then handle it
public class DialogButtonClickHandler implements DialogInterface.OnClickListener
{
	public void onClick( DialogInterface dialog, int clicked )
	{
		switch( clicked )
		{
			case DialogInterface.BUTTON_POSITIVE:
				//do some thing with the selected data
				printSelectedPlanets();
				break;
		}
	}
}

protected void printSelectedPlanets(){
	for( int i = 0; i < _options.length; i++ ){
		Log.i( "SELECTED LIST", _options[ i ] + " selected: " + _selections[i] );
	}
}

void updateFavData() {
	
	// disable the green thing on check box  
	
	//Set global flag 
	
	Global.fav=1;
	
	// find put if database is null or full :P 
	
	// delete previous data in database 
	this.dbobj.deleteAll_fav();

	//insert the new values into database 
	dbobj.insertFavData(1, Global.routeTag, Global.route, Global.stopTag, Global.stop);
	
	
}


/*
 * startPrediction
 */

void startPrediction(String route, String stop){
	
String timeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=predictions&a="+Global.agency+"&r="+route+"&s="+stop;
 
String timeXml = sobj.retrieve(timeUrl);
 
//if xml string returned is not null
System.out.println(" xml is : "+timeXml);
//if(timeXml!= null){

Global.time= xobj.parseTimeResponse(timeXml);

System.out.println("Global time is : "+Global.time);

ArrayList<String> temp= Global.time;

int count = temp.size();

System.out.println("NUMBER OF ELEMENTS IS : \n"+count);

if(count==0)
{
		System.out.println("predictionError is called .....\n");
		predictionError();
		
}

else{
	
Intent i=new Intent(this,ShowTime.class);
startActivity(i);
}

} // end of startPrediction


/**
 * predictionError
 * @param null
 * @return null 
 * @author Sangeetha
 */
void predictionError(){
	
	Intent i=new Intent(this,PredictionFailure.class);
	startActivity(i);
	
}// end of predictionError

void favPrediction(){

	 someList=dbobj.getFavorite(1); 	
	 System.out.println(someList);
	 Global.favRouteTag=someList.get(0);
	 Global.favRoute=someList.get(1);
	 Global.favStopTag=someList.get(2);
	 Global.favStop=someList.get(3);
	 
	 
	 // call getFavTime( rtag,rtitle,stag,stitle);
	 Intent i=new Intent(this,FavPrediction.class);
	startActivity(i);
	
	// fobj.getFavTime(favrTag, favrTitle, favsTag, favsTitle);	 
}

}//end of Class GetPrediction      

     

 
        
    

