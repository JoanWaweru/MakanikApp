package com.joan.makanikapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joan.makanikapp.databinding.ActivityDriverMapsBinding;
import com.joan.makanikapp.databinding.ActivityUserMapsBinding;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MechanicMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location lastLocation;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private ActivityDriverMapsBinding binding;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private String customerid = "";
    private LinearLayout customerInfo;
    private ImageView mCustomerProfileImage;
    private TextView mcustomerfname,mcustomerlname,mcustomernumber;
    SupportMapFragment mapFragment;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDriverMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        getAssignedCustomer();
        polylines = new ArrayList<>();


        mcustomerfname = findViewById(R.id.customer_fname);
        mcustomerlname = findViewById(R.id.customer_lname);
        mcustomernumber = findViewById(R.id.customer_number);
        customerInfo = (LinearLayout) findViewById(R.id.customer_information);
        mCustomerProfileImage = (ImageView) findViewById(R.id.customer_profile_image);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MechanicMapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_CODE);

        }
        else{
            mapFragment.getMapAsync(this);

        }


    }

    private void getAssignedCustomer() {
        String mechanicid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("mechanic").child(mechanicid).child("userID");
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists() ){

                        customerid = snapshot.getValue().toString();
                        getAssignedCustomerPickUpPoint();
                        getAssignedCustomerInformation();



                }else {
                    erasePolyLines();
                    customerid = "";
                    if (pickUpMarker != null) {
                        pickUpMarker.remove();
                    }
                    if (customerpickuppointlocationrefListener != null) {


                        customerpickuppointlocationref.removeEventListener(customerpickuppointlocationrefListener);


                    }
                    customerInfo.setVisibility(View.GONE);
                    mcustomerfname.setText("");
                    mcustomerlname.setText("");
                    mcustomernumber.setText("");
                    mCustomerProfileImage.setImageResource(R.drawable.ic_view_my_profile_icon);


                }}

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void getAssignedCustomerInformation() {
        customerInfo.setVisibility(View.VISIBLE);
        DatabaseReference customerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(customerid);
        customerDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String,Object>) snapshot.getValue();
                    if(map.get("fname")!=null){

                        mcustomerfname.setText(map.get("fname").toString());
                    }
                    if(map.get("lname")!=null){

                        mcustomerlname.setText(map.get("lname").toString());
                    }
                    if(map.get("phoneno")!=null){

                        mcustomerfname.setText(map.get("phoneno").toString());
                    }
//                    if(map.get("profileImageUrl")!=null){
//                        mProfileImageUrl = map.get("profileImageUrl").toString();
//                        Glide.with(getApplication()).load(mProfileImageUrl).into(mCustomerProfileImage);
//
//                    }
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    Marker pickUpMarker;
    private DatabaseReference customerpickuppointlocationref;
    private ValueEventListener customerpickuppointlocationrefListener;

    private void getAssignedCustomerPickUpPoint() {
        customerpickuppointlocationref = FirebaseDatabase.getInstance().getReference().child("customer_request").child(customerid).child("l");

        customerpickuppointlocationrefListener = customerpickuppointlocationref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists() && !customerid.equals("") ){
                    List<Object> map = (List<Object>) snapshot.getValue();
                    double locationLat = 0;
                    double locationLong = 0;
                    if(map.get(0) !=  null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) !=  null){
                        locationLong = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng userLatLang = new LatLng(locationLat, locationLong);

                    pickUpMarker = mMap.addMarker(new MarkerOptions().position(userLatLang).title("Pick Up Point"));

                    getRouteToUser(userLatLang);



                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void getRouteToUser(LatLng userLatLang) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()), userLatLang)
                .build();
        routing.execute();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MechanicMapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_CODE);

        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);


    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();
        
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference referenceAvailable = FirebaseDatabase.getInstance().getReference("mechanicavailable");

        DatabaseReference referenceWorking = FirebaseDatabase.getInstance().getReference("mechanics_working");
        GeoFire geoFireWorking = new GeoFire(referenceWorking);
        GeoFire geoFireAvailable = new GeoFire(referenceAvailable);



        switch (customerid){
            case "":
                geoFireWorking.removeLocation(userid);
                geoFireAvailable.setLocation(userid, new GeoLocation(location.getLatitude(), location.getLongitude())
                        , new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null) {
                                    System.err.println("There was an error saving the location to GeoFire: " + error);
                                } else {
                                    System.out.println("Location saved on server successfully!");
                                }

                            }
                        });
                break;

            default:
                geoFireAvailable.removeLocation(userid);
                geoFireWorking.setLocation(userid, new GeoLocation(location.getLatitude(), location.getLongitude())
                        , new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null) {
                                    System.err.println("There was an error saving the location to GeoFire: " + error);
                                } else {
                                    System.out.println("Location saved on server successfully!");
                                }

                            }
                        });
                break;
        }







    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MechanicMapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_CODE);

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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    final int PERMISSION_LOCATION_CODE =1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mapFragment.getMapAsync(this);

                }
                else {
                    Toast.makeText(MechanicMapsActivity.this,"Allow Location Permission",Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("mechanicavailable");
        GeoFire geoFire = new GeoFire(reference);
        geoFire.removeLocation(userid);



    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(MechanicMapsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MechanicMapsActivity.this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingCancelled() {

    }
    private void erasePolyLines(){
        for(Polyline line : polylines){
            line.remove();
        }
        polylines.clear();
    }
}


















