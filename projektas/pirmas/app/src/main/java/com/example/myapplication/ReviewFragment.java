package com.example.myapplication;

import static com.example.myapplication.ProfileFragment.dpToPx;

import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.FragmentReviewBinding;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
// TODO: make reviews actually functional!
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ActivityMainBinding viewModel;
    private FragmentReviewBinding binding;
    private ConnectioHelper myDb;
    private List<entry> entries;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        LoadReviews();
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void LoadReviews() {
        LinearLayout linearLayout = binding.reviewFrame.findViewById(R.id.reviewLayout);
        linearLayout.removeAllViews(); // Clear existing entries

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        getAllData();
        for (entry en : entries) {
            long reviewDate = en.getDate().getTime(); // duoda komentaro laiką (ms)
            long currentTime = System.currentTimeMillis(); // duoda dabarties laiką (ms)
            //  LatLng temp = new LatLng(en.getLoc().latitude, en.getLoc().longitude);
            //double distance = SphericalUtil.computeDistanceBetween(curr, temp);
            if (currentTime - reviewDate < 86400000) { // only for the same user will show his entiries
                View entryView = inflater.inflate(R.layout.entiry, linearLayout, false);
                InternalMethods.setBorderBackground(entryView,  Color.WHITE, Color.BLACK, dpToPx(8), dpToPx(2) );
                TextView txtName = entryView.findViewById(R.id.txtName);
                TextView txtDate = entryView.findViewById(R.id.txtDate);
                TextView txtText = entryView.findViewById(R.id.txtText);

                // Set data for each entry
                txtName.setText(en.getId());
                txtDate.setText(en.getDate().toString());
                txtText.setText(en.getText());

                // Add the inflated entry layout to the LinearLayout
                linearLayout.addView(entryView);

            }
        }
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
}