package org.runextbus.com;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import org.runextbus.com.XmlParser;
import org.runextbus.com.ShowTime;
import org.runextbus.com.Global;
import org.runextbus.com.ServerInterface;
import java.util.Iterator;
import org.runextbus.com.FavPrediction;
import org.runextbus.com.model.Direction;

import android.widget.Toast;


public class GetPrediction extends ListActivity implements OnClickListener  {
     
	/* local variables*/
    String localDirection;
    String localStop;
    String localRoute;
    
    
    
    ServerInterface sobj =  new ServerInterface();
    XmlParser xobj= new XmlParser();
    private DataHelper dbobj;
    
    
    
    /* User Interface variables */
    Button ButtonFavorite;
    CheckBox ButtonCheck;
    TextView txt;
    public ArrayAdapter<String> spinnerArrayAdapter1;
    public ArrayAdapter<String> spinnerArrayAdapter3; 
    public static Spinner spinner1 = null;
    public static Spinner spinner2 = null;
    public static Spinner spinner3 = null;
     public Activity myAct;
    public List<String> stopList = new ArrayList<String>();
    public List<String> dirList = new ArrayList<String>();
    public List<String> routeList = new ArrayList<String>();
    private List<String> lv_arr=new ArrayList<String>();
    public List<String> routeFavList = new ArrayList<String>();
    public List<String> stopFavList = new ArrayList<String>();
    public List<String> dirFavList = new ArrayList<String>();
    public List<String> someList = new ArrayList<String>();
    public static ArrayAdapter<String> adapter=null;
    public CharSequence[] _options;
    public boolean[] _selections ;

    //progress bar :
    private static int progress = 0;
    private ProgressBar progressBar;
    private int progressStatus = 0;    
    private Handler handler = new Handler();
    public String StopTitle;
    public String DirTitle;
    public String sproute;
    public String spstop;
    public String spdirection;
    public ArrayList<String> stopTag = new ArrayList<String>();
	public ArrayList<String> stopTitle = new ArrayList<String>();
   
	@Override
   public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newmain);
        this.dbobj=new DataHelper(this);
        myAct=this;
  
    /* For Database to be synced with nextbus.com enable this*/
     deleteDb();
     startUp(); 
   
     //spinners for route and stop selection 
        spinner1 = (Spinner)this.findViewById(R.id.SpinnerRoute);
        spinner2 = (Spinner)this.findViewById(R.id.SpinnerStop);
        spinner3 = (Spinner)this.findViewById(R.id.SpinnerDir);
       
        
            /* First spinner value populated */
       
       routeList= dbobj.populateRouteSpinner(); 
       
     
       spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeList);
       spinnerArrayAdapter1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
       
       spinner1.setAdapter(this.spinnerArrayAdapter1);
       spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener2());
     
       ButtonFavorite=(Button)findViewById(R.id.ButtonFavorite);
       ButtonFavorite.setOnClickListener(this);
       
       progressBar = (ProgressBar) findViewById(R.id.progressbar);
        
        //---do some work in background thread---
        new Thread(new Runnable() 
        {
   //             ProgressDialog pd = ProgressDialog.show(getBaseContext(),"","Fetching Favorites ...",false);
            public void run() 
            {
                while (progressStatus < 100) 
                {
                    progressStatus = doSomeWork();
 
                    //---Update the progress bar---
                    handler.post(new Runnable() 
                    {
                        public void run() {
                      
                            progressBar.setProgress(progressStatus);
                        }
                    });
 
                }
                //---hides the progress bar---
                handler.post(new Runnable() 
                {
                    public void run() {
                        //---0 - VISIBLE; 4 - INVISIBLE; 8 - GONE---
                        progressBar.setVisibility(8);
 //                       pd.dismiss();
                    }
                });
            }    
 
            private int doSomeWork() 
            {
                                int key=0;
                        
                        key=setListAdapterMy();
                        
                        if(key==1){
                        
                                return 100;     
                        }
                        return key;
            }
 
        }).start();
        
        //adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lv_arr);
        adapter=new ArrayAdapter<String>(this,R.layout.list_item,lv_arr);
        setListAdapter(adapter);    
        
       
 }
         
  /*
   * Support for spinner 3 ; direction 
   */
        
public class MyOnItemSelectedListener2 implements OnItemSelectedListener{

	public ArrayAdapter<String> spinnerArrayAdapter3;
            
            public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
            	String RouteTitle = parent.getItemAtPosition(pos).toString();
                dirList= dbobj.populateDirSpinner(RouteTitle);
                   spinnerArrayAdapter3 = new ArrayAdapter<String>(myAct, android.R.layout.simple_spinner_item, dirList);
                   spinnerArrayAdapter3.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                   spinner3.setAdapter(spinnerArrayAdapter3);
                   
                   spinner3.setAdapter(this.spinnerArrayAdapter3);
                   spinner3.setOnItemSelectedListener(new MyOnItemSelectedListener());
                   
                   
                      localRoute=(String)spinner1.getSelectedItem();
                     
                       
                   }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
                   
  } // end of class for direction spinner  

        
 /*
  * Support for spinner 2   
  */
 
public class MyOnItemSelectedListener implements OnItemSelectedListener{
        
   public ArrayAdapter<String> spinnerArrayAdapter2;
     
   public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
        
      System.out.println("::::::::::::: Stop ::::::::::::: ");	
      localDirection = parent.getItemAtPosition(pos).toString();
      //Toast.makeText(parent.getContext(), "Route/Direction Selected : " +localRoute+"/"+localDirection, Toast.LENGTH_LONG).show();
        
      stopList=dbobj.populateStopSpinner(localRoute,localDirection);
      stopList.add(0,"PICK STOP");
                // use the stopListvalue to populate spinner                    
                        
                MySpinnerAdapter spinnerArray = new MySpinnerAdapter(myAct,R.layout.custom_spinner_row,R.id.text, stopList);
                   spinner2.setAdapter(spinnerArray);
          
                   spinner2.setOnItemSelectedListener(new OnItemSelectedListener()

                   { 
                           public void onItemSelected(AdapterView<?> arg0,View view, int pos, long id) {
                                   
                   StopTitle = arg0.getItemAtPosition(pos).toString();
                    
                    if(StopTitle!="PICK STOP"){
                    	
                    //Global.route = (String)spinner1.getSelectedItem();
                      
                    	//Global.stop=(String)spinner2.getSelectedItem();
                   Global.route= localRoute;
                   Global.direction= localDirection;
                   Global.stop=StopTitle;
                    
                  ButtonFavorite.setEnabled(false);
                                try{
                                startPrediction(Global.route,Global.stop,Global.direction);
                                
                                }
                                	catch (Exception e) {
                                		lv_arr.clear();
                                		lv_arr.add("No Internet service..Please try again later");
                                		adapter.notifyDataSetChanged();
                                	}
                    
                               
                                
                    }
                        
                        else{
                      
                        //do nothing    
                        
                        }
                     }
                  
                     public void onNothingSelected(AdapterView parent) {                          
                      // TODO Auto-generated method stub
                  
                     }
                  
                          });
                   
               }


               private class MySpinnerAdapter extends ArrayAdapter{

               public MySpinnerAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
               
                   super(context, resource, textViewResourceId, objects);
                   
                   System.out.println("Inside Myspinner adapter :"+ objects.get(0));
                   
                   
               }    
                   
          }

/*
 * first spinner  
 *  (non-Javadoc)
 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
 */
            public void onNothingSelected(AdapterView parent) {
              // Do nothing
            }
        } // end of class for second spinner  

public void onListItemClick(ListView parent, View v,int position, long id) 
{   
           int i=position;      
   routeFavList=dbobj.getFavRoute();       
   stopFavList=dbobj.getFavStop();
   dirFavList=dbobj.getFavDir();
   
   //dir
   
   if(routeFavList.size()==0){
       lv_arr.clear();
       lv_arr.add("No current Favorites");
       adapter.notifyDataSetChanged();
   }
   
   else{
	   
   ArrayList<String> fTime =new ArrayList<String>();
   fTime.clear();
   
   fTime = getTime(routeFavList.get(i),stopFavList.get(i),dirFavList.get(i)); 
        
        if(fTime.size()==0){
                lv_arr.remove(i);
                lv_arr.add(i,routeFavList.get(i)+" at "+ stopFavList.get(i)+" :\n No Prediction");
                adapter.notifyDataSetChanged();
 
        }
        
        else{
               
        if(fTime.size()>=2){
                lv_arr.remove(i);
        lv_arr.add(i,routeFavList.get(i)+" at "+ stopFavList.get(i)+" in - \n"+fTime.get(0)+"min "+fTime.get(1)+"min");
        }
        else{
                lv_arr.remove(i);
                lv_arr.add(i,routeFavList.get(i)+" at "+ stopFavList.get(i)+"in - \n"+fTime.get(0)+"min ");
        }
        
        routeFavList.clear();
        stopFavList.clear();
      
        adapter.notifyDataSetChanged();
      
        }
        }       
        
 }
        
 /**
 * @Description On click of get prediction button we execute this
 * @param null
 * @return prediction time screen or error screen  
 */
                  

public void onClick(View p) {
      
	//Only one button to be handled
    switch(p.getId())
        
        {
        
        case R.id.ButtonFavorite:               
                Intent i=new Intent(this,listingFav.class);
                startActivity(i);
                break;
                 
                
} // end of switch case 
}// end of onClick
/*
 * setListAdapterMy : similar to setListAdapter function 
 * Used to set the values for list adapter used to display favorite predictions 
 * returns 1 : to calling function with appropriately set list array and exception handling 
 */

public int setListAdapterMy(){
            
            routeFavList=dbobj.getFavRoute();       
            stopFavList=dbobj.getFavStop();
            dirFavList=dbobj.getFavDir();
            
            lv_arr.clear();
            
            ArrayList<String> favTime = new ArrayList<String>();
        
            //No favorites 
            
           if(routeFavList.size()==0){
                    lv_arr.clear();
                lv_arr.add("No current Favorites");
 
           }
            
          //If there are favorites 
          else{
            
            //case 01- One favorite only
        	  
                if((routeFavList.size()==1)&&(stopFavList.size()==1)){
            
                        favTime.clear();        
                        try{
                              favTime = getTime(routeFavList.get(0),stopFavList.get(0),dirFavList.get(0)); 
                        if(favTime.size()==0){
                          lv_arr.add(routeFavList.get(0)+" at "+ stopFavList.get(0)+" :\n No Prediction");
                        }

                        else{
                                if(favTime.size()>=2){
                        lv_arr.add(routeFavList.get(0)+" at "+ stopFavList.get(0)+" in :\n"+favTime.get(0)+"min "+favTime.get(1)+"min");
                         }      
                        
                                else{   
                                        lv_arr.add(routeFavList.get(0)+" at "+ stopFavList.get(0)+" in :\n"+favTime.get(0)+"min ");
                                }
                                        
                        } // end of else
                        
                      }   catch (Exception e) {
                    	  	lv_arr.clear();
                            lv_arr.add("No Internet service");
                            return 1;
                      }
           }//end of one favorite block  
                        
                //case 02- Atleast 2 Favorites
                
         else if((routeFavList.size()>=2)&&(stopFavList.size()>=2)){
                        
              // System.out.println("Option selected is : Different route different stops");
                        
                for (int i=0;i<2;i++){
                        
                favTime.clear();  
                
                //try getting the prediction times and catch exception if not 
                try{
                	
                favTime = getTime(routeFavList.get(i),stopFavList.get(i),dirFavList.get(0));
                
                if(favTime.size()==0){
                       lv_arr.add(routeFavList.get(i)+" - "+ stopFavList.get(i)+" :\n No Prediction");
                }
                
                else{
                        if(favTime.size()>=2){
                lv_arr.add(routeFavList.get(i)+" - "+ stopFavList.get(i)+" :\n"+favTime.get(0)+"min "+favTime.get(1)+"min");
                 }      
                        else{
                                lv_arr.add(routeFavList.get(i)+" - "+ stopFavList.get(i)+" :\n"+favTime.get(0)+"min ");
                        }       
               }
     
                //prevent application from crash : exception handling 
                } catch (Exception e) {
                		lv_arr.clear();
                		lv_arr.add("No Internet service");
                        return 1;
             }
      
                
            } // end of for loop
                
            }// end of two favorite block 
            
            }// end of favorite exist block 
          
                 
           return 1;
}
/*
 * startPrediction : To provide prediction time for new Route and Stop from Spinners
 */

void startPrediction(String r, String s, String d){	

sproute=r;
spstop=s;
spdirection=d;
	
final ProgressDialog dialog = ProgressDialog.show(myAct,"", "Loading. Please wait...", true);
    
	final Handler handler = new Handler() {
	   public void handleMessage(Message msg) {
	      dialog.dismiss();
	      }
	   };
	   
	Thread checkUpdate = new Thread() {  

		public void run() {
		   System.out.println("Inside the dialog !!");
		  try{
		   Global.time=getTime(sproute,spstop,spdirection);
		   if(Global.time.size()!=0){
			   Intent i=new Intent(myAct,ShowTime.class);
			   startActivity(i);		   
			   }
			   else{
	               Intent i=new Intent(myAct,PredictionFailure.class);
	               startActivity(i);			   
			   }

          // ButtonFavorite.setEnabled(true);
		   
		  }
			catch (Exception e) {
               System.out.println("Oooops some thing went wrong !!! ");
               
			}
			
						handler.sendEmptyMessage(0);
	      }
		
	   };checkUpdate.start();

} // end of startPrediction
/*
 * getTime : Pass the route and stop Title to get the new prediction 
 * internally called by startPrediction
 */
public ArrayList<String> getTime(String route, String stop, String direction){

	String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
	
	
	Date anotherCurDate = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d 'at' hh:mm a 'in the year' yyyy G");
	String formattedDateString = formatter.format(anotherCurDate);  
	
	
	System.out.println("Formatted Date Time ::::::::"+formattedDateString);
	
	// textView is the TextView view that should disp lay it
	//textView.setText(currentDateTimeString);
	System.out.println("Current Time :::::::::::::::::::::::::"+currentDateTimeString);
ArrayList<String>time= new ArrayList<String>();
List<String> listRTag = new ArrayList<String>();
List<String> listSTag = new ArrayList<String>();
List<String> listDTag = new ArrayList<String>();
//get the route and stp tags for api url 
listRTag=dbobj.getRouteTag(route);
listSTag=dbobj.getStopTag(route,stop);
listDTag=dbobj.getDirTag(route,stop);

String timeUrl=null; 
String timeXml =null;

timeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=predictions&a="+Global.agency+"&s="+listSTag.get(0)+"&r="+listRTag.get(0)+"&d="+listDTag.get(0);

timeXml=sobj.retrieve(timeUrl);

time.clear();
time= xobj.parseTimeResponse(timeXml);

//clear all arraylist beffore return 
listRTag.clear();
listSTag.clear();

return time;
}

void predictionError(){  
	
	//display no prediction
        Intent i=new Intent(this,PredictionFailure.class);
        startActivity(i);
}// end of predictionError
//}//end of class GetPrediction 
/* 
* UNUSED CODE - 
* When the routes change -> change the database and sync with next bus database...
*/


@Override
protected void onRestart() {
    super.onRestart();
    
    // when the activity is re-started : When activity comes to foreground perform this
    ButtonFavorite.setEnabled(true);
    lv_arr.clear();
    setListAdapterMy();
    adapter.notifyDataSetChanged();
}




void deleteDb(){
 
this.dbobj.deleteAll(); // Delete data from the table RUData
this.dbobj.deleteAll_fav(); // Delete data from the Fav table

}

void startUp(){
                
        //database object created for class dbobj is used  
    
    String routeXml=null;
    String stopsUrl=null;
    
    //objects needed : Server interface and xml parser  
    
    ServerInterface sobj = new ServerInterface();
    XmlParser xobj= new XmlParser();
    
    HashMap<String,ArrayList<String>> dirTag=new HashMap<String,ArrayList<String>>();
    HashMap<String,ArrayList<String>> dirTitle=new HashMap<String,ArrayList<String>>();
    
   // All the URL's for invoking API on nextBus.com 
 
  String routeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=routeList&a="+Global.agency;
  routeXml = sobj.retrieve(routeUrl);
  
  int i=0;
  
    if(routeXml != null){
                                
                                ArrayList<String> routeTag = xobj.parseRouteResponseTag(routeXml);
                                ArrayList<String> routeTitle = xobj.parseRouteResponseTitle(routeXml);
                                
                                //use iterator to iterate through arrayList
                                Object rTagPass[]=routeTag.toArray();
                                Object rTitlePass[]=routeTitle.toArray();
                                
                                Iterator<String> itrTag= routeTag.iterator();
                                Iterator<String> itrTitle= routeTitle.iterator();
                                
                                
                                // if we use the iterator logic to traverse
                                //while((itrTag.hasNext())&&(itrTitle.hasNext())){
          
           while(itrTag.hasNext()){
                                        
                                // Put rTag into a variable as itrTag shall unnecessarily increment otherwise   
                                String rTag = itrTag.next();
                                //String rTitle = itrTitle.next();
                                //create a new url for each route 
                                
                                stopsUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=routeConfig&a="+Global.agency+"&r="+rTag;
                                String stops= sobj.retrieve(stopsUrl);
                                HashMap<String,Direction> hm=new HashMap<String,Direction>();
                            	hm=xobj.parseDirResponseTag(stops);
                                Set<String> keys1 =hm.keySet();
                                
                                Iterator<String> keysIter1 = keys1.iterator();
                                
                                while (keysIter1.hasNext())
                                {
                                	String k1 = keysIter1.next();
                                	Direction dirOb=new Direction();
                                	dirOb=hm.get(k1);
                                	String k2=dirOb.dirTitle;
                                	stopTag=dirOb.stopTag;
                                    stopTitle=dirOb.stopTitle;
                              
                                    // Put these in database  
                                    //Update RUData table with column
								  System.out.println("TAG::: "+":::  "+i+"::: "+k1+" TITLE::::: "+k2+":::::::: "+ stopTag+"::::::"+stopTitle);
                                  dbobj.insertAll(stopTitle,rTagPass[i],rTitlePass[i],stopTag,k1,k2);
                                    
                               }
                                    
                       i++;        
                              
                        
 } // end of while 
    
}// end of if xml not null
                        
                        
else {
                                
        Intent myIntent = new Intent(GetPrediction.this, PredictionFailure.class);
        GetPrediction.this.startActivity(myIntent);
                        
                        
} // end of else corresponding to xml null      
                        
} // end of startup





}//end of Class GetPrediction      

     

 
        