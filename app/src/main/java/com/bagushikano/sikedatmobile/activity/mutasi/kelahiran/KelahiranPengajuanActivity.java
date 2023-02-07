package com.bagushikano.sikedatmobile.activity.mutasi.kelahiran;

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
import com.bagushikano.sikedatmobile.adapter.kelahiran.KelahiranListAdapter;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.fragment.kelahiran.KelahiranAjuanFragment;
import com.bagushikano.sikedatmobile.fragment.kelahiran.KelahiranDataFragment;
import com.bagushikano.sikedatmobile.model.kelahiran.Kelahiran;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranGetResponse;
import com.bagushikano.sikedatmobile.viewmodel.KelahiranViewModel;
import com.bagushikano.sikedatmobile.viewmodel.ProfileViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelahiranPengajuanActivity extends AppCompatActivity {

    private Button pengajuanKelahiranBaruButton;

    RecyclerView kelahiranList;
    ArrayList<Kelahiran> kelahiranArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    KelahiranListAdapter kelahiranListAdapter;
    TextView kelahiranAjuanTotalText;

    LinearLayout loadingContainer, failedContainer, kelahiranEmptyContainer;
    SwipeRefreshLayout kelahiranContainer;
    Button refreshKelahiran;
    SharedPreferences loginPreferences;
    private Toolbar homeToolbar;

    KelahiranViewModel kelahiranViewModel;
    Fragment fragment1;
    Fragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelahiran_pengajuan);

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

        kelahiranViewModel = ViewModelProviders.of(this).get(KelahiranViewModel.class);


        homeTabLayout.setupWithViewPager(homeViewPager);

        KelahiranFragmentAdapter kelahiranFragmentAdapter = new KelahiranFragmentAdapter(getSupportFragmentManager());
        kelahiranFragmentAdapter.addFrag(new KelahiranAjuanFragment(), "Daftar Ajuan");
        kelahiranFragmentAdapter.addFrag(new KelahiranDataFragment(), "Daftar Kelahiran");
        homeViewPager.setAdapter(kelahiranFragmentAdapter);
        fragment1 = kelahiranFragmentAdapter.getItem(0);
        fragment2 = kelahiranFragmentAdapter.getItem(1);


/*
        kelahiranViewModel = ViewModelProviders.of(this).get(KelahiranViewModel.class);

        pengajuanKelahiranBaruButton = findViewById(R.id.kelahiran_pengajuan_baru_button);
        pengajuanKelahiranBaruButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pengajuanKelahiranBaruIntent = new Intent(getApplicationContext() ,KelahiranPengajuanBaruActivity.class);
                startActivity(pengajuanKelahiranBaruIntent);
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        loadingContainer = findViewById(R.id.kelahiran_loading_container);
        failedContainer = findViewById(R.id.kelahiran_failed_container);
        kelahiranContainer = findViewById(R.id.kelahiran_container);
        refreshKelahiran = findViewById(R.id.kelahiran_refresh);
        kelahiranList = findViewById(R.id.kelahiran_ajuan_list);
        kelahiranAjuanTotalText = findViewById(R.id.kelahiran_ajuan_total_text);
//        kramaAllDataLoadedTextView = findViewById(R.id.all_data_loaded_krama_text);
//        kelahiranEmptyContainer = findViewById(R.id.krama_empty_container);

        linearLayoutManager = new LinearLayoutManager(this);
        kelahiranListAdapter = new KelahiranListAdapter(this, kelahiranArrayList);
        kelahiranList.setLayoutManager(linearLayoutManager);
        kelahiranList.setAdapter(kelahiranListAdapter);


        refreshKelahiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        kelahiranContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        kelahiranContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KelahiranGetResponse> kelahiranGetResponseCall = getData.getKelahiran("Bearer " + token);
        kelahiranGetResponseCall.enqueue(new Callback<KelahiranGetResponse>() {
            @Override
            public void onResponse(Call<KelahiranGetResponse> call, Response<KelahiranGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    kelahiranArrayList.clear();
                    kelahiranArrayList.addAll(response.body().getKelahiranList());
                    kelahiranListAdapter.notifyDataSetChanged();
                    kelahiranAjuanTotalText.setText(String.valueOf(kelahiranArrayList.size()));
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
                kelahiranContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KelahiranGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    */



//    public void setFailedContainerVisible() {
//        loadingContainer.setVisibility(GONE);
//        failedContainer.setVisibility(View.VISIBLE);
//        kelahiranContainer.setVisibility(GONE);
//    }
//
//    public void setLoadingContainerVisible() {
//        loadingContainer.setVisibility(View.VISIBLE);
//        failedContainer.setVisibility(GONE);
//        kelahiranContainer.setVisibility(GONE);
//    }
//
//    public void setKramaContainerVisible() {
//        loadingContainer.setVisibility(GONE);
//        failedContainer.setVisibility(GONE);
//        kelahiranContainer.setVisibility(View.VISIBLE);
//    }
}