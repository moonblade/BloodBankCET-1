package moonblade.bloodbankcet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Nisham on 1/2/2015.
 */
public class DonorAdapter extends ArrayAdapter<Donor>{

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
        // Return the completed view to render on screen
        return convertView;
    }
}
