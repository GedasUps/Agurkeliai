package com.example.myapplication;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.threeten.bp.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements  OnMapReadyCallback  {

    ActivityMainBinding binding;
    boolean creted = false;
    SharedPreferences sp;//=getSharedPreferences("Login",MODE_PRIVATE);//puts data;

    SharedPreferences spGet;//=this.getSharedPreferences("Login",MODE_PRIVATE);//gets data form it;
    SharedPreferences.Editor ed;//=sp.edit();

    String uName;
    private List<entry> entries=new ArrayList<>();//Review list
    private final int FINE_PEMITON_CODE = 1;
    Location currentLocation;
    //FusedLocationProviderClient fusedLocationProviderClient;
    //database stuff
private Connection connetion;
private String name,str;
ResultSet rss;
ConnectioHelper connectioHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sp = getSharedPreferences("Login", MODE_PRIVATE);//puts
        // mySql data base tried
        //connectioHelper = new ConnectioHelper();
       // Connect();

        ed = sp.edit();// init local data storing

        AndroidThreeTen.init(this);

        // Initialize the map fragment
        replaceFragment(new MapFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.map) {
                // Do nothing if already on map
                replaceFragment(new MapFragment());
            } else if (item.getItemId() == R.id.review) {
                replaceFragment(new ReviewFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

   //----------------------Map pin sttuff
    private ScrollView form;

    public void onSaveReviewClick(View view)
    {
        EditText dek = findViewById(R.id.txtProblem);
        Button save= findViewById(R.id.btnSave);

        //GetTextFromSql(view);
        SharedPreferences sharedPreferences = getSharedPreferences("Loc", MODE_PRIVATE);
        String markerData = sharedPreferences.getString("loc", null);
        String name=sharedPreferences.getString("name", null);


// uzpildo atsidariusi window
        if (markerData!=null) {


            // Split the markerData string to extract latitude and longitude
            String[] parts = markerData.split("[(),]");
            double latitude = Double.parseDouble(parts[1]);
            double longitude = Double.parseDouble(parts[2]);

            // Create a new LatLng object
            LatLng temp = new LatLng(latitude, longitude);


// Assuming your LocalDateTime object is named localDateTime
            LocalDateTime localDateTime = LocalDateTime.now();

// Create a Calendar instance and set its fields using the LocalDateTime object
            Calendar calendar = Calendar.getInstance();
            calendar.set(localDateTime.getYear(), localDateTime.getMonthValue() - 1, localDateTime.getDayOfMonth(),
                    localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());

// Get the Date object from the Calendar instance
            Date date = calendar.getTime();

            entry review = new entry(name, date,dek.getText().toString(), temp);
            //close info window
            form=findViewById(R.id.layPin);
            form.setVisibility(View.INVISIBLE);
            form.setEnabled(false);
            ed.putBoolean("set",true);
            ed.commit();
            // saving stuff to db
            ConnectioHelper mydb=new ConnectioHelper(this);
            mydb.addMarker(name,"Kelio darbai",new java.sql.Date(date.getTime()),dek.getText().toString(),latitude,longitude);

        }
    }
    public void onCancelReviewClick(View view)
    {
        ed.putBoolean("set",false);
        ed.commit();
        Button cancel= findViewById(R.id.btnCancel);
        form=findViewById(R.id.layPin);
        form.setVisibility(View.INVISIBLE);
        form.setEnabled(false);
    }
    //----------------------Profile stuff

    int countt = 0;
    public void onBtnChangeClick(View view) {
        // gets button stuff
        Button change = findViewById(R.id.btnChangeContacts);

        // gets all edtxt elemts from the scene
        EditText name = findViewById(R.id.edtxtName);
        EditText email = findViewById(R.id.edtxtEmail);
        EditText number = findViewById(R.id.edtxtNumber);
        EditText history = findViewById(R.id.edtxtHistory);
        if (uName != null) {
            name.setText(uName);
        }
        // lets to change contacts

        if (change.getText().toString().equals("Save") && countt > 0) {

            name.setEnabled(false);
            email.setEnabled(false);
            number.setEnabled(false);
            change.setText("Change contacts");
            countt--;
        } else {
            name.setEnabled(true);
            email.setEnabled(true);
            number.setEnabled(true);

            change.setText("Save");
        }
        countt++;
        if (!name.getText().toString().isEmpty()) {
            ed.putString("name", name.getText().toString());
            ed.commit();
            ed.putString("email", email.getText().toString());
            ed.commit();
            ed.putString("number", number.getText().toString());
            ed.commit();
        }

        //  String n = new String(name.getText().toString());

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //gMap = googleMap;
        LatLng loc = new LatLng(-34, 151);
        //gMap.addMarker(new MarkerOptions().position(loc).title("Kaunas"));
        //gMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }

    public void onReviewClick(View view) {
        replaceFragment(new ReviewFragment2());
    }
}

