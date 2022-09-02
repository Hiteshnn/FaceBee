package com.example.facebee.Models;

public class Classes2 {
    private String class_name,class_number;

    public Classes2(String class_name,String number){
        this.class_name=class_name;
        this.class_number=number;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }
}
