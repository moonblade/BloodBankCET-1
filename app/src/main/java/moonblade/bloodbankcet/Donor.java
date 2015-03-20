package moonblade.bloodbankcet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nisham on 1/2/2015.
 */
public class Donor {
    public int id;
    public String name;
    public String bloodgroup;
    public String branch;
    public String hostel;
    public String mobile;
    public long date;
    public int year;

    public Donor(JSONObject object){
        try {
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.bloodgroup = object.getString("bloodgroup");
            this.branch = object.getString("branch");
            this.hostel = object.getString("hostel");
            this.mobile = object.getString("mobile");
            this.date = Long.parseLong(object.getString("date"));
            this.year = Integer.parseInt(object.getString("year"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Donor> fromJson(JSONArray jsonObjects) {
        ArrayList<Donor> users = new ArrayList<Donor>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new Donor(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }
    public static ArrayList<Donor> fromJsonbg(JSONArray jsonObjects,String bg) {
        ArrayList<Donor> users = new ArrayList<Donor>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                Donor a=new Donor(jsonObjects.getJSONObject(i));
                if (a.bloodgroup.equals(bg))
                    users.add(new Donor(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

public static ArrayList<Donor> fromJsonbranch(JSONArray jsonObjects,String branch) {
        ArrayList<Donor> users = new ArrayList<Donor>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                Donor a=new Donor(jsonObjects.getJSONObject(i));
                if (a.branch.toLowerCase().contains(branch.toLowerCase()))
                    users.add(new Donor(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public Donor(String name,String bloodgroup)
    {
        this.name=name;
        this.bloodgroup=bloodgroup;
    }
    public Donor(String name,String bloodgroup,String branch,String hostel,String mobile,long date,int year )
    {
        this.name=name;
        this.bloodgroup=bloodgroup;
        this.branch=branch;
        this.hostel=hostel;
        this.mobile=mobile;
        this.date=date;
        this.year=year;
    }
}
