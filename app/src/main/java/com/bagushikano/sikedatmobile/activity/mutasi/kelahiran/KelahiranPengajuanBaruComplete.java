package com.bagushikano.sikedatmobile.activity.mutasi.kelahiran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuan;
import com.google.gson.Gson;

public class KelahiranPengajuanBaruComplete extends AppCompatActivity {

    private ImageView checkImage;
    private AnimatedVectorDrawable checkAnim;
    private Button backToLogin, kelahiranDetail;
    private TextView kelahiranNamaAnak, kelahiranNikAnak, kelahiranNoAktaAnak;
    private final String KELAHIRAN_DETAIL_KEY = "KELAHIRAN_DETAIL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelahiran_pengajuan_baru_complete);

        backToLogin = findViewById(R.id.register_complete_button);
        checkImage = findViewById(R.id.register_check_image);
        Drawable d = checkImage.getDrawable();
        if (d instanceof AnimatedVectorDrawable) {
            checkAnim = (AnimatedVectorDrawable) d;
            checkAnim.start();
        }
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Gson gson = new Gson();
        KelahiranAjuan kelahiran = gson.fromJson(getIntent().getStringExtra("KELAHIRAN_KEY"), KelahiranAjuan.class);

        kelahiranNamaAnak = findViewById(R.id.kelahiran_ajuan_nama);
        kelahiranNikAnak = findViewById(R.id.kelahiran_ajuan_nik);
        kelahiranNoAktaAnak = findViewById(R.id.kelahiran_ajuan_no_akta_kelahiran);
        kelahiranDetail = findViewById(R.id.kelahiran_ajuan_detail);

        kelahiranDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kelahiranDetail = new Intent(getApplicationContext(), KelahiranAjuanDetailActivity.class);
                kelahiranDetail.putExtra(KELAHIRAN_DETAIL_KEY, kelahiran.getId());
                startActivity(kelahiranDetail);
            }
        });

        kelahiranNamaAnak.setText(kelahiran.getCacahKramaMipil().getPenduduk().getNama());

        if (kelahiran.getNomorAktaKelahiran() == null) {
            kelahiranNoAktaAnak.setText("-");
        } else {
            kelahiranNoAktaAnak.setText(kelahiran.getNomorAktaKelahiran());
        }
        if (kelahiran.getCacahKramaMipil().getPenduduk().getNik() == null) {
            kelahiranNikAnak.setText("-");
        } else {
            kelahiranNikAnak.setText(kelahiran.getCacahKramaMipil().getPenduduk().getNik());
        }

    }
}