package com.cluelesstech.mysqlcurdoperation;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InsertActivity extends AppCompatActivity {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    EditText id, name, programmingLanguage;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        id = findViewById(R.id.id);
        name = findViewById(R.id.name);
        programmingLanguage = findViewById(R.id.favLanguage);

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = null;
                InputStream inputStream = null;

                String inputID = id.getText().toString();
                String inputName = name.getText().toString();
                String inputProgLang = programmingLanguage.getText().toString();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("inputID", inputID));
                nameValuePairs.add(new BasicNameValuePair("inputName", inputName));
                nameValuePairs.add(new BasicNameValuePair("inputProgLang", inputProgLang));

                StrictMode.setThreadPolicy(policy);

                try {
                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost("http://192.168.0.182/MySQLCURDOperation/insert.php"); //YOUR PHP SCRIPT ADDRESS
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity httpEntity = response.getEntity();

                    inputStream = httpEntity.getContent();

                    Log.e("log_pass", inputStream.toString());
                    // Toast.makeText(getApplicationContext(), "Record Inserted ", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("log_error", "Error in http connection" + e.toString());
                    Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_LONG).show();
                }

                //convert response to string
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + " \n");
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                    }
                    inputStream.close();

                    result = stringBuilder.toString();
                    Log.e("log_result", result);
                } catch (Exception e) {
                    Log.e("log_Error", "Error converting result" + e.toString());
                }

                try {
                    JSONObject json_data = new JSONObject(result);
                    CharSequence charSequence = (CharSequence) json_data.get("re");
                    Toast.makeText(getApplicationContext(), charSequence, Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "Error parsing data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}