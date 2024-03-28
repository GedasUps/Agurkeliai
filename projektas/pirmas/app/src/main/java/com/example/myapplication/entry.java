package com.example.myapplication;
import java.time.DateTimeException;
import  java.util.*;
public class entry {
    private String id;
    private String name;
    private Calendar date;
    private String text;
    public entry(String id, String name, Calendar date, String text)
    {
        this.id=id;
        this.name=name;
        this.date=date;
        this.text=text;
        /*Padarytas pakeitimas*/
    }
    public entry()
    {
        this.id="-1";
        this.name="";
        this.date=null;
        this.text="";
    }



}
