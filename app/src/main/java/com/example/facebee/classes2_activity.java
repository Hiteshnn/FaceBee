package com.example.facebee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.facebee.Adapters.classes2_adapter;
import com.example.facebee.Models.Classes2;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class classes2_activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Classes2> class_list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button take_attendance;
    private FirebaseFirestore firestore;
    String group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes2);
        getSupportActionBar().setTitle("My Classes");
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout2);
        firestore=FirebaseFirestore.getInstance();
        class_list=new ArrayList<>();
        Intent i=getIntent();
        group_id= i.getStringExtra("group_id");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_classes2);
        take_attendance=(Button)findViewById(R.id.give_attendance);

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                class_list.clear();
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
                swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id=item.getItemId();
        if(item_id==R.id.take){
            //Toast.makeText(getApplicationContext(),"Take" , Toast.LENGTH_SHORT).show();
            int length=len(class_list)+1;
            String no_of_classes=String.valueOf(length);
            //Toast.makeText(getApplicationContext(),no_of_classes , Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getApplicationContext(),datetimepicker_activity.class);
            i.putExtra("group_id",group_id);
            i.putExtra("class_number",no_of_classes);
            startActivity(i);
        }
        else if(item_id==R.id.update){
            //Toast.makeText(getApplicationContext(),"Update", Toast.LENGTH_SHORT).show();
            int length=len(class_list);
            String no_of_classes=String.valueOf(length);
            Intent i=new Intent(getApplicationContext(),update_attendance.class);
            i.putExtra("group_id",group_id);
            i.putExtra("classes",no_of_classes);
            startActivity(i);


        }
        else if(item_id==R.id.download){
            //Toast.makeText(getApplicationContext(),"Download", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}