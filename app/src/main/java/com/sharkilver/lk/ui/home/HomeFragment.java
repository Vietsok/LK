package com.sharkilver.lk.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.sharkilver.lk.R;
import com.sharkilver.lk.databinding.FragmentHomeBinding;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private RecyclerView recyclerView;
    private AdapterItem adapterItem;

    private GoogleMap mMap;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private static final int REQUEST_CODE = 101;
    ArrayList<Double> lngArray;
    ArrayList<Double> latArray;

    private ArrayList<ModelCars> itemArrayList;

    private RequestQueue requestQueue; // file dattente des requetes;

    FusedLocationProviderClient fusedLocationProviderClient;


    public void init(){
        recyclerView = recyclerView.findViewById(R.id.recyclerView); // connection entre le graphisme et les data
        recyclerView.setHasFixedSize(true); // taille

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // associer le nombre de ligne en fonction des entrées

        itemArrayList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getContext());

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        init();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLastLocation();

        /** Google Maps **/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
    }

    private void fetchLastLocation() {

double Destination_Lat = 48.9250858;
        double Destination_Lng =2.4090749;
        LatLng TO_DESTINATION = new LatLng(Destination_Lat,Destination_Lng);


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                Location currentLocation = location;
                Toast.makeText(getContext(), currentLocation.getLatitude()
                        + " " + currentLocation.getLongitude(), Toast.LENGTH_LONG).show();

                LatLng TO_LOCATION = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                parseJSON();

                Log.i("latArray", "latArray: "+latArray.get(0)); ;

                mMap.addMarker(new MarkerOptions().position(TO_LOCATION)
                        .title("Me")
                        .snippet("Départ Lat:"+currentLocation.getLatitude()+" Lng:"+currentLocation.getLongitude()));

                mMap.addMarker(new MarkerOptions().position(TO_DESTINATION)
                        .title("Destination Title")
                        .snippet("Destination Lat:"+Destination_Lat+" Lng:"+Destination_Lng)
                .icon(BitmapFromVector(getContext(), R.drawable.ic_baseline_directions_car_24)));



                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TO_LOCATION, 14));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

                SupportMapFragment supportMapFragment = (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(HomeFragment.this);
            }
        });
    }



    /** change vector -> Bitmap **/

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void parseJSON(){




        String urlJSONFile = "https://pixabay.com/api/?key=24254422-302d91095ecb99f9762286a04&q=nature&image=photo&pretty=true";
        Log.d("TAG", "urlJSONFile: "+urlJSONFile);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlJSONFile, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("hits");

                    Log.d("TAG", "jsonArray: "+jsonArray.length());

                    latArray.clear();

                    for(int i = 0; i < jsonArray.length(); i++){

                        JSONObject hit =  jsonArray.getJSONObject(i);

                        String idcar = hit.getString("idcar");;
                        Double lat= hit.getDouble("lat");;
                        Double lng = hit.getDouble("lng");

                          itemArrayList.add(new ModelCars(idcar, lat, lng));

                    }


                    adapterItem = new AdapterItem(getContext(), itemArrayList); // il atteint un context et un Arraylist

                    // adapter recyclerview à l'adapter
                    recyclerView.setAdapter(adapterItem);

                } catch (JSONException e) {
                    Log.d("TAG", "onResponse: NONONOONONO");
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

//        requestQueue.add(request);


    }




}