package com.bagushikano.sikedatmobile.activity.mutasi.datangkeluar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bagushikano.sikedatmobile.R;

public class DatangKeluarAjuanBaruActivity extends AppCompatActivity {

    private RadioGroup datangKeluarTypeRadioGroup;
    private TextView datangKeluarTypeDescriptionText;
    private Button datangKeluarNextButton;
    /**
     * 1 = Satu krama
     * 2 = individual
     */
    private int datangKeluarType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datang_keluar_ajuan_baru);

        datangKeluarTypeDescriptionText = findViewById(R.id.datang_keluar_ajuan_description_text);
        datangKeluarNextButton = findViewById(R.id.datang_keluar_next_button);

        datangKeluarTypeRadioGroup = findViewById(R.id.datang_keluar_type_radio);
        datangKeluarTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.datang_keluar_type_satu_krama_radio) {
                    datangKeluarTypeDescriptionText.setText("Perpindahan satu krama adalah perpindahan....");
                    datangKeluarType = 1;
                }
                else if (i == R.id.datang_keluar_type_individual_radio) {
                    datangKeluarTypeDescriptionText.setText("Perpindahan individual adalah perpindahan....");
                    datangKeluarType = 2;
                }
            }
        });
    }
}