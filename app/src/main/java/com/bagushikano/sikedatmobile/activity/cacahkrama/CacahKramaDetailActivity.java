package com.bagushikano.sikedatmobile.activity.cacahkrama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.profile.EditPasswordActivity;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobile.model.master.Koordinat;
import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class CacahKramaDetailActivity extends AppCompatActivity {
    LinearLayout kramaImageLoadingContainer, kramaLoadingContainer, kramaFailedContainer;
    SwipeRefreshLayout kramaContainer;
    TextView kramaNameText, kramaAlamatText, kramaNotlpText, kramaJkText, kramaGoldarText, kramaAgamaText,
            kramaTglLahirText, kramaTempatLahirText, kramaPekerjaanText, kramaPendidikanText, kramaNikText,
            kramaNoMipilText;
    ImageView kramaImage;
    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;
    Button kramaRefreshButton, kramaEditFotoButton, kramaLocationOpenGoogleMaps, kramaEditProfileButton, kramaEditPasswordButton;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cacah_krama_detail);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        Penduduk penduduk = gson.fromJson(getIntent().getStringExtra("PENDUDUK"), Penduduk.class);

        kramaImageLoadingContainer = findViewById(R.id.krama_image_loading_container);
        kramaLoadingContainer = findViewById(R.id.krama_loading_container);
        kramaFailedContainer = findViewById(R.id.krama_failed_container);

        kramaNameText = findViewById(R.id.krama_nama_text);
        kramaAlamatText = findViewById(R.id.krama_alamat_text);
        kramaNotlpText = findViewById(R.id.krama_notelp_text);
        kramaJkText = findViewById(R.id.krama_jk_text);
        kramaGoldarText = findViewById(R.id.krama_goldar_text);
        kramaAgamaText = findViewById(R.id.krama_agama_text);
        kramaTglLahirText = findViewById(R.id.krama_tgllahir_text);
        kramaTempatLahirText = findViewById(R.id.krama_tempatlahir_text);
        kramaPekerjaanText = findViewById(R.id.krama_pekerjaan_text);
        kramaPendidikanText = findViewById(R.id.krama_pendidikan_text);
        kramaNikText = findViewById(R.id.krama_nik_text);
        kramaImage = findViewById(R.id.krama_image);
        kramaEditFotoButton = findViewById(R.id.krama_change_photo_button);
        kramaLocationOpenGoogleMaps = findViewById(R.id.krama_open_maps_koordinat_button);
        kramaEditProfileButton = findViewById(R.id.krama_edit_profile_button);
        kramaEditPasswordButton = findViewById(R.id.krama_edit_password_button);

        kramaEditPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePasswordIntent = new Intent(getApplicationContext(), EditPasswordActivity.class);
                startActivity(changePasswordIntent);
            }
        });

        kramaContainer = findViewById(R.id.krama_container);
        kramaRefreshButton = findViewById(R.id.krama_refresh_button);

        kramaRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        kramaContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        kramaEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfileIntent = new Intent(getApplicationContext(), CacahKramaEditActivity.class);
                Gson gson = new Gson();
                String kelahiranJSon = gson.toJson(penduduk);
                editProfileIntent.putExtra("PENDUDUK", kelahiranJSon);
                startActivity(editProfileIntent);
            }
        });

        if (penduduk.getKoordinatAlamat() != null) {
            kramaLocationOpenGoogleMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Koordinat koordinat = gson.fromJson(penduduk.getKoordinatAlamat(), Koordinat.class);
                    String url = String.format("https://maps.google.com/?q=%s,%s", koordinat.lat, koordinat.lng);
                    Intent openGmaps = new Intent(Intent.ACTION_VIEW);
                    openGmaps.setData(Uri.parse(url));
                    startActivity(openGmaps);
                }
            });
        }

        kramaEditFotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editFotoIntent = new Intent(getApplicationContext(), CacahKramaEditFotoActivity.class);
                if (penduduk.getFoto() != null) {
                    editFotoIntent.putExtra("FOTO", penduduk.getFoto());
                }
                startActivity(editFotoIntent);
            }
        });

        String namaFormated = penduduk.getNama();
        if (penduduk.getGelarDepan() != null) {
            namaFormated = String.format("%s %s",
                    penduduk.getGelarDepan(),
                    penduduk.getNama()
            );
        }
        if (penduduk.getGelarBelakang() != null) {
            namaFormated = String.format("%s %s",
                    namaFormated,
                    penduduk.getGelarBelakang()
            );
        }
        kramaNameText.setText(namaFormated);
        kramaAlamatText.setText(penduduk.getAlamat());
        kramaJkText.setText(penduduk.getJenisKelamin());
        kramaAgamaText.setText(penduduk.getAgama());
        kramaTglLahirText.setText(ChangeDateTimeFormat.changeDateFormat(penduduk.getTanggalLahir()));
        kramaTempatLahirText.setText(penduduk.getTempatLahir());
        kramaPendidikanText.setText(penduduk.getPendidikan().getJenjangPendidikan());
        kramaPekerjaanText.setText(penduduk.getPekerjaan().getProfesi());
        if (penduduk.getTelepon() != null) {
            kramaNotlpText.setText(penduduk.getTelepon().toString());
        }
        kramaNikText.setText(penduduk.getNik());
        if (penduduk.getFoto() != null) {
            kramaImageLoadingContainer.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(getResources().getString(R.string.image_endpoint) + penduduk.getFoto())
                    .into(kramaImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            kramaImageLoadingContainer.setVisibility(View.GONE);
                            kramaImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            kramaLoadingContainer.setVisibility(View.GONE);
                            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaImage);
                        }
                    });
        } else {
            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaImage);
            kramaImage.setVisibility(View.VISIBLE);
        }
    }
}