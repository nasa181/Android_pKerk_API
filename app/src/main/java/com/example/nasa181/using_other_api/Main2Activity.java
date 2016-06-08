package com.example.nasa181.using_other_api;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {
    String session_id;
    HashMap<String,String> hm = new HashMap<>();
    TextView finding_user;



    String searchUser_url = "https://mis.cp.eng.chula.ac.th/mobile/api/?q=api/searchUser";
    String addContact_url = "https://mis.cp.eng.chula.ac.th/mobile/api/?q=api/addContact";
    String getContact_url = "https://mis.cp.eng.chula.ac.th/mobile/api/?q=api/getContact";
    String postMessage_url = "https://mis.cp.eng.chula.ac.th/mobile/api/?q=api/postMessage";
    String getMessage_url = "https://mis.cp.eng.chula.ac.th/mobile/api/?q=api/getMessage";
    String deleteMessage_url = "https://mis.cp.eng.chula.ac.th/mobile/api/?q=api/delMessage";



    EditText key_word;
    Button search_user;
    EditText username;
    Button add_contact;
    TextView friend_list;
    Button list_of_contact;
    EditText targetname;
    EditText posting_message;
    Button post_message;
    EditText seqno;
    TextView message;
    Button get_message;
    EditText deleting_message;
    EditText last_seqno;
    Button delete_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle bundle = getIntent().getExtras();
        session_id = bundle.getString("data");



        finding_user = (TextView)findViewById(R.id.finding_user);
        key_word = (EditText)findViewById(R.id.key_word);
        search_user = (Button)findViewById(R.id.search_user);
        username = (EditText)findViewById(R.id.username);
        add_contact = (Button)findViewById(R.id.add_contact);
        friend_list = (TextView)findViewById(R.id.friend_list);
        list_of_contact = (Button)findViewById(R.id.list_of_contact);
        targetname = (EditText)findViewById(R.id.target);
        posting_message = (EditText)findViewById(R.id.posting_message);
        post_message = (Button)findViewById(R.id.post_message);
        message = (TextView)findViewById(R.id.message);
        get_message = (Button)findViewById(R.id.get_message);
        deleting_message = (EditText)findViewById(R.id.deleting_message);
        delete_message = (Button)findViewById(R.id.delete_message);
        seqno = (EditText)findViewById(R.id.seqno);
        last_seqno = (EditText)findViewById(R.id.last_seqno);

        hm.put("sessionid",session_id);

    }
    public void search(View v){

        new DownloadWebpageTask().execute(searchUser_url,"search",key_word.getText().toString());
    }
    public void addContact(View v){
        new DownloadWebpageTask().execute(addContact_url,"add contact",username.getText().toString());
    }
    public void showFriend(View v){
        new DownloadWebpageTask().execute(getContact_url,"show friend");
    }
    public void postMessage(View v){
        new DownloadWebpageTask().execute(postMessage_url,"post message",targetname.getText().toString(),posting_message.getText().toString());
    }
    public void retriveMessage(View v){
        new DownloadWebpageTask().execute(getMessage_url,"retrive message",seqno.getText().toString());
    }
    public void deleteMessage(View v){
        new DownloadWebpageTask().execute(deleteMessage_url,"delete message",last_seqno.getText().toString());
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final HTTPHelper hp = new HTTPHelper();
            if(params[1].equalsIgnoreCase("search")){
                hm.put("keyword",params[2]);
                return hp.POST(params[0],hm);
            }
            else if(params[1].equalsIgnoreCase("add contact")){
                hm.put("username",params[2]);
                return hp.POST(params[0],hm);
            }
            else if(params[1].equalsIgnoreCase("show friend")){
                return hp.POST(params[0],hm);
            }
            else if(params[1].equalsIgnoreCase("post message")){
                hm.put("targetname",params[2]);
                hm.put("message",params[3]);
                return hp.POST(params[0],hm);
            }
            else if(params[1].equalsIgnoreCase("retrive message")){
                hm.put("seqno",params[2]);
                return hp.POST(params[0],hm);
            }
            else if(params[1].equalsIgnoreCase("delete message")){
                hm.put("seqno",params[2]);
                return hp.POST(params[0],hm);
            }
            else return null;
        }
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
            finding_user.setText(result);
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
