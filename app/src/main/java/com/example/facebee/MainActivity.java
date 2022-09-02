package com.example.facebee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText username,password;
    private TextView goto_register,forgot_password;
    private Spinner role;
    private Button login;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firestore=FirebaseFirestore.getInstance();
        getSupportActionBar().setTitle("Login");
        username=(EditText)findViewById(R.id.l_username);
        password=(EditText)findViewById(R.id.l_password);
        goto_register=(TextView)findViewById(R.id.goto_register);
        forgot_password=(TextView)findViewById(R.id.forgot_password);
        role=(Spinner)findViewById(R.id.l_role);
        login=(Button)findViewById(R.id.login_button);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.role,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(adapter);
        role.setOnItemSelectedListener(this);

        username.setText("nnhitesh");
        password.setText("hi");
        goto_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Register_activity.class);
                startActivity(i);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i=new Intent(MainActivity.this,location_activity.class);
                if(username.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Type username",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(MainActivity.this, Forgotpw_activity.class);
                    i.putExtra("Username", username.getText().toString());
                    startActivity(i);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Type username",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(password.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Type password",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                /*task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });*/
                                if(task.isSuccessful()){
                                    QueryDocumentSnapshot document1 = null;
                                    for(QueryDocumentSnapshot document:task.getResult()){
                                        if(username.getText().toString().equals(document.get("Username"))){
                                            document1=document;
                                            break;
                                        }
                                    }
                                    if(document1==null){
                                        Toast.makeText(getApplicationContext(),"Invalid username",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        if(password.getText().toString().equals(document1.get("Password"))){
                                            if(role.getSelectedItem().toString().equals(document1.get("Role"))){
                                                Toast.makeText(getApplicationContext(),"Logged in successful",Toast.LENGTH_SHORT).show();
                                                Intent j=new Intent(MainActivity.this,groups_activity.class);
                                                j.putExtra("id",document1.getId());
                                                j.putExtra("role",role.getSelectedItem().toString());
                                                startActivity(j);
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),"Select the correct role",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Invalid password",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                else{
                                   Toast.makeText(getApplicationContext(),"Unable to connect to server",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
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
}