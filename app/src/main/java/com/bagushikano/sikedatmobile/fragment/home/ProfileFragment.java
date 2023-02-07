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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.auth.LoginActivity;
import com.bagushikano.sikedatmobile.activity.cacahkrama.CacahKramaDetailActivity;
import com.bagushikano.sikedatmobile.activity.cacahkrama.CacahMipilDetailActivity;
import com.bagushikano.sikedatmobile.activity.cacahkrama.CacahTamiuDetailActivity;
import com.bagushikano.sikedatmobile.viewmodel.ProfileViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    View v;
    SharedPreferences loginPreferences;
    SharedPreferences userPreferences;

    private LinearLayout profileLoadingContainer, profileFailedContainer;
    private SwipeRefreshLayout profileContainer;
    private Button profileRefreshButton, profileDetailButton,
            profileCacahMipilDetail, profileCacahTamiuDetail;
    private MaterialCardView profileLogoutButton, profileCacahMipilCard, profileCacahTamiuCard;

    private ImageView profileCacahKramaImage;
    private TextView profileCacahKramaNama, profileCacahKramaNik, profileCacahKramaNoTlp,
            profileCacahMipilDesaAdat, profileCacahMipilBanjarAdat, profileCacahTamiuDesaAdat, profileCacahTamiuBanjarAdat;

    ProfileViewModel profileViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        loginPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        profileLoadingContainer = v.findViewById(R.id.profile_loading_container);
        profileFailedContainer = v.findViewById(R.id.profile_failed_container);
        profileContainer = v.findViewById(R.id.profile_container);
        profileRefreshButton = v.findViewById(R.id.profile_refresh);

        profileDetailButton = v.findViewById(R.id.profile_detail_button);
        profileCacahMipilDetail = v.findViewById(R.id.cacah_krama_mipil_detail_button);

        profileCacahTamiuDetail = v.findViewById(R.id.cacah_krama_tamiu_detail_button);
        profileCacahTamiuDesaAdat = v.findViewById(R.id.cacah_tamiu_desa_adat);
        profileCacahTamiuBanjarAdat = v.findViewById(R.id.cacah_tamiu_banjar_adat);

        profileCacahKramaImage = v.findViewById(R.id.cacah_krama_image);
        profileCacahKramaNama = v.findViewById(R.id.cacah_krama_nama_text);
        profileCacahKramaNik = v.findViewById(R.id.cacah_krama_nik_text);
        profileCacahKramaNoTlp = v.findViewById(R.id.cacah_krama_notelp_text);

        profileCacahMipilCard = v.findViewById(R.id.cacah_mipil_profile_card);
        profileCacahMipilDesaAdat = v.findViewById(R.id.cacah_mipil_desa_adat_text);
        profileCacahMipilBanjarAdat = v.findViewById(R.id.cacah_mipil_banjar_adat_text);

        profileCacahTamiuCard = v.findViewById(R.id.cacah_tamiu_profile_card);

        profileLogoutButton = v.findViewById(R.id.logout_sikedat_button);
        profileLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginPreferences.getInt("login_status", 0) != 0) {
                    SharedPreferences.Editor loginPrefEditor = loginPreferences.edit();

                    loginPrefEditor.putInt("login_status", 0);
                    loginPrefEditor.putString("token", "empty");
                    loginPrefEditor.apply();
                }
                Toast.makeText(getActivity(), "Logout berhasil", Toast.LENGTH_SHORT).show();
                Intent mainActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(mainActivity);
                getActivity().finishAffinity();
            }
        });

        profileRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"), 1);
            }
        });


        profileContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
            profileViewModel.init(token);
        }
        else if (flag == 1) {
            profileViewModel.getData(token);
        }
        setLoadingContainerVisible();
        profileViewModel.getUserProfie().observe(getViewLifecycleOwner(), cacahMipilTamiuGetResponse -> {
            if (cacahMipilTamiuGetResponse != null) {
                profileCacahKramaNik.setText(cacahMipilTamiuGetResponse.getPenduduk().getNik());

                String namaFormated = cacahMipilTamiuGetResponse.getPenduduk().getNama();
                if (cacahMipilTamiuGetResponse.getPenduduk().getGelarDepan() != null) {
                    namaFormated = String.format("%s %s",
                            cacahMipilTamiuGetResponse.getPenduduk().getGelarDepan(),
                            cacahMipilTamiuGetResponse.getPenduduk().getNama()
                    );
                }
                if (cacahMipilTamiuGetResponse.getPenduduk().getGelarBelakang() != null) {
                    namaFormated = String.format("%s %s",
                            namaFormated,
                            cacahMipilTamiuGetResponse.getPenduduk().getGelarBelakang()
                    );
                }
                profileCacahKramaNama.setText(namaFormated);

                profileDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pendudukDetail = new Intent(getActivity(), CacahKramaDetailActivity.class);
                        Gson gson = new Gson();
                        String kelahiranJSon = gson.toJson(cacahMipilTamiuGetResponse.getPenduduk());
                        pendudukDetail.putExtra("PENDUDUK", kelahiranJSon);
                        getActivity().startActivity(pendudukDetail);
                    }
                });

                if (cacahMipilTamiuGetResponse.getPenduduk().getTelepon() != null) {
                    profileCacahKramaNoTlp.setText(cacahMipilTamiuGetResponse.getPenduduk().getTelepon().toString());
                }

                if (cacahMipilTamiuGetResponse.getCacahKramaMipil() != null) {
                    profileCacahMipilDesaAdat.setText(
                            cacahMipilTamiuGetResponse.getCacahKramaMipil().getBanjarAdat().getDesaAdat().getDesadatNama()
                    );
                    profileCacahMipilBanjarAdat.setText(
                            cacahMipilTamiuGetResponse.getCacahKramaMipil().getBanjarAdat().getNamaBanjarAdat()
                    );
                    profileCacahMipilDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent cacahMipilDetail = new Intent(getActivity(), CacahMipilDetailActivity.class);
                            Gson gson = new Gson();
                            String kelahiranJSon = gson.toJson(cacahMipilTamiuGetResponse.getCacahKramaMipil());
                            cacahMipilDetail.putExtra("CACAH_MIPIL", kelahiranJSon);
                            getActivity().startActivity(cacahMipilDetail);
                        }
                    });
                }
                else {
                    profileCacahMipilCard.setVisibility(GONE);
                }

                if (cacahMipilTamiuGetResponse.getCacahKramaTamiu() != null) {
                    profileCacahTamiuCard.setVisibility(View.VISIBLE);
                    profileCacahTamiuBanjarAdat.setText(cacahMipilTamiuGetResponse.getCacahKramaTamiu().getBanjarAdat().getNamaBanjarAdat());
                    profileCacahTamiuDesaAdat.setText(cacahMipilTamiuGetResponse.getCacahKramaTamiu().getBanjarAdat().getDesaAdat().getDesadatNama());
                    profileCacahTamiuDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent cacahMipilDetail = new Intent(getActivity(), CacahTamiuDetailActivity.class);
                            Gson gson = new Gson();
                            String kelahiranJSon = gson.toJson(cacahMipilTamiuGetResponse.getCacahKramaTamiu());
                            cacahMipilDetail.putExtra("CACAH_TAMIU", kelahiranJSon);
                            getActivity().startActivity(cacahMipilDetail);
                        }
                    });
                }
                else {
                    profileCacahTamiuCard.setVisibility(GONE);
                }

                if (cacahMipilTamiuGetResponse.getPenduduk().getFoto() != null) {
//                    holder.progressContainer.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(getActivity().getResources().getString(R.string.image_endpoint) +
                                    cacahMipilTamiuGetResponse.getPenduduk().getFoto())
                            .into(profileCacahKramaImage, new Callback() {
                                @Override
                                public void onSuccess() {
//                                    holder.progressContainer.setVisibility(View.GONE);
//                                    holder.produkImageContainer.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(R.drawable.paceholder_krama_pict).into(profileCacahKramaImage);
                                }
                            });
                }
                else {
                    Picasso.get().load(R.drawable.paceholder_krama_pict).into(profileCacahKramaImage);
                }
                setProfileContainerVisible();
            }
            else {
                setFailedContainerVisible();
            }
        });
        profileContainer.setRefreshing(false);
    }


    public void setFailedContainerVisible() {
        profileLoadingContainer.setVisibility(GONE);
        profileFailedContainer.setVisibility(View.VISIBLE);
        profileContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        profileLoadingContainer.setVisibility(View.VISIBLE);
        profileFailedContainer.setVisibility(GONE);
        profileContainer.setVisibility(GONE);
    }

    public void setProfileContainerVisible() {
        profileLoadingContainer.setVisibility(GONE);
        profileFailedContainer.setVisibility(GONE);
        profileContainer.setVisibility(View.VISIBLE);
    }
}