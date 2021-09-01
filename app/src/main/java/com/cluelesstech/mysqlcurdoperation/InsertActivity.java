package com.cluelesstech.mysqlcurdoperation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
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
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity httpEntity = response.getEntity();
                    inputStream = httpEntity.getContent();

                    Log.e("log_pass", "Connection Success");
                    Toast.makeText(getApplicationContext(), "Record Inserted ", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("log_error", "Error in http connection" + e.toString());
                    Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}