package org.runextbus.com;

import java.io.IOException;
import java.io.InputStream; 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.runextbus.com.FlushedInputStream;

public class ServerInterface {

   private DefaultHttpClient client = new DefaultHttpClient();  
  
   //retrieve
   public String retrieve(String url) {
	     
	   // System.out.println("Begin Http RETRIEVE"); 
	   HttpGet getRequest = new HttpGet(url);
        
      try {
         HttpResponse getResponse = client.execute(getRequest);
         final int statusCode = getResponse.getStatusLine().getStatusCode();
         if (statusCode != HttpStatus.SC_OK) {
        	 System.out.println(statusCode);
        	 Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
            return null;
         }
         HttpEntity getResponseEntity = getResponse.getEntity();
         if (getResponseEntity != null) {
            return EntityUtils.toString(getResponseEntity);
         }
      }
      catch (IOException e) {
         getRequest.abort();
         Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
      }
      return null;
   }
   
   // retrieveStream
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