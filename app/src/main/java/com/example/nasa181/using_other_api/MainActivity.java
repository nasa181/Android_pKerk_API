package com.example.nasa181.using_other_api;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    TextView textView;
    TextView textView2;
    Intent it;
    JSONObject response;
    String htmlText;
    Button button;
    String session_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        textView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        textView2 = (TextView)findViewById(R.id.textView2);
    }
    public void startConnect(View v){

        new DownloadWebpageTask().execute("https://mis.cp.eng.chula.ac.th/mobile/api/?q=api/signIn",username.getText().toString(),password.getText().toString());

//        Thread x=new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                HashMap<String,String> hm = new HashMap<>();
//                hm.put("username",username);
//                hm.put("password", password);
//                final com.example.nasa181.using_other_api.HTTPHelper hp = new com.example.nasa181.using_other_api.HTTPHelper();
//                final String respose=hp.POST("https://mis.cp.eng.chula.ac.th/mobile/api/?q=api/signIn", hm);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText(respose);
//                    }
//                });
//            }
//        };
//
//        x.start();

    }
    public void startNext(){
        if(session_id.equalsIgnoreCase("Invalid Username/Password")){
            textView2.setText(session_id);
        }
        else{
            it = new Intent(this,Main2Activity.class);
            it.putExtra("data", session_id);
            startActivity(it);
        }
    }
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final HTTPHelper hp = new HTTPHelper();
            HashMap<String,String> hm = new HashMap<>();
            hm.put("username", params[1]);
            hm.put("password", params[2]);
            return hp.POST(params[0],hm);
        }
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
            htmlText = result;
            try {
                response = new JSONObject(htmlText);
                session_id = response.getString("content");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startNext();
        }

        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                //Log.d(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }


}
