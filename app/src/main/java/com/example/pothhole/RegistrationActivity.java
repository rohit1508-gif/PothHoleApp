package com.example.pothhole;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView textView2;
    EditText editText2,editText3;
    Button button;
    ImageButton imageButton2;
    Spinner spinner2;
    String text;
    int b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textView2 = findViewById(R.id.textView2);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        b=1;
        button = findViewById(R.id.button);
        imageButton2 = findViewById(R.id.imageButton2);
        final Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.districts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        imageButton2.setOnClickListener(new View.OnClickListener() {
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
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i4);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                String name = editText3.getText().toString();
                String password = editText2.getText().toString();
                if(name.isEmpty()){
                    editText3.setError("Please enter Municipality name");
                    editText3.requestFocus();
                }
                else if(password.isEmpty()){
                    editText2.setError("Please enter Password");
                    editText2.requestFocus();
                }
                else if(text.equals("Select District"))
                {Toast.makeText(RegistrationActivity.this,"Please select district",Toast.LENGTH_SHORT).show();}
                else{
                    submit(name,password);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void submit(String name,String password){
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(RegistrationActivity.this));
        String url = "https://pothholeapp.herokuapp.com/users";
        JSONObject o = new JSONObject();
        try{
            o.put("name",name);
            o.put("password",password);
            o.put("district",text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, o, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String token = response.getString("token");
                    SharedPreferences preferences = RegistrationActivity.this.getSharedPreferences("Pothhole", Context.MODE_PRIVATE);
                    preferences.edit().putString("TOKEN",token).apply();}
                catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i4 = new Intent(RegistrationActivity.this,DetailActivity.class);
                startActivity(i4);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error",error.toString());
            }
        });
        requestQueue.add(request);
    }
}
