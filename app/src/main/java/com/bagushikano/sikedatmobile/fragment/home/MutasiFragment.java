package com.bagushikano.sikedatmobile.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.mutasi.datangkeluar.DatangKeluarPengajuanActivity;
import com.bagushikano.sikedatmobile.activity.mutasi.kelahiran.KelahiranPengajuanActivity;
import com.bagushikano.sikedatmobile.activity.mutasi.kematian.KematianPengajuanActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

public class MutasiFragment extends Fragment {
    SharedPreferences userPreferences;
    private Chip namaUserChip;

    View v;

    MaterialCardView mutasiKelahiranButton, mutasiKematianButton, mutasiDatangKeluarButton;

    public MutasiFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.fragment_mutasi, container, false);

        userPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        namaUserChip = v.findViewById(R.id.banner_nama_user_chip);
        if (!(userPreferences.getString("nama_user", "empty").equals("empty"))) {
            namaUserChip.setText(userPreferences.getString("nama_user", "empty"));
            namaUserChip.setVisibility(View.VISIBLE);
        }

       mutasiKelahiranButton = v.findViewById(R.id.mutasi_kelahiran_button);
       mutasiKelahiranButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent kelahiranIntent = new Intent(getActivity(), KelahiranPengajuanActivity.class);
               startActivity(kelahiranIntent);
           }
       });

       mutasiKematianButton = v.findViewById(R.id.mutasi_kematian_button);
       mutasiKematianButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent kematianIntent = new Intent(getActivity(), KematianPengajuanActivity.class);
               startActivity(kematianIntent);
           }
       });

        mutasiDatangKeluarButton = v.findViewById(R.id.mutasi_datang_masuk_button);
        mutasiDatangKeluarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mutasiDatangKeluarIntent = new Intent(getActivity(), DatangKeluarPengajuanActivity.class);
                startActivity(mutasiDatangKeluarIntent);
            }
        });

       return v;
    }
}