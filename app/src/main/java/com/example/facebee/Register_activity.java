package com.example.facebee;

import static android.text.TextUtils.isEmpty;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Register_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView goto_login;
    private EditText username,password,cpassword;
    private Spinner role;
    private Button register_button;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference reference;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private Uri image_uri=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        username=(EditText)findViewById(R.id.r_username);
        goto_login=(TextView)findViewById(R.id.goto_login);
        password=(EditText)findViewById(R.id.r_password);
        cpassword=(EditText)findViewById(R.id.r_cpassword);
        register_button=(Button)findViewById(R.id.register_button);
        role=(Spinner)findViewById(R.id.r_role);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.role,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(adapter);
        role.setOnItemSelectedListener(this);
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Bundle bundle=result.getData().getExtras();
                Bitmap bitmap=(Bitmap)bundle.get("data");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, bos);
                byte[] bitmapdata = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

                reference=storage.getReference()
                        .child(username.getText().toString());
                //Toast.makeText(getApplicationContext(),uri.toString(),Toast.LENGTH_SHORT).show();
                reference.putStream(bs).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(getApplicationContext(),"Image uploaded",Toast.LENGTH_SHORT).show();

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image_uri=uri;
                                //Toast.makeText(getApplicationContext(),image_uri.toString(),Toast.LENGTH_SHORT).show();
                                uploadToDatabase();
                            }
                        });


                    }
                });

            }


        });



        if(ContextCompat.checkSelfPermission(Register_activity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Register_activity.this,new String[]{Manifest.permission.CAMERA},101);
        }
        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Register_activity.this,MainActivity.class);
                startActivity(i);
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Type username",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(password.getText().toString().isEmpty()||cpassword.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Type password",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(!(password.getText().toString().equals(cpassword.getText().toString()))){
                            Toast.makeText(getApplicationContext(),"Type the same password twice",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(role.getSelectedItem().toString().equals("Teacher")){
                                uploadToDatabase();
                            }
                            else {
                                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (i.resolveActivity(getPackageManager()) != null) {
                                    activityResultLauncher.launch(i);


                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }



                        }

                    }
                }


            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text=adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void uploadToDatabase(){

            Map<String, Object> users = new HashMap<>();
            users.put("Username", username.getText().toString());
            users.put("Password", password.getText().toString());
            users.put("Role", role.getSelectedItem().toString());
            if(role.getSelectedItem().toString().equals("Student")){
                if(image_uri!=null) {
                    users.put("Image", image_uri.toString());
                }
            }

            //Toast.makeText(getApplicationContext(), "data upload started", Toast.LENGTH_SHORT).show();
            firestore.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(), "Registration as " + username.getText().toString() + " successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Registration as " + username.getText().toString() + " failed", Toast.LENGTH_SHORT).show();
                }
            });
    }

}