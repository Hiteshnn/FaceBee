package com.example.facebee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class classes_activity extends AppCompatActivity{
    private ArrayList<Classes> class_list;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore firestore;
    private Button give_attendance,present,absent;
    private EditText entered_code;
    private TextView att_display;
    int no_of_classes=0,attended=0;
    String name = null,number,actual_code=null,group_id,username;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        getSupportActionBar().setTitle("My Classes");
        class_list=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_classes);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        Intent i=getIntent();
        att_display=(TextView)findViewById(R.id.att_display);
        username = i.getStringExtra("username");
        group_id= i.getStringExtra("group_id");
        firestore=FirebaseFirestore.getInstance();
        give_attendance=(Button)findViewById(R.id.give_attendance);
        classes_adapter adapter=new classes_adapter(class_list);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //Toast.makeText(getApplicationContext(),group_id, Toast.LENGTH_LONG).show();
        firestore.collection("groups").document(group_id).collection("classes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                    number=documentSnapshot.getId();
                    name=(String)documentSnapshot.get("class_name");

                    Classes classes=new Classes(name,"false", number);
                    class_list.add(classes);

                }
                //Toast.makeText(getApplicationContext(),"Success 1", Toast.LENGTH_LONG).show();
            }
        });

        firestore.collection("groups").document(group_id).collection("students").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                for(Classes c:class_list){
                    String number=c.getClass_number();
                    String t=(String)documentSnapshot.get(number);
                    if(t.equals("true")){
                        c.setAttendance("Present");
                        attended+=1;
                    }
                    else{
                        c.setAttendance("Absent");
                    }
                    no_of_classes+=1;
                    adapter.notifyDataSetChanged();
                }
                if(no_of_classes!=0) {
                    float temp = (float) attended / no_of_classes;
                    att_display.setText("Attendance : " + String.format("%.2f", temp*100) + "%");
                    //Toast.makeText(getApplicationContext(),String.valueOf(temp), Toast.LENGTH_LONG).show();
                }
            }
        });


        give_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("groups").document(group_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String temp=(String)documentSnapshot.get("no_of_classes");
                        no_of_classes=Integer.parseInt(temp);
                        //no_of_classes=(int) documentSnapshot.get("no_of_classes");
                    }
                });
                if(no_of_classes==0){
                    Toast.makeText(getApplicationContext(),"Class is not live at this moment",Toast.LENGTH_SHORT).show();
                }
                else {
                    firestore.collection("groups").document(group_id).collection("classes").document(String.valueOf(no_of_classes)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String open=(String)documentSnapshot.get("window_open");
                            actual_code=(String)documentSnapshot.get("code");
                            if(open.equals("Yes")){

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
                                        //Toast.makeText(getApplicationContext(),entered_code.getText().toString(), Toast.LENGTH_LONG).show();
                                        if (entered_code.getText().toString().equals(actual_code)) {
                                            firestore.collection("groups").document(group_id).collection("students").document(username).update(String.valueOf(no_of_classes), "true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getApplicationContext(), "Attendance updated", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Incorrect code", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });

                                absent.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Class is not live at this moment",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                class_list.clear();
                attended=0;
                no_of_classes=0;
                firestore.collection("groups").document(group_id).collection("classes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                            number=documentSnapshot.getId();
                            name=(String)documentSnapshot.get("class_name");

                            Classes classes=new Classes(name,"false", number);
                            class_list.add(classes);

                        }
                        //Toast.makeText(getApplicationContext(),"Success 1", Toast.LENGTH_LONG).show();
                    }
                });

                firestore.collection("groups").document(group_id).collection("students").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for(Classes c:class_list){
                            String number=c.getClass_number();
                            String t=(String)documentSnapshot.get(number);
                            if(t.equals("true")){
                                c.setAttendance("Present");
                                attended+=1;
                            }
                            else{
                                c.setAttendance("Absent");
                            }
                            no_of_classes+=1;
                            adapter.notifyDataSetChanged();
                        }
                        if(no_of_classes!=0) {
                            float temp = (float) attended / no_of_classes;
                            att_display.setText("Attendance : " + String.format("%.2f", temp*100) + "%");
                            //Toast.makeText(getApplicationContext(),String.valueOf(temp), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}

