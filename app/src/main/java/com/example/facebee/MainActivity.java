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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
            public void onClick(View view) {
                if(username.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Type username and password",Toast.LENGTH_SHORT).show();
                }
                else{
                    firestore.collection("users").document(username.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc=task.getResult();
                                if(doc.exists()){
                                    if(doc.get("Password").equals(password.getText().toString())){
                                        if(doc.get("Role").equals(role.getSelectedItem().toString())){
                                            //Toast.makeText(getApplicationContext(),"Logged in",Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), groups_activity.class);
                                            i.putExtra("id", username.getText().toString());
                                            i.putExtra("role", role.getSelectedItem().toString());
                                            startActivity(i);
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Select correct role",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Incorrect password",Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"No such username",Toast.LENGTH_SHORT).show();

                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Unable to connect to server",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
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