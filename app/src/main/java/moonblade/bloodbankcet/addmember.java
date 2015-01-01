package moonblade.bloodbankcet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import moonblade.bloodbankcet.R;

public class addmember extends Activity {
    Button baddentry;
    EditText etdbname,etdbclass,etdbmob,etdbhostel,etdbjyear;
//    private int day,month,year;
    Spinner add_spinner;
    String name,clas,mob,hostel,bg;
    int joinyear;
    long millisec;
    Date dAte;
    DatePicker datePicker;
    static final int DATE_DIALOG_ID = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);
        baddentry=(Button)findViewById(R.id.baddentry);
        etdbname=(EditText)findViewById(R.id.etdbname);
        etdbclass=(EditText)findViewById(R.id.etdbclass);
        etdbmob=(EditText)findViewById(R.id.etmob);
        etdbhostel=(EditText)findViewById(R.id.ethostel);
        etdbjyear=(EditText)findViewById(R.id.etdbjoin);
        datePicker=(DatePicker)findViewById(R.id.date_Picker);
        final String[] bloodgroup = new String[1];
        add_spinner=(Spinner)findViewById(R.id.add_spinner);
        add_spinner.setPrompt("Blood Group");
        add_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodgroup[0] = add_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        baddentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int day = datePicker.getDayOfMonth();
                final int month = datePicker.getMonth();
                final int year = datePicker.getYear();
                final Date date = new Date(year,month,day);

                millisec=date.getTime();

                name=etdbname.getText().toString();
                clas=etdbclass.getText().toString();
                mob=etdbmob.getText().toString();
                hostel=etdbhostel.getText().toString();
                joinyear= Integer.parseInt(etdbjyear.getText().toString());
                bg=bloodgroup[0];
                dAte = new Date(millisec);
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");

//                Toast.makeText(addmember.this,df2.format(dAte),Toast.LENGTH_SHORT).show();
                if(name.isEmpty()||clas.isEmpty()||mob.isEmpty()||hostel.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill the Entries", Toast.LENGTH_SHORT).show();
                }else if(mob.length()<10||mob.length()>11) {
                    Toast.makeText(getApplicationContext(), "Not a valid phone number", Toast.LENGTH_SHORT).show();
                }else{
                    submitdata();
                    Toast.makeText(getApplicationContext(), "Entry Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }

    public void submitdata(){
        SubmitDatatophp task = new SubmitDatatophp(addmember.this);
        task.execute();

    }

    private class SubmitDatatophp extends AsyncTask<String, Void, String> {
        ProgressDialog progress;
        Context c;
        public SubmitDatatophp(addmember entry) {
            c = entry;
            progress= new ProgressDialog(this.c);
            progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        protected void onPreExecute(){

//            progress.setTitle("Submitting Data");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Sending data to server");
            progress.show();
        }
        @Override
        protected String doInBackground(String... params) {

            try {
                String link="http://moonblade.in/bloodbankcet/blood/addmember.php";
                String data  = URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("clas", "UTF-8")
                        + "=" + URLEncoder.encode(clas, "UTF-8");
                data += "&" + URLEncoder.encode("bg", "UTF-8")
                        + "=" + URLEncoder.encode(bg, "UTF-8");
                data += "&" + URLEncoder.encode("mob", "UTF-8")
                        + "=" + URLEncoder.encode(mob, "UTF-8");
                data += "&" + URLEncoder.encode("hostel", "UTF-8")
                        + "=" + URLEncoder.encode(hostel, "UTF-8");
                data += "&" + URLEncoder.encode("year", "UTF-8")
                        + "=" + URLEncoder.encode(String.valueOf(joinyear), "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8")
                        + "=" + URLEncoder.encode(String.valueOf(millisec), "UTF-8");
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

            }
            catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("success"))
            {
                Intent k = new Intent(addmember.this,Home.class);
                startActivity(k);
                Toast.makeText(addmember.this,"Added Successfully",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
            else
            {
                Intent k = new Intent(addmember.this,Home.class);
                startActivity(k);
                Toast.makeText(addmember.this,"Record Not Added",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addmember, menu);
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
