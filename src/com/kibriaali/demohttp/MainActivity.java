package com.kibriaali.demohttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//Widgets
	Button btn;
	TextView etResponse;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Assign widgets
        listeners();
        etResponse = (TextView) findViewById(R.id.textView1);
        
        //Check internet connection
        if(isConnected()){
        	//Execute http task
        	requestJSON();
        } else {
        	createToast("no internet connection");
        	//notification
//        	Toast.makeText(getBaseContext(), "no internet connection", Toast.LENGTH_LONG).show();
        }
        
    }
    
    public void listeners(){
    	btn = (Button) findViewById(R.id.button1);
    	btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etResponse.setText("...");
				requestJSON();
			}
		});
    }
    
    public void requestJSON(){
    	new HttpAsyncTask().execute("http://android-demo.kibriaali.com/api/articles");
    }
    
    
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }


    private boolean isConnected() {
    	ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
    	}else{
            return false;
    	}    
	}
    
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		
		
		
		@Override
        protected String doInBackground(String... urls) {
 
            return GET(urls[0]);
        }
		// onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            etResponse.setText(result);
            
            try{
            	JSONArray arr = new JSONArray(result);
                for(int i = 0; i < arr.length(); i++){
                	JSONObject obj = arr.getJSONObject(i);
                	String title = obj.getString("title");
                	createToast(title);
//                	Toast.makeText(getBaseContext(), title, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
            	Toast.makeText(getApplicationContext(), (CharSequence) e, Toast.LENGTH_LONG).show();
            }
            
       }
    }
	
	public void createToast(String e){
		Toast.makeText(getBaseContext(), e, Toast.LENGTH_LONG).show();
	}
    
}
