package com.kazmik.andro.onlinedbtest;

/**
 * Created by Akzin on 11/7/2014.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import static android.support.v4.app.ActivityCompat.startActivity;

public class SigninActivity  extends AsyncTask<String,Void,String>{

    TextView statuss;
    ProgressDialog progress;
    private Context context;
    private int byGetOrPost = 0;
    //flag 0 means get and 1 means post.(By default it is get.)
    public SigninActivity(Context context, TextView status) {
        this.context = context; this.statuss = status;
         progress= new ProgressDialog(this.context);
    }

    protected void onPreExecute(){

        progress.setTitle("Logging In");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.setMessage("Authenticating User");
        progress.show();
    }
    @Override
    protected String doInBackground(String... arg0) {

            try{
                String username = (String)arg0[0];
                String password = (String)arg0[1];

                String link="http://kazmikkhan.comli.com/phpandrologin.php";
                String data  = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8")
                        + "=" + URLEncoder.encode(password, "UTF-8");
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter
                        (conn.getOutputStream());
                wr.write( data );
                wr.flush();
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }

                return sb.toString();

            }catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

    @Override
    protected void onPostExecute(String result){
        if(result.equals("success")) {
            Intent i = new Intent(context, Loginsuccess.class);
            context.startActivity(i);
            this.statuss.setText(" ");
            progress.dismiss();
        }
        else
        {
            this.statuss.setText("Invalid Credentials!!!");
            progress.dismiss();
        }
    }
}
