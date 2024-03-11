package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;
//import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.*;//is it worth to import all library

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.net.URI;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    ActivityMainBinding binding;
boolean creted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new MapFragment());
        
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.map){
                replaceFragment(new MapFragment());
                if(creted=false)
                {

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mpView);
                    mapFragment.getMapAsync(this);

                    creted=true;
                }

            }
            else if(item.getItemId() == R.id.review){
                replaceFragment(new ReviewFragment());
            }
            else if(item.getItemId() == R.id.profile){
                replaceFragment(new ProfileFragment());

               // getSupportFragmentManager(). inflate(R.layout.fragment_map,binding.getRoot());
            }

            return true;
        });
    }

    //----------------------Profile stuff
    public void ChangePhoto(View view)
    {
        ImageView profile = findViewById(R.id.imgProfile);

        EditText hist = findViewById(R.id.edtxtHistory);
showFileChooser();
        hist.setText(profile.getDrawable().toString());
    }
    static final int code=0;
private void showFileChooser()
{
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("*/*");
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    try {
        startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
               code);
    } catch (android.content.ActivityNotFoundException ex) {
        // Potentially direct the user to the Market with a Dialog
        Toast.makeText(this, "Please install a File Manager.",
                Toast.LENGTH_SHORT).show();
    }
}
    @Override
    protected  void  onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case code:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    ImageView  prof=findViewById(R.id.imgProfile);
                    prof.setImageURI(uri);
                    EditText his = findViewById(R.id.edtxtHistory);
                    his.append( "File Uri: " + uri.toString());// rezulting image path
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    int countt=0;
    public void onBtnChangeClick(View view)
    {
        // gets button stuff
        Button change = findViewById(R.id.btnChangeContacts);

        // gets all edtxt elemts from the scene
        EditText name= findViewById(R.id.edtxtName);
        EditText email= findViewById(R.id.edtxtEmail);
        EditText number= findViewById(R.id.edtxtNumber);
        EditText history= findViewById(R.id.edtxtHistory);
        // lets to change contacts

        if(change.getText().toString().equals("Save")&&countt>0)
        {
            name.setEnabled(false);
            email.setEnabled(false);
            number.setEnabled(false);
            change.setText("Change contacts");
            countt--;
        }
        else
        {
            name.setEnabled(true);
            email.setEnabled(true);
            number.setEnabled(true);
            change.setText("Save");
            countt++;
        }

    }
// -----------------------MAps stuff----------------------
private GoogleMap gMap;
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
    gMap =googleMap;

    LatLng loc = new LatLng(-34,151);
    gMap.addMarker(new MarkerOptions().position(loc).title("Kaunas"));
    gMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}