package com.example.facebee.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebee.Models.Classes;
import com.example.facebee.R;

import java.util.ArrayList;

public class classes_adapter extends RecyclerView.Adapter<classes_adapter.MyViewHolder> {
    private ArrayList<Classes> class_list;

    public classes_adapter(ArrayList<Classes> class_list){
        this.class_list=class_list;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,attendance,number;
        public MyViewHolder(final View view) {
            super(view);
            name=view.findViewById(R.id.class_name);
            attendance=view.findViewById(R.id.present_or_absent);
            number=view.findViewById(R.id.class_number);

        }


    }
    @NonNull
    @Override
    public classes_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_items,parent,false);
        return new classes_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull classes_adapter.MyViewHolder holder, int position) {
        String name,attendance,number;
        name=class_list.get(position).getClass_name();
        attendance=class_list.get(position).getAttendance();
        number=class_list.get(position).getClass_number();

        holder.name.setText("Class   : "+name);
        holder.attendance.setText("Attendance : "+attendance);
        holder.number.setText("Class number : "+number);


    }

    @Override
    public int getItemCount() {
        return class_list.size();
    }
}
