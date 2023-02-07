package com.bagushikano.sikedatmobile.fragment.kematian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.mutasi.kematian.KematianPengajuanBaruActivity;
import com.bagushikano.sikedatmobile.adapter.kematian.KematianAjuanListAdapter;
import com.bagushikano.sikedatmobile.adapter.kematian.KematianListAdapter;
import com.bagushikano.sikedatmobile.model.kematian.Kematian;
import com.bagushikano.sikedatmobile.model.kematian.KematianAjuan;
import com.bagushikano.sikedatmobile.viewmodel.KelahiranViewModel;
import com.bagushikano.sikedatmobile.viewmodel.KematianViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;


public class KematianAjuanFragment extends Fragment {

    View v;
    private Button pengajuanKematianBaruButton;
    RecyclerView kematianList;
    ArrayList<KematianAjuan> kematianArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    KematianAjuanListAdapter kematianListAdapter;
    TextView kematianAjuanTotalText;

    LinearLayout loadingContainer, failedContainer, kematianEmptyContainer;
    SwipeRefreshLayout kematianContainer;
    Button refreshKelahiran;
    SharedPreferences loginPreferences;
    private KematianViewModel kematianViewModel;
    ActivityResultLauncher<Intent> startActivityIntent;

    public KematianAjuanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kematianViewModel = ViewModelProviders.of(getActivity()).get(KematianViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_kematian_ajuan, container, false);

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            getData(loginPreferences.getString("token", "empty"), 1);
                        }
                    }
                });

        pengajuanKematianBaruButton = v.findViewById(R.id.kematian_pengajuan_baru_button);
        pengajuanKematianBaruButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pengajuanKematianBaruIntent = new Intent(getActivity(), KematianPengajuanBaruActivity.class);
                startActivityIntent.launch(pengajuanKematianBaruIntent);
            }
        });

        loginPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        loadingContainer = v.findViewById(R.id.kematian_loading_container);
        failedContainer = v.findViewById(R.id.kematian_failed_container);
        kematianContainer = v.findViewById(R.id.kematian_container);
        refreshKelahiran = v.findViewById(R.id.kematian_refresh);
        kematianList = v.findViewById(R.id.kematian_ajuan_list);
        kematianAjuanTotalText = v.findViewById(R.id.kematian_ajuan_total_text);
        kematianEmptyContainer = v.findViewById(R.id.kematian_empty_container);

//        kramaAllDataLoadedTextView = findViewById(R.id.all_data_loaded_krama_text);
//        kematianEmptyContainer = findViewById(R.id.krama_empty_container);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        kematianListAdapter = new KematianAjuanListAdapter(getActivity(), kematianArrayList);
        kematianList.setLayoutManager(linearLayoutManager);
        kematianList.setAdapter(kematianListAdapter);


        refreshKelahiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"), 0);
            }
        });

        kematianContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"), 1);
            }
        });

        getData(loginPreferences.getString("token", "empty"), 0);

        return v;
    }

    public void getData(String token, int flag) {
        if (flag == 0) {
            kematianViewModel.init(token);
        }
        else if (flag == 1) {
            kematianViewModel.getDataAjuan(token);
        }
        setLoadingContainerVisible();
        kematianViewModel.getKematianAjuan().observe(getViewLifecycleOwner(), kematianGetResponse -> {
            if (kematianGetResponse != null) {
                kematianArrayList.clear();
                kematianArrayList.addAll(kematianGetResponse.getKematianAjuanList());
                kematianAjuanTotalText.setText(String.valueOf(kematianArrayList.size()));
                setKramaContainerVisible();
                if (kematianArrayList.size() == 0) {
                    kematianList.setVisibility(View.GONE);
                    kematianEmptyContainer.setVisibility(View.VISIBLE);
                } else {
                    kematianListAdapter.notifyDataSetChanged();
                }
            }
            else {
                setFailedContainerVisible();
            }
        });
        kematianContainer.setRefreshing(false);
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(View.GONE);
        failedContainer.setVisibility(View.VISIBLE);
        kematianContainer.setVisibility(View.GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(View.GONE);
        kematianContainer.setVisibility(View.GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(View.GONE);
        failedContainer.setVisibility(View.GONE);
        kematianContainer.setVisibility(View.VISIBLE);
    }
}