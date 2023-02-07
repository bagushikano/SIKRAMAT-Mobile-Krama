package com.bagushikano.sikedatmobile.fragment.kelahiran;

import static android.view.View.GONE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import com.bagushikano.sikedatmobile.adapter.kelahiran.KelahiranListAdapter;
import com.bagushikano.sikedatmobile.model.kelahiran.Kelahiran;
import com.bagushikano.sikedatmobile.viewmodel.KelahiranViewModel;

import java.util.ArrayList;


public class KelahiranDataFragment extends Fragment {

    View v;
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
    private KelahiranViewModel kelahiranViewModel;

    public KelahiranDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kelahiranViewModel = ViewModelProviders.of(getActivity()).get(KelahiranViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_kelahiran_data, container, false);

        loginPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        loadingContainer = v.findViewById(R.id.kelahiran_loading_container);
        failedContainer = v.findViewById(R.id.kelahiran_failed_container);
        kelahiranContainer = v.findViewById(R.id.kelahiran_container);
        refreshKelahiran = v.findViewById(R.id.kelahiran_refresh);
        kelahiranList = v.findViewById(R.id.kelahiran_ajuan_list);
        kelahiranAjuanTotalText = v.findViewById(R.id.kelahiran_ajuan_total_text);
//        kramaAllDataLoadedTextView = findViewById(R.id.all_data_loaded_krama_text);
//        kelahiranEmptyContainer = findViewById(R.id.krama_empty_container);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        kelahiranListAdapter = new KelahiranListAdapter(getActivity(), kelahiranArrayList);
        kelahiranList.setLayoutManager(linearLayoutManager);
        kelahiranList.setAdapter(kelahiranListAdapter);


        refreshKelahiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"), 0);
            }
        });

        kelahiranContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
            kelahiranViewModel.init(token);
        }
        else if (flag == 1) {
            kelahiranViewModel.getData(token);
        }
        setLoadingContainerVisible();
        kelahiranViewModel.getKelahiran().observe(getViewLifecycleOwner(), kelahiranGetResponse -> {
            if (kelahiranGetResponse != null) {
                kelahiranArrayList.clear();
                kelahiranArrayList.addAll(kelahiranGetResponse.getKelahiranList());
                kelahiranListAdapter.notifyDataSetChanged();
                kelahiranAjuanTotalText.setText(String.valueOf(kelahiranArrayList.size()));
                setKramaContainerVisible();
            }
            else {
                setFailedContainerVisible();
            }
        });
        kelahiranContainer.setRefreshing(false);
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