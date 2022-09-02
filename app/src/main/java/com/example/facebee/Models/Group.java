package com.example.facebee.Models;

public class Group {
    String name,subject,teacher,section;

    public Group(String name,String subject,String teacher,String section){
        this.name=name;
        this.subject=subject;
        this.teacher=teacher;
        this.section=section;
    }

    public String getName(){
        return name;
    }

    public String getSubject(){
        return subject;
    }

    public String getTeacher(){
        return teacher;
    }

    public String getSection(){
        return section;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setSubject(String subject){
        this.subject=subject;
    }

    public void setTeacher(String teacher){
        this.teacher=teacher;
    }

    public void setSection(String section){
        this.section=section;
    }



}
