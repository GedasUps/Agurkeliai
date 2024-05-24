package com.example.myapplication;


import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements  OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE = 101;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private String mParam1;
    private String mParam2;
    private LatLng originDes;
    private Button navButton;
    private Marker destinationMarker;
    private SharedPreferences sp;
    private SharedPreferences sp1;
    private SharedPreferences.Editor ed;
    private SharedPreferences.Editor ed1;
    private ConnectioHelper myDb;
    private List<entry> entries;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap gMap;
    private SearchView mapSearchView;
    private ScrollView form;
    private LatLng curr;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        }

        myDb = new ConnectioHelper(getActivity());
        entries = new ArrayList<>();
        getAllData();

        sp = getContext().getSharedPreferences("Loc", MODE_PRIVATE);
        sp1 = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        ed = sp.edit();
        ed1 = sp1.edit();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    ed.putString("latitude", String.valueOf(location.getLatitude()));
                    ed.putString("longitude", String.valueOf(location.getLongitude()));
                    ed.apply();
                   // SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mpView);
                   // if (supportMapFragment != null) {
                      //  supportMapFragment.getMapAsync(MapFragment.this);
                   // }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        navButton = view.findViewById(R.id.btnNavigate);
        form = view.findViewById(R.id.layPin);
        form.setVisibility(View.INVISIBLE);
        form.setEnabled(false);
        mapSearchView = view.findViewById(R.id.mapSearch);

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle navigation button click event
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mpView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (addressList != null && !addressList.isEmpty()) {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    gMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Loc", MODE_PRIVATE);
        gMap = googleMap;
        int id = mapSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = mapSearchView.findViewById(id);
        textView.setTextColor(Color.BLACK);
        getLocation();

        double lat = Double.parseDouble(sharedPreferences.getString("latitude", "0"));
        double lng = Double.parseDouble(sharedPreferences.getString("longitude", "0"));
        if (lat > 0 && lng > 0)
            curr = new LatLng(lat, lng);
        else
            curr = null;

        if (curr != null) {
            for (entry en : entries) {
                LatLng temp = new LatLng(en.getLoc().latitude, en.getLoc().longitude);
                long reviewDate = en.getDate().getTime(); // duoda komentaro laiką (ms)
                long currentTime = System.currentTimeMillis(); // duoda dabarties laiką (ms)
                double distance = SphericalUtil.computeDistanceBetween(curr, temp);
                if (distance <= 20000 && (currentTime - reviewDate < 86400000)) // only 20 km radius and less than 86400 seconds ago
                    gMap.addMarker(new MarkerOptions().position(en.getLoc()).title(en.getname()));
            }
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            gMap.setMyLocationEnabled(true);
            gMap.setOnMapClickListener(this);
            gMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
        boolean ok = sharedPreferences.getBoolean("set", false);

        if (destinationMarker != null && !ok) {
            destinationMarker.remove();
        }
        if (ok) {
            ed1.putBoolean("set", false);
            ed1.apply();
        }
        destinationMarker = gMap.addMarker(new MarkerOptions().position(point).title("Your choice").draggable(true));
        originDes = point;
        navButton.setBackgroundColor(getResources().getColor(R.color.blue));
        navButton.setEnabled(true);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        form.setEnabled(true);
        form.setVisibility(View.VISIBLE);
        EditText user = getView().findViewById(R.id.txtUser);
        EditText problem = getView().findViewById(R.id.txtProblem);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
        String userName = sharedPreferences.getString("name", null);
        boolean del = true;

        for (entry en : entries) {
            if (en.getLoc().latitude == marker.getPosition().latitude && en.getLoc().longitude == marker.getPosition().longitude) {
                user.setText(en.getId());
                problem.setText(en.getText());
                del = false;
            } else {
                user.setText(userName);
            }
            if (problem.getText().length() < 0)
                del = true;
            if (del) {
                problem.setText("");
            }
        }

        ConstraintLayout clt = getView().findViewById(R.id.WndThis);
        clt.setVisibility(View.VISIBLE);
        clt.setEnabled(true);

        ed.putString("loc", marker.getPosition().toString());
        ed.apply();
        ed.putString("name", user.getText().toString());
        ed.apply();

        return false;
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
                   // Toast.makeText(getActivity(), "we are parsing wrong data", Toast.LENGTH_SHORT).show();
                }
                en.setDate(date);
                en.setText(cursor.getString(4));
                LatLng loc = new LatLng(cursor.getDouble(5), cursor.getDouble(6));
                en.setMarker(loc);
                entries.add(en);
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}