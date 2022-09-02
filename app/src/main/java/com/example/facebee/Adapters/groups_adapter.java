package com.example.facebee.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebee.Models.Group;
import com.example.facebee.R;
import com.example.facebee.RecyclerViewClickInterface;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class groups_adapter extends RecyclerView.Adapter<groups_adapter.MyViewHolder> {
//public class groups_adapter extends FirebaseRecyclerAdapter
    private String role;
    private ArrayList<Group> group_list;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    public groups_adapter(ArrayList<Group> group_list,String role,RecyclerViewClickInterface recyclerViewClickInterface){
        this.group_list=group_list;
        this.role=role;
        this.recyclerViewClickInterface=recyclerViewClickInterface;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,subject,teacher_or_section;
        public MyViewHolder(final View view) {
            super(view);
            name=view.findViewById(R.id.group_name);
            subject=view.findViewById(R.id.subject);
            teacher_or_section=view.findViewById(R.id.teacher_or_section);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }


    }
    @NonNull
    @Override
    public groups_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.group_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull groups_adapter.MyViewHolder holder, int position) {
        String name,subject,teacher,section;
        name=group_list.get(position).getName();
        subject=group_list.get(position).getSubject();

        holder.name.setText("Name   : "+name);
        holder.subject.setText("Subject : "+subject);
        if(role.equals("Teacher")){
            section=group_list.get(position).getSection();
            holder.teacher_or_section.setText("Section : "+section);
        }
        else {
            teacher=group_list.get(position).getTeacher();
            holder.teacher_or_section.setText("Teacher : " + teacher);
        }
    }


    @Override
    public int getItemCount() {
        return group_list.size();
    }
}
