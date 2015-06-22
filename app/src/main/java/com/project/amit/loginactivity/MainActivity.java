package com.project.amit.loginactivity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;


public class MainActivity extends Activity {

    TextView textView;
    public static String token;
    EditText ed1,ed2 ;
    Button  button;
    private String data;
    private static final String fixstring = "&grant_type=password&client_id=099153c2625149bc8ecb3e85e03f0022";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.wait);
        ed1 = (EditText)findViewById(R.id.ename);
        ed2 = (EditText)findViewById(R.id.epass);
        button = (Button)findViewById(R.id.log);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ed1.setEnabled(false);
               ed2.setEnabled(false);
               if(isConnected()){
                   textView.setText("Please wait...");
                   new HttpAsyncTask2().execute("http://smsservices.infocouture.com/oauth/token");
               }
               else{
                   Toast.makeText(getBaseContext(), "No Internet access!!", Toast.LENGTH_LONG).show();
                   ed1.setEnabled(true);
                   ed2.setEnabled(true);
               }
           }
       });
    }
    protected void onPause()
    {
        super.onPause();
        finish();
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpGet request = new HttpGet(url);
            request.addHeader("Authorization","Bearer "+token);
            HttpResponse httpResponse = httpclient.execute(request);

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public String getdata() {
        ed1 = (EditText)findViewById(R.id.ename);
        ed2 = (EditText)findViewById(R.id.epass);
        String s1,s2;
        s1 = new String(URLEncoder.encode(ed1.getText().toString()));
        s2 = new String(URLEncoder.encode(ed2.getText().toString()));
        data = ("username="+s1+"&"+"password="+s2+fixstring).toString();
        return data;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(MainActivity.this,site.class);
            startActivity(intent);
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        }
    }

    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0], getdata());
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
           // textView2.setText(result);
            try {
                JSONObject object = new JSONObject(result);
                if(object.has("error"))
                {
                    Toast.makeText(getBaseContext(), "The password or username is incorrect", Toast.LENGTH_LONG).show();
                    textView.setText("");
                    ed1.setEnabled(true);
                    ed2.setEnabled(true);
                }
                else if(object.has("access_token"))
                {
                  //  Toast.makeText(getBaseContext(), "yuipppeeee", Toast.LENGTH_LONG).show();
                    token = object.getString("access_token");
                    new HttpAsyncTask().execute("http://smsservices.infocouture.com/api/Test/getwithauth");
                }
                else
                {
                    Toast.makeText(getBaseContext(), "kuch to gadbad hai", Toast.LENGTH_LONG).show();
                    //textView2.setText(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public static String POST(String url,String data){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(data);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

}
