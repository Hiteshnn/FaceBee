package com.example.facebee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.facebee.Adapters.groups_adapter;
import com.example.facebee.Models.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class groups_activity extends AppCompatActivity implements RecyclerViewClickInterface{
    private ArrayList<Group> group_list;
    private ArrayList<String> group_ids;
    private RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    String username,role;

    //@SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        getSupportActionBar().setTitle("My Groups");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        Intent i=getIntent();
        String id=i.getStringExtra("id");
        role=i.getStringExtra("role");
        firestore=FirebaseFirestore.getInstance();
        group_list=new ArrayList<>();
        group_ids=new ArrayList<>();

        groups_adapter adapter=new groups_adapter(group_list,role,this);
        //adapter.notifyDataSetChanged();
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        firestore.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    group_ids=(ArrayList<String>)documentSnapshot.get("Groups");
                    Collections.sort(group_ids);
                    username=(String)documentSnapshot.get("Username");
                    firestore.collection("groups").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            //Toast.makeText(getApplicationContext(),queryDocumentSnapshots.,Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(),group_ids.toString(),Toast.LENGTH_LONG).show();
                            for(QueryDocumentSnapshot document:queryDocumentSnapshots){

                                String id=document.getId();
                                //Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
                                if(group_ids.contains(id)){
                                    String name, teacher, subject,section;
                                    name = document.get("name").toString();
                                    teacher = document.get("teacher").toString();
                                    subject = document.get("subject").toString();
                                    section=document.get("section").toString();
                                    Group group;
                                    group = new Group(name,subject,teacher,section);

                                    group_list.add(group);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            //Toast.makeText(getApplicationContext(),group_list.toString(),Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }

        });


    }

    @Override
    public void onItemClick(int position) {

        if(role.equals("Student")) {
            Intent i = new Intent(getApplicationContext(), classes_activity.class);
            i.putExtra("username", username);
            i.putExtra("group_id", group_ids.get(position));
            startActivity(i);
        }
        else{
            Intent i = new Intent(getApplicationContext(), classes2_activity.class);
            i.putExtra("group_id", group_ids.get(position));
            //Toast.makeText(getApplicationContext(),group_ids.get(position),Toast.LENGTH_LONG).show();
            startActivity(i);
        }
    }

    @Override
    public void onLongItemClick(int position) {
       // Toast.makeText(getApplicationContext(),group_ids.get(position),Toast.LENGTH_LONG).show();
    }
}