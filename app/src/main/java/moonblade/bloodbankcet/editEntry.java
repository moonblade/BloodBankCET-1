package moonblade.bloodbankcet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

import moonblade.bloodbankcet.R;

public class editEntry extends Activity {

    Button beditentry;
    EditText etdbname,etdbbranch,etdbmob,etdbhostel;
    String name;
    Spinner edit_spinner;
    DatePicker edit_picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);
        beditentry = (Button) findViewById(R.id.beeditentry);
        edit_picker=(DatePicker)findViewById(R.id.edatePicker);
        edit_spinner=(Spinner)findViewById(R.id.edit_spinner);
         etdbname = (EditText) findViewById(R.id.etedbname);
        etdbbranch = (EditText) findViewById(R.id.etedbbranch);
      etdbmob = (EditText) findViewById(R.id.etemob);
        etdbhostel = (EditText) findViewById(R.id.etehostel);
        final String[] bloodgroup = new String[1];
        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        String brancha = b.getString("branch");
        Long date_val = b.getLong("date");
        String bg = b.getString("bg");
        final String id = b.getString("id");
        String mob = b.getString("mob");
        String hostel = b.getString("hostel");
        etdbname.setText(name);
        etdbbranch.setText(brancha);
        etdbhostel.setText(hostel);
        etdbmob.setText(mob);
        int position=0;
        if (bg.equals("A+")) {
              position=0;
        } else if (bg.equals("A-")) {
            position=1;
        } else if (bg.equals("B+")) {
            position=2;
        } else if (bg.equals("B-")) {
            position=3;
        } else if (bg.equals("AB+")) {
            position=4;
        }
        else if(bg.equals("AB-")) {
            position=5;
         }
        else if(bg.equals("O+"))
        {position=6;
            }
        else if(bg.equals("O-")){
            position=7;
            }
        edit_spinner.setSelection(position);
        edit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodgroup[0]=edit_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Date edit_date=new Date(date_val);
        int year=edit_date.getYear();
        int month=edit_date.getMonth();
        int day=edit_date.getDate();
        edit_picker.updateDate(year,month,day);
        beditentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int day = edit_picker.getDayOfMonth();
                final int month = edit_picker.getMonth();
                final int year = edit_picker.getYear();
                final Date date = new Date(year,month,day);

                final long millisec=date.getTime();
                String namea=etdbname.getText().toString();
                String branc=etdbbranch.getText().toString();
                String mob=etdbmob.getText().toString();
                String hostel=etdbhostel.getText().toString();
                String bg=bloodgroup[0];
                Long long_date=edit_date.getTime();
                sqldb add = new sqldb(editEntry.this);
                add.open();

                if(name.isEmpty()||branc.isEmpty()||mob.isEmpty()||hostel.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill the Entries", Toast.LENGTH_SHORT).show();
                }else if(mob.length()<10) {
                    Toast.makeText(getApplicationContext(), "Not a valid phone number", Toast.LENGTH_SHORT).show();
                }else{
                        add.updateData(Long.parseLong(id), namea, bg, branc, mob, hostel,millisec);
                        add.close();
                        Toast.makeText(getApplicationContext(), "Edit Successfull", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }


        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_entry, menu);
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
