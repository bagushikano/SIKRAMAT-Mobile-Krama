package com.bagushikano.sikedatmobile.activity.mutasi.kelahiran;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.DocumentViewerActivity;
import com.bagushikano.sikedatmobile.activity.cacahkrama.CacahKramaDetailActivity;
import com.bagushikano.sikedatmobile.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobile.adapter.kelahiran.KelahiranListAdapter;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.kelahiran.Kelahiran;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuanDetailResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuanGetResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranDetailResponse;
import com.bagushikano.sikedatmobile.model.krama.KramaMipilGetResponse;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobile.util.DownloadUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelahiranAjuanDetailActivity extends AppCompatActivity {

    TextView statusAjuanKelahiran, tanggalAjuanKelahiran,
            noAktaKelahiran, namaAnak, tanggalLahirAnak, kelahiranTolakText, kelahiranKeteranganText;
    Button showAktaKelahiranButton, showDetailAnakButton;

    MaterialCardView tolakAlasanCard;


    private final String KELAHIRAN_DETAIL_KEY = "KELAHIRAN_DETAIL_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    private final String FLAG_KELAHIRAN_DETAIL_KEY = "KELAHIRAN";

    LinearLayout loadingContainer, failedContainer, kelahiranEmptyContainer;
    SwipeRefreshLayout kelahiranContainer;
    Button refreshKelahiran;
    SharedPreferences loginPreferences;
    private Toolbar homeToolbar;
    private Button kelahiranResubmitButton;

    private final String KELAHIRAN_RESUBMIT_KEY = "KELAHIRAN_RESUBMIT_DETAIL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelahiran_ajuan_detail);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.kelahiran_loading_container);
        failedContainer = findViewById(R.id.kelahiran_failed_container);
        kelahiranContainer = findViewById(R.id.kelahiran_container);
        refreshKelahiran = findViewById(R.id.kelahiran_refresh);

        statusAjuanKelahiran = findViewById(R.id.kelahiran_ajuan_status);
        tanggalAjuanKelahiran = findViewById(R.id.kelahiran_ajuan_tanggal);
        noAktaKelahiran = findViewById(R.id.kelahiran_ajuan_no_akta_kelahiran);
        namaAnak = findViewById(R.id.kelahiran_ajuan_nama_anak);
        tanggalLahirAnak = findViewById(R.id.kelahiran_ajuan_tanggal_lahir_anak);
        showAktaKelahiranButton = findViewById(R.id.kelahiran_ajuan_akta_kelahiran_show);
        showDetailAnakButton = findViewById(R.id.kelahiran_ajuan_detail_anak_button);
        kelahiranKeteranganText = findViewById(R.id.kelahiran_ajuan_keterangan);

        kelahiranTolakText = findViewById(R.id.kelahiran_ajuan_tolak_text);
        tolakAlasanCard = findViewById(R.id.kelahiran_ajuan_alasan_tolak_card);

        kelahiranResubmitButton = findViewById(R.id.kelajiran_resubmit_button);

        int flag = getIntent().getIntExtra(FLAG_KELAHIRAN_DETAIL_KEY, -1);

        refreshKelahiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetailAjuan(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
            }
        });

        kelahiranContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetailAjuan(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
            }
        });
        getDetailAjuan(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
    }

    public void getDetail(String token, int id) {
        setLoadingContainerVisible();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KelahiranDetailResponse> kelahiranDetailResponseCall = getData.getKelahiranDetail("Bearer " + token, id);
        kelahiranDetailResponseCall.enqueue(new Callback<KelahiranDetailResponse>() {
            @Override
            public void onResponse(Call<KelahiranDetailResponse> call, Response<KelahiranDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getKelahiran().getStatus() == 1) {
                        statusAjuanKelahiran.setText("Sah");
                        statusAjuanKelahiran.setTextColor(getApplicationContext().getColor(R.color.green));
                    }

                    namaAnak.setText(response.body().getKelahiran().getCacahKramaMipil().getPenduduk().getNama());
                    if (response.body().getKelahiran().getNomorAktaKelahiran() != null) {
                        noAktaKelahiran.setText(response.body().getKelahiran().getNomorAktaKelahiran());
                    }
                    else {
                        noAktaKelahiran.setText("-");
                    }

                    if (response.body().getKelahiran().getFileAktaKelahiran() != null ) {
                        showAktaKelahiranButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getKelahiran().getFileAktaKelahiran());
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
                        showAktaKelahiranButton.setVisibility(GONE);
                    }

                    tanggalLahirAnak.setText(ChangeDateTimeFormat.changeDateFormat(
                            response.body().getKelahiran().getCacahKramaMipil().getPenduduk().getTanggalLahir())
                    );

                    tanggalAjuanKelahiran.setText(ChangeDateTimeFormat.changeDateTimeFormatForCreatedAt(
                            response.body().getKelahiran().getCreatedAt())
                    );


                    showDetailAnakButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getKelahiran().getCacahKramaMipil().getId());
                            startActivity(kramaDetail);
                        }
                    });

                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kelahiranContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KelahiranDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getDetailAjuan(String token, int id) {
        setLoadingContainerVisible();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KelahiranAjuanDetailResponse> kelahiranDetailResponseCall = getData.getKelahiranAjuanDetail("Bearer " + token, id);
        kelahiranDetailResponseCall.enqueue(new Callback<KelahiranAjuanDetailResponse>() {
            @Override
            public void onResponse(Call<KelahiranAjuanDetailResponse> call, Response<KelahiranAjuanDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getKelahiranAjuan().getStatus() == 1) {
                        statusAjuanKelahiran.setText("Ajuan sedang dalam proses");
                        statusAjuanKelahiran.setTextColor(getApplicationContext().getColor(R.color.yellow));
                    } else if (response.body().getKelahiranAjuan().getStatus() == 0) {
                        statusAjuanKelahiran.setText("Menunggu untuk diproses prajuru");
                        statusAjuanKelahiran.setTextColor(getApplicationContext().getColor(R.color.yellow));
                    }
                    else if (response.body().getKelahiranAjuan().getStatus() == 2) {
                        statusAjuanKelahiran.setText("Ajuan ditolak");
                        tolakAlasanCard.setVisibility(View.VISIBLE);
                        kelahiranTolakText.setText(response.body().getKelahiranAjuan().getAlasanTolakAjuan().toString());
                        statusAjuanKelahiran.setTextColor(getApplicationContext().getColor(R.color.red));
                        kelahiranResubmitButton.setVisibility(View.VISIBLE);
                        kelahiranResubmitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent pendudukDetail = new Intent(getApplicationContext(), KelahiranPengajuanBaruActivity.class);
                                Gson gson = new Gson();
                                String kelahiranJSon = gson.toJson(response.body().getKelahiranAjuan());
                                pendudukDetail.putExtra(KELAHIRAN_RESUBMIT_KEY, kelahiranJSon);
                                startActivity(pendudukDetail);
                                finish();
                            }
                        });
                    } else if (response.body().getKelahiranAjuan().getStatus() == 3) {
                        statusAjuanKelahiran.setText("Ajuan telah sah");
                        statusAjuanKelahiran.setTextColor(getApplicationContext().getColor(R.color.green));
                    }

                    if (response.body().getKelahiranAjuan().getKeterangan() != null) {
                        kelahiranKeteranganText.setText(response.body().getKelahiranAjuan().getKeterangan());
                    }

                    namaAnak.setText(response.body().getKelahiranAjuan().getCacahKramaMipil().getPenduduk().getNama());
                    if (response.body().getKelahiranAjuan().getNomorAktaKelahiran() != null) {
                        noAktaKelahiran.setText(response.body().getKelahiranAjuan().getNomorAktaKelahiran());
                    }
                    else {
                        noAktaKelahiran.setText("-");
                    }
                    if (response.body().getKelahiranAjuan().getFileAktaKelahiran() != null ) {
                        showAktaKelahiranButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getKelahiranAjuan().getFileAktaKelahiran());
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
                        showAktaKelahiranButton.setVisibility(GONE);
                    }

                    tanggalLahirAnak.setText(ChangeDateTimeFormat.changeDateFormat(
                            response.body().getKelahiranAjuan().getCacahKramaMipil().getPenduduk().getTanggalLahir())
                    );

                    tanggalAjuanKelahiran.setText(ChangeDateTimeFormat.changeDateTimeFormatForCreatedAt(
                            response.body().getKelahiranAjuan().getCreatedAt())
                    );


                    showDetailAnakButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getKelahiranAjuan().getCacahKramaMipil().getId());
                            startActivity(kramaDetail);
                        }
                    });

                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kelahiranContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KelahiranAjuanDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        kelahiranContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        kelahiranContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        kelahiranContainer.setVisibility(View.VISIBLE);
    }
}