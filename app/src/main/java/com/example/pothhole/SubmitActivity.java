package com.example.pothhole;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class SubmitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView imageView2;
    EditText editText3;
    String number,text;
    Button button3;
    int Pick_image =1;
    Uri imageUri,resultUri;
    String imageUrl="";
    Bitmap bitmap;
    private StorageReference mStorageRef;
    private StorageTask<UploadTask.TaskSnapshot> mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        number = "07r73r3";
//        Intent i1 = getIntent();
//        number= i1.getStringExtra("sender");
        imageView2 = findViewById(R.id.imageView2);
        editText3 = findViewById(R.id.editText3);
        button3 = findViewById(R.id.button3);
        mStorageRef = FirebaseStorage.getInstance().getReference("Users");
        final Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.districts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        if (ContextCompat.checkSelfPermission(SubmitActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SubmitActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100
            );
        }
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editText3.getText().toString();
                String district = text;
                if(location.isEmpty()){
                    editText3.setError("Please enter Email Id");
                    editText3.requestFocus();
                }
                else if(text.equals("Select District"))
                {Toast.makeText(SubmitActivity.this,"Please select district",Toast.LENGTH_SHORT).show();}
                else if(imageUrl.equals("")){
                    Toast.makeText(SubmitActivity.this,"Please upload a picture of Pothhole",Toast.LENGTH_SHORT).show();
                    Log.i("Url","Error");
                }
                else{
                    submit(location,district,imageUrl);
                }
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(gallery,Pick_image);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_image && resultCode == RESULT_OK) {
            assert data != null;
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView2.setImageBitmap(bitmap);
            final StorageReference fileReference = mStorageRef.child(number);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data1 = baos.toByteArray();
            mUploadTask = fileReference.putBytes(data1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    Log.i("Url",imageUrl);
                                }
                            });
                        }
                    });
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void submit(String location,String district,String image){
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(SubmitActivity.this));
        String url = "https://pothholeapp.herokuapp.com/holes";
        JSONObject o = new JSONObject();
        try{
            o.put("image",image);
            o.put("location",location);
            o.put("district",district);
            o.put("sender",number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, o, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(SubmitActivity.this,"Thank You for your Contribution!, we will try to solve " +
                        "it as soon as possilbe",Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(SubmitActivity.this,SubmitActivity.class);
                startActivity(i1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }
}
