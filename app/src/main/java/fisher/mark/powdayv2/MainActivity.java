package fisher.mark.powdayv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    //private RecyclerView recyclerView;
    //private RecyclerView.Adapter adapter;
    //private RecyclerView.LayoutManager layoutManager;

    ImageView today_button;
    ImageView five_day_button;
    ImageView webcam_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Button Action
        today_button = (ImageView) findViewById(R.id.button_today);
        today_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, TodayWeather.class);
                startActivity(myIntent);
            }
        });

        // Setup Button Action
        five_day_button = (ImageView) findViewById(R.id.button_fiveday);
        five_day_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, FiveDayWeatherActivity.class);
                startActivity(myIntent);
            }
        });

        // Setup Button Action
        webcam_button = findViewById(R.id.button_webcam);
        webcam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Webcams.class);
                startActivity(myIntent);
            }
        });

        //recyclerView = findViewById(R.id.recycler_view);
        //adapter = new WeatherAdapter(getApplicationContext());
        //layoutManager = new LinearLayoutManager(this);

        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(layoutManager);

    }
}


