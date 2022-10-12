package com.example.facebee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class update_attendance extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner attendance;
    private TextView student,class_no;
    private FirebaseFirestore firestore;
    private EditText search_for_dialog;
    private ListView listView;
    ArrayList<String> arrayList1,arrayList2;
    Dialog dialog;
    String group_id,no_of_classes;
    private Button update_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Update attendance");
        setContentView(R.layout.activity_update_attendance);
        firestore=FirebaseFirestore.getInstance();
        student=(TextView)findViewById(R.id.student);
        class_no=(TextView)findViewById(R.id.class_no);
        attendance=(Spinner)findViewById(R.id.attendance_spinner);
        update_button=(Button)findViewById(R.id.update_button);
        Intent i=getIntent();
        group_id= i.getStringExtra("group_id");
        no_of_classes=i.getStringExtra("classes");
        arrayList1=new ArrayList<>();
        arrayList2=new ArrayList<>();

        ArrayAdapter<CharSequence> adapter3=ArrayAdapter.createFromResource(this,R.array.attendance,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attendance.setAdapter(adapter3);
        attendance.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        firestore.collection("groups").document(group_id).collection("students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String id;
                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    id=queryDocumentSnapshot.getId();
                    arrayList1.add(id);
                }
            }
        });
        int temp=Integer.parseInt(no_of_classes);
        for(int k=1;k<=temp;k++){
            arrayList2.add(String.valueOf(k));
        }

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(update_attendance.this);
                dialog.setContentView(R.layout.dialog_spinner);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.show();

                search_for_dialog=(EditText) dialog.findViewById(R.id.search_for_dialog);
                listView=(ListView) dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter1=new ArrayAdapter<>(update_attendance.this,
                        android.R.layout.simple_list_item_1,arrayList1);
                listView.setAdapter(adapter1);

                search_for_dialog.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter1.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        student.setText(adapter1.getItem(i));
                        dialog.dismiss();
                    }
                });
            }
        });

        class_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(update_attendance.this);
                dialog.setContentView(R.layout.dialog_spinner);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.show();

                search_for_dialog=(EditText) dialog.findViewById(R.id.search_for_dialog);
                listView=(ListView) dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter2=new ArrayAdapter<>(update_attendance.this,
                        android.R.layout.simple_list_item_1,arrayList2);
                listView.setAdapter(adapter2);

                search_for_dialog.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter2.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        class_no.setText(adapter2.getItem(i));
                        dialog.dismiss();
                    }
                });
            }
        });



        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp;
                if(attendance.getSelectedItem().toString().equals("Absent")){
                    temp="false";
                }
                else{
                    temp="true";
                }
                firestore.collection("groups").document(group_id).collection("students").document(student.getText().toString()).update(class_no.getText().toString(),temp).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Attendance updated", Toast.LENGTH_SHORT).show();
                    }
                });
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