package com.bagushikano.sikedatmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.auth.LoginActivity;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.PendudukGetResponse;
import com.bagushikano.sikedatmobile.viewmodel.BerandaViewModel;
import com.bagushikano.sikedatmobile.viewmodel.KramaViewModel;
import com.bagushikano.sikedatmobile.viewmodel.ProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {

    BottomNavigationView homeBottomNav;
    SharedPreferences userPreferences, loginPreferences;
    ProfileViewModel profileViewModel;
    KramaViewModel kramaViewModel;
    BerandaViewModel berandaViewModel;
    boolean doubleBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        userPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        homeBottomNav = findViewById(R.id.home_bottom_nav);
        NavController navController = Navigation.findNavController(this, R.id.nav_home_fragment);
        NavigationUI.setupWithNavController(homeBottomNav, navController);

        // handler for snackbar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar snackbar = Snackbar.make(
                        getWindow().getDecorView().findViewById(android.R.id.content),
                        "Selamat datang di SIKRAMAT!",Snackbar.LENGTH_SHORT);
                snackbar.setAnchorView(homeBottomNav);
                snackbar.show();
            }
        }, 1000);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        kramaViewModel = ViewModelProviders.of(this).get(KramaViewModel.class);
        berandaViewModel = ViewModelProviders.of(this).get(BerandaViewModel.class);
        getPenduduk(loginPreferences.getString("token", "empty"));
    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            super.onBackPressed();
            this.finish();
        }
        else {
            this.doubleBack = true;
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),"Tekan sekali lagi untuk keluar",Snackbar.LENGTH_SHORT);
            snackbar.setAnchorView(homeBottomNav);
            snackbar.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBack=false;
                }
            }, 1500);
        }
    }

    public void getPenduduk(String token) {
        ApiRoute getPendudukData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PendudukGetResponse> pendudukGetResponseCall = getPendudukData.getPenduduk("Bearer " + token);
        pendudukGetResponseCall.enqueue(new Callback<PendudukGetResponse>() {
            @Override
            public void onResponse(Call<PendudukGetResponse> call, Response<PendudukGetResponse> response) {
                if (response.code() == 200 && response.body().getStatusCode() == 200
                        && response.body().getMessage().equals("data penduduk sukses")) {

                    String namaFormated = response.body().getPenduduk().getNama();
                    if (response.body().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                response.body().getPenduduk().getGelarDepan(),
                                response.body().getPenduduk().getNama()
                        );
                    }
                    if (response.body().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                response.body().getPenduduk().getGelarBelakang()
                        );
                    }
                    SharedPreferences.Editor userPrefEditor = userPreferences.edit();
                    userPrefEditor.putString("nama_user", response.body().getPenduduk().getNama());
                    userPrefEditor.apply();
                } else if (response.code() == 401) {
                    if (loginPreferences.getInt("login_status", 0) != 0) {
                        SharedPreferences.Editor loginPrefEditor = loginPreferences.edit();

                        loginPrefEditor.putInt("login_status", 0);
                        loginPrefEditor.putString("token", "empty");
                        loginPrefEditor.apply();
                    }
                    Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mainActivity);
                    finishAffinity();
                }
                else {
                    SharedPreferences.Editor userPrefEditor = userPreferences.edit();
                    userPrefEditor.putString("nama_user", null);
                    userPrefEditor.apply();
                }
            }

            @Override
            public void onFailure(Call<PendudukGetResponse> call, Throwable t) {
                SharedPreferences.Editor userPrefEditor = userPreferences.edit();
                userPrefEditor.putString("nama_user", null);
                userPrefEditor.apply();
            }
        });
    }
}