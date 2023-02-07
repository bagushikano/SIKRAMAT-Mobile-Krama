package com.bagushikano.sikedatmobile.activity.mutasi.kematian;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.kematian.KematianDetailResponse;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobile.util.DownloadUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KematianDetailActivity extends AppCompatActivity {


    TextView statusAjuanKematian, tanggalAjuanKematian, kematianTolak,
            noAktaKematian, namaCacahKematian, tanggalKematian, penyebabKematian, noSuketKematian, keteranganKematianText;
    Button showAktaKematianButton, showSuketKematianButton, showDetailCacahMeninggal;


    MaterialCardView tolakAlasanCard;
    private final String KEMATIAN_DETAIL_KEY = "KEMATIAN_DETAIL_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    private final String FLAG_KEMATIAN_DETAIL_KEY = "KEMATIAN";

    LinearLayout loadingContainer, failedContainer, kematianEmptyContainer;
    SwipeRefreshLayout kematianContainer;
    Button refreshKematian;
    SharedPreferences loginPreferences;
    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kematian_detail);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.kematian_loading_container);
        failedContainer = findViewById(R.id.kematian_failed_container);
        kematianContainer = findViewById(R.id.kematian_container);
        refreshKematian = findViewById(R.id.kematian_refresh);

        statusAjuanKematian = findViewById(R.id.kematian_ajuan_status);
        tanggalAjuanKematian = findViewById(R.id.kematian_ajuan_tanggal);
        noAktaKematian = findViewById(R.id.kematian_ajuan_no_akta_kematian);
        noSuketKematian = findViewById(R.id.kematian_ajuan_no_suket_kematian);
        namaCacahKematian = findViewById(R.id.kematian_ajuan_nama_meninggal);
        tanggalKematian = findViewById(R.id.kematian_ajuan_tanggal_kematian);
        penyebabKematian = findViewById(R.id.kematian_ajuan_penyebab_kematian);
        keteranganKematianText = findViewById(R.id.kematian_keterangan);

        showSuketKematianButton = findViewById(R.id.kematian_ajuan_suket_kematian_show);
        showAktaKematianButton = findViewById(R.id.kematian_ajuan_akta_kematian_show);
        showDetailCacahMeninggal = findViewById(R.id.kematian_ajuan_detail_cacah_mipil_button);

        refreshKematian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
            }
        });

        kematianContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
            }
        });
        getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
    }

    public void getDetail(String token, int id) {
        setLoadingContainerVisible();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KematianDetailResponse> kematianDetailResponseCall = getData.getKematianDetail("Bearer " + token, id);
        kematianDetailResponseCall.enqueue(new Callback<KematianDetailResponse>() {
            @Override
            public void onResponse(Call<KematianDetailResponse> call, Response<KematianDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    namaCacahKematian.setText(response.body().getKematian().getCacahKramaMipil().getPenduduk().getNama());
                    if (response.body().getKematian().getNomorAktaKematian() != null) {
                        noAktaKematian.setText(response.body().getKematian().getNomorAktaKematian());
                    }
                    else {
                        noAktaKematian.setText("-");
                    }
                    if (response.body().getKematian().getFileAktaKematian() != null ) {
                        showAktaKematianButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getKematian().getFileAktaKematian());
                                if (download != 0) {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas sedang diunduh", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas gagal diunduh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        showAktaKematianButton.setVisibility(GONE);
                    }

                    if (response.body().getKematian().getKeterangan() != null) {
                        keteranganKematianText.setText(response.body().getKematian().getKeterangan());
                    }

                    if (response.body().getKematian().getNomorSuketKematian() != null) {
                        noSuketKematian.setText(response.body().getKematian().getNomorSuketKematian());
                    }
                    else {
                        noSuketKematian.setText("-");
                    }
                    if (response.body().getKematian().getFileSuketKematian() != null ) {
                        showSuketKematianButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getKematian().getFileSuketKematian());
                                if (download != 0) {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas sedang diunduh", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas gagal diunduh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        showSuketKematianButton.setVisibility(GONE);
                    }

                    tanggalKematian.setText(ChangeDateTimeFormat.changeDateFormat(
                            response.body().getKematian().getTanggalKematian())
                    );

                    tanggalAjuanKematian.setText(ChangeDateTimeFormat.changeDateTimeFormatForCreatedAt(
                            response.body().getKematian().getCreatedAt())
                    );

                    penyebabKematian.setText(response.body().getKematian().getPenyebabKematian());

                    showDetailCacahMeninggal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getKematian().getCacahKramaMipil().getId());
                            startActivity(kramaDetail);
                        }
                    });

                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kematianContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KematianDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        kematianContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        kematianContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        kematianContainer.setVisibility(View.VISIBLE);
    }
}