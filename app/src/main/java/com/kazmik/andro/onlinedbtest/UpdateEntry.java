package com.kazmik.andro.onlinedbtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kazmik.andro.onlinedbtest.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UpdateEntry extends ActionBarActivity {

    EditText name,batch,addr,mob,lastdon;
    Button submit;

    String clas,bloodg,namea,mobile,address,last,oldname,oldbatch,oldbg,oldaddr,oldmob,oldlast,oldclass;
    int batcha;
    Spinner classs,bg;
    String[] classes,groups;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);
        name = (EditText)findViewById(R.id.etuename);
        submit = (Button)findViewById(R.id.buesubmit);
        classs = (Spinner)findViewById(R.id.sueclass);
        batch = (EditText)findViewById(R.id.etuebatch);
        addr = (EditText)findViewById(R.id.etueaddr);
        lastdon = (EditText)findViewById(R.id.etuelastdon);
        mob = (EditText)findViewById(R.id.etuemob);
        bg = (Spinner)findViewById(R.id.suebg);
        b = getIntent().getExtras();
         oldname = b.getString("name");
        name.setText(oldname);
         oldclass = b.getString("class");

        oldbatch = b.getString("batch");
        batch.setText(oldbatch);
        oldmob = b.getString("mob");
        mob.setText(oldmob);
        oldaddr = b.getString("address");
        addr.setText(oldaddr);
        oldlast = b.getString("lastdon");
        lastdon.setText(oldlast);
        oldbg = b.getString("bg");


        classes = new String[]{"CSE", "CE", "EEE", "ECE", "ME", "ICE"};
        groups = new String[]  {"A+" , "A-" ,"B+","B-","O+" , "O-","AB+" , "AB-" };
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, classes);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter_state_bg = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, groups);
        adapter_state_bg
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classs.setAdapter(adapter_state);
        bg.setAdapter(adapter_state_bg);
        classs.setSelection(getIndex(classs, oldclass));

        bg.setSelection(getIndex(bg,oldbg));
        
        bg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodg = bg.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        classs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classs.setSelection(position);
                clas = classs.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namea = name.getText().toString();
                mobile = mob.getText().toString();
                batcha = Integer.parseInt(batch.getText().toString());
                address = addr.getText().toString();
                last = lastdon.getText().toString();

                submitdata();



            }
        });
    }

    private int getIndex(Spinner spin, String s) {

        int index = 0;

        for (int i=0;i<spin.getCount();i++){
            if (spin.getItemAtPosition(i).toString().equalsIgnoreCase(s)){
                index = i;
            //    i=spin.getCount();//will stop the loop, kind of break, by making condition false
                break;
            }
        }
        return index;
    }

    public void submitdata(){
        SubmitDatatophp task = new SubmitDatatophp(UpdateEntry.this);
        task.execute();

    }
    private class SubmitDatatophp extends AsyncTask<String, Void, String> {
        ProgressDialog progress;
        Context c;
        public SubmitDatatophp(UpdateEntry entry) {
            c = entry;
            progress= new ProgressDialog(this.c);
        }

        protected void onPreExecute(){

            progress.setTitle("Updating Data");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Sending data to server");
            progress.show();
        }
        @Override
        protected String doInBackground(String... params) {

            try {
                String link="http://kazmikkhan.comli.com/phpupdatedetails.php";
                String data  = URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(namea, "UTF-8");
                data += "&" + URLEncoder.encode("clas", "UTF-8")
                        + "=" + URLEncoder.encode(clas, "UTF-8");
                data += "&" + URLEncoder.encode("batchfrom", "UTF-8")
                        + "=" + URLEncoder.encode(String.valueOf(batcha), "UTF-8");
                data += "&" + URLEncoder.encode("batchto", "UTF-8")
                        + "=" + URLEncoder.encode(String.valueOf(batcha+4), "UTF-8");
                data += "&" + URLEncoder.encode("bg", "UTF-8")
                        + "=" + URLEncoder.encode(bloodg, "UTF-8");
                data += "&" + URLEncoder.encode("mob", "UTF-8")
                        + "=" + URLEncoder.encode(mobile, "UTF-8");
                data += "&" + URLEncoder.encode("address", "UTF-8")
                        + "=" + URLEncoder.encode(address, "UTF-8");
                data += "&" + URLEncoder.encode("lastdon", "UTF-8")
                        + "=" + URLEncoder.encode(last, "UTF-8");
                data += "&" + URLEncoder.encode("oldname", "UTF-8")
                        + "=" + URLEncoder.encode(oldname, "UTF-8");
                data += "&" + URLEncoder.encode("oldclass", "UTF-8")
                        + "=" + URLEncoder.encode(oldclass, "UTF-8");
                data += "&" + URLEncoder.encode("oldbatch", "UTF-8")
                        + "=" + URLEncoder.encode(oldbatch, "UTF-8");
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
                Intent k = new Intent(UpdateEntry.this,Loginsuccess.class);
                startActivity(k);
                Toast.makeText(UpdateEntry.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
            else
            {
                Intent k = new Intent(UpdateEntry.this,Loginsuccess.class);
                startActivity(k);
                Toast.makeText(UpdateEntry.this,"Record Not Updated",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_entry, menu);
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
