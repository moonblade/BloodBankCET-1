package moonblade.bloodbankcet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import moonblade.bloodbankcet.R;

public class AdminLogin extends Activity {
private EditText user,pass;
    private Button login;
    private TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        user=(EditText)findViewById(R.id.admin_username);
        pass=(EditText)findViewById(R.id.admin_password);
        login=(Button)findViewById(R.id.button_admin_login);
        status=(TextView)findViewById(R.id.admin_status);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=user.getText().toString();
                String password=pass.getText().toString();
                new AdminSigninActivity(AdminLogin.this,status).execute(username,password);
            }
        });
    }


    public class AdminSigninActivity  extends AsyncTask<String,Void,String> {

        TextView statuss;
        ProgressDialog progress;
        private Context context;
        private int byGetOrPost = 0;
        //flag 0 means get and 1 means post.(By default it is get.)
        public AdminSigninActivity(AdminLogin context, TextView status) {
            this.context = (Context) context; this.statuss = status;
            progress= new ProgressDialog(this.context);
            progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        protected void onPreExecute(){

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

                String link="http://moonblade.in/bloodbankcet/admin/userlogin.php";
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

                this.statuss.setText("Success");
                progress.dismiss();
                Intent i = new Intent(context, Home.class);
                SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                editor.putBoolean(getResources().getString(R.string.pref_admin_logged_in), true);
                editor.commit();
                context.startActivity(i);

            }
            else
            {
                this.statuss.setText("Invalid Credentials!!!");
                progress.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
