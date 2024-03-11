package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new MapFragment());
        
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.map){
                replaceFragment(new MapFragment());
            }
            else if(item.getItemId() == R.id.review){
                replaceFragment(new ReviewFragment());
            }
            else if(item.getItemId() == R.id.profile){
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }
    public void ChangePhoto(View view)
    {
        ImageView profile = findViewById(R.id.imgProfile);

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

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}