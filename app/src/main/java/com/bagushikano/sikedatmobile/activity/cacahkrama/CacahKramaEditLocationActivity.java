package com.bagushikano.sikedatmobile.activity.cacahkrama;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adevinta.leku.LocationPickerActivity;
import com.bagushikano.sikedatmobile.R;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class CacahKramaEditLocationActivity extends AppCompatActivity {

    TextView latText, lngText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cacah_krama_edit_location);

        latText = findViewById(R.id.test_map_lat);
        lngText = findViewById(R.id.test_map_lng);

        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withLocation(-8.367834, 115.204572)
                .withGeolocApiKey("AIzaSyD8j5baLZrBNK3XnvuqAapOU8-OoayhJOo")
                .withSearchZone("id_ID")
                .shouldReturnOkOnBackPressed()
                .withSatelliteViewHidden()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .build(getApplicationContext());

        Button openMapsTestButton = findViewById(R.id.open_maps_button);
        openMapsTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(locationPickerIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1) {
                Double latitude = data.getDoubleExtra("latitude", 0.0D);
                Double longitude = data.getDoubleExtra("longitude", 0.0D);
                latText.setText(String.valueOf(latitude));
                lngText.setText(String.valueOf(longitude));

            }
        }
    }
}