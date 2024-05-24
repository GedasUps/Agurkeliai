package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ActivityMainBinding viewModel;
    private FragmentProfileBinding binding; // Add this line
    private ActivityResultLauncher<Intent> imagePicLauncher;
    private ConnectioHelper myDb;
    private List<entry> entries;
    Uri selectedImageUri;
    ImageView profile ;
    ScrollView history;
    LatLng curr;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new ConnectioHelper(getActivity());
        entries = new ArrayList<>();
        // Initialize ActivityResultLauncher for image picker
        imagePicLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            InternalMethods.setProfilePic(getContext(), selectedImageUri, profile);
                            // Save the selected image URI to SharedPreferences
                            saveProfilePictureUri(selectedImageUri);
                        }
                    }
                });

        // Load the saved profile picture URI from SharedPreferences
        selectedImageUri = loadProfilePictureUri();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize views
        EditText name = binding.edtxtName;
        EditText email = binding.edtxtEmail;
        EditText number = binding.edtxtNumber;
        profile = binding.imgProfile;
        Button btnPic = binding.btnPhoto;
        LoadMarkers();

        // Set user data
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
        String userName = sharedPreferences.getString("name", null);
        String userMail = sharedPreferences.getString("email", null);
        String userNumber = sharedPreferences.getString("number", null);
        name.setText(userName);
        email.setText(userMail);
        number.setText(userNumber);

        // Set profile picture if it exists
        if (selectedImageUri != null) {

            InternalMethods.setProfilePic(getContext(), selectedImageUri, profile);
        }

        // Set onClickListener for the photo button
        btnPic.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512).createIntent(intent -> {
                imagePicLauncher.launch(intent);
                return null;
            });
        });

        return view;

    }
    private void LoadMarkers() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Loc", MODE_PRIVATE);
        double lat = Double.parseDouble(sharedPreferences.getString("latitude", "0"));
        double lng = Double.parseDouble(sharedPreferences.getString("longitude", "0"));
        if (lat > 0 && lng > 0) {
            curr = new LatLng(lat, lng);
        } else {
            curr = null;
        }

        LinearLayout linearLayout = binding.frHistory.findViewById(R.id.lLinear);
        linearLayout.removeAllViews(); // Clear existing entries

        LayoutInflater inflater = LayoutInflater.from(getActivity());
getAllData();
        for (entry en : entries) {

            LatLng temp = new LatLng(en.getLoc().latitude, en.getLoc().longitude);
            double distance = SphericalUtil.computeDistanceBetween(curr, temp);
            if (distance <= 20000) { // only 20 km radius
                View entryView = inflater.inflate(R.layout.entiry, linearLayout, false);
                InternalMethods.setBorderBackground(entryView,  Color.WHITE, Color.BLACK, dpToPx(8), dpToPx(2) );
                EditText txtName = entryView.findViewById(R.id.txtName);
                EditText txtDate = entryView.findViewById(R.id.txtDate);
                EditText txtText = entryView.findViewById(R.id.txtText);

                // Set data for each entry
                txtName.setText(en.getId());
                txtDate.setText(en.getDate().toString());
                txtText.setText(en.getText());

                // Add the inflated entry layout to the LinearLayout
                linearLayout.addView(entryView);

            }
        }
    }
    public static int dpToPx(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    private void getAllData() {
        Cursor cursor = myDb.ReadAllData();
        if (cursor.getCount() == 0) {
          //  Toast.makeText(getActivity(), "noData", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                entry en = new entry();
                en.setId(cursor.getString(1));
                en.setname(cursor.getString(2));
                String din = cursor.getString(3);
                String pattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                Date date = null;
                try {
                    date = simpleDateFormat.parse(din);
                } catch (Exception ex) {
                 //   Toast.makeText(getActivity(), "we are parsing wrong data", Toast.LENGTH_SHORT).show();
                }
                en.setDate(date);
                en.setText(cursor.getString(4));
                LatLng loc = new LatLng(cursor.getDouble(5), cursor.getDouble(6));
                en.setMarker(loc);
                entries.add(en);
            }
        }
    }
    // Method to save the selected profile picture URI to SharedPreferences
    private void saveProfilePictureUri(Uri uri) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile_picture_uri", uri.toString());
        editor.apply();
    }

    // Method to load the saved profile picture URI from SharedPreferences
    private Uri loadProfilePictureUri() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Profile", MODE_PRIVATE);
        String uriString = sharedPreferences.getString("profile_picture_uri", null);
        if (uriString != null) {
            return Uri.parse(uriString);
        } else {
            return null;
        }
    }
}
