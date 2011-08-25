package org.runextbus.com;

import java.io.IOException;
import java.io.InputStream; 
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.runextbus.com.FlushedInputStream;

public class ServerInterface {

   private DefaultHttpClient client=new DefaultHttpClient();  
  
   //retrieve
   public String retrieve(String url)    {
	   String ret=null;
	   System.out.println("Server Interface received: "+url);
	   HttpGet getRequest ;
   	   getRequest=new HttpGet(url);  
   	HttpParams httpParameters = new BasicHttpParams();
   	int timeoutConnection = 3000;
   	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
   	// Set the default socket timeout (SO_TIMEOUT) 
   	// in milliseconds which is the timeout for waiting for data.
   	int timeoutSocket = 5000;
   	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

   	DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
  
   	  
   	   try {
         HttpResponse getResponse =null; 
         //getResponse=client.execute(getRequest);
         getResponse = httpClient.execute(getRequest);
         final int statusCode = getResponse.getStatusLine().getStatusCode();
         if (statusCode != HttpStatus.SC_OK) {
        	 //System.out.println(statusCode);
        	 Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
            return null;
         }
         
         HttpEntity getResponseEntity =null; 
        
         getResponseEntity=getResponse.getEntity();
         
         if (getResponseEntity != null) {
        	 ret=EntityUtils.toString(getResponseEntity);
        	 //System.out.println("XML IN SERVER INTERFACE  :"+ret);
        	getResponseEntity =null;
        	return ret;
         }
      }
      catch (IOException e) {
         getRequest.abort();
         Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
      }
      
      return null;
   }
   
   public InputStream retrieveStream(String url) {
      HttpGet getRequest = new HttpGet(url);
      

      try {
         HttpResponse getResponse = client.execute(getRequest);
         
         final int statusCode = getResponse.getStatusLine().getStatusCode();
         
         if (statusCode != HttpStatus.SC_OK) {
            Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
            return null;
         }
         
         HttpEntity getResponseEntity = getResponse.getEntity();
         return getResponseEntity.getContent();
      }
      
      catch (IOException e) {
         getRequest.abort();
         Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
      }
       
      return null;
       
   }

   
   // retrieveBitmap exception
   public Bitmap retrieveBitmap(String url) throws Exception {
            InputStream inputStream = null;
      try {
         inputStream = this.retrieveStream(url);
         System.out.println(inputStream);
         final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
         return bitmap;
      }
      finally {
        closeStreamQuietly(inputStream);
      }   
   }
   
   public static void closeStreamQuietly(InputStream inputStream) {
	   try {
	   if(inputStream != null) {
	    inputStream.close();
	    }
	    } 
	   catch (IOException e) {
	       System.err.print("IOException: ");
	       }
	   } // end of method


}

   
