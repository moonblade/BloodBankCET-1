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

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class Home extends Activity {
    private int logged_in=0,is_admin=0;
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

        if(logged_in==1)
            invalidateOptionsMenu();

        add=(Button)findViewById(R.id.add);
        viewblood=(Button)findViewById(R.id.viewblood);
        seperatoradd=(View)findViewById(R.id.seperatoradd);
        seperatorview=(View)findViewById(R.id.seperatorview);
        seperatoradd.setVisibility(View.INVISIBLE);

        if(logged_in==0){
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



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        if (logged_in==1) {

            MenuItem loginn=menu.findItem(R.id.action_login);
            loginn.setVisible(false);


        }

        if (logged_in==0) {

            MenuItem logout=menu.findItem(R.id.action_logout);
            MenuItem menu_import=menu.findItem(R.id.action_import);
            logout.setVisible(false);
            menu_import.setVisible(false);
        }
        if(is_admin==0){
            MenuItem export=menu.findItem(R.id.action_export);
            export.setVisible(false);
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
        if (id == R.id.action_export) {
//            csv_wtf();
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source=null;
            FileChannel destination=null;
            String currentDBPath = "/data/"+ "moonblade.bloodbankcet" +"/databases/"+"_bdb";
            String backupDBPath = "_bdb";
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(Home.this, "DataBase Exported!", Toast.LENGTH_LONG).show();
            } catch(IOException e) {
                Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


            return  true;
        }
        if (id == R.id.action_import) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                startActivityForResult(
                        Intent.createChooser(intent, "Select a Database File"),
                        FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException ex) {
                // Potentially direct the user to the Market with a Dialog
                Toast.makeText(Home.this, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        if (id == R.id.action_logout){
            SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
            editor.putInt("Logged_in",0);
            editor.putInt(getResources().getString(R.string.pref_is_admin),0);
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
