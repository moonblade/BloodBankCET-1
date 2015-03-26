package com.kazmik.andro.onlinedbtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Loginsuccess extends Activity {

    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    private String jsonResult,names,filterbg,filterclass,filterbatch;
    JSONArray jsonMainNode;
    String[] groups;
    JSONObject jsonResponse;
    JSONObject jsonChildNode = null,node = null;
    SimpleAdapter simpleAdapter;
    String url="http://kazmikkhan.comli.com/phpfetchdetails.php";
    private ListView listView;
    TextView tvbgfilter,tvclassfilter,tvfilterbatch;
    ProgressDialog dialog,dialogbg,dialogclass,dialogboth;
    Spinner spfilterbg,spfilterclass;
    EditText etfilterbatch;
    LinearLayout llfilterlist;
    int flagbg=0,flagcls=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsuccess);
        final Bundle b = new Bundle();
        llfilterlist = (LinearLayout)findViewById(R.id.llfilterlist);
        listView = (ListView) findViewById(R.id.listView1);
        etfilterbatch = (EditText)findViewById(R.id.etfilterbatch);
        tvfilterbatch = (TextView)findViewById(R.id.tvfilterbatch);
        tvbgfilter = (TextView)findViewById(R.id.tvfilterbg);
        tvclassfilter = (TextView)findViewById(R.id.tvfilterclass);
        spfilterbg = (Spinner)findViewById(R.id.spinfiltbg);
        spfilterclass = (Spinner)findViewById(R.id.spinfiltclass);
        String[] classes = new String[]{"NONE","CSE", "CE", "EEE", "ECE", "ME", "ICE"};
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, classes);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spfilterclass.setAdapter(adapter_state);
        spfilterclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterclass  = spfilterclass.getSelectedItem().toString();
                Toast.makeText(Loginsuccess.this,filterclass,Toast.LENGTH_SHORT).show();
                if(flagcls==0)
                {
                    flagcls=1;
                }
                else if(spfilterclass.getSelectedItem().toString().equals("NONE"))
                {   if(!spfilterbg.getSelectedItem().toString().equals("NONE"))
                        accessWebServicefilterbg(spfilterbg.getSelectedItem().toString());
                    else
                    accessWebService(Loginsuccess.this);
                }
                else if(spfilterbg.getSelectedItem().toString().equals("NONE"))
                {
                    accessWebServicefilterclass(spfilterclass.getSelectedItem().toString());
                }
                else {
                    accessWebServicebyboth(spfilterbg.getSelectedItem().toString(), spfilterclass.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        groups = new String[]  {"NONE","A+" , "A-" ,"B+","B-","O+" , "O-","AB+" , "AB-" };
        ArrayAdapter<String> adapter_state_bg = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, groups);
        adapter_state_bg
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spfilterbg.setAdapter(adapter_state_bg);
        spfilterbg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterbg = spfilterbg.getSelectedItem().toString();
                Toast.makeText(Loginsuccess.this,filterbg,Toast.LENGTH_SHORT).show();
                if(flagbg==0)
                {
                    flagbg=1;
                }
                else if(spfilterbg.getSelectedItem().toString().equals("NONE"))
                {   if(!spfilterclass.getSelectedItem().toString().equals("NONE"))
                    accessWebServicefilterclass(spfilterclass.getSelectedItem().toString());
                    else
                    accessWebService(Loginsuccess.this);
                }

                else if(spfilterclass.getSelectedItem().toString().equals("NONE"))
                    {
                        accessWebServicefilterbg(spfilterbg.getSelectedItem().toString());
                    }
                else
                    accessWebServicebyboth(spfilterbg.getSelectedItem().toString(), spfilterclass.getSelectedItem().toString());


                }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accessWebService(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final View selectedView = view ;
                //String name = listView.getItemAtPosition(position).toString();
                TextView txt  = (TextView) view.findViewById(R.id.tvlistviewname);
                final String name = txt.getText().toString();

                SimpleDateFormat postFormater = new SimpleDateFormat("MMM dd");
                final Dialog diag = new Dialog(Loginsuccess.this);
                diag.setCanceledOnTouchOutside(false);
                diag.setCancelable(false);
                diag.setContentView(R.layout.dbdialog);
                diag.setTitle("Detail of Student");
                TextView namea= (TextView)diag.findViewById(R.id.tvdiagname);
                TextView classa= (TextView)diag.findViewById(R.id.tvdiagclass);
                TextView batch = (TextView)diag.findViewById(R.id.tvdiagbatch);
                TextView bg= (TextView)diag.findViewById(R.id.tvdiagbg);
                TextView mob= (TextView)diag.findViewById(R.id.tvdiagmob);
                TextView hos= (TextView)diag.findViewById(R.id.tvdiaghostel);
                TextView lastdon =(TextView)diag.findViewById(R.id.tvdiaglastdon);
                try {

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    jsonChildNode = jsonMainNode.getJSONObject(i);


                    if(name.equals(jsonChildNode.optString("name")))
                    {
                        node = jsonChildNode;
                        namea.setText(name);
                        classa.setText(jsonChildNode.optString("class"));
                        bg.setText(jsonChildNode.optString("bg"));
                        batch.setText(jsonChildNode.optString("batchfrom")+"-"+jsonChildNode.optString("batchto"));
                        mob.setText(jsonChildNode.optString("mob"));
                        hos.setText(jsonChildNode.optString("address"));
                        lastdon.setText(jsonChildNode.optString("lastdon"));
                        b.putString("name",name);
                        b.putString("class",jsonChildNode.optString("class"));
                        b.putString("batch",jsonChildNode.optString("batchfrom"));
                        b.putString("mob",jsonChildNode.optString("mob"));
                        b.putString("address",jsonChildNode.optString("address"));
                        b.putString("lastdon",jsonChildNode.optString("lastdon"));
                        b.putString("bg",jsonChildNode.optString("bg"));
                    }

                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Button bok = (Button)diag.findViewById(R.id.bdiagdok);
                bok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        diag.dismiss();
                    }
                });
                Button del = (Button) diag.findViewById(R.id.bdiagddel);
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(Loginsuccess.this);
                        alert.setTitle("Confirm Delete");
                        alert
                                .setMessage("Do you want to DELETE these entry?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        diag.dismiss();
                                        String cls = node.optString("class");
                                        String bch = node.optString("batchfrom");

                                        deleterecord(name, cls, bch);
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alert.create();

                        // show it
                        alertDialog.show();
                    }
                });
                Button bupdate = (Button)diag.findViewById(R.id.bdiagdupdate);
                bupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent k = new Intent(Loginsuccess.this,UpdateEntry.class);
                        k.putExtras(b);
                        startActivity(k);
                    }
                });
                diag.show();

            }
        });
    }

    private void accessWebServicebyboth(String bgfilt, String clsfilt) {
        dialogbg = new ProgressDialog(Loginsuccess.this);
        dialogbg.setTitle("Filtering Data By Blood Group : " + bgfilt+ " and Class : " + clsfilt );
        dialogbg.setCancelable(false);
        dialogbg.setCanceledOnTouchOutside(false);
        dialogbg.setMessage("Repopulating List");
        dialogbg.show();
        JsonReadTaskfilterbyboth task = new JsonReadTaskfilterbyboth(bgfilt,clsfilt);
        task.execute(new String[] { url });
    }
    private class JsonReadTaskfilterbyboth extends AsyncTask<String, Void, String> {
        String bg,cls;
        public JsonReadTaskfilterbyboth(String bgfilt, String s) {
            bg=bgfilt;
            cls=s;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaerfilterbyboth(bg,cls);
        }

    }// end async task

    // build hash set for list view
    public void ListDrwaerfilterbyboth(String bg, String cls) {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {
            jsonResponse = new JSONObject(jsonResult);
            jsonMainNode = jsonResponse.optJSONArray("user_info");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("name");
                String blood = jsonChildNode.optString("bg");
                String fclas = jsonChildNode.optString("class");
                String outPut=null;
                if(blood.equals(bg)&&fclas.equals(cls))
                    outPut=name;
                if(outPut!=null)
                    employeeList.add(createEmployeefilterbyboth("usernames", outPut));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        simpleAdapter = new SimpleAdapter(this, employeeList,
                R.layout.listviewsamp,
                new String[] { "usernames" }, new int[] { R.id.tvlistviewname });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ModeCallback());
        listView.setAdapter(simpleAdapter);
        dialogbg.dismiss();
    }

    private HashMap<String, String> createEmployeefilterbyboth(String name, String number) {
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    private void accessWebServicefilterbg(String s) {
        dialogbg = new ProgressDialog(Loginsuccess.this);
        dialogbg.setTitle("Filtering Data By Blood Group : " + spfilterbg.getSelectedItem().toString());
        dialogbg.setCancelable(false);
        dialogbg.setCanceledOnTouchOutside(false);
        dialogbg.setMessage("Repopulating List");
        dialogbg.show();
        JsonReadTaskfilterbg task = new JsonReadTaskfilterbg(s);
        // passes values for the urls string array
        task.execute(new String[] { url });
        //dialogbg.dismiss();
    }
    /////

    private class JsonReadTaskfilterbg extends AsyncTask<String, Void, String> {
        String bg;
        public JsonReadTaskfilterbg(String s) {
            bg=s;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaerfilterbg(bg);
        }

    }// end async task

    // build hash set for list view
    public void ListDrwaerfilterbg(String bg) {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {
            jsonResponse = new JSONObject(jsonResult);
            jsonMainNode = jsonResponse.optJSONArray("user_info");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("name");
                String blood = jsonChildNode.optString("bg");

                String outPut=null;
                if(blood.equals(bg))
                outPut=name;
                if(outPut!=null)
                employeeList.add(createEmployeefilterbg("usernames", outPut));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        simpleAdapter = new SimpleAdapter(this, employeeList,
                R.layout.listviewsamp,
                new String[] { "usernames" }, new int[] { R.id.tvlistviewname });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ModeCallback());
        listView.setAdapter(simpleAdapter);
        dialogbg.dismiss();
    }

    private HashMap<String, String> createEmployeefilterbg(String name, String number) {
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    //////

    private void accessWebServicefilterclass(String s) {
        dialogclass = new ProgressDialog(Loginsuccess.this);
        dialogclass.setTitle("Filtering Data By Class : " + spfilterclass.getSelectedItem().toString());
        dialogclass.setCancelable(false);
        dialogclass.setCanceledOnTouchOutside(false);
        dialogclass.setMessage("Repopulating List");
        dialogclass.show();
        JsonReadTaskfilterclass task = new JsonReadTaskfilterclass(s);
        // passes values for the urls string array
        task.execute(new String[] { url });
        //dialogclass.dismiss();
    }

    ////
    private class JsonReadTaskfilterclass extends AsyncTask<String, Void, String> {
        String cls;
        public JsonReadTaskfilterclass(String s) {
            cls=s;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaerfilterclass(cls);
        }

    }// end async task

    // build hash set for list view
    public void ListDrwaerfilterclass(String cls) {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {
            jsonResponse = new JSONObject(jsonResult);
            jsonMainNode = jsonResponse.optJSONArray("user_info");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("name");
                String clas = jsonChildNode.optString("class");
                String outPut=null;
                if(clas.equals(cls))
                outPut=name;
                if(outPut!=null)
                employeeList.add(createEmployeefilterclass("usernames", outPut));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        simpleAdapter = new SimpleAdapter(this, employeeList,
                R.layout.listviewsamp,
                new String[] { "usernames" }, new int[] { R.id.tvlistviewname });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ModeCallback());
        listView.setAdapter(simpleAdapter);
        dialogclass.dismiss();
    }

    private HashMap<String, String> createEmployeefilterclass(String name, String number) {
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }


    ////
    class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_multiple_delete, menu);
            mode.setTitle("Select Items");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.share:
                    final int c=listView.getCheckedItemCount();
                    final int count = listView.getCount();
                    final SparseBooleanArray checked = listView.getCheckedItemPositions();
                    final AlertDialog.Builder alert = new AlertDialog.Builder(Loginsuccess.this);
                    alert.setTitle("Confirm Delete");
                    alert
                            .setMessage("Do you want to DELETE these entry?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    names =  null;

                                    for (int i = 0; i < count; i++) {
                                        if (checked.valueAt(i) == true) {
                                           //names+= simpleAdapter.getItem(i).toString()+"\n";

                                            names +=listView.getItemAtPosition(i).toString()+"\n";

                                        }

                                    }
                                   Toast.makeText(Loginsuccess.this,names,Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Loginsuccess.this, "Deleted " + c +
                                            " items", Toast.LENGTH_SHORT).show();


                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alert.create();

                    // show it
                    alertDialog.show();
                    mode.finish();
                    break;
                default:
                    Toast.makeText(Loginsuccess.this, "Clicked " + item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            final int checkedCount = listView.getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }

    }


    //
    ///
    ///

    private void deleterecord(String name, String cls, String bch) {
        deleterecordphp del = new deleterecordphp(Loginsuccess.this,name,cls,bch);

        del.execute();
    }
    private class deleterecordphp extends AsyncTask<String, Void, String> {

        ProgressDialog progress;
           Context c;
        String name,clasd,batchd;
        public deleterecordphp(Loginsuccess loginsuccess, String name, String cls, String bch) {
            this.c = loginsuccess;
            progress= new ProgressDialog(this.c);
            this.name = name;
            clasd = cls;
            batchd = bch;
            Toast.makeText(Loginsuccess.this,name+"\n"+clasd+"\n"+batchd,Toast.LENGTH_SHORT).show();

        }
        protected void onPreExecute(){

            progress.setTitle("Deleting Record");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Updating Database At Server");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://kazmikkhan.comli.com/phpdeletedetails.php";
                String data  = URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("clas", "UTF-8")
                        + "=" + URLEncoder.encode(clasd, "UTF-8");
                data += "&" + URLEncoder.encode("batch", "UTF-8")
                        + "=" + URLEncoder.encode(batchd, "UTF-8");

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
                Intent k = new Intent(Loginsuccess.this,Loginsuccess.class);
                startActivity(k);
                Loginsuccess.this.finish();
                Toast.makeText(Loginsuccess.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
            else
            {
                Intent k = new Intent(Loginsuccess.this,Loginsuccess.class);
                startActivity(k);
                Toast.makeText(Loginsuccess.this,"Record Not Deleted",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }
    }

    public void accessWebService(Loginsuccess loginsuccess) {
        dialog = new ProgressDialog(loginsuccess);
        dialog.setTitle("Fetching Data");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Populating List");
        dialog.show();
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[] { url });
    }

    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaer();
        }

    }// end async task

    // build hash set for list view
    public void ListDrwaer() {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {
            jsonResponse = new JSONObject(jsonResult);
            jsonMainNode = jsonResponse.optJSONArray("user_info");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("name");
                String outPut;

                outPut=name;
                employeeList.add(createEmployee("usernames", outPut));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        simpleAdapter = new SimpleAdapter(this, employeeList,
                R.layout.listviewsamp,
                new String[] { "usernames" }, new int[] { R.id.tvlistviewname });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ModeCallback());
        listView.setAdapter(simpleAdapter);
        dialog.dismiss();
    }

    private HashMap<String, String> createEmployee(String name, String number) {
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loginsuccess, menu);
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
        else if (id==R.id.action_newentry){
            Intent k = new Intent(Loginsuccess.this,AddNewEntry.class);
            startActivity(k);
        }
        else if(id==R.id.filter)
        {
            final ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) listView
                    .getLayoutParams();
            final Dialog dag = new Dialog(Loginsuccess.this);
            dag.setCancelable(false);
            dag.setCanceledOnTouchOutside(false);
            dag.setContentView(R.layout.fileterby);
            dag.setTitle("Select filters");
            final CheckBox cbbg,cbclass,cbbatch;
            cbbg = (CheckBox)dag.findViewById(R.id.cbbg);
            cbclass = (CheckBox)dag.findViewById(R.id.cbclass);
            cbbatch = (CheckBox)dag.findViewById(R.id.cbbatch);
            Button bdagok = (Button)dag.findViewById(R.id.bdiagfilter);
            bdagok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cbbg.isChecked())
                    {
                        tvbgfilter.setVisibility(View.VISIBLE);
                        spfilterbg.setVisibility(View.VISIBLE);
                        p.addRule(RelativeLayout.BELOW, R.id.spinfiltbg);
                        llfilterlist.setLayoutParams(p);

                    }
                    else
                    {
                        tvbgfilter.setVisibility(View.GONE);
                        spfilterbg.setVisibility(View.GONE);
                        spfilterbg.setSelection(getIndex(spfilterbg,"NONE"));

                    }
                    if(cbclass.isChecked())
                    {
                        tvclassfilter.setVisibility(View.VISIBLE);
                        spfilterclass.setVisibility(View.VISIBLE);
                        p.addRule(RelativeLayout.BELOW, R.id.spinfiltclass);
                        llfilterlist.setLayoutParams(p);
                    }
                    else
                    {
                        tvclassfilter.setVisibility(View.GONE);
                        spfilterclass.setVisibility(View.GONE);
                        spfilterclass.setSelection(getIndex(spfilterclass,"NONE"));
                    }
                    if(cbbatch.isChecked())
                    {
                        tvfilterbatch.setVisibility(View.VISIBLE);
                        etfilterbatch.setVisibility(View.VISIBLE);
                        p.addRule(RelativeLayout.BELOW, R.id.etfilterbatch);
                        llfilterlist.setLayoutParams(p);
                    }
                    else
                    {
                        tvfilterbatch.setVisibility(View.GONE);
                        etfilterbatch.setVisibility(View.GONE);
                    }
                    if(!cbbatch.isChecked()&&!cbbg.isChecked()&&!cbclass.isChecked())
                        accessWebService(Loginsuccess.this);
                    dag.dismiss();
                }
            });
            //Toast.makeText(Loginsuccess.this,"Filter activated",Toast.LENGTH_SHORT).show();
            dag.show();
        }
        return super.onOptionsItemSelected(item);
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


}
