package moonblade.bloodbankcet;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import static moonblade.bloodbankcet.R.layout.listviewlayout;

public class ViewBlood extends Activity implements AdapterView.OnItemSelectedListener{

    ArrayList<String> blood_list;
    private String jsonResult;
    ProgressDialog dialog;
    JSONArray jsonMainNode;
    JSONObject jsonResponse;
    ArrayAdapter<String> blood_adapter;
    private int curday,curmon,curyear;
    private String url="http://moonblade.in/bloodbankcet/blood/fetchdetails.php";
    private long totdays;
    private int number_of_months;
    int long_clicked=0;
    private int logged_in=0,is_admin=0;
    Spinner spinner_blood;
    ImageView green,red;
//    ListView data =(ListView)findViewById(R.id.lvdata);
    private CursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blood);

        try{
            Intent logged = this.getIntent();
            if (logged!=null){
                SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
                logged_in=prefs.getInt("Logged_in", 0);
                is_admin=prefs.getInt(getResources().getString(R.string.pref_is_admin), 0);
                number_of_months=prefs.getInt(getResources().getString(R.string.pref_months),3);
            }
        }
        catch (Exception e){

        }

        initialise_adapter();
        setcurrentdate();
        String[] blood_groups = getResources().getStringArray(R.array.bloodgroups);
        ArrayAdapter adapter=new ArrayAdapter<String>(this, R.layout.blood_item, R.id.label, blood_groups);

//        final ListView data =(ListView)findViewById(R.id.lvdata);
        accessWebService(this);
//        update_red_green(data);


/*        data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Cursor cursor = (Cursor) data.getItemAtPosition(position);
                final Dialog dialog = new Dialog(ViewBlood.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Detail of Student");
                TextView namea = (TextView) dialog.findViewById(R.id.tvdiagname);
                TextView brancha = (TextView) dialog.findViewById(R.id.tvdiagbranch);
                TextView bg = (TextView) dialog.findViewById(R.id.tvdiagbg);
                TextView mob = (TextView) dialog.findViewById(R.id.tvdiagmob);
                TextView hos = (TextView) dialog.findViewById(R.id.tvdiaghostel);
                TextView dat = (TextView) dialog.findViewById(R.id.tvdiagdate);
                ImageView indicator=(ImageView)dialog.findViewById(R.id.indicator);


                namea.setText(cursor.getString(cursor.getColumnIndexOrThrow("_name")));
                brancha.setText(cursor.getString(cursor.getColumnIndexOrThrow("_branch")));
                bg.setText(cursor.getString(cursor.getColumnIndexOrThrow("_bg")));
                mob.setText(cursor.getString(cursor.getColumnIndexOrThrow("_phone")));
                final String num = cursor.getString(cursor.getColumnIndexOrThrow("_phone"));

                hos.setText(cursor.getString(cursor.getColumnIndexOrThrow("_hostel")));
                Long val=cursor.getLong(cursor.getColumnIndexOrThrow("_date"));

                final Button dbitton = (Button) dialog.findViewById(R.id.bdiagdok);
                final Button callbutton = (Button) dialog.findViewById(R.id.bdiagcall);

                Date date = new Date(val);
                int day=date.getDate();
                int month=date.getMonth()+1;
                int year=date.getYear();
                int total=year*365+month*30+day;
//                Toast.makeText(ViewBlood.this,""+day+" "+month+" "+year,Toast.LENGTH_SHORT).show();
                if(totdays-total<(30*number_of_months)){
                    indicator.setImageResource(R.drawable.red);
                }else{
                    indicator.setImageResource(R.drawable.green);
                }
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                dat.setText(df2.format(date));
                dbitton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                callbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + num));
                        startActivity(callIntent);

                    }
                });
                if (long_clicked == 0)
                    dialog.show();
                long_clicked = 0;
            }
        });*/
/*
        data.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Cursor cursor = (Cursor) data.getItemAtPosition(position);
                final Dialog dialog = new Dialog(ViewBlood.this);
                long_clicked=1;

                dialog.setContentView(R.layout.long_click_layout);
                dialog.setTitle("Admin Only");
                final Button editbutton = (Button)dialog.findViewById(R.id.edit_button);
                final Button okbutton = (Button)dialog.findViewById(R.id.ok_button);
                final Button deletebutton = (Button)dialog.findViewById(R.id.delete_button);
                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                editbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (logged_in == 0) {
                            Toast.makeText(ViewBlood.this, "You do not have permission for editing", Toast.LENGTH_SHORT).show();
                        } else {
                            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                            String name = cursor.getString(cursor.getColumnIndexOrThrow("_name"));
                            String brancha = cursor.getString(cursor.getColumnIndexOrThrow("_branch"));
                            String bg = cursor.getString(cursor.getColumnIndexOrThrow("_bg"));
                            String mob = cursor.getString(cursor.getColumnIndexOrThrow("_phone"));
                            String hostel = cursor.getString(cursor.getColumnIndexOrThrow("_hostel"));
                            long val=cursor.getLong(cursor.getColumnIndexOrThrow("_date"));

                            Bundle b = new Bundle();
                            b.putString("name", name);
                            b.putString("id", id);
                            b.putString("branch", brancha);
                            b.putString("bg", bg);
                            b.putString("mob", mob);
                            b.putString("hostel", hostel);
                            b.putLong("date",val);
                            Intent i = new Intent(ViewBlood.this, editEntry.class);
                            i.putExtras(b);
                            startActivity(i);
                            dialog.cancel();
                        }
                    }
                });
*/
 /*               final AlertDialog.Builder alert = new AlertDialog.Builder(ViewBlood.this);
                deletebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.setTitle("Confirm Delete");
                        alert
                                .setMessage("Do you want to DELETE this entry?")
                                .setCancelable(false)
                                .setPositiveButton("Do it!",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        long delid=cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                                        sqldb vi = new sqldb(ViewBlood.this);
                                        vi.open();
                                        vi.deleteId(delid);
                                        vi.close();
                                        dialog.cancel();


                                    }
                                })
                                .setNegativeButton("I need this",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
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
                if(logged_in==1)
                    dialog.show();
                return false;
            }
        });
 */   }

    public void accessWebService(ViewBlood viewblood) {
        dialog = new ProgressDialog(viewblood);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Populating List");
        dialog.show();
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url});
    }

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
            ListDrawer();
        }

    }// end async task

    // build hash set for list view
    public void ListDrawer() {
        final ListView data =(ListView)findViewById(R.id.lvdata);
        List<Map<String, String>> donorList = new ArrayList<Map<String, String>>();

        try {
            jsonResponse = new JSONObject(jsonResult);
            jsonMainNode = jsonResponse.optJSONArray("user_info");
            ArrayList<Donor> newUsers = Donor.fromJson(jsonMainNode);
            DonorAdapter donorAdapter = new DonorAdapter(this,newUsers);
//            donorAdapter.addAll(newUsers);
            donorAdapter.addAll(newUsers);
            data.setAdapter(donorAdapter);

//            for (int i = 0; i < jsonMainNode.length(); i++) {
//                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
//                String name = jsonChildNode.optString("name");
//                String bg = jsonChildNode.optString("bloodgroup");
//                String outPut;
//                outPut=name;
//                donorList.add(createdonor("name", outPut));
//                donorList.add(createdonor("bg", bg));
//            }
//        }
    } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        int[] to = new int[]{R.id.set_name,R.id.set_bg};
//        adapter = new SimpleCursorAdapter(ViewBlood.this,R.layout.listviewlayout,c,columns,to,0);
//        data.setAdapter(adapter);

//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, donorList,
//                listviewlayout,
//                new String[] { "name" }, new int[] { R.id.set_name });
//
//        DonorAdapter donorAdapter=new DonorAdapter(this,donorList);
//        data.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        data.setMultiChoiceModeListener(new ModeCallback());
//        data.setAdapter(simpleAdapter);
        dialog.dismiss();
    }

    private HashMap<String, String> createdonor(String name, String number) {
        HashMap<String, String> donorNameNo = new HashMap<String, String>();
        donorNameNo.put(name, number);
        return donorNameNo;
    }

/*sdfghjklkjhgfdssdfghjklkjgfds*/
    private void initialise_adapter() {
        blood_list = new ArrayList<String>();
        blood_list.add("All");
        blood_list.add("A+");
        blood_list.add("A-");
        blood_list.add("B+");
        blood_list.add("B-");
        blood_list.add("O+");
        blood_list.add("O-");
        blood_list.add("AB+");
        blood_list.add("AB-");
        blood_adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_action_bar, blood_list);
        blood_adapter.setDropDownViewResource(R.layout.spinner_dropdown);
    }

  /*  private void update_red_green(ListView data){
        sqldb get_size=new sqldb(ViewBlood.this);
        get_size.open();
        int size=get_size.get_row_count();
        get_size.close();
        setcurrentdate();
        int visibleChildCount = (data.getLastVisiblePosition() - data.getFirstVisiblePosition()) + 1;
        int firstPosition = data.getFirstVisiblePosition() - data.getHeaderViewsCount();
        Toast.makeText(ViewBlood.this,""+data.getChildCount(),Toast.LENGTH_SHORT).show();
        for(int datai=firstPosition;datai<size;datai++){

            final Cursor cursor = (Cursor) data.getItemAtPosition(datai);
            Long val=cursor.getLong(cursor.getColumnIndexOrThrow("_date"));
            Date date = new Date(val);
            int day=date.getDay();
            int month=date.getMonth()+1;
            int year=date.getYear();
            long total=year*365+month*30+day;
            long diff=totdays-total;
            if(diff<(30*number_of_months)){
//                 green.setVisibility(View.INVISIBLE);
//                 Toast.makeText(ViewBlood.this,""+diff,Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String blood_group = parent.getItemAtPosition(position).toString();
/*        ListView data=(ListView)findViewById(R.id.lvdata);
        if(blood_group.equals("All")){
            getdatanone(data);
        }else{
            getdatablood(data,blood_group);
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setcurrentdate(){
        final Calendar c = Calendar.getInstance();
        curyear = c.get(Calendar.YEAR);
        curmon = c.get(Calendar.MONTH)+1;
        curday = c.get(Calendar.DAY_OF_MONTH);
//        Toast.makeText(ViewBlood.this,""+curday+" "+curmon+" "+curyear,Toast.LENGTH_SHORT).show();
        totdays=curyear*365+curmon*30+curday;
    }
    private void getdatanone(ListView data){

/*        sqldb table = new sqldb(this);
        table.open();
        Cursor c=table.readAll();
        c.moveToFirst();
        String[] columns = new String[] {table.KEY_NAME,table.KEY_BG};
        int[] to = new int[]{R.id.set_name,R.id.set_bg};
        adapter = new SimpleCursorAdapter(ViewBlood.this,R.layout.listviewlayout,c,columns,to,0);
        data.setAdapter(adapter);

        table.close();*/
    }

    private void getdatablood(ListView data,String blood_group){

/*        sqldb table = new sqldb(this);
        table.open();
        Cursor c=table.readBlood(blood_group);
        c.moveToFirst();
        String[] columns = new String[] {table.KEY_NAME,table.KEY_BG};
        int[] to = new int[]{R.id.set_name,R.id.set_bg};
        adapter = new SimpleCursorAdapter(ViewBlood.this,R.layout.listviewlayout,c,columns,to,0);
        data.setAdapter(adapter);

        table.close();*/
    }

    private void getdataBranch(ListView data,String branch){

/*        sqldb table = new sqldb(this);
        table.open();
        Cursor c=table.readBranch(branch);
        c.moveToFirst();
        String[] columns = new String[] {table.KEY_NAME,table.KEY_BG};
        int[] to = new int[]{R.id.set_name,R.id.set_bg};
        adapter = new SimpleCursorAdapter(ViewBlood.this,R.layout.listviewlayout,c,columns,to,0);
        data.setAdapter(adapter);

        table.close();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewblood_action, menu);
        spinner_blood=(Spinner)menu.findItem(R.id.blood_spinner2).getActionView();
        spinner_blood.setVisibility(View.INVISIBLE);
        spinner_blood.setAdapter(blood_adapter);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setQueryHint("Branch");
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                ListView data=(ListView)findViewById(R.id.lvdata);
                getdataBranch(data,newText);
                spinner_blood.setSelection(0);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
//                Toast.makeText(ViewBlood.this,query,Toast.LENGTH_SHORT).show();
                ListView data=(ListView)findViewById(R.id.lvdata);
                getdataBranch(data,query);
                spinner_blood.setSelection(0);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        spinner_blood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String blood_group = parent.getItemAtPosition(position).toString();
                ListView data=(ListView)findViewById(R.id.lvdata);
                if(blood_group.equals("All")){
                    getdatanone(data);
                }else{
                    getdatablood(data,blood_group);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent setting = new Intent(ViewBlood.this,settings.class);
            startActivity(setting);
            return true;
        }
        if (id==R.id.action_search){

        }
        return super.onOptionsItemSelected(item);
    }


}
