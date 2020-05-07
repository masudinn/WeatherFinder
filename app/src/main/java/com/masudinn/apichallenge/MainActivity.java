package com.masudinn.apichallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText city;
    Button search;
    TextView tvHasil;
    class Weather extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... address) {

            try {
                URL url = new URL(address[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();

                //retrieve data url
                InputStream is = con.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //return 3 string
                int data = isr.read();
                String content = "";
                char ch;
                while(data != -1){
                    ch = (char) data;
                    content = content+ch;
                    data = isr.read();
                }
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void search(View view){
        city = findViewById(R.id.city);
        search = findViewById(R.id.btncari);
        tvHasil = findViewById(R.id.tampil);

        String cname = city.getText().toString();


        String content;
        Weather weather = new Weather();
        //retriev json
        try {
            content = weather.execute("http://api.openweathermap.org/data/2.5/weather?q=" +
                    cname+"&appid=e6f44a4662dc8be8ad620ecd51dae523").get();
            //check data retrieve
            Log.i("contentData",content);
            JSONObject object = new JSONObject(content);
            String weatherdata = object.getString("weather");
            String mainTemp =object.getString("main");
            Log.i("weatherdata",weatherdata);

            //weather data in array
            JSONArray arr = new JSONArray(weatherdata);

            String main = "";
            String description = "";
            String temperature = "";

            for(int i =0; i<arr.length();i++){
                JSONObject weatherpart = arr.getJSONObject(i);
                main = weatherpart.getString("main");
                description = weatherpart.getString("description");
            }

            JSONObject mainpart = new JSONObject(mainTemp);
            temperature = mainpart.getString("temp");
           // visibility=Double.parseDouble(object.getString("visibility"));
           // int visibilityy = (int)visibility/1000;
            Log.i("main",main);
            Log.i("description",description);
            Log.i("temperatur",temperature);

            String result = "Weather       : "+main+"\n" +
                    "Description   : "+description+"\n" +
                    "Temperature : "+temperature+" K\n"
                    ;
            tvHasil.setText(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
