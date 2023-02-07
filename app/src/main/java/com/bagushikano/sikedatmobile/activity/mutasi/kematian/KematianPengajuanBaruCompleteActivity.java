package com.bagushikano.sikedatmobile.activity.mutasi.kematian;

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
import com.bagushikano.sikedatmobile.model.kematian.KematianAjuan;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.google.gson.Gson;

public class KematianPengajuanBaruCompleteActivity extends AppCompatActivity {

    private ImageView checkImage;
    private AnimatedVectorDrawable checkAnim;
    private Button backToLogin, kematianDetail;
    private TextView kematianNamaCacahKrama, kematianNoAktaKemaitan, kematianTanggal;
    private final String KEMATIAN_DETAIL_KEY = "KEMATIAN_DETAIL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kematian_pengajuan_baru_complete);

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
        KematianAjuan kematian = gson.fromJson(getIntent().getStringExtra("KEMATIAN_KEY"), KematianAjuan.class);

        kematianNamaCacahKrama = findViewById(R.id.kematian_ajuan_nama);
        kematianNoAktaKemaitan = findViewById(R.id.kematian_ajuan_no_akta_kematian);
        kematianTanggal = findViewById(R.id.kematian_ajuan_tanggal_kematian);
        kematianDetail = findViewById(R.id.kematian_ajuan_detail);


        kematianDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kelahiranDetail = new Intent(getApplicationContext(), KematianAjuanDetailActivity.class);
                kelahiranDetail.putExtra(KEMATIAN_DETAIL_KEY, kematian.getId());
                startActivity(kelahiranDetail);
            }
        });

        kematianNamaCacahKrama.setText(kematian.getCacahKramaMipil().getPenduduk().getNama());
        if (kematian.getNomorAktaKematian() == null) {
            kematianNoAktaKemaitan.setText("-");
        } else {
            kematianNoAktaKemaitan.setText(kematian.getNomorAktaKematian());
        }
        kematianTanggal.setText(ChangeDateTimeFormat.changeDateFormat(kematian.getTanggalKematian()));
    }
}