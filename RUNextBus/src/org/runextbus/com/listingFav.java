package org.runextbus.com;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity; 
import android.content.DialogInterface;
import android.content.Intent;
import org.runextbus.com.GetPrediction;


public class listingFav extends ListActivity implements OnClickListener 
{
	OnTouchListener tl;	
	private List<String> lv_arr=new ArrayList<String>();
	  public List<String> someList = new ArrayList<String>();
	  public List<String> routeList = new ArrayList<String>();
	  public List<String> stopList = new ArrayList<String>();
	  public List<String> dirList = new ArrayList<String>();
	  
	  Button ManageButton;
	public  ArrayAdapter<String> adapter;
	//private DataHelper dbobj;
	public CharSequence[] _options; 
	public boolean[] _selections ;
	public GetPrediction gpObj;	
	public int manage=0;
	
	TextView labeld;
	
	
	
    @Override  
    public void onCreate(Bundle savedInstanceState) 
    {
    	
    	//this.dbobj=new DataHelper(this);
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.screen02);
        //setContentView(R.layout.rating);
    	
        ManageButton=(Button)findViewById(R.id.ManageButton);
    	ManageButton.setOnClickListener((OnClickListener) this);
    	ManageButton.setEnabled(false);

    	listCreation();

    }
    	
   public void listCreation(){
   
	   //get the list of all routes and stops that are marked favorites 
	   routeList=Global.dbobj.getFavRoute();	    
	    stopList=Global.dbobj.getFavStop();
	    dirList=Global.dbobj.getFavDir();
	    
	    if(routeList.size()==0){
	    	//show no favorites in case list is empty 
	    	lv_arr.add("No current Favorites");
	    	
	    	ArrayList<RowModel> list=new ArrayList<RowModel>();
	    	ArrayList<Float> rate = new ArrayList<Float>();
	    	
	    	//lv_arr -> label and rate -> rating 
	    	
	    	for (String s : lv_arr) {
	    	      list.add(new RowModel(s));
	    	    }
	    	
	    	setListAdapter(new RatingAdapter(list));
	    	labeld = (TextView)findViewById(R.id.label);
	    	//adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lv_arr);
	    	//adapter=new ArrayAdapter<String>(this,R.layout.list_item,lv_arr);
	    	
			//setListAdapter(adapter);	   
			
	    }
	    
	    
	    else{
	    	
	    //Manage button is needed if there are entries in list view 
	    	
	    ManageButton.setEnabled(true);
	    //System.out.println("DB returned\n");
	    
		for (int i=0;i<routeList.size();i++){
		lv_arr.add(routeList.get(i)+" "+ stopList.get(i));
		}
		
		_options = lv_arr.toArray(new CharSequence[lv_arr.size()]);
		_selections= new boolean[ _options.length];
		 //adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lv_arr);
	
		ArrayList<RowModel> list=new ArrayList<RowModel>();
    	for (String s : lv_arr) {
    	      list.add(new RowModel(s));
    	    }
    	
    	setListAdapter(new RatingAdapter(list));
		labeld = (TextView)findViewById(R.id.label);
		//adapter=new ArrayAdapter<String>(this, R.layout.list_item,lv_arr);
		 //setListAdapter(adapter);
		 
	    }
    }    // end of listCreation
    
   
   
 /*  public void onListItemClick(ListView parent, View v,
           int position, long id) {
	   labeld.setText(lv_arr.get(position));
	   
	   System.out.println("CLICKED OPTION ::::::::::::::::::"+position);
   }*/
   
   
   private RowModel getModel(int position) {
	    return(((RatingAdapter)getListAdapter()).getItem(position));
	  }
   
/* 
 * Fill customized ListView 
 */
   class RatingAdapter extends ArrayAdapter<RowModel> {
	    RatingAdapter(ArrayList<RowModel> list) {
	      super(listingFav.this, R.layout.rating_list_item, R.id.label, list);
	    }

	    public View getView(int position, View convertView,ViewGroup parent) {
		
	      View row=super.getView(position, convertView, parent);
	      ViewHolder holder=(ViewHolder)row.getTag();
	      TextView label=(TextView)row.findViewById(R.id.label);
	      
	      if (holder==null) {
	    	holder=new ViewHolder(row);
	        label.setText(lv_arr.get(position));
	        row.setTag(holder);
	        
	        RatingBar.OnRatingBarChangeListener l= new RatingBar.OnRatingBarChangeListener() {
	        
	        	public void onRatingChanged(RatingBar ratingBar,float rating,boolean fromTouch)  {
	            Integer myPosition=(Integer)ratingBar.getTag();
	            RowModel model=getModel(myPosition);
	            model.rating=rating;
	            
	             LinearLayout parent=(LinearLayout)ratingBar.getParent();
	            TextView label=(TextView)parent.findViewById(R.id.label);
	            
	     	   int i = lv_arr.indexOf(label.getText());
	     	   System.out.println("Number "+ i +"'s rating changed to"+rating);
	     	   
	     	   Global.dbobj.addRate(routeList.get(i),stopList.get(i),dirList.get(i),rating);
	     	   
	           // label.setText(model.toString());
	          }
	        };
	        
	        
	    OnTouchListener tl= new OnTouchListener() {
	        	@Override
	    		public boolean onTouch(View v, MotionEvent event) {
	        		TextView tv = (TextView)v;
	        		ListView lv = (ListView)tv.getParent().getParent();
	        		
	        	System.out.println("Clicked :::::");
	        		
	        		Integer myPosition=(Integer)lv.getTag();
	        		   System.out.println(":::::::::::"+lv_arr.indexOf(tv.getText()));
	        		   int i = lv_arr.indexOf(tv.getText());
	        		   favPrediction(i);
	        		   
	        		
	    			// TODO Auto-generated method stub
	    			return false;
	    		}
	        };
	       
	        //Check if Text View or rating bar is clicked 
	        holder.label.setOnTouchListener(tl);
	        holder.rate.setOnRatingBarChangeListener(l);
	        
	      }
	      
	      
	      RowModel model=getModel(position);
	   
	      holder.rate.setTag(new Integer(position));
	      holder.rate.setRating(model.rating);
	     
	      return(row);
	    }
	
    
  }
  
 
	/*
	 * favPrediction(int option) : To provide favorite prediction for list view option clicked
	 * 
	 */
	void favPrediction(int option){
    	 
    	
    	if(routeList.size()==0){
    			//do nothing on NO Elements in list
   
 	   }
 	   else{
 		   
    	//get the route and stop combination from favorites list if favorites are present
 		   
 		   
    	List<String> listRouteTag = new ArrayList<String>();
    	List<String> listStopTag = new ArrayList<String>();
    	List<String> listDirTag = new ArrayList<String>();
    	 
    	//put sorted list here 
    	listRouteTag=Global.dbobj.getRouteTag(routeList.get(option));
    	listDirTag=Global.dbobj.getDirTag(routeList.get(option),stopList.get(option));
    	listStopTag=Global.dbobj.getStopTag(routeList.get(option),stopList.get(option));
    	
    	// set the route , stop and direction tags 
    	Global.routeTag = listRouteTag.get(0);
    	Global.stopTag = listStopTag.get(0);
    	Global.dirTag = listDirTag.get(0);
    	
    	Global.favRouteTag=Global.routeTag;
    	Global.favRoute=routeList.get(option);
    	Global.favStopTag=Global.stopTag;
    	Global.favStop=stopList.get(option);
    	Global.favDirTag=Global.dirTag;
    	Global.favDir= dirList.get(option);
    	 
    	/* Show process dialog here :  display the prediction screen */
    	
    	Intent i=new Intent(this,FavPrediction.class);
    	 startActivity(i);
    	 
    	   }
    }
    
    public void onClick(View p) {
    
        switch(p.getId())
    	{
    	
    	case R.id.ManageButton:			
    		System.out.println(_options);
    		System.out.println("Value of manage is : "+manage);
    		
    		//to refresh the dialog contents with updates list view - 
    		//can also migrate it to onPause()?
    		onStart();
    		
    		//show updated manage dialog
    		
    		showDialog(manage);	
    	 	break;
    	
    	}
        
    } 

    
        
/*
 * onCreateDialog : To manage deletion of favorites ( On click of Manage Button)
 * @see android.app.Activity#onCreateDialog(int)
 */
        
    @Override
    protected Dialog onCreateDialog( int id ) 
    {
    	return 
    	new AlertDialog.Builder( this )
        	.setTitle( "Favorites" )
        	
        	//read the selected into options array and selections array
        	.setMultiChoiceItems( _options, _selections, new DialogSelectionClickHandler() )
        	.setPositiveButton( "DELETE", new DialogButtonClickHandler() )
        	.create();
    } // end of dialog on create


    public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener
    {
    	public void onClick( DialogInterface dialog, int clicked, boolean selected )
    	{
    		Log.i( "FAVORITES LIST SELECTION", _options[ clicked ] + " selected: " + selected );
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
    				deleteSelected();
    				break;
    		}
    	}
    }// end of dialog handler

    
  /*
   * deleteSelected() : delete the selected entry 
   * used by dialog above 
   */  
    
  protected void deleteSelected(){
    	for( int i = 0; i < _options.length; i++ ){
    		if(_selections[i]){
    		Log.i( "SELECTED LIST", _options[ i ] + " selected ");
    		Global.dbobj.deleteFav(routeList.get(i),stopList.get(i));
    		}
    	}
    	
    	//adapter.notifyDataSetChanged() doesn't work as the list needs to be updated first   
    	// refresh the list in the background
    	onStart();
    
 }// end of delete selected 
 
    
    /*
     * onStart() : call this every time you want to refresh dialog and the list view
     * (non-Javadoc)
     * @see android.app.Activity#onStart()
     * By default executed on start of activity 
     */
    
    @Override
    protected void onStart() {
        super.onStart();
        
       // clear all arraylists 
      routeList.clear();
      stopList.clear();
      dirList.clear();
      lv_arr.clear();
      manage=manage+1;//the version of dialog view
       
       // When no entries in list : no need for Manage button
       ManageButton.setEnabled(false);
        listCreation();
        
    }


    
    
    
    
    /* 
     * Custom Row Layout Holder : Rating bar rating and Textview label 
     */
    	
        class ViewHolder {
    		  RatingBar rate=null;
    		  TextView label = null;
    		  
    		ViewHolder(View base) {
    		    this.rate=(RatingBar)base.findViewById(R.id.rate);
    		    this.label=(TextView)base.findViewById(R.id.label);
    		  }
    	}
        
class RowModel {
    String label;
    float rating=0.0f; // no rating initially
    
    RowModel(String label) {
    	//RowModel(String label, Float rate ) {
      this.label=label;
      //this.rating = rate;
    }
    
    public String toString() {
      if (rating==5.0) {
    	  
        //return(label.toUpperCase());
      }
return(label);
    }
  }
}


