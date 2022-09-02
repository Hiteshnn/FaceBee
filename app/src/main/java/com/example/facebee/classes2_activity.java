package com.example.facebee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.facebee.Adapters.classes2_adapter;
import com.example.facebee.Adapters.classes_adapter;
import com.example.facebee.Models.Classes;
import com.example.facebee.Models.Classes2;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

public class classes2_activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Classes2> class_list;
    private Button take_attendance;
    private FirebaseFirestore firestore;
    String group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes2);
        getSupportActionBar().setTitle("My Classes");
        firestore=FirebaseFirestore.getInstance();
        class_list=new ArrayList<>();
        Intent i=getIntent();
        group_id= i.getStringExtra("group_id");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_classes2);
        take_attendance=(Button)findViewById(R.id.take_attendance);

        classes2_adapter adapter=new classes2_adapter(class_list);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        firestore.collection("groups").document(group_id).collection("classes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    if (document.exists()) {
                        String name, number;
                        name = (String) document.get("class_name");
                        number = (String) document.get("class_number");
                        Classes2 classes2 = new Classes2(name, number);
                        class_list.add(classes2);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });

        take_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),datetimepicker_activity.class);
                i.putExtra("group_id",group_id);
                i.putExtra("class_number",String.valueOf(len(class_list)+1));
                startActivity(i);

            }
        });

    }
    public int len(ArrayList<Classes2> class_list) {
        Iterator<Classes2> itr = class_list.iterator();
        int count = 0;
        while (itr.hasNext()) {
            itr.next();
            count++;
        }
        return count;
    }

}