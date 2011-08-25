package org.runextbus.com;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Class name : DataHelper 
 * @author Sriram
 */

public class DataHelper{
        
        // Variables 
        
           private static final String DATABASE_NAME = "RUBusPrediction.db";
           private static final int DATABASE_VERSION = 1;
           private static final String TABLE_NAME = "RUData";
           private static final String TABLE_NAME_GetLocation = "GetLocation";
           private static final String TABLE_NAME_Fav = "Favorite";

           
           //public OpenHelper ob;
           private Context context;
         
           public SQLiteDatabase db;
           //private SQLiteStatement insertStmt;
           
/**
 *      DataHelper 
 * @param context
 */
public DataHelper(Context context) {
              this.context = context;
              DataBaseHelper obj=new DataBaseHelper(this.context);
              
             OpenHelper openHelper = new OpenHelper(this.context);
              this.db = openHelper.getWritableDatabase();
              
} // end of DataHelper 
           

// To be checked 

public void insertAllSan(ArrayList<String> stopTag,ArrayList<String> stopTitle,String routeTag,String routeTitle){
                
        //insert all of them together StopTag Stoptitle RouteTag RouteTitle 
        
                Iterator<String> Tagitr= stopTag.iterator();
                Iterator<String> Titleitr= stopTitle.iterator();
                
                while(Tagitr.hasNext()&&Titleitr.hasNext()){
                                db.execSQL("INSERT INTO "+TABLE_NAME+"(StopTag,StopTitle,RouteTag,RouteTitle) Values ('" + Tagitr.next() + "','" + Titleitr.next() + "','" + routeTag + "','" + routeTitle + "');");
                
                }
                
        }
        

/**
 * insertRouteTag
 * @param routeTag
 */
public void insertRouteTag(ArrayList<String> routeTag){
        
        //      Iterator<String> itr= routeTag.iterator();  : Iterator unused
                System.out.println("Entering into Data helper");
                Object route[] = routeTag.toArray();
                for(int i=0;i<route.length;i++)
                        
                db.execSQL("INSERT INTO "+TABLE_NAME+"(RouteTag) Values ('" + route[i] + "');");                        
}



/**
 * deleteAll for deleting data from the table RUData
 */
public void deleteAll() {
      this.db.delete(TABLE_NAME, null, null);
}// end of deleteAll

/**
 * deleteAll for deleting data from the table GetLocation
 */
public void deleteAll_GetLocation() {
      this.db.delete(TABLE_NAME_GetLocation, null, null);
}// end of deleteAll_GetLocation



   

/**
 * selectAll
 * @param null
 * @return list
 */
public List<String> populateRouteSpinner() {
  List<String> list = new ArrayList<String>();
                // populate the first spinner : SpinnerRoute
      Cursor cursor = this.db.rawQuery("SELECT DISTINCT RouteTitle FROM "+TABLE_NAME, null);
      if (cursor.moveToFirst()) {
      do {
            list.add(cursor.getString(0));
         } while (cursor.moveToNext());
      }
      if (cursor != null && !cursor.isClosed()) {
         cursor.close();
      }
      //System.out.println(" I HAVE THIS DATA :" + list );
      return list;
} // end of selectAll
   

/**
 * selectAll1
 * @param null
 * @return list
 * @author Santosh Sriram 
 */
public List<String> populateStopSpinner(String RouteTitle) {
              List<String> list = new ArrayList<String>();
              Cursor cursor = this.db.rawQuery("SELECT DISTINCT StopTitle FROM "+TABLE_NAME+" WHERE RouteTitle = ('" + RouteTitle + "');", null);
              if (cursor.moveToFirst()) {
              do {
                    list.add(cursor.getString(0));
                 } while (cursor.moveToNext());
              }
              if (cursor != null && !cursor.isClosed()) {
                 cursor.close();
              }
              return list;
              
} // end of populateStopSpinner
   

/**
 * Class OpenHelper 
 * @author Santosh Sriram
 */ 

public static class OpenHelper extends SQLiteOpenHelper {
      
OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      
}//end of OpenHelper
      
@Override
public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(RouteTag varchar,RouteTitle varchar,StopTag varchar,StopTitle varchar)");         
         db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Fav + "(favNum INTEGER PRIMARY KEY,RouteTag varchar,RouteTitle varchar,StopTag varchar,StopTitle varchar)");
}//end of SQLiteDatabase

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.w("RUBusPrediction", "Upgrading database, this will drop tables and recreate.");
       db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
         onCreate(db);
         
      }//end of onUpgrade
   }// end of OpenHelper

/**
 * getRouteTag : Used to get the tag corresponding to Route Title selected
 * @param RouteTitle
 * @return Tag list
 * @author Santosh Sriram 
 */

public List<String> getRouteTag(String RouteTitle) {
        // TODO Auto-generated method stub
    List<String> list = new ArrayList<String>();
    Cursor cursor;
        cursor=db.rawQuery("SELECT DISTINCT RouteTag FROM "+ TABLE_NAME +" WHERE RouteTitle = ('" + RouteTitle + "')", null);
        
        if (cursor.moveToFirst()) {
              do {
                    list.add(cursor.getString(0));
                 } while (cursor.moveToNext());
              }
              if (cursor != null && !cursor.isClosed()) {
                 cursor.close();
              }
              return list;
                
} // end of getRouteTag



/**
 * getStopTag : Used to get the tag corresponding to Stop Title selected
 * @param StopTitle
 * @return Tag list
 * @author Santosh Sriram 
 */

public List<String> getStopTag(String RouteTitle,String StopTitle) {
        // TODO Auto-generated method stub
    List<String> list = new ArrayList<String>();
    Cursor cursor;
    
        cursor=db.rawQuery("SELECT DISTINCT StopTag FROM "+ TABLE_NAME +" WHERE RouteTitle = ('" + RouteTitle + "') AND StopTitle = ('" + StopTitle + "')", null);       
        
        if (cursor.moveToFirst()) {
              do {
                    list.add(cursor.getString(0));
                 } while (cursor.moveToNext());
              }
              if (cursor != null && !cursor.isClosed()) {
                 cursor.close();
              }
              return list;
              
}// end of getStopTag



/**
 * insertAll : To insert all data to RUData table 
 * @param stopTitle rtag rtitle stopTag
 * @return null
 * @author Santosh Sriram
 */

public void insertAll(ArrayList<String> stopTitle, Object rtag,Object rtitle,ArrayList<String> stopTag){
        
        Object stitle[] = stopTitle.toArray();
        Object stag[]=stopTag.toArray();
        
                for(int i=0;i<stitle.length&stitle[i]!=null;i++)
                {
                        
                                db.execSQL("INSERT INTO "+TABLE_NAME+"(StopTitle,RouteTag,RouteTitle,StopTag) Values ('" + stitle[i] + "','" + rtag + "','" + rtitle + "','" + stag[i] + "');");
                        
                }
                
                
}// end of insertAll

/**
 *insertStreetAndStops : Used by GetNearestStop Class
 *@param streets and stops as arrays 
 *@return null 
 */

public void insertStreetAndStops(Object[] streets, Object[] stops){
        
        for(int i=0;i<streets.length;i++)
                {
                        
                                db.execSQL("INSERT INTO GetLocation(Street,Stop) Values ('" + streets[i] + "','" + stops[i] + "');");
                        
                }
                
        
}// end of insertStreetAndStops

/**
 * getNearestStops Used by GetNearestStop Class
 * @param null
 * @return List
 * @author Sriram Santosh  
 */

public List<String> getNearestStops(String streetName ) {
    List<String> list = new ArrayList<String>();
    Cursor cursor = this.db.rawQuery("SELECT Stop FROM GetLocation WHERE Street= ('" + streetName + "');", null);
    if (cursor.moveToFirst()) {
    do {
          list.add(cursor.getString(0));
       } while (cursor.moveToNext());
    }
    if (cursor != null && !cursor.isClosed()) {
       cursor.close();
    }
    return list;
 
} // end of getNearestStops



/**
 * getStreetNames
 * @param null
 * @return list
 */


public List<String> getStreetNames() {
    List<String> list = new ArrayList<String>();
    Cursor cursor = this.db.rawQuery("SELECT Street FROM GetLocation ", null);
    if (cursor.moveToFirst()) {
    do {
          list.add(cursor.getString(0));
       } while (cursor.moveToNext());
    }
    if (cursor != null && !cursor.isClosed()) {
       cursor.close();
    }
    return list;
 
} // end of getStreetNames




public long insert(String name) {
        // TODO Auto-generated method stub
        return 0;
}


public List<String> selectAll() {
        // TODO Auto-generated method stu
        
        return null;
}

/**
 * getRouteForCloseStop used in DisplayNearestStop Class
 * @author Sangeetha Siddegowda
 * @param closeStop
 * @return
 */
public List<String> getRouteForCloseStop(String closestStop) {
        // TODO Auto-generated method stub
        System.out.println("Inside the getRouteforc....");
        System.out.println(closestStop);
    List<String> list = new ArrayList<String>();
        Cursor cursor =db.rawQuery("SELECT DISTINCT RouteTitle FROM "+TABLE_NAME +" WHERE StopTitle = ('" + closestStop + "');", null);
    
    if (cursor.moveToFirst()) {
    do {
          list.add(cursor.getString(0));
       } while (cursor.moveToNext());
    }
    if (cursor != null && !cursor.isClosed()) {
       cursor.close();
    }
    return list;

}// end of getRouteForCloseStop



public List<String> getFavorite(int option) {
    // TODO Auto-generated method stub
    
       // System.out.println("Inside the getFavorite function....\n");
        List<String> favList = new ArrayList<String>();
    //option has to be 1 or 2 
    
// read the first favorite 
if (option==1){
        
Cursor cursor =db.rawQuery("SELECT * FROM Favorite WHERE favNum = ('" + option + "');", null);
if (cursor.moveToFirst()) {
        do {
      favList.add(cursor.getString(1));
      favList.add(cursor.getString(2));
      favList.add(cursor.getString(3));
      favList.add(cursor.getString(4));
      
        } while (cursor.moveToNext());

}
if (cursor != null && !cursor.isClosed()) {
   cursor.close();
}
System.out.println("Data : "+favList+"\n");

} // end of if option=1

//second favorite 
else if(option==2){
        
        Cursor cursor =db.rawQuery("SELECT * FROM Favorite WHERE favNum = ('" + option + "');", null);

        if (cursor.moveToFirst()) {
        do {
                  favList.add(cursor.getString(1));
              favList.add(cursor.getString(2));
              favList.add(cursor.getString(3));
              favList.add(cursor.getString(4));
              
           } while (cursor.moveToNext());
        }
        
        if (cursor != null && !cursor.isClosed()) {
           cursor.close();
        } 
        
        System.out.println("Data :"+favList+"\n");
} // end of else if 

return favList;

}// end of getFavorite



public void insertFavData(int i,String rTag, String rTitle, String sTag,String sTitle){
    
        db.execSQL("UPDATE Favorite SET RouteTag = '"+ rTag +"',RouteTitle = '"+ rTitle +"',StopTag ='"+ sTag +"',StopTitle = '"+ sTitle +"' WHERE favNum ='"+i+"'");
                                                
                        
}// end of insertAll


public void deleteAll_fav() {
        // TODO Auto-generated method stub
        
          this.db.delete(TABLE_NAME_Fav, null, null);
        
        //  insertFav(1);
          //insertFav(2);
}//end of deleteAll_fav


public void insertFav(int i){
    

    System.out.println("Updating the Favorite table ");
    // fav num  : 1 and 2 
    db.execSQL("INSERT INTO Favorite(favNum) Values ('" + i + "');");                                     

} // end of insertFav


//add the entry

public void addFav(String rTitle,String sTitle){
        int flag=1;
        System.out.println(sTitle);
        System.out.println(rTitle);
        System.out.println("Adding Favorite with new function");
db.execSQL("UPDATE "+TABLE_NAME+" SET FavStatus = '"+ flag +"' WHERE StopTitle= '"+ sTitle +"' and RouteTitle  ='"+ rTitle +"'");
                                                                              
        }// end of updateFavStatus


//delete the entry 
public void deleteFav(String rTitle,String sTitle){
        int flag=0;
        System.out.println("Deleting Favorite with new function");
db.execSQL("UPDATE "+TABLE_NAME+" SET FavStatus = '"+ flag +"' WHERE StopTitle= '"+sTitle+"' and RouteTitle  ='"+rTitle+"'");
                                                                                
        }// end of updateFavStatus



public List<String> getFavRoute() {
    // TODO Auto-generated method stub
    
       // System.out.println("Inside the getFavorite function....\n");
	
List<String> list = new ArrayList<String>();
    Cursor cursor =db.rawQuery("SELECT RouteTitle from "+TABLE_NAME+" Where FavStatus='"+1+"'", null);

if (cursor.moveToFirst()) {
        
do {
      list.add(cursor.getString(0));
} while (cursor.moveToNext());
}
if (cursor != null && !cursor.isClosed()) {
   cursor.close();
}
return list;

}

public List<String> getFavStop() {
    // TODO Auto-generated method stub
    
    //    System.out.println("Inside the getFavorite function....\n");
List<String> list = new ArrayList<String>();
    Cursor cursor =db.rawQuery("SELECT StopTitle from "+TABLE_NAME+" Where FavStatus='"+1+"'", null);

if (cursor.moveToFirst()) {
        
do {
      list.add(cursor.getString(0));
} while (cursor.moveToNext());
}
if (cursor != null && !cursor.isClosed()) {
   cursor.close();
}
return list;
}




} // end of Class  DataHelper
