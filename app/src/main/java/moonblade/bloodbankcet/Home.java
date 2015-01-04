package moonblade.bloodbankcet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class Home extends Activity {
    private int logged_in=0,is_admin=0;
    private boolean admin_unlocked,admin_logged_in;
    private Button about;
    Button viewblood,add;
    View seperatorview,seperatoradd;
    private static final int FILE_SELECT_CODE = 0;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try{
            Intent logged = this.getIntent();
            if (logged!=null){
                SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
                logged_in=prefs.getInt("Logged_in", 0);
                is_admin=prefs.getInt(getResources().getString(R.string.pref_is_admin), 0);
            }
        }
        catch (Exception e){

        }

        SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
        admin_unlocked=prefs.getBoolean(getResources().getString(R.string.pref_admin_unlock), false);
        admin_logged_in=prefs.getBoolean(getResources().getString(R.string.pref_admin_logged_in),false);

        if(logged_in==1)
            invalidateOptionsMenu();

        add=(Button)findViewById(R.id.add);
        about=(Button)findViewById(R.id.about);
        viewblood=(Button)findViewById(R.id.viewblood);
        seperatoradd=(View)findViewById(R.id.seperatoradd);
        seperatorview=(View)findViewById(R.id.seperatorview);
        seperatoradd.setVisibility(View.INVISIBLE);

        if(logged_in!=1){
            add.setVisibility(View.INVISIBLE);
            seperatorview.setVisibility(View.INVISIBLE);
        }else{
            add.setVisibility(View.VISIBLE);
            seperatorview.setVisibility(View.VISIBLE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,addmember.class);
                startActivity(i);
            }
        });
        viewblood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view=new Intent(Home.this,ViewBlood.class);
                view.putExtra("logged",logged_in);
                startActivity(view);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about=new Intent(getApplicationContext(),About.class);
                startActivity(about);
            }
        });



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);


        MenuItem admin_login=menu.findItem(R.id.action_admin_login);
        MenuItem logout=menu.findItem(R.id.action_logout);
        MenuItem loginn=menu.findItem(R.id.action_login);
        admin_login.setVisible(admin_unlocked);

        if (logged_in==1) {
            loginn.setVisible(false);
            logout.setVisible(true);
            admin_login.setVisible(false);

        }

        if (logged_in==0) {
            logout.setVisible(false);

        }
         if(admin_logged_in){
            logout.setVisible(admin_logged_in);
             admin_login.setVisible(!admin_logged_in);
            loginn.setVisible(!admin_logged_in);
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
            Intent setting = new Intent(Home.this,settings.class);
            startActivity(setting);
        }
        if (id == R.id.action_login) {
            Intent login =new Intent(Home.this,LoginPage.class);
            startActivity(login);
            finish();
        }
        if(id==R.id.action_admin_login){
            Intent adminlogin=new Intent(getApplicationContext(),AdminLogin.class);
            startActivity(adminlogin);
        }

     if (id == R.id.action_logout){
            SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
            editor.putInt("Logged_in",0);
            editor.putBoolean(getResources().getString(R.string.pref_is_admin),false);
            editor.putBoolean(getResources().getString(R.string.pref_admin_logged_in),false);
            editor.commit();
            Intent logout =new Intent (Home.this,Home.class);
            startActivity(logout);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    //Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        path = FileUtils.getPath(this, uri);
                        String DB_FILEPATH =Environment.getDataDirectory()+"/data/"+ "moonblade.bloodbankcet" +"/databases/"+"_bdb";
                        // Close the SQLiteOpenHelper so it will commit the created empty
                        // database to internal storage.
                        sqldb db = new sqldb(Home.this);
                        db.open();
                        db.close();
                        File newDb = new File(path);
                        File oldDb = new File(DB_FILEPATH);
                        if (newDb.exists()) {
                            try {
                                FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
                            } catch (IOException e) {
                                Toast.makeText(Home.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            // Access the copied database so SQLiteHelper will cache it and mark
                            // it as created.
                            db.open();
                            db.close();
                        }

                        Toast.makeText(Home.this, "DB Imported!",
                                Toast.LENGTH_SHORT).show();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    //Log.d(TAG, "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void csv_wtf() {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("filename"), '\t');
            sqldb rs=new sqldb(Home.this);
            rs.open();
            java.sql.ResultSet myResultSet = rs.getresultset();
            rs.close();
            writer.writeAll(myResultSet,true);
        } catch (FileNotFoundException e) {
            Toast.makeText(Home.this,"ERROR1",Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Home.this,"ERROR2",Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(Home.this,"ERROR3",Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(Home.this,"ERROR4",Toast.LENGTH_SHORT).show();
        }
    }
}
