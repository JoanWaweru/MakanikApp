package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joan.makanikapp.databinding.ActivityDriverMapsBinding;
import com.joan.makanikapp.databinding.ActivityUserMapsBinding;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private ActivityUserMapsBinding binding;
    GoogleApiClient googleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location lastLocation;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private Button call_mechanic;
    private LatLng pick_up_point;
    private int radius = 1;
    private Boolean foundMechanic = false;
    private String foundMechanicID;
    private Boolean requestBol = false;
    private Marker pickupMarker;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        call_mechanic = findViewById(R.id.call_mechanic);


    }



    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull @NotNull Location location) {
        lastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

    }
    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void call_mechanic(View view) {
        if(requestBol){
            requestBol = false;

            geoQuery.removeAllListeners();
            mechaniclocationref.removeEventListener(mechanicLocationListener);

            if (foundMechanicID!=null){
                DatabaseReference mechanicref = FirebaseDatabase.getInstance().getReference().child("mechanic").child(foundMechanicID);
                mechanicref.setValue(true);
                foundMechanicID = null;


            }
            foundMechanic = false;
            radius = 1;


            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customer_request");
            GeoFire geoFire = new GeoFire(reference);

            geoFire.removeLocation(userid);
            if(pickupMarker!= null){
                pickupMarker.remove();
            }
            call_mechanic.setText("Call Mechanic");




        }
        else{
            requestBol = true;
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customer_request");
            GeoFire geoFire = new GeoFire(reference);

            geoFire.setLocation(userid, new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude())
                    , new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (error != null) {
                                System.err.println("There was an error saving the request to GeoFire: " + error);
                            } else {
                                System.out.println("Request saved on server successfully!");
                            }

                        }
                    });
            pick_up_point = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            pickupMarker =mMap.addMarker(new MarkerOptions().position(pick_up_point).title("Pick Up Point"));
            call_mechanic.setText("Getting your Mechanic...");
            getClosestMechanic();

        }

    }
//    List<Marker> markerList = new ArrayList<Marker>();
//    private void mechanicsAround(){
//        DatabaseReference mechanicLocation = FirebaseDatabase.getInstance().getReference().child("mechanicavailable");
//        GeoFire geoFire = new GeoFire(mechanicLocation);
//        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()),10000);
//        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String key, GeoLocation location) {
//                for(Marker marker: markerList){
//                    if(marker.getTag().equals(key)){
//                        return;
//                    }
//                }
//                LatLng mechaniclocation = new LatLng(location.latitude,location.longitude);
//                Marker mechanicmarker = mMap.addMarker(new MarkerOptions().position(mechaniclocation).title("Mechanic"));
//                mechanicmarker.setTag(key);
//                markerList.add(mechanicmarker);
//
//
//
//
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//                for(Marker marker: markerList){
//                    if(marker.getTag().equals(key)){
//                        marker.remove();
//                        markerList.remove(marker);
//                        return;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//                for(Marker marker: markerList){
//                    if(marker.getTag().equals(key)){
//                        marker.setPosition(new LatLng(location.latitude,location.longitude));
//                        return;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        });
//
//
//
//
//    }
    GeoQuery geoQuery;



    private void getClosestMechanic() {
        DatabaseReference mechanicLocation = FirebaseDatabase.getInstance().getReference().child("mechanicavailable");
        GeoFire geoFire = new GeoFire(mechanicLocation);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(pick_up_point.latitude,pick_up_point.longitude),radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!foundMechanic && requestBol){
                    foundMechanic = true;
                    foundMechanicID = key;
                    //Toast.makeText(UserMapsActivity.this,"Mechanic found",Toast.LENGTH_SHORT).show();

                    DatabaseReference mechanicref = FirebaseDatabase.getInstance().getReference().child("mechanic").child(foundMechanicID);
                    String customerid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap hashMap = new HashMap();
                    hashMap.put("userID",customerid);
                    mechanicref.updateChildren(hashMap);

                    getMechanicLocation();
                    call_mechanic.setText("Finding Mechanic's Location...");

                }


            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(!foundMechanic && requestBol){
                    radius++;
                    getClosestMechanic();
//                    Toast.makeText(UserMapsActivity.this,"Not Found",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    private DatabaseReference mechaniclocationref;
    private ValueEventListener mechanicLocationListener;

    private Marker driverMarker;
    private void getMechanicLocation() {
        mechaniclocationref= FirebaseDatabase.getInstance().getReference().child("mechanics_working").child(foundMechanicID).child("l");

        mechanicLocationListener = mechaniclocationref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<Object> map = (List<Object>) snapshot.getValue();
                    double locationLat = 0;
                    double locationLong = 0;
                    call_mechanic.setText("Found Mechanic's Location...");
                    if(map.get(0) !=  null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) !=  null){
                        locationLong = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng mechanicLatLang = new LatLng(locationLat, locationLong);
                    if (driverMarker != null)
                    {
                        driverMarker.remove();
                    }
                    Location loc1 = new Location("");
                    loc1.setLatitude(pick_up_point.latitude);
                    loc1.setLongitude(pick_up_point.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(mechanicLatLang.latitude);
                    loc2.setLongitude(mechanicLatLang.longitude);

                    float distance = loc1.distanceTo(loc2);
                    if(distance<100){
                        call_mechanic.setText("Mechanic is Here");

                    }
                    else {
                        call_mechanic.setText("Mechanic Found: "+String.valueOf(distance));

                    }



                    driverMarker = mMap.addMarker(new MarkerOptions().position(mechanicLatLang).title("Your Mechanic"));


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}