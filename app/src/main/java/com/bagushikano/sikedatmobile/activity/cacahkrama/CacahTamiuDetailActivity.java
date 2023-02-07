package com.bagushikano.sikedatmobile.activity.cacahkrama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaTamiu;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.google.gson.Gson;

public class CacahTamiuDetailActivity extends AppCompatActivity {

    private TextView desaAdat, banjarAdat, tempekan, nomorCacahKramaMipil;
    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cacah_tamiu_detail);


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
        CacahKramaTamiu cacahKramaTamiu = gson.fromJson(getIntent().getStringExtra("CACAH_TAMIU"), CacahKramaTamiu.class);


        desaAdat.setText(cacahKramaTamiu.getBanjarAdat().getDesaAdat().getDesadatNama());
        banjarAdat.setText(cacahKramaTamiu.getBanjarAdat().getNamaBanjarAdat());
        tempekan.setText(ChangeDateTimeFormat.changeDateFormat(cacahKramaTamiu.getTanggalMasuk()));
        nomorCacahKramaMipil.setText(cacahKramaTamiu.getNomorCacahKramaTamiu());
    }
}