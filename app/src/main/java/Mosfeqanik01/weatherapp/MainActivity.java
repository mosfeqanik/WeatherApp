package Mosfeqanik01.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editext;
    Button WeatherButton;
    TextView resultTextView;

    public void getWeather(View view){
        String location=editext.getText().toString();
        try {
            //URL e Space handling korar jonon
            String encodedCityName = URLEncoder.encode(location, "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=b6907d289e10d714a6e88b30761fae22");
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Couldn't find the Weather Details :(", Toast.LENGTH_SHORT).show();

        }
        //keyboard auto chole jayar jonno
        InputMethodManager mgr =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editext.getWindowToken(),0);
    }
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String Result ="";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                //url,urlConnection,InputStream,InputStreamReader,JSONObject
                url= new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);
                int data = reader.read();
                while(data!= -1){
                    char current= (char)data;
                    Result +=current;
                    data= reader.read();
                }
                return Result;

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Couldn't find the Weather Details :(", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject weatherApi = new JSONObject(s);
                String WeatherInfo =weatherApi.getString("weather");
                JSONArray openWeatherApiArray = new JSONArray(WeatherInfo);
                String message="";
                for(int i=0;i<openWeatherApiArray.length();i++){
                    JSONObject jsonpart = openWeatherApiArray.getJSONObject(i);
                    String main = jsonpart.getString("main");
                    String description = jsonpart.getString("description");

                    if (!main.equals("") && !description.equals("")){
                        message += main +":"+ description + "\r\n";
                    }
                }
                if (!message.equals("")){
                    resultTextView.setText(message);
                }else{
                    Toast.makeText(getApplicationContext(), "Couldn't find the Weather Details :(", Toast.LENGTH_SHORT).show();

                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Couldn't find the Weather Details :(", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editext = findViewById(R.id.CityNameEditText);
        WeatherButton = findViewById(R.id.WeatherButton);
        resultTextView = findViewById(R.id.resultTextView);
    }
}
