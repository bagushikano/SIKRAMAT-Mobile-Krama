package com.bagushikano.sikedatmobile.activity.mutasi.kematian;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;

import com.bagushikano.sikedatmobile.adapter.KelahiranFragmentAdapter;
import com.bagushikano.sikedatmobile.adapter.kematian.KematianListAdapter;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;

import com.bagushikano.sikedatmobile.fragment.kelahiran.KelahiranAjuanFragment;
import com.bagushikano.sikedatmobile.fragment.kelahiran.KelahiranDataFragment;
import com.bagushikano.sikedatmobile.fragment.kematian.KematianAjuanFragment;
import com.bagushikano.sikedatmobile.fragment.kematian.KematianDataFragment;
import com.bagushikano.sikedatmobile.model.kematian.Kematian;
import com.bagushikano.sikedatmobile.model.kematian.KematianGetResponse;
import com.bagushikano.sikedatmobile.viewmodel.KelahiranViewModel;
import com.bagushikano.sikedatmobile.viewmodel.KematianViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KematianPengajuanActivity extends AppCompatActivity {

//    private Button pengajuanKematianBaruButton;
//
//    RecyclerView kematianList;
//    ArrayList<Kematian> kematianArrayList = new ArrayList<>();
//    LinearLayoutManager linearLayoutManager;
//    KematianListAdapter kematianListAdapter;
//    TextView kematianAjuanTotalText;
//
//    LinearLayout loadingContainer, failedContainer, kematianEmptyContainer;
//    SwipeRefreshLayout kematianContainer;
//    Button refreshKelahiran;
//    SharedPreferences loginPreferences;
    private Toolbar homeToolbar;

    KematianViewModel kematianViewModel;
    Fragment fragment1;
    Fragment fragment2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kematian_pengajuan);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        TabLayout homeTabLayout = findViewById(R.id.home_tab_bar);
        TabItem belanjaanTab = findViewById(R.id.belanjaan_tab);
        TabItem barangTab = findViewById(R.id.barang_tab);
        ViewPager homeViewPager = findViewById(R.id.home_view_pager);

        kematianViewModel = ViewModelProviders.of(this).get(KematianViewModel.class);


        homeTabLayout.setupWithViewPager(homeViewPager);

        KelahiranFragmentAdapter kelahiranFragmentAdapter = new KelahiranFragmentAdapter(getSupportFragmentManager());
        kelahiranFragmentAdapter.addFrag(new KematianAjuanFragment(), "Daftar Ajuan");
        kelahiranFragmentAdapter.addFrag(new KematianDataFragment(), "Daftar Kematian");
        homeViewPager.setAdapter(kelahiranFragmentAdapter);
        fragment1 = kelahiranFragmentAdapter.getItem(0);
        fragment2 = kelahiranFragmentAdapter.getItem(1);


        /*
        pengajuanKematianBaruButton = findViewById(R.id.kematian_pengajuan_baru_button);
        pengajuanKematianBaruButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pengajuanKematianBaruIntent = new Intent(getApplicationContext(), KematianPengajuanBaruActivity.class);
                startActivity(pengajuanKematianBaruIntent);
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        loadingContainer = findViewById(R.id.kematian_loading_container);
        failedContainer = findViewById(R.id.kematian_failed_container);
        kematianContainer = findViewById(R.id.kematian_container);
        refreshKelahiran = findViewById(R.id.kematian_refresh);
        kematianList = findViewById(R.id.kematian_ajuan_list);
        kematianAjuanTotalText = findViewById(R.id.kematian_ajuan_total_text);

//        kramaAllDataLoadedTextView = findViewById(R.id.all_data_loaded_krama_text);
//        kematianEmptyContainer = findViewById(R.id.krama_empty_container);

        linearLayoutManager = new LinearLayoutManager(this);
        kematianListAdapter = new KematianListAdapter(this, kematianArrayList);
        kematianList.setLayoutManager(linearLayoutManager);
        kematianList.setAdapter(kematianListAdapter);


        refreshKelahiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        kematianContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        getData(loginPreferences.getString("token", "empty"));
        */
    }
/*
    public void getData(String token) {
        setLoadingContainerVisible();
        kematianContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KematianGetResponse> kematianGetResponseCall = getData.getKematian("Bearer " + token);
        kematianGetResponseCall.enqueue(new Callback<KematianGetResponse>() {
            @Override
            public void onResponse(Call<KematianGetResponse> call, Response<KematianGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    kematianArrayList.clear();
                    kematianArrayList.addAll(response.body().getKematianList());
                    kematianListAdapter.notifyDataSetChanged();
                    kematianAjuanTotalText.setText(String.valueOf(kematianArrayList.size()));
//                    if (kramaMipilArrayList.size() == 0) {
//                        kramaEmptyContainer.setVisibility(View.VISIBLE);
//                    } else {
//                        kramaEmptyContainer.setVisibility(GONE);
//                        currentPage = response.body().getKramaMipilPaginate().getCurrentPage();
//                        lastPage = response.body().getKramaMipilPaginate().getLastPage();
//                        if (currentPage != lastPage) {
//                            //TODO add automatic handler if data is need to loaded
//                            nextPage = currentPage + 1;
//                        } else {
//                            kramaAllDataLoadedTextView.setVisibility(View.VISIBLE);
//                        }
//                    }
                    setKramaContainerVisible();
                } else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kematianContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KematianGetResponse> call, Throwable t) {
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
    */

}