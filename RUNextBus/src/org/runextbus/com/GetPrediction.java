package org.runextbus.com;


import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;


public class GetPrediction extends ListActivity implements OnClickListener  {
        
    //public static SQLiteDatabase db;
    
    Button ButtonFavorite;
    CheckBox ButtonCheck;
    
    TextView txt;
    ServerInterface sobj =  new ServerInterface();
    XmlParser xobj= new XmlParser();
    private DataHelper dbobj;
    private ShowFavtime sftob=new ShowFavtime();
    
    
    //variables used by spinners 
    public static Spinner spinner1 = null;
    public ArrayAdapter<String> spinnerArrayAdapter1;
    public static Spinner spinner2 = null;
    public Activity myAct;
    public List<String> stopList = new ArrayList<String>();
    public List<String> routeList = new ArrayList<String>();
        
        // variables used by Listview for favorite prediction 
    private List<String> lv_arr=new ArrayList<String>();
    public List<String> routeFavList = new ArrayList<String>();
    public List<String> stopFavList = new ArrayList<String>();
    public List<String> someList = new ArrayList<String>();
    public static ArrayAdapter<String> adapter=null;
    public CharSequence[] _options;
    public boolean[] _selections ;

    //progress bar :
    private static int progress = 0;
    private ProgressBar progressBar;
    private int progressStatus = 0;    
    private Handler handler = new Handler();
    
        @Override
        public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newmain);
        this.dbobj=new DataHelper(this);
        //activity
        myAct=this;
  
    /* For Database to be synced with nextbus.com enable this
    * deleteDb();
    startUp(); */
    
    //for the first time call this to set the favorite predictions 
    
        
        //spinners for route and stop selection 
        spinner1 = (Spinner)this.findViewById(R.id.SpinnerRoute);
        spinner2 = (Spinner)this.findViewById(R.id.SpinnerStop);
       
       
        
            /* First spinner value populated */
       
       routeList= dbobj.populateRouteSpinner(); 
     
     
       spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeList);
       spinnerArrayAdapter1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );  
       spinner1.setAdapter(this.spinnerArrayAdapter1);
       spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener());
                     
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
        
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lv_arr); 
        setListAdapter(adapter);    
        
       
 }
         
  
 /**
  * on selection of spinner 1 update spinner 2 
  * @author Sangeetha
  *
  */
 
public class MyOnItemSelectedListener implements OnItemSelectedListener{
        
        public ArrayAdapter<String> spinnerArrayAdapter2;
        
        public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
   
//      Toast.makeText(parent.getContext(), "Route Selected : " +parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
                
                String RouteTitle = parent.getItemAtPosition(pos).toString();
                
                stopList=dbobj.populateStopSpinner(RouteTitle);
                stopList.add(0,"PICK STOP");
                // use the stopListvalue to populate spinner                    
                        
                MySpinnerAdapter spinnerArray = new MySpinnerAdapter(myAct,R.layout.custom_spinner_row,R.id.text, stopList);
                   spinner2.setAdapter(spinnerArray);
          
                   spinner2.setOnItemSelectedListener(new OnItemSelectedListener()

                   { 
                           public void onItemSelected(AdapterView<?> arg0,View view, int pos, long id) {
                                   
                    String StopTitle = arg0.getItemAtPosition(pos).toString();
                    
                    if(StopTitle!="PICK STOP"){
                    Global.route = (String)spinner1.getSelectedItem();
                       //Global.stop=(String)spinner2.getSelectedItem();
                        Global.stop=StopTitle;
                        ButtonFavorite.setEnabled(false);
                        startPrediction(Global.route,Global.stop);
                    
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
 * first spinner thing 
 *          (non-Javadoc)
 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
 */
            public void onNothingSelected(AdapterView parent) {
              // Do nothing.
            }
        } // end of class 





 

public void onListItemClick(ListView parent, View v,int position, long id) 
{   
           int i=position;
         
   routeFavList=dbobj.getFavRoute();       
   stopFavList=dbobj.getFavStop();
   
   ArrayList<String> fTime =new ArrayList<String>();
   fTime.clear();
   
   fTime = getTime(routeFavList.get(i),stopFavList.get(i)); 
        
        if(fTime.size()==0){
                lv_arr.remove(i);
                lv_arr.add(i,routeFavList.get(i)+" - "+ stopFavList.get(i)+" :\n No Prediction");
                adapter.notifyDataSetChanged();
 
        }
        
        else{
               
        if(fTime.size()>=2){
                lv_arr.remove(i);
        lv_arr.add(i,routeFavList.get(i)+" - "+ stopFavList.get(i)+" :\n"+fTime.get(0)+"min "+fTime.get(1)+"min");
        }
        else{
                lv_arr.remove(i);
                lv_arr.add(i,routeFavList.get(i)+" - "+ stopFavList.get(i)+" :\n"+fTime.get(0)+"min ");
        }
        
        routeFavList.clear();
        stopFavList.clear();
      
        adapter.notifyDataSetChanged();
      
        
        }       
        
 }
        






 /**
 * @Description On click of get prediction button we execute this
 * @param null
 * @return prediction time screen or error screen  
 */
                  

public void onClick(View p) {
        // TODO Auto-generated method stub
                
    switch(p.getId())
        
        {
        
        case R.id.ButtonFavorite:               
                Intent i=new Intent(this,listingFav.class);
                startActivity(i);
                break;
                 
                
} // end of switch case 
}// end of onClick



//public void setListAdapterMy(){

public int setListAdapterMy(){
            
                        routeFavList=dbobj.getFavRoute();       
            stopFavList=dbobj.getFavStop();
            lv_arr.clear();
            
            ArrayList<String> favTime = new ArrayList<String>();
        
           if(routeFavList.size()==0){
                    lv_arr.clear();
                lv_arr.add("No current Favorites");
 
           }
            
          
          else{
            
                  //one fav route and stop
                if((routeFavList.size()==1)&&(stopFavList.size()==1)){
            
                        favTime.clear();        
                                        try{
                                        
                                        favTime = getTime(routeFavList.get(0),stopFavList.get(0)); 

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
           }//end of if block 
                        

         else if((routeFavList.size()>=2)&&(stopFavList.size()>=2)){
                        
                  System.out.println("Option selected is : Different route different stops");
                        
                //continue after sleep
                
                for (int i=0;i<2;i++){
                        
                favTime.clear();  
                try{
                	
                favTime = getTime(routeFavList.get(i),stopFavList.get(i));
                
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
                
                }               catch (Exception e) {
                       
                		lv_arr.clear();
                		lv_arr.add("No Internet service");
                         
                                  return 1;
                                }
      
                
            } // end of for
                
            }// end of else fav >=2
            
            }// end of main else 
          
                 
           return 1;
}
 

/*
 * startPrediction
 */

void startPrediction(String r, String s){        
        
Global.time=getTime(r,s);                
    // get the route and stop tags to be passed to get prediction 
//System.out.println("Global time is : "+Global.time);

if(Global.time.size()==0)
{
                System.out.println("predictionError is called .....\n");
                ButtonFavorite.setEnabled(true);
                Intent i=new Intent(this,PredictionFailure.class);
                startActivity(i);
}

else{
        //display time 
         ButtonFavorite.setEnabled(true);
Intent i=new Intent(this,ShowTime.class);
startActivity(i);
//finish();
}
} // end of startPrediction

/*
 * getTime : To get current prediction
 */

public ArrayList<String> getTime(String route, String stop){

ArrayList<String>time= new ArrayList<String>();
List<String> listRTag = new ArrayList<String>();
List<String> listSTag = new ArrayList<String>();


listRTag=dbobj.getRouteTag(route);
//Global.routeTag = listRouteTag.get(0);
listSTag=dbobj.getStopTag(route,stop);

String rr=null; 
rr=listRTag.get(0);
String ss=null;
ss=listSTag.get(0);
//Global.stopTag = listStopTag.get(0);
 

String timeUrl=null; 
String timeXml =null;
//String timeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=predictions&a="+Global.agency+"&r="+Global.routeTag+"&s="+Global.stopTag;

timeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=predictions&a="+Global.agency+"&s="+ss+"&r="+rr; 
timeXml=sobj.retrieve(timeUrl); 
time.clear();
time= xobj.parseTimeResponse(timeXml);

listRTag.clear();
listSTag.clear();

return time;
}


/**
 * predictionError : Display no prediction screen
 * 
 */ 

void predictionError(){
        
        Intent i=new Intent(this,PredictionFailure.class);
        startActivity(i);
//        finish();
}// end of predictionError


/*
 * onStart
 * (non-Javadoc)
 * @see android.app.Activity#onStart()
 */

@Override
protected void onRestart() {
    super.onRestart();
    // * when the activity is started perform the prediction of two favorites 
    lv_arr.clear();
    setListAdapterMy();
    adapter.notifyDataSetChanged();
}


}//end of class
//******************************** UNUSED *********************************************//


/*void deleteDb(){


this.dbobj = new DataHelper(this); // database object 
this.dbobj.deleteAll(); // Delete data from the table RUData
this.dbobj.deleteAll_fav(); // Delete data from the table RUData

}

void startUp(){
                
        this.dbobj = new DataHelper(this); // database object 
    
    String routeXml=null;
    String stopsUrl=null;
    //objects needed 
    ServerInterface sobj = new ServerInterface();
    XmlParser xobj= new XmlParser();
    
       
    
   // All the URL's for invoking API on nextBus.com 
 
  String routeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=routeList&a="+Global.agency;
  routeXml = sobj.retrieve(routeUrl);
        // check for favorite flag 
  
                        if(routeXml != null){
                                //System.out.println("GetPrediction Log: Retrieve http response SUCCESS\n");
                                
                                ArrayList<String> routeTag = xobj.parseRouteResponseTag(routeXml);
                //              System.out.println(routeTag+"\n");
                                ArrayList<String> routeTitle = xobj.parseRouteResponseTitle(routeXml);
                  //            System.out.println(routeTitle+"\n");
                       
                                
                                
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
                        
                                
                                 // Put these in database  
                                 //Update RUData table with column 
                                 
                                
        dbobj.insertAll(stopTitle,rTagPass[i],rTitlePass[i],stopTag);
        i++;
                        
 } // end of while 
    
}// end of if xml not null
                        
                        
else {
                                
        Intent myIntent = new Intent(GetPrediction.this, PredictionFailure.class);
        GetPrediction.this.startActivity(myIntent);
                        
                        
} // end of else corresponding to xml null      
                        
} // end of startup

*/



//}//end of Class GetPrediction      

     

 
        