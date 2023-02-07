package com.bagushikano.sikedatmobile.activity.cacahkrama;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipilDetailResponse;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CacahKramaMipilDetailActivity extends AppCompatActivity {

    LinearLayout kramaImageLoadingContainer, kramaLoadingContainer, kramaFailedContainer;
    SwipeRefreshLayout kramaContainer;
    TextView kramaNameText, kramaAlamatText, kramaNotlpText, kramaJkText, kramaGoldarText, kramaAgamaText,
            kramaTglLahirText, kramaTempatLahirText, kramaPekerjaanText, kramaPendidikanText,
            kramaJenisKependudukan, kramaBanjarAdat, kramaBanjarDinas, kramaNikText, kramaNoIndukText,
            kramaNoMipilText;
    ImageView kramaImage;
    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;
    Button kramaRefreshButton;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cacah_krama_mipil_detail);


        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

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
        kramaJenisKependudukan = findViewById(R.id.krama_jenis_kependudukan_text);
        kramaBanjarAdat = findViewById(R.id.krama_banjar_adat_text);
        kramaBanjarDinas = findViewById(R.id.krama_banjar_dinas_text);
        kramaNikText = findViewById(R.id.krama_nik_text);
        kramaNoMipilText = findViewById(R.id.krama_no_krama_mipil_text);
        kramaNoIndukText = findViewById(R.id.krama_no_induk_text);
        kramaImage = findViewById(R.id.krama_image);

        kramaContainer = findViewById(R.id.krama_container);
        kramaRefreshButton = findViewById(R.id.krama_refresh_button);

        kramaRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
            }
        });

        kramaContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
            }
        });
        getData(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
    }

    public void getData(String token, int id) {
        setLoadingContainerVisible();
        kramaContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<CacahKramaMipilDetailResponse> kramaMipilDetailResponseCall = getData.getCacahKramaMipilDetail("Bearer " + token, id);
        kramaMipilDetailResponseCall.enqueue(new Callback<CacahKramaMipilDetailResponse>() {
            @Override
            public void onResponse(Call<CacahKramaMipilDetailResponse> call, Response<CacahKramaMipilDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    String namaFormated = response.body().getCacahKramaMipil().getPenduduk().getNama();
                    if (response.body().getCacahKramaMipil().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                response.body().getCacahKramaMipil().getPenduduk().getGelarDepan(),
                                response.body().getCacahKramaMipil().getPenduduk().getNama()
                        );
                    }
                    if (response.body().getCacahKramaMipil().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                response.body().getCacahKramaMipil().getPenduduk().getGelarBelakang()
                        );
                    }
                    kramaNameText.setText(namaFormated);
                    kramaAlamatText.setText(response.body().getCacahKramaMipil().getPenduduk().getAlamat());
                    kramaJkText.setText(response.body().getCacahKramaMipil().getPenduduk().getJenisKelamin());
                    kramaAgamaText.setText(response.body().getCacahKramaMipil().getPenduduk().getAgama());
                    kramaTglLahirText.setText(ChangeDateTimeFormat.changeDateFormat(response.body().getCacahKramaMipil().getPenduduk().getTanggalLahir()));
                    kramaTempatLahirText.setText(response.body().getCacahKramaMipil().getPenduduk().getTempatLahir());
                    kramaPendidikanText.setText(response.body().getCacahKramaMipil().getPenduduk().getPendidikan().getJenjangPendidikan());
                    kramaPekerjaanText.setText(response.body().getCacahKramaMipil().getPenduduk().getPekerjaan().getProfesi());
                    kramaBanjarAdat.setText(response.body().getCacahKramaMipil().getBanjarAdat().getNamaBanjarAdat());
                    if (!(response.body().getCacahKramaMipil().getJenisKependudukan().toString().equals("adat"))) {
                        kramaBanjarDinas.setText(response.body().getCacahKramaMipil().getBanjarDinas().getNamaBanjarDinas());
                    } else {
                        kramaBanjarDinas.setText("-");
                    }
                    kramaNikText.setText(response.body().getCacahKramaMipil().getPenduduk().getNik());
                    kramaNoIndukText.setText(response.body().getCacahKramaMipil().getPenduduk().getNomorIndukCacahKrama());
                    kramaNoMipilText.setText(response.body().getCacahKramaMipil().getNomorCacahKramaMipil());
                    kramaJenisKependudukan.setText(response.body().getCacahKramaMipil().getJenisKependudukan().replace("_", " "));
                    if (response.body().getCacahKramaMipil().getPenduduk().getFoto() != null) {
                        kramaImageLoadingContainer.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(getResources().getString(R.string.image_endpoint) + response.body().getCacahKramaMipil().getPenduduk().getFoto())
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
                    }
                    else {
                        Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaImage);
                        kramaImage.setVisibility(View.VISIBLE);
                    }
                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kramaContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<CacahKramaMipilDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        kramaLoadingContainer.setVisibility(GONE);
        kramaFailedContainer.setVisibility(View.VISIBLE);
        kramaContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        kramaLoadingContainer.setVisibility(View.VISIBLE);
        kramaFailedContainer.setVisibility(GONE);
        kramaContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        kramaLoadingContainer.setVisibility(GONE);
        kramaFailedContainer.setVisibility(GONE);
        kramaContainer.setVisibility(View.VISIBLE);
    }
}