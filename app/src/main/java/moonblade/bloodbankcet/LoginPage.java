package moonblade.bloodbankcet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import moonblade.bloodbankcet.R;

public class LoginPage extends Activity {
    EditText username, password;
    Button action_login, action_sign_up;
    private boolean is_a_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        action_login = (Button) findViewById(R.id.button_login);
        action_sign_up = (Button) findViewById(R.id.button_sign_up);
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);
        is_a_user = pref.getBoolean(getResources().getString(R.string.pref_is_user), false);
        action_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_up=new Intent(LoginPage.this,signup.class);
                startActivity(sign_up);
                finish();
            }
        });
        action_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int log = 0;
                String user = username.getText().toString();
                String pass = password.getText().toString();
                SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);

                String login_user = "Secret";
                String login_pass = "backdoor";
                String login_pref_user = pref.getString(getResources().getString(R.string.pref_user_name), "nimda");
                String login_pref_pass = pref.getString(getResources().getString(R.string.pref_pass_word), "drowssap");
                if (user.equals(login_user) && pass.equals(login_pass)) {
                    Toast.makeText(LoginPage.this, "Success", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                    editor.putInt(getResources().getString(R.string.pref_is_admin),1);
                    editor.commit();
                    log = 1;
                } else if (user.equals(login_pref_user) && pass.equals(login_pref_pass)) {
                    Toast.makeText(LoginPage.this, "Success", Toast.LENGTH_SHORT).show();
                    log = 1;
                } else {

                }

                if (log == 1) {
                    SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                    editor.putInt("Logged_in", log);
                    editor.commit();
                    callintent();

                } else {
                    password.setText("");
                    Toast.makeText(LoginPage.this, "Username or Password incorrect", Toast.LENGTH_SHORT).show();

                }*/
                String back_login_user = "Secret";
                String back_login_pass = "backdoor";
                String enter_user = username.getText().toString();
                String enter_pass = password.getText().toString();
                new SigninActivity(this,status).execute(enter_user,enter_pass);

            }
        });
    }

    private void callintent() {
        Intent i=new Intent (LoginPage.this,Home.class);
        startActivity(i);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_page, menu);
        SharedPreferences pref = getSharedPreferences("Preferences", MODE_PRIVATE);
        is_a_user = pref.getBoolean(getResources().getString(R.string.pref_is_user), false);
        MenuItem action_delete_user =menu.findItem(R.id.action_delete_user);
        if(is_a_user==false){
            action_delete_user.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent setting=new Intent(LoginPage.this,settings.class);
            startActivity(setting);
        }
        if(id==R.id.action_delete_user){
            SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
            editor.putBoolean(getResources().getString(R.string.pref_is_user), false);
            editor.putString(getResources().getString(R.string.pref_user_name), "nimda");
            editor.putString(getResources().getString(R.string.pref_pass_word), "drowssap");
            editor.commit();
            editor.putInt("Logged_in", 0);
            callintent();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callintent();
    }
}

