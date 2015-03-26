package com.kazmik.andro.onlinedbtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private EditText usernameField,password;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameField = (EditText)findViewById(R.id.editText1);
        password=(EditText)findViewById( R.id.editText2);
        status=(TextView)findViewById(R.id.textView2);
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void loginPost(View view){
        String username = usernameField.getText().toString();
        String pass = password.getText().toString();

        new SigninActivity(this,status).execute(username,pass);

    }
    public void done() {
        finish();
    }


}