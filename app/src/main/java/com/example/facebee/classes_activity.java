package com.example.facebee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facebee.Adapters.classes_adapter;
import com.example.facebee.Models.Classes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class classes_activity extends AppCompatActivity{
    private ArrayList<Classes> class_list;
    private RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    private Button give_attendance,present,absent;
    private EditText entered_code;
    int no_of_classes;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        getSupportActionBar().setTitle("My Classes");
        class_list=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_classes);
        Intent i=getIntent();
        String username = i.getStringExtra("username");
        String group_id= i.getStringExtra("group_id");
        firestore=FirebaseFirestore.getInstance();
        give_attendance=(Button)findViewById(R.id.take_attendance);
        classes_adapter adapter=new classes_adapter(class_list);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Classes c=new Classes("bjncbsdj","fnjsd","vnbsdj");
        class_list.add(c);
        adapter.notifyDataSetChanged();

        firestore.collection("groups").document(group_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                no_of_classes=(int)documentSnapshot.get("no_of_classes");
                Toast.makeText(getApplicationContext(),String.valueOf(no_of_classes), Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        /*for(int j=1;j<=no_of_classes;j++){
            final String[] name = new String[1];
            final String[] number = new String[1];
            final String[] attendance = new String[1];
            firestore.collection("groups").document(group_id).collection("classes").document(String.valueOf(j)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    name[0] = (String) documentSnapshot.get("class_name");
                    number[0] =(String) documentSnapshot.get("class_number");
                }
            });
            firestore.collection("groups").document(group_id).collection("students").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    attendance[0] =(String)documentSnapshot.get(number[0]);
                }
            });
            Classes classes=new Classes(name[0],attendance[0], number[0]);
            class_list.add(classes);
            adapter.notifyDataSetChanged();
        }*/



        give_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),String.valueOf(no_of_classes), Toast.LENGTH_LONG).show();
                dialog = new Dialog(classes_activity.this);
                dialog.setContentView(R.layout.custom_dialog2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                entered_code = (EditText) dialog.findViewById(R.id.enter_code);
                present = (Button) dialog.findViewById(R.id.present);
                absent = (Button) dialog.findViewById(R.id.absent);
                dialog.show();

                present.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                absent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });




    }
}