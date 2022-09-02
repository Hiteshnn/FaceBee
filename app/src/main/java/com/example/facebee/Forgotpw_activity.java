package com.example.facebee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Forgotpw_activity extends AppCompatActivity {
    private TextView username;
    private EditText new_password,confirm_password;
    private Button change_password;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpw);
        getSupportActionBar().setTitle("Change password");
        username=(TextView)findViewById(R.id.f_username);
        change_password=(Button)findViewById(R.id.change_password);
        new_password=(EditText)findViewById(R.id.f_password);
        confirm_password=(EditText)findViewById(R.id.f_cpassword);
        firestore=FirebaseFirestore.getInstance();
        Intent i=getIntent();
        String username1=i.getStringExtra("Username");
        username.setText(username1);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(new_password.getText().toString().isEmpty()||confirm_password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Type password",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!(new_password.getText().toString().equals(confirm_password.getText().toString()))){
                        Toast.makeText(getApplicationContext(),"Passwords are not matching",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String id=null;
                                if (task.isSuccessful()) {
                                    QueryDocumentSnapshot document1 = null;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (username.getText().toString().equals(document.get("Username"))) {
                                            id=document.getId();
                                            break;
                                        }
                                    }
                                    if (id==null) {
                                        Toast.makeText(getApplicationContext(), "Invalid username", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        DocumentReference docRef=firestore.collection("users").document(id);
                                        Map<String,Object> map=new HashMap<>();
                                        map.put("Password",new_password.getText().toString());
                                        docRef.set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "Password updation successful", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Password updation failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Intent j=new Intent(Forgotpw_activity.this,MainActivity.class);
                                        startActivity(j);
                                    }

                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Unable to connect to the server", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    }
                }

            }
        });
    }
}