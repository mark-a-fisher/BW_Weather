package fisher.mark.powdayv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Webcams extends AppCompatActivity {

    // Debugg tag
    private static final String TAG = "Debug";

    // Webcam URL Variables
    String hw33_url;
    String sg_url;
    String cliff_url;
    String hv_url;
    String village_url;
    String gem_top_url;
    String gem_bot_url;
    String park_url;
    String bf_url;
    String pow_url;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webcams_scroll_layout);

        // Code to help execute in the background
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        // In background process
        executor.execute(() -> {

            Grab_img_urls();

        // UI creation process
            handler.post(() -> {
                // Set imageviews
                ImageView village_img = findViewById(R.id.village);
                Glide.with(this).load(village_url).into(village_img);

                ImageView hw33_img = findViewById(R.id.hw33);
                Glide.with(this).load(hw33_url).into(hw33_img);

                ImageView sg_img = findViewById(R.id.sg);
                Glide.with(this).load(sg_url).into(sg_img);

                ImageView bf_img = findViewById(R.id.bf);
                Glide.with(this).load(bf_url).into(bf_img);

                ImageView cliff_img = findViewById(R.id.cliff);
                Glide.with(this).load(cliff_url).into(cliff_img);

                ImageView park_img = findViewById(R.id.park);
                Glide.with(this).load(park_url).into(park_img);

                ImageView gem_bot_img = findViewById(R.id.gem_bot);
                Glide.with(this).load(gem_bot_url).into(gem_bot_img);

                ImageView gem_top_img = findViewById(R.id.gem_top);
                Glide.with(this).load(gem_top_url).into(gem_top_img);

                ImageView hv_img = findViewById(R.id.hv);
                Glide.with(this).load(hv_url).into(hv_img);

                ImageView pow_img = findViewById(R.id.pow);
                Glide.with(this).load(pow_url).into(pow_img);


            });
        });

    }

    public void Grab_img_urls () {

        //URL that contains all the webcam images
        String url = "https://www.bigwhite.com/mountain-conditions/webcams";

        try {
            Log.d(TAG, "Entered try block");
            // Connect to website
            Document doc = Jsoup.connect(url).get();
            Log.d(TAG, "Connection worked.");

            // Select Webcam Image elements
            Elements hw33_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/hwy33]");
            Elements cliff_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/cliff]");
            Elements sg_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/snowghost]");
            Elements hv_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/happyvalley]");
            Elements village_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/village]");
            Elements gem_top_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/gemlake]");
            Elements gem_bot_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/westridge]");
            Elements park_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/teluspark]");
            Elements bf_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/blackforest]");
            Elements pow_ele = doc.select("[href*=https://www.bigwhite.com/sites/default/files/powpow]");
            Log.d(TAG, "Select elements worked");

            // Locate the URLs of images
            hw33_url = hw33_ele.attr("href");
            cliff_url = cliff_ele.attr("href");
            sg_url = sg_ele.attr("href");
            hv_url = hv_ele.attr("href");
            village_url = village_ele.attr("href");
            gem_top_url = gem_top_ele.attr("href");
            gem_bot_url = gem_bot_ele.attr("href");
            park_url = park_ele.attr("href");
            bf_url = bf_ele.attr("href");
            pow_url = pow_ele.attr("href");
            Log.d(TAG, "Grabbed IMG URLs");

        } catch (Exception e){
            Log.d(TAG, "Error parsing URL with Jsoup ");
            e.printStackTrace();
        }




    }


}