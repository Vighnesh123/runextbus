package org.runextbus.com;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import org.runextbus.com.listingFavRate.RatingAdapter;
import org.runextbus.com.listingFavRate.RowModel;
import org.runextbus.com.listingFavRate.ViewHolder;

public class listingFav extends ListActivity implements OnClickListener 
{
	
	private List<String> lv_arr=new ArrayList<String>();
	  public List<String> someList = new ArrayList<String>();
	  public List<String> routeList = new ArrayList<String>();
	  public List<String> stopList = new ArrayList<String>();
	  public List<String> dirList = new ArrayList<String>();
	  
	  Button ManageButton;
	public  ArrayAdapter<String> adapter;
	private DataHelper dbobj;
	public CharSequence[] _options; 
	public boolean[] _selections ;
	public GetPrediction gpObj;	
	public int manage=0;
	
	
	
    @Override  
    public void onCreate(Bundle savedInstanceState) 
    {
    	
    	this.dbobj=new DataHelper(this);
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
	   routeList=dbobj.getFavRoute();	    
	    stopList=dbobj.getFavStop();
	    dirList=dbobj.getFavDir();
	    
	    if(routeList.size()==0){
	    	//show no favorites in case list is empty 
	    	lv_arr.add("No current Favorites");
	    	
	    	ArrayList<RowModel> list=new ArrayList<RowModel>();
	    	for (String s : lv_arr) {
	    	      list.add(new RowModel(s));
	    	    }
	    	setListAdapter(new RatingAdapter(list));
	    	
	    	//adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lv_arr);
	    	//adapter=new ArrayAdapter<String>(this,R.layout.list_item,lv_arr);
	    	
			//setListAdapter(adapter);	   
			
	    }
	    
	    
	    else{
	    	
	    //Manage button is needed if there are entries in list view 
	    	
	    ManageButton.setEnabled(true);
	    System.out.println("DB returned\n");
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
		
		//adapter=new ArrayAdapter<String>(this, R.layout.list_item,lv_arr);
		 //setListAdapter(adapter);
		 
	    }
    }    // end of listCreation
    
   
   
   private RowModel getModel(int position) {
	    return(((RatingAdapter)getListAdapter()).getItem(position));
	  }
   
	class RatingAdapter extends ArrayAdapter<RowModel> {
	    RatingAdapter(ArrayList<RowModel> list) {
	      super(listingFav.this, R.layout.rating_list_item, R.id.label, list);
	    }
	    
	public View getView(int position, View convertView,ViewGroup parent) {
	      View row=super.getView(position, convertView, parent);
	      ViewHolder holder=(ViewHolder)row.getTag();
	                         
	      if (holder==null) {    
	        holder=new ViewHolder(row);
	        row.setTag(holder);
	        
	        RatingBar.OnRatingBarChangeListener l=
	                    new RatingBar.OnRatingBarChangeListener() {
	          public void onRatingChanged(RatingBar ratingBar,
	                                       float rating,
	                                       boolean fromTouch)  {
	            Integer myPosition=(Integer)ratingBar.getTag();
	            RowModel model=getModel(myPosition);
	            
	            
	            model.rating=rating;
	     
	            
	            System.out.println("ROW IS ::::::::::::::::: " + myPosition);
	            System.out.println("RATING IS ::::::::::::: "+ rating);
	          
	            LinearLayout parent=(LinearLayout)ratingBar.getParent();
	            TextView label=(TextView)parent.findViewById(R.id.label);
	        
	            label.setText(model.toString());
	          }
	        };
	        holder.rate.setOnRatingBarChangeListener(l);
	      }
	      RowModel model=getModel(position);
	      
	      holder.rate.setTag(new Integer(position));
	      holder.rate.setRating(model.rating);
	     
	return(row);
	    }
	  } 
   
	
//public void onListItemClick(ListView parent, View v,int position, long id)
	public void onListItemClick(ViewGroup parent, View convertView,int position, long id) 
    {   
        //Toast.makeText(this, "You have selected " + lv_arr.get(position),Toast.LENGTH_SHORT).show();
        int i=position+1;
        System.out.println("THE NUMBER SELECTED IS "+i);
        //favPrediction(i-1);
        
    }
    
    
    void favPrediction(int option){
    	 
    	
    	if(routeList.size()==0){
 		   
 		   //do nothing if no favorites  
 	   }
 	   else{
    	//get the route and stop combination from favorites list if favorites are present   
    	List<String> listRouteTag = new ArrayList<String>();
    	listRouteTag=dbobj.getRouteTag(routeList.get(option));
    	   Global.routeTag = listRouteTag.get(0);	   
    	    List<String> listStopTag = new ArrayList<String>();
    	    listStopTag=dbobj.getStopTag(routeList.get(option),stopList.get(option));
    	    Global.stopTag = listStopTag.get(0);
    	    
    	    List<String> listDirTag = new ArrayList<String>();
    	    listDirTag=dbobj.getDirTag(routeList.get(option),stopList.get(option));
    	    Global.dirTag = listDirTag.get(0);

    	 Global.favRouteTag=Global.routeTag;
    	 Global.favRoute=routeList.get(option);
    	 Global.favStopTag=Global.stopTag;
    	 Global.favStop=stopList.get(option);
    	 Global.favDirTag=Global.dirTag;
    	 Global.favDir= dirList.get(option);
    	 
    	 
    	 Intent i=new Intent(this,FavPrediction.class);
    	 startActivity(i);
    	   }
    }
    
    
    class ViewHolder {
		  RatingBar rate=null;
		ViewHolder(View base) {
		    this.rate=(RatingBar)base.findViewById(R.id.rate);
		  }
	}

    public void onClick(View p) {
    	// there is only button in this view
        switch(p.getId())
    	{
    	
    	case R.id.ManageButton:			
    		System.out.println(_options);
    		System.out.println("Value of manage is : "+manage);
    		
    		//to refresh the dialog contents with updates list view - can also migrate it to onPause()?
    		onStart();
    		//show updated manage dialog 
    		showDialog(manage);	
    	 	break;
    	}
        
    } // end of onclick for manage favoirtes button

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

    protected void deleteSelected(){
    	for( int i = 0; i < _options.length; i++ ){
    		if(_selections[i]){
    		Log.i( "SELECTED LIST", _options[ i ] + " selected ");
    		dbobj.deleteFav(routeList.get(i),stopList.get(i));
    		}
    	}
    	
    	//adapter.notifyDataSetChanged(); doesn't work as the list needs to be updated first   
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
      routeList.clear();
      stopList.clear();
      dirList.clear();
      
      lv_arr.clear();
      
       manage=manage+1;//the version of dialog view 
       ManageButton.setEnabled(false);
        listCreation();
        
    }



class RowModel {
    String label;
    float rating=2.0f;
RowModel(String label) {
      this.label=label;
    }
public String toString() {
      if (rating==5.0) {
        return(label.toUpperCase());
      }
return(label);
    }
  }
}

//********************************************UN USED******************************************//
/* void refresh(){
 	//routeList=dbobj.getFavRoute();	    
	    //stopList=dbobj.getFavStop();
 	//kill activity or keep????
	    lv_arr.clear();
	    Intent i=new Intent(this,listingFav.class);
	 	startActivity(i);
 }*/
 


