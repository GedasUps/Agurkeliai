package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;//is it worth to import all library

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import java.net.URI;

public class MainActivity extends AppCompatActivity implements  OnMapReadyCallback  {

    ActivityMainBinding binding;
    boolean creted = false;
    SharedPreferences sp;//=getSharedPreferences("Login",MODE_PRIVATE);//puts data;
    SharedPreferences spGet;//=this.getSharedPreferences("Login",MODE_PRIVATE);//gets data form it;
    SharedPreferences.Editor ed;//=sp.edit();
    String uName;

    private final int FINE_PEMITON_CODE = 1;
    Location currentLocation;
    //FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sp = getSharedPreferences("Login", MODE_PRIVATE);//puts data
        ed = sp.edit();// init local data storing

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
        if (name.getText().toString().length() > 0) {
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
}

