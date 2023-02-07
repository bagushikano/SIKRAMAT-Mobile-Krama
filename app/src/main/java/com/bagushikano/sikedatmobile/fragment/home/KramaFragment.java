package com.bagushikano.sikedatmobile.fragment.home;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.krama.mipil.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobile.activity.krama.tamiu.KramaTamiuDetailActivity;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobile.viewmodel.KramaViewModel;
import com.bagushikano.sikedatmobile.viewmodel.ProfileViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class KramaFragment extends Fragment {

    View v;
    SharedPreferences loginPreferences;
    SharedPreferences userPreferences;
    private Chip namaUserChip;
    private LinearLayout kramaLoadingContainer, kramaFailedContainer;
    private SwipeRefreshLayout kramaContainer;
    private Button kramaMipilDetailButton, kramaRefreshButton, kramaTamiuDetailButton;

    private MaterialCardView kramaMipilCardLayout, kramaTamiuTitleCard, kramaTamiuCard;

    private TextView kramaMipilNameText, kramaMipilNoText,
            kramaMipilTempekanText, kramaMipilBanjarAdatText, kramaMipilDesaAdatText, kramaTamiuNameText, kramaTamiuNoText,
            kramaTamiuDate, kramaTamiuBanjarAdatText, kramaTamiuDesaAdatText;

    private KramaViewModel kramaViewModel;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public KramaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kramaViewModel = ViewModelProviders.of(getActivity()).get(KramaViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_krama, container, false);
        loginPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        userPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        namaUserChip = v.findViewById(R.id.banner_nama_user_chip);
        if (!(userPreferences.getString("nama_user", "empty").equals("empty"))) {
            namaUserChip.setText(userPreferences.getString("nama_user", "empty"));
            namaUserChip.setVisibility(View.VISIBLE);
        }

        kramaMipilNameText = v.findViewById(R.id.krama_mipil_name_text);
        kramaMipilNoText = v.findViewById(R.id.krama_mipil_no_text);
        kramaMipilTempekanText = v.findViewById(R.id.krama_mipil_tempekan_text);
        kramaMipilBanjarAdatText = v.findViewById(R.id.krama_mipil_banjar_adat_text);
        kramaMipilDesaAdatText = v.findViewById(R.id.krama_mipil_desa_adat_text);
        kramaMipilCardLayout = v.findViewById(R.id.krama_mipil_card);

        kramaMipilDetailButton = v.findViewById(R.id.krama_mipil_detail_button);
        kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaMipilDetail = new Intent(getActivity(), KramaMipilDetailActivity.class);
                startActivity(kramaMipilDetail);
            }
        });

        kramaTamiuNameText = v.findViewById(R.id.krama_tamiu_name);
        kramaTamiuNoText = v.findViewById(R.id.krama_tamiu_no_text);
        kramaTamiuDate = v.findViewById(R.id.krama_tamiu_tanggal_registrasi);
        kramaTamiuBanjarAdatText = v.findViewById(R.id.krama_tamiu_banjar_adat_text);
        kramaTamiuDesaAdatText = v.findViewById(R.id.krama_tamiu_desa_adat_text);
        kramaTamiuCard = v.findViewById(R.id.krama_tamiu_card);
        kramaTamiuTitleCard = v.findViewById(R.id.krama_tamiu_title_card);

        kramaTamiuDetailButton = v.findViewById(R.id.krama_tamiu_detail_button);


        kramaMipilDetailButton = v.findViewById(R.id.krama_mipil_detail_button);
        kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaMipilDetail = new Intent(getActivity(), KramaMipilDetailActivity.class);
                startActivity(kramaMipilDetail);
            }
        });

        kramaLoadingContainer = v.findViewById(R.id.krama_loading_container);
        kramaFailedContainer = v.findViewById(R.id.krama_failed_container);
        kramaContainer = v.findViewById(R.id.krama_container);
        kramaRefreshButton = v.findViewById(R.id.krama_refresh);

        kramaRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"), 1);
                getKramaTamiu(loginPreferences.getString("token", "empty"), 1);
            }
        });


        kramaContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"), 1);
                getKramaTamiu(loginPreferences.getString("token", "empty"), 1);
            }
        });

        getData(loginPreferences.getString("token", "empty"), 0);
        getKramaTamiu(loginPreferences.getString("token", "empty"), 0);
        return v;
    }

    public void getData(String token, int flag) {
        if (flag == 0) {
            kramaViewModel.init(token);
        }
        else if (flag == 1) {
            kramaViewModel.getData(token);
        }
        setLoadingContainerVisible();
        kramaViewModel.getUserProfie().observe(getViewLifecycleOwner(), kramaMipilGetResponse -> {
            if (kramaMipilGetResponse != null) {
                if (kramaMipilGetResponse.getKramaMipil() != null) {
                    String namaFormated = kramaMipilGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getNama();
                    if (kramaMipilGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                kramaMipilGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarDepan(),
                                kramaMipilGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getNama()
                        );
                    }
                    if (kramaMipilGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                kramaMipilGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarBelakang()
                        );
                    }
                    kramaMipilNameText.setText(namaFormated);
                    kramaMipilNoText.setText(kramaMipilGetResponse.getKramaMipil().getNomorKramaMipil());
                    kramaMipilTempekanText.setText(kramaMipilGetResponse.getKramaMipil().getCacahKramaMipil().getTempekan().getNamaTempekan());
                    kramaMipilBanjarAdatText.setText(kramaMipilGetResponse.getKramaMipil().getBanjarAdat().getNamaBanjarAdat());
                    kramaMipilDesaAdatText.setText(kramaMipilGetResponse.getKramaMipil().getBanjarAdat().getDesaAdat().getDesadatNama());
                }
                setProfileContainerVisible();
            }
            else {
                setFailedContainerVisible();
            }
        });
        kramaContainer.setRefreshing(false);
    }

    public void getKramaTamiu(String token, int flag) {
        if (flag == 0) {
            kramaViewModel.init(token);
        }
        else if (flag == 1) {
            kramaViewModel.getData(token);
        }
        setLoadingContainerVisible();
        kramaViewModel.getKramaTamiu().observe(getViewLifecycleOwner(), kramaTamiuGetResponse -> {
            if (kramaTamiuGetResponse != null) {
                if (kramaTamiuGetResponse.getKramaTamiu() != null) {
                    kramaTamiuCard.setVisibility(View.VISIBLE);
                    kramaTamiuTitleCard.setVisibility(View.VISIBLE);
                    String namaFormated = kramaTamiuGetResponse.getKramaTamiu().getCacahKramaTamiu().getPenduduk().getNama();
                    if (kramaTamiuGetResponse.getKramaTamiu().getCacahKramaTamiu().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                kramaTamiuGetResponse.getKramaTamiu().getCacahKramaTamiu().getPenduduk().getGelarDepan(),
                                kramaTamiuGetResponse.getKramaTamiu().getCacahKramaTamiu().getPenduduk().getNama()
                        );
                    }
                    if (kramaTamiuGetResponse.getKramaTamiu().getCacahKramaTamiu().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                kramaTamiuGetResponse.getKramaTamiu().getCacahKramaTamiu().getPenduduk().getGelarBelakang()
                        );
                    }

                    kramaTamiuDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaTamiuDetail = new Intent(getActivity(), KramaTamiuDetailActivity.class);
                            kramaTamiuDetail.putExtra(KRAMA_DETAIL_KEY, kramaTamiuGetResponse.getKramaTamiu().getId());
                            startActivity(kramaTamiuDetail);
                        }
                    });

                    kramaTamiuNameText.setText(namaFormated);
                    kramaTamiuNoText.setText(kramaTamiuGetResponse.getKramaTamiu().getNomorKramaTamiu());
                    kramaTamiuDate.setText(ChangeDateTimeFormat.changeDateFormat(kramaTamiuGetResponse.getKramaTamiu().getTanggalRegistrasi()));
                    kramaTamiuBanjarAdatText.setText(kramaTamiuGetResponse.getKramaTamiu().getBanjarAdat().getNamaBanjarAdat());
                    kramaTamiuDesaAdatText.setText(kramaTamiuGetResponse.getKramaTamiu().getBanjarAdat().getDesaAdat().getDesadatNama());
                }
            }
            else {
                kramaTamiuCard.setVisibility(GONE);
                kramaTamiuTitleCard.setVisibility(GONE);
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

    public void setProfileContainerVisible() {
        kramaLoadingContainer.setVisibility(GONE);
        kramaFailedContainer.setVisibility(GONE);
        kramaContainer.setVisibility(View.VISIBLE);
    }
}