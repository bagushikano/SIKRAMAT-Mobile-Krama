package com.bagushikano.sikedatmobile.activity.mutasi.datangkeluar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bagushikano.sikedatmobile.R;
import com.google.android.material.card.MaterialCardView;

public class DatangKeluarPengajuanActivity extends AppCompatActivity {

    private MaterialCardView datangKeluarAjuanBaruButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datang_keluar_pengajuan);


        datangKeluarAjuanBaruButton = findViewById(R.id.datang_keluar_ajuan_baru_button);
        datangKeluarAjuanBaruButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent datangKeluarAjuanBaruIntent = new Intent(getApplicationContext(), DatangKeluarAjuanBaruActivity.class);
                startActivity(datangKeluarAjuanBaruIntent);
            }
        });
    }
}