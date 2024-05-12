package com.example.myapplication;
import com.google.android.gms.internal.maps.zzx;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import  java.util.*;
public class entry {
    private String id;// user who created
    private String name;// problem type
    private Date date;//creation date
    private String text;//problem description
    private LatLng loc; // location
    public entry(String id, String name, Date date, String text, LatLng loc)
    {
        this.id=id;
        this.name=name;
        this.date=date;
        this.text=text;
        this.loc=loc;
        /*Padarytas pakeitimas*/
    }
    public entry()
    {
        this.id="-1";
        this.name="";
        this.date=null;
        this.text="";
        this.loc=null;
    }
    public entry(String id, Date date, String text, LatLng loc) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.loc = loc;
    }

    public String getId() {return id;}
    public String getname() {return name;}
    public Date getDate(){return date;}
    public String getText(){return text;}
    public LatLng getLoc(){return loc;}

    public void setId(String id){this.id=id;}
    public void setname(String name){this.name=name;}
    public void setDate(Date date){this.date=date;}
    public void setText(String text){this.text=text;}
    public void setMarker(LatLng loc){this.loc=loc;}


}
