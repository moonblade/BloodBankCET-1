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

public class signup extends Activity {
    private Button sign_up,cancel;
    private EditText user,pass,confpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sign_up=(Button)findViewById(R.id.button_sign_signup);
        cancel=(Button)findViewById(R.id.button_sign_cancel);
        user=(EditText)findViewById(R.id.sign_user);
        pass=(EditText)findViewById(R.id.sign_pass);
        confpass=(EditText)findViewById(R.id.sign_pass_check);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=user.getText().toString();
                String password=pass.getText().toString();
                String conpass=confpass.getText().toString();

                if(password.equals(conpass)){
                    /*SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                    editor.putString(getResources().getString(R.string.pref_user_name),username);
                    editor.putString(getResources().getString(R.string.pref_pass_word),password);
                    Toast.makeText(signup.this,"Success",Toast.LENGTH_SHORT).show();
                    editor.putBoolean(getResources().getString(R.string.pref_is_user),true);
                    editor.putInt("Logged_in",1);
                    editor.commit();
                    Intent home=new Intent(signup.this,Home.class);
                    startActivity(home);*/
                    new SignupActivity(signup.this).execute(username,password);
                }else{
                    confpass.setText("");
                    Toast.makeText(signup.this,"Passwords don't match",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(signup.this,LoginPage.class);
                startActivity(login);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent login=new Intent(signup.this,LoginPage.class);
        startActivity(login);
        finish();
    }
}
