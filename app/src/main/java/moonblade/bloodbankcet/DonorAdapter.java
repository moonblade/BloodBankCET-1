package moonblade.bloodbankcet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Nisham on 1/2/2015.
 */
public class DonorAdapter extends ArrayAdapter<Donor>{

    private int curyear,curmon,curday,totdays;
    public DonorAdapter(Context context, ArrayList<Donor> donors) {
        super(context, R.layout.listviewlayout, donors);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Donor donor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewlayout, parent, false);
        }
        // Lookup view for data population
        TextView set_name = (TextView) convertView.findViewById(R.id.set_name);
        TextView set_bg = (TextView) convertView.findViewById(R.id.set_bg);
        // Populate the data into the template view using the data object
        set_name.setText(donor.name);
        set_bg.setText(donor.bloodgroup);

        ImageView indicator=(ImageView) convertView.findViewById(R.id.green);
        long dateval=donor.date;
        long current=System.currentTimeMillis();

        Date date=new Date(dateval);
        Date cur=new Date();

        int daygiv=date.getDay();
        int mongiv=date.getMonth()+1;
        int yeargiv=date.getYear();
        long givtot=daygiv+mongiv*30+yeargiv*365;

        int daycur=cur.getDay();
        int moncur=cur.getMonth()+1;
        int yearcur=cur.getYear()+1900;
        long curtot=daycur+moncur*30+yearcur*365;

        long difference= curtot-givtot;
        long accepted_diff=30*3;

        if(difference<accepted_diff){
            indicator.setImageResource(R.drawable.red);
        }else{
            indicator.setImageResource(R.drawable.green);
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
