package com.bagushikano.sikedatmobile.activity.cacahkrama;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.adevinta.leku.LocationPickerActivity;
import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.AuthResponse;
import com.bagushikano.sikedatmobile.model.master.Koordinat;
import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.bagushikano.sikedatmobile.model.profile.ProfileEditResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CacahKramaEditActivity extends AppCompatActivity {

    private TextInputLayout namaForm, gelarDepanForm,
            gelarBelakangForm, namaAliasForm, alamatForm, notelpForm;

    private TextInputEditText namaField, gelarDepanField,
            gelarBelakangField, namaAliasField, alamatField, notelpField;

    private Button selectLocationButton, submitButton;
    private LinearLayout editProfileProgressContainer;
    SharedPreferences loginPreferences;
    private Koordinat koordinat = new Koordinat();
    Gson gson = new Gson();
    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cacah_krama_edit);


        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Penduduk penduduk = gson.fromJson(getIntent().getStringExtra("PENDUDUK"), Penduduk.class);
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        namaForm = findViewById(R.id.krama_cacah_edit_profile_nama_form);
        gelarDepanForm = findViewById(R.id.krama_cacah_edit_profile_gelar_depan_form);
        gelarBelakangForm = findViewById(R.id.krama_cacah_edit_profile_gelar_belakang_form);
        namaAliasForm = findViewById(R.id.krama_cacah_edit_profile_nama_alias_form);
        alamatForm = findViewById(R.id.krama_cacah_edit_profile_alamat_form);
        notelpForm = findViewById(R.id.krama_cacah_edit_profile_notelp_form);

        namaField = findViewById(R.id.krama_cacah_edit_profile_nama_field);
        gelarDepanField = findViewById(R.id.krama_cacah_edit_profile_gelar_depan_field);
        gelarBelakangField = findViewById(R.id.krama_cacah_edit_profile_gelar_belakang_field);
        namaAliasField = findViewById(R.id.krama_cacah_edit_profile_nama_alias_field);
        alamatField = findViewById(R.id.krama_cacah_edit_profile_alamat_field);
        notelpField = findViewById(R.id.krama_cacah_edit_profile_notelp_field);

        namaField.setText(penduduk.getNama());
        if (penduduk.getGelarDepan() != null) {
            gelarDepanField.setText(penduduk.getGelarDepan());
        }

        if (penduduk.getGelarBelakang() != null) {
            gelarBelakangField.setText(penduduk.getGelarBelakang());
        }

        if (penduduk.getNamaAlias() != null) {
            namaAliasField.setText(penduduk.getNamaAlias().toString());
        }

        if (penduduk.getAlamat() != null) {
            alamatField.setText(penduduk.getAlamat());
        }

        if (penduduk.getTelepon() != null) {
            notelpField.setText(penduduk.getTelepon().toString());
        }


        selectLocationButton = findViewById(R.id.krama_cacah_edit_profile_alamat_maps_button);
        editProfileProgressContainer = findViewById(R.id.krama_cacah_edit_profile_progress_layout);
        submitButton = findViewById(R.id.krama_cacah_edit_profile_submit_button);

        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withLocation(-8.367834, 115.204572)
                .withGeolocApiKey("AIzaSyD8j5baLZrBNK3XnvuqAapOU8-OoayhJOo")
                .withSearchZone("id_ID")
                .shouldReturnOkOnBackPressed()
                .withSatelliteViewHidden()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(getApplicationContext());
        locationPickerIntent.putExtra("layouts_to_hide", "plus_code");

        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(locationPickerIntent, 1);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
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
                alamatField.setText(data.getStringExtra("location_address") + ", " + data.getStringExtra("zipcode"));
                koordinat.setLat(String.valueOf(latitude));
                koordinat.setLng(String.valueOf(longitude));
            }
        }
    }

    public void submitData() {
        submitButton.setVisibility(View.GONE);
        editProfileProgressContainer.setVisibility(View.VISIBLE);
        ApiRoute submitData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<ProfileEditResponse> profileEditResponseCall = submitData.profileEdit(
                "Bearer " + loginPreferences.getString("token", "empty"),
                namaField.getText().toString(),
                gelarDepanField.getText().toString(),
                gelarBelakangField.getText().toString(),
                namaAliasField.getText().toString(),
                notelpField.getText().toString(),
                alamatField.getText().toString(),
                gson.toJson(koordinat).toString()

        );
        profileEditResponseCall.enqueue(new Callback<ProfileEditResponse>() {
            @Override
            public void onResponse(Call<ProfileEditResponse> call, Response<ProfileEditResponse> response) {
                submitButton.setVisibility(View.VISIBLE);
                editProfileProgressContainer.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("update profile berhasil")) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Profile berhasil diperbaharui, akan di butuhkan beberapa saat hingga perubahan terlihat pada sistem.", Snackbar.LENGTH_LONG).show();
                }  else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileEditResponse> call, Throwable t) {
                submitButton.setVisibility(View.VISIBLE);
                editProfileProgressContainer.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}