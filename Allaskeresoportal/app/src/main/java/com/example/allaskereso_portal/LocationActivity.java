package com.example.allaskereso_portal;

import static androidx.core.location.LocationManagerCompat.getCurrentLocation;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import android.location.Location;
import android.location.LocationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {
    private static final String TAG = "LocationActivity";
    private TextView locationTextView;
    private LocationManager locationManager;
    private String locationProvider;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Engedély megadva, lekérhetjük a helyzetet
                    getCurrentLocation();
                } else {
                    // Engedély megtagadva, tájékoztathatjuk a felhasználót
                    Toast.makeText(this, "Need permission to locate whereabout!", Toast.LENGTH_LONG).show();
                    locationTextView.setText("Need permission to locate whereabout!");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location);

        locationTextView = findViewById(R.id.locationTextView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.GPS_PROVIDER;
        Log.d(TAG, "Aktuális locationProvider: " + locationProvider);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, " inside if of contextcompat.checkselfpermission");

            getCurrentLocation();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getCurrentLocation() {
        Log.d(TAG, "getCurrentLocation() meghívva");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestSingleUpdate(locationProvider, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
//                        locationTextView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
                        getAddressFromCoordinates(latitude,longitude);
                        locationManager.removeUpdates(this);
                    } else {
                        locationTextView.setText("Couldn't get location!");
                        Log.d(TAG, "Couldn't get location after refresh!.");
                    }
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.d(TAG, "onStatusChanged: " + provider + " - " + status);
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.d(TAG, "onProviderEnabled: " + provider);
                }
                @Override
                public void onProviderDisabled(String provider) {
                    Log.d(TAG, "onProviderDisabled: " + provider);
                }
            }, null);
        } else {
            Log.d(TAG, "Permission not granted!");
            locationTextView.setText("Allow Location Permission!");
        }
    }

    private void getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(latitude, longitude, 1, addressesResult -> {
                    if (addressesResult != null && !addressesResult.isEmpty()) {
                        Address address = addressesResult.get(0);
                        String countryName = address.getCountryName();
                        String cityName = address.getLocality();
                        String subAdminArea = address.getSubAdminArea();
                        String postalCode = address.getPostalCode();
                        String addressLine = address.getAddressLine(0);

                        StringBuilder locationInfo = new StringBuilder();
                        if (cityName != null) {
                            locationInfo.append(cityName).append(", ");
                        } else if (subAdminArea != null) {
                            locationInfo.append(subAdminArea).append(", ");
                        }
                        if (countryName != null) {
                            locationInfo.append(countryName);
                        }

                        locationTextView.setText(locationInfo.toString() + "\nAddress: " + addressLine + ",\n " + postalCode);
                    } else {
                        locationTextView.setText("Couldn't get address with given coordinates!");
                    }
                });
            } else {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    String countryName = address.getCountryName();
                    String cityName = address.getLocality();
                    String subAdminArea = address.getSubAdminArea();
                    String postalCode = address.getPostalCode();
                    String addressLine = address.getAddressLine(0);

                    StringBuilder locationInfo = new StringBuilder();
                    if (cityName != null) {
                        locationInfo.append(cityName).append(", ");
                    } else if (subAdminArea != null) {
                        locationInfo.append(subAdminArea).append(", ");
                    }
                    if (countryName != null) {
                        locationInfo.append(countryName);
                    }

                    locationTextView.setText(locationInfo.toString() + "\nAddress: " + addressLine + ",\n" + postalCode);
                } else {
                    locationTextView.setText("Couldn't get address!");
                }
            }
        } catch (IOException e) {
            locationTextView.setText("Error while getting address!");
            e.printStackTrace();
        }
    }
}