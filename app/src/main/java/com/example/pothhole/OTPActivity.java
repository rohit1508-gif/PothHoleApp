package com.example.pothhole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class OTPActivity extends AppCompatActivity {
    EditText editText,editText2;
    Button button,button2;
    String number;
    int random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        button=findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Construct data
                    String apiKey = "apikey=" + "pVK08LFzsAM-pVFXpxUyNT7OvBVoJjcj9IBeqhZkel";
                     random = new Random().nextInt(8998) ;
                    String message = "&message=" + "Hey your OTP is " + random;
                    String sender = "&sender=" + "PothHole";
                    String numbers = "&numbers=" + editText.getText().toString();
                    number = editText.getText().toString();
                    Log.i("Number",number);
                    // Send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
                    String data = apiKey + numbers + message + sender;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    rd.close();
                    Toast.makeText(OTPActivity.this,"OTP has been sent successfully",Toast.LENGTH_SHORT).show();
                   // return stringBuffer.toString();
                } catch (Exception e) {
                    System.out.println("Error SMS "+e);
                    Toast.makeText(OTPActivity.this,"Error sending OTP" + e.toString(),Toast.LENGTH_SHORT).show();
                    //return "Error "+e;
                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(editText2.getText().toString().isEmpty())){
                if(random==Integer.parseInt(editText2.getText().toString())){
                    Intent i1 = new Intent(OTPActivity.this,SubmitActivity.class);
                    i1.putExtra("sender",number);
                    startActivity(i1);
                }
                else{
                    Toast.makeText(OTPActivity.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
                }}
            }
        });
    }
}
