package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.ActivityMainBinding;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
         //   SharedPreferences spGet=getView().this.getSharedPreferences("Login",MODE_PRIVATE);//gets data form it;

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        EditText name = view.findViewById(R.id.edtxtName);
        EditText email = view.findViewById(R.id.edtxtEmail);
        EditText number = view.findViewById(R.id.edtxtNumber);
        ImageView profile = view.findViewById(R.id.imgProfile);
        EditText hist = view.findViewById(R.id.edtxtHistory);

        // Get data from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
        String userName = sharedPreferences.getString("name", null); // Assuming "userName" is the key for the user's name
        String userMail = sharedPreferences.getString("email", null);
        String userNumber = sharedPreferences.getString("number", null);
        String pic = sharedPreferences.getString("profPic", null);

        // Set user data
        name.setText(userName);
        email.setText(userMail);
        number.setText(userNumber);

        // Check if the profile picture is set
        if (pic != null) {
            try {
                Uri uri = Uri.parse(pic);
                profile.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception (e.g., load default profile picture)
                profile.setImageResource(R.drawable.baseline_account_circle_24);
            }
        } else {
            // Load default profile picture if not set
            profile.setImageResource(R.drawable.baseline_account_circle_24);
        }

        return view;
    }


}