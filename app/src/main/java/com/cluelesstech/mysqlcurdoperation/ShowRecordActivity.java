package com.cluelesstech.mysqlcurdoperation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.NumberFormat;
import java.util.ArrayList;

public class ShowRecordActivity extends AppCompatActivity {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    private EditText inputId;
    private Button submit;
    private TextView name, proLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);

        inputId = findViewById(R.id.editID);
        name = findViewById(R.id.userName);
        proLang = findViewById(R.id.programLang);
        submit = findViewById(R.id.submitRecord);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = null;
                InputStream stream = null;

                String id = inputId.getText().toString();
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", id));
                StrictMode.setThreadPolicy(policy);
                try {
                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost("http://<YOUR_IPADDRESS>/MySQLCURDOperation/showRecord.php"); //YOUR PHP SCRIPT ADDRESS
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity httpEntity = response.getEntity();

                    stream = httpEntity.getContent();

                    Log.e("log_pass", stream.toString());
                } catch (Exception e) {
                    Log.e("log_error", "Error in http connection" + e.toString());
                    Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_LONG).show();
                }

                //convert response to string

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"), 8);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + " \n");
                    }
                    stream.close();

                    result = stringBuilder.toString();
                    Log.e("log_result", result);
                } catch (Exception e) {
                    Log.e("log_Error", "Error converting result" + e.toString());
                }


                //parse json data
                try {
                    JSONObject json_data = new JSONObject(result);
                    String s = json_data.getString("re");
                    if (s.equals("success")) {
                        JSONObject object = json_data.getJSONObject("id");
                        String nm = object.getString("name");
                        long l = object.getLong("proLang");

                        name.setText(nm);
                        String lang = NumberFormat.getInstance().format(l);
                        proLang.setText(lang);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Record not found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error parsing data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}