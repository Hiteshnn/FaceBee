package com.example.facebee;

import static com.google.firebase.firestore.FieldValue.arrayUnion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import org.w3c.dom.Text;

import java.sql.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class datetimepicker_activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView select_date,select_startTime,select_endTime,date,startTime,endTime,generated_code,timer;
    private Button generate_code;
    private FirebaseFirestore firestore;
    int t1Hour,t1Minute,t2Hour,t2Minute;
    String code,group_id,class_number;
    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetimepicker);
        getSupportActionBar().setTitle("DateTime");
        firestore=FirebaseFirestore.getInstance();
        select_date = (TextView) findViewById(R.id.date_tv);
        select_startTime = (TextView) findViewById(R.id.time_tv1);
        select_endTime = (TextView) findViewById(R.id.time_tv2);
        date = (TextView) findViewById(R.id.tv_date);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        generate_code=(Button)findViewById(R.id.generate_code);
        Intent i=getIntent();
        group_id=i.getStringExtra("group_id");
        class_number=i.getStringExtra("class_number");

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(view);
            }
        });

        select_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(datetimepicker_activity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t1Hour = i;
                                t1Minute = i1;
                                String time = t1Hour + ":" + t1Minute;
                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    startTime.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(t1Hour, t1Minute);
                timePickerDialog.show();
            }
        });

        select_endTime.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(datetimepicker_activity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t2Hour = i;
                                t2Minute = i1;
                                String time = t2Hour + ":" + t2Minute;
                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    endTime.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(t2Hour, t2Minute);
                timePickerDialog.show();
            }

        });

        generate_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),class_number,Toast.LENGTH_SHORT).show();
                code=generate_randomCode();
                String datetime=date.getText().toString()+" "+startTime.getText().toString()+"-"+endTime.getText().toString();
                if(date.getText().toString().isEmpty()||startTime.getText().toString().isEmpty()||endTime.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Select date and time",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(getApplicationContext(),group_id , Toast.LENGTH_SHORT).show();
                    uploadClassDetails(datetime);

                    dialog = new Dialog(datetimepicker_activity.this);
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);

                    generated_code = (TextView) dialog.findViewById(R.id.generated_code);
                    timer = (TextView) dialog.findViewById(R.id.timer);

                    dialog.show();
                    generated_code.setText("Code : " + code);
                    long duration = TimeUnit.MINUTES.toMillis(1);
                    new CountDownTimer(duration, 1000) {

                        @Override
                        public void onTick(long l) {
                            //Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
                            String sDuration = String.format(Locale.ENGLISH, "%02d : %02d"
                                    , TimeUnit.MILLISECONDS.toMinutes(l)
                                    , TimeUnit.MILLISECONDS.toSeconds(l) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                            timer.setText(sDuration);
                        }


                        @Override
                        public void onFinish() {
                            firestore.collection("groups").document(group_id).collection("classes").document(class_number).update("window_open","No").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(),"Window is closed",Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                            //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                        }
                    }.start();



                }
            }
        });
    }

    public void uploadClassDetails(String datetime){

        Map<String,Object> map=new HashMap<>();
        map.put("class_name",datetime);
        map.put("code",code);
        map.put("class_number",class_number);
        map.put("window_open","Yes");

        firestore.collection("groups").document(group_id).collection("classes").document(class_number).set(map);
        firestore.collection("groups").document(group_id).collection("students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    String id=documentSnapshot.getId();
                    Map<String,Object> map=new HashMap<>();
                    map.put(class_number,"false");
                    firestore.collection("groups").document(group_id).collection("students").document(id).update(map);


                }
            }
        });


        firestore.collection("groups").document(group_id).update("no_of_classes",class_number);

    }

    public String generate_randomCode(){
        String str="abcdefghijklmnopqrstuvwxyz123456789@#",req="";
        int upperbound=str.length();
        Random random=new Random();
        for(int i=0;i<6;i++){
            int index=random.nextInt(upperbound);
            req+=str.charAt(index);
        }
        return req;

    }
    public void datePicker(View view){
        DatePickerFragment fragment=new DatePickerFragment();
        fragment.show(getSupportFragmentManager(),"date");
    }

    private void setDate(final Calendar calendar){
        final DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM);
        date.setText(dateFormat.format(calendar.getTime()));

    }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar val=new GregorianCalendar(i,i1,i2);
        setDate(val);
    }

    public static class DatePickerFragment extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c= Calendar.getInstance();
            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)getActivity(),year,month,day);
        }
    }

}