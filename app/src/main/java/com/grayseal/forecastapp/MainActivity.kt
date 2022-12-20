package com.grayseal.forecastapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.grayseal.forecastapp.navigation.WeatherNavigation
import com.grayseal.forecastapp.ui.theme.ForecastApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

// For dagger to know that this is where we start using and connecting all the dependencies that the project may have
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }

    override fun onStart() {
        super.onStart()

        // To create an instance of the fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        // Determine whether app was already granted the permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            /* Calling ActivityCompat#requestPermissions to request the missing permissions and 
            then overriding
                public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                         int[] grantResults)
                to handle the case where the user grants the permission */
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
            return
        }

        // To get the current location
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnSuccessListener { location: Location? ->
                // Get location. In some rare situations this can be null.
                if (location != null) {
                    Toast.makeText(
                        this,
                        "Location: lat = ${location.latitude} lon = ${location.longitude}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Cannot get location", Toast.LENGTH_SHORT).show()
            }
    }
}

@Composable
fun WeatherApp() {
    ForecastApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.primary
        ) {
            WeatherNavigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherApp()
}