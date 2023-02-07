package com.bagushikano.sikedatmobile.fragment.home;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.cacahkrama.CacahKramaEditLocationActivity;
import com.bagushikano.sikedatmobile.adapter.NotifikasiListAdapter;
import com.bagushikano.sikedatmobile.adapter.kelahiran.KelahiranAjuanListAdapter;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.AuthResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuan;
import com.bagushikano.sikedatmobile.model.notifikasi.Notifikasi;
import com.bagushikano.sikedatmobile.model.notifikasi.NotifikasiGetResponse;
import com.bagushikano.sikedatmobile.util.CloseKeyboard;
import com.bagushikano.sikedatmobile.viewmodel.BerandaViewModel;
import com.bagushikano.sikedatmobile.viewmodel.KramaViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BerandaFragment extends Fragment {

    View v;
    SharedPreferences userPreferences, loginPreferences;
    private Chip namaUserChip;
    private TextView greetingText, greetingTextSub;
    private ImageView greetingImage, greetingBerandaIcon;
    private BerandaViewModel berandaViewModel;
    private Button markAllAsReadNotif;

    RecyclerView notifikasiList;
    ArrayList<Notifikasi> notifikasiArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    NotifikasiListAdapter notifikasiListAdapter;
    TextView notifikasiTotalText;
    SwipeRefreshLayout berandaContainer;
    LinearLayout notificationEmptyContainer;

    public BerandaFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        berandaViewModel = ViewModelProviders.of(getActivity()).get(BerandaViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_beranda, container, false);

        userPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        loginPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        berandaContainer = v.findViewById(R.id.beranda_container);

        notifikasiList = v.findViewById(R.id.beranda_notifikasi_list);
        notifikasiTotalText = v.findViewById(R.id.beranda_notif_count_text);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        notifikasiListAdapter = new NotifikasiListAdapter(getActivity(), notifikasiArrayList);
        notifikasiList.setLayoutManager(linearLayoutManager);
        notifikasiList.setAdapter(notifikasiListAdapter);

        notificationEmptyContainer = v.findViewById(R.id.notifikasi_empty_container);
        markAllAsReadNotif = v.findViewById(R.id.read_all_notif_button);

        namaUserChip = v.findViewById(R.id.banner_nama_user_chip);
        greetingText = v.findViewById(R.id.greeting_name_beranda_text);
        greetingTextSub = v.findViewById(R.id.greeting_beranda_text);
        greetingImage = v.findViewById(R.id.greeting_beranda_image);
        greetingBerandaIcon = v.findViewById(R.id.greeting_beranda_icon);

        Button testMapButton = v.findViewById(R.id.test_map_button);
        testMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testMapIntent = new Intent(getActivity(), CacahKramaEditLocationActivity.class);
                startActivity(testMapIntent);
            }
        });
        testMapButton.setVisibility(View.GONE);

        markAllAsReadNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readAllNotif(loginPreferences.getString("token", "empty"));
            }
        });

        berandaContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"), 1);
            }
        });

        //get local time for appbar title
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String greeting;

        if (hour>= 12 && hour < 17) {
            greeting = "Selamat siang dan semangat dalam menjalankan aktifitas";
            greetingImage.setImageResource(R.drawable.pagi_replace);
            greetingBerandaIcon.setImageResource(R.drawable.ic_outline_wb_sunny_24);
        } else if (hour >= 17 && hour < 19) {
            greeting = "Selamat sore, dan semoga sisa hari anda menyenangkan";
            greetingImage.setImageResource(R.drawable.sore_replace);
            greetingBerandaIcon.setImageResource(R.drawable.ic_outline_wb_twilight_24);
        } else if (hour >= 19 && hour < 24) {
            greeting = "Selamat malam dan selamat beristirahat";
            greetingImage.setImageResource(R.drawable.sore_replace);
            greetingBerandaIcon.setImageResource(R.drawable.ic_outline_nights_stay_24);
        } else {
            greeting = "Selamat pagi dan selamat beraktifitas! ";
            greetingImage.setImageResource(R.drawable.pagi_replace);
            greetingBerandaIcon.setImageResource(R.drawable.ic_outline_wb_sunny_24);
        }
        String namaUser = userPreferences.getString("nama_user", "empty");
        String[] arrayString = namaUser.split(" ");

        if (!(userPreferences.getString("nama_user", "empty").equals("empty"))) {
            greetingText.setText("Om Swastyastu, " + arrayString[arrayString.length-1]);
            namaUserChip.setText(namaUser);
            namaUserChip.setVisibility(View.VISIBLE);
        }
        greetingTextSub.setText(greeting);
        getData(loginPreferences.getString("token", "empty"), 0);
        return v;
    }

    public void getData(String token, int flag) {
        if (flag == 0) {
            berandaViewModel.init(token);
        }
        else if (flag == 1) {
            berandaViewModel.getDataNotifikasi(token);
        }
        berandaViewModel.getNotifikasi().observe(getViewLifecycleOwner(), notifikasiGetResponse -> {
            if (notifikasiGetResponse != null) {
                notifikasiArrayList.clear();
                notifikasiArrayList.addAll(notifikasiGetResponse.getNotifikasiList());
                if (notifikasiGetResponse.getNotifikasiList().size() == 0) {
                    notificationEmptyContainer.setVisibility(View.VISIBLE);
                    notifikasiList.setVisibility(View.GONE);
                } else {
                    notificationEmptyContainer.setVisibility(View.GONE);
                    notifikasiList.setVisibility(View.VISIBLE);
                    notifikasiTotalText.setText(String.valueOf(notifikasiArrayList.size()));
                }
            }
            else {
                notificationEmptyContainer.setVisibility(View.VISIBLE);
                notifikasiArrayList.clear();
                notifikasiTotalText.setText(String.valueOf(0));
            }
        });
        notifikasiListAdapter.notifyDataSetChanged();
        berandaContainer.setRefreshing(false);
    }

    public void readAllNotif(String token) {
        ApiRoute submitData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<NotifikasiGetResponse> authResponseCall = submitData.readAllNotifikasi("Bearer " + token);
        authResponseCall.enqueue(new Callback<NotifikasiGetResponse>() {
            @Override
            public void onResponse(Call<NotifikasiGetResponse> call, Response<NotifikasiGetResponse> response) {
                if (response.code() == 200 && response.body().getStatusCode() == 200
                        && response.body().getMessage().equals("read notifikasi sukses")) {

                    getData(loginPreferences.getString("token", "empty"), 1);

                    Snackbar snackbar = Snackbar.make(
                            getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                            "Sukses",Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(
                            getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<NotifikasiGetResponse> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(
                        getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}