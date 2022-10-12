package com.example.facebee.Models;

public class Classes {
    private String class_name,attendance,class_number;

    public Classes(String class_name, String attendance, String class_number){
        this.class_name=class_name;
        this.attendance=attendance;
        this.class_number=class_number;
    }


    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }
}
