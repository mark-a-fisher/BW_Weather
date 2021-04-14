package fisher.mark.powdayv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class FiveDayWeatherActivity extends AppCompatActivity {

    // Create textviews
    private TextView textview_1;
    private TextView textview_2;
    private TextView textview_3;
    private TextView textview_4;
    private TextView textview_5;

    // Setup request queue and lists
    private RequestQueue requestQueue;
    public List<Weather> weatherList = new ArrayList<>();
    public List<WeatherAverage> weatherAverageList = new ArrayList<>();

    // Debugg tag
    private static final String TAG = "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_day_weather2);

        // Create a new request queue
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Link textviews to their location in layout
        textview_1 = findViewById(R.id.five_day_weather_textview1);
        textview_2 = findViewById(R.id.five_day_weather_textview2);
        textview_3 = findViewById(R.id.five_day_weather_textview3);
        textview_4 = findViewById(R.id.five_day_weather_textview4);
        textview_5 = findViewById(R.id.five_day_weather_textview5);

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
                                weatherList.add(new Weather(formatted_date_string, date, time, weather_type, (int) average_temp, snow_amount_float));

                            } // If no snow
                            catch (Exception e) {
                                float snow_amount = 0;
                                // Add new Weather data to array
                                weatherList.add(new Weather(formatted_date_string, date, time, weather_type, (int) average_temp, snow_amount));
                            }
                        }
                        catch (JSONException e) { Log.e("mark_debug", "Could not parse JSON list"); }
                    }
                }
                catch (JSONException e) { Log.e("mark_debug", "Json error", e); }
                populate_weather_average ();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { Log.e("mark_debug", "Could not load JSON.", error); }
        });
        // Add request to the queue
        requestQueue.add(request);
    }

    // Populates weatherAverageList
    public void populate_weather_average () {
        // Setup arrays
        List<Float> temperature_list = new ArrayList<>();
        List<Float> snowfall_list = new ArrayList<>();
        List<String> weather_type_list = new ArrayList<>();
        String current_date = "";

        // Iterate over each entry in weatherList
        for (int i = 0; i < weatherList.size(); i++) {
            Weather current_weather = weatherList.get(i);
            String date_to_check = current_weather.getDate();

            // For the first iteration, set the current_date and add first entry to temperature_list and snowfall_list
            if (current_date.equals("")) {
                current_date = date_to_check;

                // Update temperature_list, snowfall_list and weather_type_list
                snowfall_list.add(current_weather.getSnow_amount());
                temperature_list.add((float) current_weather.getAverage_temperature());
                weather_type_list.add(current_weather.getWeather_type());
                continue;
            }

            // Prep dates for if statements
            String current_date_test = current_date.replaceAll("[^0-9]", "");
            String date_to_check_test = date_to_check.replaceAll("[^0-9]", "");
            int current_date_test_int = Integer.parseInt(current_date_test);
            int date_to_check_test_int = Integer.parseInt(date_to_check_test);
            // Changed to integers; was having trouble comparing strings
            // Therefore can use maths to compare if they are the same date

            // If there is a date match
            if (current_date_test_int - date_to_check_test_int == 0) {

                // Update lists
                snowfall_list.add(current_weather.getSnow_amount());
                temperature_list.add((float) current_weather.getAverage_temperature());
                weather_type_list.add(current_weather.getWeather_type());
            }
            // If the dates do not match, a new date is found
            if (current_date_test_int - date_to_check_test_int != 0) {

                // Total snowfall
                float total_snowfall = 0;
                for (int j = 0; j < snowfall_list.size(); j++) {
                    total_snowfall = total_snowfall + snowfall_list.get(j);
                }
                // Find average of temperature_list
                float total_temp = 0;
                for (int k = 0; k < temperature_list.size(); k++) {
                    total_temp = total_temp + temperature_list.get(k);
                }
                float average_temp = total_temp / temperature_list.size();

                // Sort the weather_type_list, then find the most common string
                Collections.sort(weather_type_list);
                int count = 0;
                int highest_count = 0;
                String current_type = "";
                String highest_type = "";

                for (int l = 0; l < weather_type_list.size(); l++) {
                    // For first iteration
                    if (count == 0) {
                        current_type = weather_type_list.get(l);
                        count++;
                        continue;
                    }
                    // If weather types match increase count
                    if (current_type.equals(weather_type_list.get(l))) {
                        count++;
                    }
                    // If new weather type
                    if (!(current_type.equals(weather_type_list.get(l)))) {
                        // If current count is greater than the highest_count, update
                        if (count > highest_count) {
                            highest_count = count;
                            highest_type = current_type;
                        }
                        // Start count for new weather type
                        count = 1;
                        current_type = weather_type_list.get(l);
                    }
                }

                // Add date, average_temp and total_snowfall to AverageWeatherList
                weatherAverageList.add(new WeatherAverage(current_date, highest_type, (int) average_temp, total_snowfall));

                // Clear temperature_list and snowfall_list ready for new date
                temperature_list.clear();
                snowfall_list.clear();

                // Update current_date
                current_date = date_to_check;

                // Update temperature_list and snowfall_list
                temperature_list.add((float) current_weather.getAverage_temperature());
                snowfall_list.add(current_weather.getSnow_amount());
            }
        }

        // Allocate each item in weatherAverageList to a variable
        // I know there can only be a max of 5 entries, as this is the max given by the API
        WeatherAverage item1 = weatherAverageList.get(0);
        WeatherAverage item2 = weatherAverageList.get(1);
        WeatherAverage item3 = weatherAverageList.get(2);
        WeatherAverage item4 = weatherAverageList.get(3);

        // Setup text for textviews
        String item1_content = "Date: " + item1.getDate() + "\nWeather: " + item1.getWeather_type() + "\nAverage Temperature: " + item1.getAverage_temp() + "\u2103" + "\nTotal Snowfall: " + String.format("%.01f", item1.getTotal_snowfall()) + "cm";
        String item2_content = "Date: " + item2.getDate() + "\nWeather: " + item2.getWeather_type() + "\nAverage Temperature: " + item2.getAverage_temp() + "\u2103" + "\nTotal Snowfall: " + String.format("%.01f", item2.getTotal_snowfall()) + "cm";
        String item3_content = "Date: " + item3.getDate() + "\nWeather: " + item3.getWeather_type() + "\nAverage Temperature: " + item3.getAverage_temp() + "\u2103" + "\nTotal Snowfall: " + String.format("%.01f", item3.getTotal_snowfall()) + "cm";
        String item4_content = "Date: " + item4.getDate() + "\nWeather: " + item4.getWeather_type() + "\nAverage Temperature: " + item4.getAverage_temp() + "\u2103" + "\nTotal Snowfall: " + String.format("%.01f", item4.getTotal_snowfall()) + "cm";

        // Add text to textviews
        textview_1.setText(item1_content);
        textview_2.setText(item2_content);
        textview_3.setText(item3_content);
        textview_4.setText(item4_content);

        // Item 5 in try block, sometimes there is only 4 days worth of data
        try {
            WeatherAverage item5 = weatherAverageList.get(4);
            String item5_content = "Date: " + item5.getDate() + "\nWeather: " + item5.getWeather_type() + "\nAverage Temperature: " + item5.getAverage_temp() + "\u2103" + "\nTotal Snowfall: " + String.format("%.01f", item5.getTotal_snowfall()) + "cm";
            textview_5.setText(item5_content);
        } catch (Exception e) {
        }
    }
}