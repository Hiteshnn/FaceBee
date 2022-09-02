package com.example.facebee.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebee.Models.Classes;
import com.example.facebee.Models.Classes2;
import com.example.facebee.R;

import java.util.ArrayList;

public class classes2_adapter extends RecyclerView.Adapter<classes2_adapter.MyViewHolder> {
    private ArrayList<Classes2> class_list;

    public classes2_adapter(ArrayList<Classes2> class_list){
        this.class_list=class_list;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,class_number;
        public MyViewHolder(final View view) {
            super(view);
            name=view.findViewById(R.id.class_name2);
            class_number=view.findViewById(R.id.class_number);

        }


    }
    @NonNull
    @Override
    public classes2_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_items2,parent,false);
        return new classes2_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull classes2_adapter.MyViewHolder holder, int position) {
        String name,class_number;
        name=class_list.get(position).getClass_name();
        class_number=class_list.get(position).getClass_number();

        holder.name.setText("Class   : "+name);
        holder.class_number.setText("Number : "+class_number);


    }

    @Override
    public int getItemCount() {
        return class_list.size();
    }
}
