package com.example.pothhole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText editText, editText2;
    Button button;
    TextView textView2,textView3;
    ImageButton imageButton3;
    String name,password;
    int b;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText = findViewById(R.id.editText);
        b=1;
        imageButton3 = findViewById(R.id.imageButton3);
        editText2 = findViewById(R.id.editText2);
        textView3= findViewById(R.id.textView3);
        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(i2);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b==1)
                {
                    editText2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    b=0;
                }
                else if(b==0){
                    editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    b=1;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText.getText().toString();
                password = editText2.getText().toString();
                if(name.isEmpty()){
                    editText.setError("Please enter Municipality Name");
                    editText.requestFocus();
                }
                else if(password.isEmpty()){editText2.setError("Please enter Password");
                    editText2.requestFocus();}
                else{
                    login(name,password);
                }
            }
        });
       }
       public void login(String name,String password){
           requestQueue = Volley.newRequestQueue(Objects.requireNonNull(LoginActivity.this));
           String url = "https://pothholeapp.herokuapp.com/users/login";
           JSONObject o = new JSONObject();
           try{
               o.put("name",name);
               o.put("password",password);
           } catch (JSONException e) {
               e.printStackTrace();
           }
           JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, o, new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject response) {
                   try{
                       String token = response.getString("token");
                       SharedPreferences preferences = LoginActivity.this.getSharedPreferences("Pothhole", MODE_PRIVATE);
                       preferences.edit().putString("TOKEN",token).apply();
                   }
                   catch (JSONException e) {
                       e.printStackTrace();
                   }
                   Intent i3 =  new Intent(LoginActivity.this,DetailActivity.class);
                   startActivity(i3);
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {

               }
           });
           requestQueue.add(request);
       }
    }
