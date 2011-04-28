package org.runextbus.com;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity; 
import android.content.DialogInterface;
import android.content.Intent;

import org.runextbus.com.GetPrediction;

public class listingFav extends ListActivity implements OnClickListener 
{
	
	private List<String> lv_arr=new ArrayList<String>();
	  public List<String> someList = new ArrayList<String>();
	  public List<String> routeList = new ArrayList<String>();
	  public List<String> stopList = new ArrayList<String>();
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
        listCreation();  
    	
        ManageButton=(Button)findViewById(R.id.ManageButton);
    	ManageButton.setOnClickListener((OnClickListener) this);

    }
    	
    	public void listCreation(){
    	routeList=dbobj.getFavRoute();	    
	    stopList=dbobj.getFavStop();
	    
	    if(routeList.size()==0){
	    	
	    	lv_arr.add("No current Favorites");
	    //	_options = lv_arr.toArray(new CharSequence[lv_arr.size()]);
		//	_selections= new boolean[ _options.length];
	    
			adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lv_arr);
			 setListAdapter(adapter);	    
	    }
	    
	    
	    else{
	    System.out.println("DB returned\n");
		for (int i=0;i<routeList.size();i++){
		lv_arr.add(routeList.get(i)+" "+ stopList.get(i));
		}
      
		_options = lv_arr.toArray(new CharSequence[lv_arr.size()]);
		_selections= new boolean[ _options.length];
		 adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lv_arr);
		 setListAdapter(adapter);

	    }
    }    // end of listCreation
    
 
    public void onListItemClick(ListView parent, View v,int position, long id) 
    {   
        //Toast.makeText(this, "You have selected " + lv_arr.get(position),Toast.LENGTH_SHORT).show();
        int i=position+1;
        System.out.println("THE NUMBER SELECTED IS "+i);    
        favPrediction(i-1);
        
    }
    
    
    void favPrediction(int option){
    	
    	System.out.println("Fav prediction is called .....\n");
    	 
    	List<String> listRouteTag = new ArrayList<String>();
        
    	   listRouteTag=dbobj.getRouteTag(routeList.get(option));
    	   Global.routeTag = listRouteTag.get(0);
    	   
    	    
    	    List<String> listStopTag = new ArrayList<String>();
    	    listStopTag=dbobj.getStopTag(routeList.get(option),stopList.get(option));
    	    Global.stopTag = listStopTag.get(0);

    	 Global.favRouteTag=Global.routeTag;
    	 Global.favRoute=routeList.get(option);
    	 Global.favStopTag=Global.stopTag;
    	 Global.favStop=stopList.get(option);
    	 
    	 Intent i=new Intent(this,FavPrediction.class);
    	 startActivity(i);
    	 	 
    }

    public void onClick(View p) {
    	// TODO Auto-generated method stub
    	     	
        
        switch(p.getId())
    	
    	{
    	
    	case R.id.ManageButton:	
    			
    		System.out.println(_options);
    		System.out.println("Value of manage is : "+manage);
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


    //if submit id pressed then handle it
    public class DialogButtonClickHandler implements DialogInterface.OnClickListener
    {
    	public void onClick( DialogInterface dialog, int clicked )
    	{
    		switch( clicked )
    		{
    			case DialogInterface.BUTTON_POSITIVE:
    				deleteSelected();
//    				adapter.notifyDataSetChanged();
    				break;
    		}
    	}
    }// end of dialoghandler

    protected void deleteSelected(){
    	for( int i = 0; i < _options.length; i++ ){
    		if(_selections[i]){
    		Log.i( "SELECTED LIST", _options[ i ] + " selected ");
    		dbobj.deleteFav(routeList.get(i),stopList.get(i));
	
    		
    		}
    	}
    	
    	adapter.notifyDataSetChanged();  
    	onStart();
    }// end of delete selected 

    void refresh(){
    	//routeList=dbobj.getFavRoute();	    
	    //stopList=dbobj.getFavStop();
    	//kill activity or keep????
	    lv_arr.clear();
	    Intent i=new Intent(this,listingFav.class);
   	 	startActivity(i);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
       
      routeList.clear();
      stopList.clear( );
      lv_arr.clear();
       manage=manage+1;
        listCreation();
        
    }

}//end of refresh

// end of class listingFav