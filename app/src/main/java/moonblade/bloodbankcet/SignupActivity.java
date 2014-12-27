package moonblade.bloodbankcet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Nisham on 12/16/2014.
 */
public class SignupActivity  extends AsyncTask<String,Void,String> {

    TextView urll;
    ProgressDialog progress;
    private Context context;
    private int byGetOrPost = 0;
    //flag 0 means get and 1 means post.(By default it is get.)
    public SignupActivity(signup context,TextView urll) {
        this.context = (Context) context;
        this.urll = urll;
        progress= new ProgressDialog(this.context);
    }

    protected void onPreExecute(){

        progress.setTitle("Signing Up");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.setMessage("Verifying Username");
        progress.show();
    }
    @Override
    protected String doInBackground(String... arg0) {

        try{
            String username = (String)arg0[0];
            String password = (String)arg0[1];

            String link="http://moonblade.in/bloodbankcet/user/adduser.php";
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
//            Intent i = new Intent(context, Loginsuccess.class);
//            context.startActivity(i);
            urll.setText(result);
            progress.dismiss();
        }
        else
        {
            urll.setText(result);
            progress.dismiss();
        }
    }

}
