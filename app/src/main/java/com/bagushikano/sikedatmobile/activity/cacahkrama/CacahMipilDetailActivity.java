package com.bagushikano.sikedatmobile.activity.cacahkrama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobile.model.kematian.Kematian;
import com.google.gson.Gson;

public class CacahMipilDetailActivity extends AppCompatActivity {

    private TextView desaAdat, banjarAdat, tempekan, nomorCacahKramaMipil;
    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cacah_mipil_detail);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        desaAdat = findViewById(R.id.cacah_mipil_desa_adat_text);
        banjarAdat = findViewById(R.id.cacah_mipil_banjar_adat_text);
        tempekan = findViewById(R.id.cacah_mipil_tempekan_text);
        nomorCacahKramaMipil = findViewById(R.id.cacah_mipil_no_text);

        Gson gson = new Gson();
        CacahKramaMipil cacahKramaMipil = gson.fromJson(getIntent().getStringExtra("CACAH_MIPIL"), CacahKramaMipil.class);


        desaAdat.setText(cacahKramaMipil.getBanjarAdat().getDesaAdat().getDesadatNama());
        banjarAdat.setText(cacahKramaMipil.getBanjarAdat().getNamaBanjarAdat());
        tempekan.setText(cacahKramaMipil.getTempekan().getNamaTempekan());
        nomorCacahKramaMipil.setText(cacahKramaMipil.getNomorCacahKramaMipil());
    }
}