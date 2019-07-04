package edu.training.droidbountyhunter.network;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.training.droidbountyhunter.data.DatabaseBountyHunter;
import edu.training.droidbountyhunter.models.Fugitive;

public class JSONUtils {

    public static boolean parseFugitives(String response, Context context){
        DatabaseBountyHunter database = new DatabaseBountyHunter(context);
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0 ; i < array.length() ; i++){
                JSONObject object = array.getJSONObject(i);
                String nameFugitive = object.optString("name","");
                database.InsertFugitive(new Fugitive(0, nameFugitive,"0"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            return true;
        }
    }

}
