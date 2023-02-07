package com.bagushikano.sikedatmobile.activity.krama.mipil;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.bagushikano.sikedatmobile.adapter.krama.KramaMipilAnggotaKeluargaListAdapter;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobile.model.krama.KramaMipilGetResponse;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KramaMipilDetailActivity extends AppCompatActivity {

    LinearLayout kramaMipilDetailLoadingContainer, kramaMipilFailedContainer,
            kramaMipilAnggotaFailed, kramaMipilAnggotaLoadingContainer;

    SwipeRefreshLayout kramaMipilContainer;
    TextView kramaMipilNoText, kramaMipilNamaText, kramaMipilBanjarAdatText, kramaMipilRegistrasiDateText, tempekan;
    RecyclerView kramaMipilAnggotaList;
    ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    KramaMipilAnggotaKeluargaListAdapter kramaMipilAnggotaKeluargaListAdapter;

    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;
    Button kramaMipilDetailRefresh, kramaMipilAnggotaRefresh;
    Button kramaMipilCacahDetailButton;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    private Button kartuKramaMipilButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_krama_mipil_detail);


        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kramaMipilDetailLoadingContainer = findViewById(R.id.krama_mipil_detail_loading_container);
        kramaMipilFailedContainer = findViewById(R.id.krama_mipil_detail_failed_container);
        kramaMipilAnggotaFailed = findViewById(R.id.krama_mipil_anggota_failed_container);
        kramaMipilAnggotaLoadingContainer = findViewById(R.id.krama_mipil_angggota_loading_container);

        kramaMipilAnggotaList = findViewById(R.id.krama_mipil_anggota_list);
        linearLayoutManager = new LinearLayoutManager(this);
        kramaMipilAnggotaKeluargaListAdapter = new KramaMipilAnggotaKeluargaListAdapter(this, anggotaKramaMipilArrayList);
        kramaMipilAnggotaList.setLayoutManager(linearLayoutManager);
        kramaMipilAnggotaList.setAdapter(kramaMipilAnggotaKeluargaListAdapter);

        kramaMipilAnggotaRefresh = findViewById(R.id.krama_mipil_anggota_refresh);
        kramaMipilAnggotaRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        kramaMipilNoText = findViewById(R.id.krama_mipil_no_text);
        kramaMipilNamaText = findViewById(R.id.krama_mipil_nama_text);
        kramaMipilBanjarAdatText = findViewById(R.id.krama_mipil_banjar_adat_text);
        kramaMipilRegistrasiDateText = findViewById(R.id.krama_mipil_registrasi_date_text);
        tempekan = findViewById(R.id.krama_mipil_tempekan_text);

        kartuKramaMipilButton = findViewById(R.id.kartu_krama_mipil_button);

        kramaMipilCacahDetailButton = findViewById(R.id.krama_mipil_cacah_detail_button);

        kramaMipilContainer = findViewById(R.id.krama_mipil_detail_container);
        kramaMipilDetailRefresh = findViewById(R.id.krama_mipil_detail_refresh);

        kramaMipilDetailRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
            }
        });

        kramaMipilContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
            }
        });
        getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
    }

    public void getDetail(String token, int id) {
        setLoadingContainerVisible();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KramaMipilGetResponse> kramaMipilGetResponseCall = getData.getKramaMipil("Bearer " + token);
        kramaMipilGetResponseCall.enqueue(new Callback<KramaMipilGetResponse>() {
            @Override
            public void onResponse(Call<KramaMipilGetResponse> call, Response<KramaMipilGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    String namaFormated = response.body().getKramaMipil().getCacahKramaMipil().getPenduduk().getNama();
                    if (response.body().getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                response.body().getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarDepan(),
                                response.body().getKramaMipil().getCacahKramaMipil().getPenduduk().getNama()
                        );
                    }
                    if (response.body().getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                response.body().getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarBelakang()
                        );
                    }
                    tempekan.setText(response.body().getKramaMipil().getCacahKramaMipil().getTempekan().getNamaTempekan());
                    kramaMipilNamaText.setText(namaFormated);
                    kramaMipilNoText.setText(response.body().getKramaMipil().getNomorKramaMipil());
                    kramaMipilBanjarAdatText.setText(response.body().getKramaMipil().getBanjarAdat().getNamaBanjarAdat());
                    kramaMipilRegistrasiDateText.setText(ChangeDateTimeFormat.changeDateFormat(response.body().getKramaMipil().getTanggalRegistrasi()));

                    kramaMipilCacahDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getKramaMipil().getCacahKramaMipil().getId());
                            startActivity(kramaDetail);
                        }
                    });

                    anggotaKramaMipilArrayList.clear();
                    anggotaKramaMipilArrayList.addAll(response.body().getKramaMipil().getAnggotaKramaMipilList());
                    kartuKramaMipilButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kartuKramaMipilIntent = new Intent(getApplicationContext(), KramaMipilKartuActivity.class);
                            kartuKramaMipilIntent.putExtra("KRAMA_KEY", response.body().getKramaMipil().getId());
                            kartuKramaMipilIntent.putExtra("KRAMA_NO_KEY", response.body().getKramaMipil().getNomorKramaMipil());
                            startActivity(kartuKramaMipilIntent);
                        }
                    });
//                    anggotaKramaListAdapter.notifyDataSetChanged();
//                    if (kramaMipilArrayList.size() == 0) {
//                        kramaEmptyContainer.setVisibility(View.VISIBLE);
//                    }
                    kramaMipilAnggotaLoadingContainer.setVisibility(GONE);
                    kramaMipilAnggotaFailed.setVisibility(GONE);
                    kramaMipilAnggotaList.setVisibility(View.VISIBLE);
                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kramaMipilContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KramaMipilGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        kramaMipilDetailLoadingContainer.setVisibility(GONE);
        kramaMipilFailedContainer.setVisibility(View.VISIBLE);
        kramaMipilContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        kramaMipilDetailLoadingContainer.setVisibility(View.VISIBLE);
        kramaMipilFailedContainer.setVisibility(GONE);
        kramaMipilContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        kramaMipilDetailLoadingContainer.setVisibility(GONE);
        kramaMipilFailedContainer.setVisibility(GONE);
        kramaMipilContainer.setVisibility(View.VISIBLE);
    }
}