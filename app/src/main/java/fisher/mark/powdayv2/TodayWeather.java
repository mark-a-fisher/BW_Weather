package fisher.mark.powdayv2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodayWeather extends AppCompatActivity {

    // Setup TextViews
    private TextView textview_1;
    private TextView textview_2;
    private TextView textview_3;
    private TextView textview_4;
    private TextView textview_5;
    private TextView textview_6;
    private TextView textview_7;
    private TextView textview_0;

    // Setup request queue and lists
    private RequestQueue requestQueue;
    public List<Weather> weatherListToday = new ArrayList<>();

    // Debugg tag
    private static final String TAG = "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_weather);

        // Create a new request queue
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Link textviews to their location in layout
        textview_0 = findViewById(R.id.today_text_1);
        textview_1 = findViewById(R.id.today_text_2);
        textview_2 = findViewById(R.id.today_text_3);
        textview_3 = findViewById(R.id.today_text_4);
        textview_4 = findViewById(R.id.today_text_5);
        textview_5 = findViewById(R.id.today_text_6);
        textview_6 = findViewById(R.id.today_text_7);
        textview_7 = findViewById(R.id.today_text_8);

        loadWeather();

    }

    public void loadWeather () {

        Log.d(TAG, "Entered LoadWeather ");

        String base_url = "https://api.openweathermap.org/data/2.5/forecast?";
        // Lat & Lon of BigWhite
        String lat = "49.72";
        String lon = "-118.93";
        String count = "30";
        String units = "metric";
        String api_key = BuildConfig.API_KEY;

        // Complete API request URL
        String url = base_url + "lat=" + lat + "&lon=" + lon + "&cnt=" + "&units=" + units + "&appid=" + api_key;
        // With count variable
        //String url = base_url + "lat=" + lat + "&lon=" + lon + "&cnt=" + count + "&units=" + units + "&appid=" + api_key;

        // Create new request for JSON Object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Obtain array from JSON
                    JSONArray main_list = response.getJSONArray("list");
                    for (int i = 0; i < main_list.length(); i++) {
                        try {
                            JSONObject item = main_list.getJSONObject(i);

                            // Convert API date & time (UTC) to local date & time
                            String date_time = item.getString("dt_txt");
                            date_time = date_time.replace(" ", "T");
                            date_time = date_time + "Z";
                            Instant instant = Instant.parse(date_time);
                            ZonedDateTime local_date_time = instant.atZone(ZoneId.systemDefault());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
                            String formatted_date_string = local_date_time.format(formatter);

                            // Date Attributes
                            String date = formatted_date_string.substring(0, 11);
                            String time = formatted_date_string.substring(14);

                            // Temperature Attributes
                            JSONObject main = item.getJSONObject("main");
                            String min_temp_string = main.getString("temp_min");
                            String max_temp_string = main.getString("temp_max");

                            // Prepare strings for calculations
                            String min_temp_strip = min_temp_string.replaceAll("[^0-9.-]", "");
                            String max_temp_strip = max_temp_string.replaceAll("[^0-9.-]", "");
                            float min_temp_float = Float.parseFloat(min_temp_strip);
                            float max_temp_float = Float.parseFloat(max_temp_strip);
                            float average_temp = (min_temp_float + max_temp_float) / 2;

                            // Weather Attributes
                            JSONArray weather_array = item.getJSONArray("weather");
                            JSONObject weather_info = weather_array.getJSONObject(0);
                            String weather_type = weather_info.getString("description");
                            // TODO: Make the start of each word a capital letter if possible.


                            // If there is snow forecast
                            try {
                                JSONObject snow = item.getJSONObject("snow");
                                String snow_amount = snow.getString("3h");
                                float snow_amount_float = Float.parseFloat(snow_amount);
                                // Add new Weather data to array
                                weatherListToday.add(new Weather(formatted_date_string, date, time, weather_type, (int) average_temp, snow_amount_float));

                            } // If no snow
                            catch (Exception e) {
                                float snow_amount = 0;
                                // Add new Weather data to array
                                weatherListToday.add(new Weather(formatted_date_string, date, time, weather_type, (int) average_temp, snow_amount));
                            }
                        }
                        catch (JSONException e) { Log.e("mark_debug", "Could not parse JSON list"); }
                    }
                }
                catch (JSONException e) { Log.e("mark_debug", "Json error", e); }
                populate_today_weather ();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { Log.e("mark_debug", "Could not load JSON.", error); }
        });
        // Add request to the queue
        requestQueue.add(request);
    }

    public void populate_today_weather (){

        //TODO Up to 8 potential weather slots (every 3 hours)
        //TODO Needs to show the time, weathertype, average temp & snowfall amount
        //TODO The info is stored in 'weatherList'. Traverse this list, check if the "date" is same as current date.
        //TODO If same date, display the data
        //TODO If NOT same date, ignore

        //First entry in weatherList is from current date
        //Use this date to compare date with other items in list
        Weather today_weather = weatherListToday.get(0);
        String today_date = today_weather.getDate();

        //Counter to keep track of what entry in weatherList is accessed, to then apply it to the correct TextView
        int count = 0;

        for (int i = 0; i < weatherListToday.size(); i++ ){
            //Check if the current iteration is the same date as today
            Weather check_weather = weatherListToday.get(i);
            String check_date = check_weather.getDate();

            //If same date
            if (today_date.equals(check_date)){
                // Set the text views
                Weather current_entry = weatherListToday.get(i);
                String textview_content = "Time: " + current_entry.getTime() + "\nWeather: " + current_entry.getWeather_type() + "\nTemp: " + current_entry.getAverage_temperature() + "\u2103";

                if (count == 0){
                    textview_0.setText(textview_content);
                }
                if (count == 1){
                    textview_1.setText(textview_content);
                }
                if (count == 2){
                    textview_2.setText(textview_content);
                }
                if (count == 3){
                    textview_3.setText(textview_content);
                }
                if (count == 4){
                    textview_4.setText(textview_content);
                }
                if (count == 5){
                    textview_5.setText(textview_content);
                }
                if (count == 6){
                    textview_6.setText(textview_content);
                }
                if (count == 7){
                    textview_7.setText(textview_content);
                }

            }
            count++;

        }


    }
}
